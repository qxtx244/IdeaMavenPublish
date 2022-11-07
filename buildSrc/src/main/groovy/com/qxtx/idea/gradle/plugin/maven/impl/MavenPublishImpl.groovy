package com.qxtx.idea.gradle.plugin.maven.impl

import com.qxtx.idea.gradle.plugin.maven.Base
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.DefaultExcludeRule
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

/**
 * maven发布的配置
 */
class MavenPublishImpl extends Base {

    private Task javadocsTask = null
    private Task javadocsJarTask = null
    private Task sourceJarTask = null

    /**
     * 构建输出版本的绝对路径。如外部未配置，返回默认生成路径
     * @return 路径
     */
    private String getPubBuildFilePath(Project project) {
        if (config.pubBuildFilePath == null) {
            config.pubBuildFilePath("${project.buildDir.absolutePath}/libs/${project.name}.jar")
        }
        return config.pubBuildFilePath
    }

    /**
     * 获取maven仓库地址，如果ext未定义，使用本地仓库
     * @return maven仓库地址
     */
    private String getPubMavenCentral() {
        if (config.pubMavenCentral == null) {
            config.pubMavenCentral("${System.getProperties().getProperty('user.home')}/.mavenCentral")
        }
        return config.pubMavenCentral
    }

    /**
     * 获取artifactId，如果ext未定义，则使用project名称
     * @return artifactId
     */
    private String getPubArtifactId(Project project) {
        if (config.pubArtifactId == null) {
            config.pubArtifactId(project.name)
        }
        return config.pubArtifactId
    }

    /**
     * 获取versionName
     * @return versionName
     */
    private String getPubVersionName() {
        if (config.pubVersionName == null) {
            config.pubVersionName("1.0.0.${System.currentTimeMillis()}")
        }
        return config.pubVersionName
    }

    @Override
    void start(Project target) {
        println '开始执行插件功能...'

        createPackagingTask(target)

        target.publishing {
            publications {
                register(target.name, MavenPublication.class) {
                    println("${target.name}发布的目标maven仓库：${getPubMavenCentral()}" +
                            "\ngroupId=${config.pubGroupId}" +
                            "\nartifactId=${getPubArtifactId(target)}" +
                            "\nversion=${getPubVersionName()}" +
                            "\ndesc=${config.pubDesc}" +
                            "\nsource? ${config.pubSourceEnable}" +
                            "\njavadoc? ${config.pubJavaDocEnable}")

                    if (config.pubGroupId == null) {
                        throw new IllegalArgumentException("无效的groupId！")
                    }

                    groupId       config.pubGroupId
                    artifactId   getPubArtifactId(target)
                    version       getPubVersionName()
                    if (config.pubDesc != null) {
                        description config.pubDesc
                    }

                    String buildFilePath = getPubBuildFilePath(target)
                    if (buildFilePath != null) {
                        artifact(buildFilePath)
                    }

                    def srcDir = config.pubSrcDirs
                    if (config.pubSourceEnable) {
                        if (srcDir == null || srcDir.isEmpty()) {
                            println "无效的源码目录，无法发布源码"
                        } else {
                            artifact(sourceJarTask)
                        }
                    }

                    if (config.pubJavaDocEnable) {
                        artifact(javadocsJarTask)
                    }

                    //添加三方依赖
                    pom.withXml {
                        //三方依赖项
                        //<dependencies>
                        //  ...
                        //  <dependency>
                        //      <groupId>x.x.x</groupId>
                        //      <artifactId>yyy</artifactId>
                        //      <version>z.z.z</version>
                        //      <scope>compile|runtime</scope>
                        //      <type>jar/war/aar/pom等等，默认jar</type>
                        //  </dependency>
                        //  ...
                        //</dependencies>
                        def depNode = asNode().appendNode('dependencies')
                        try {
                            target.configurations.api.allDependencies.each { dependency ->
                                def node = addDependencyNode(depNode, dependency)
                                if (node != null) {
                                    node.appendNode('scope', 'compile')

                                    //排除的依赖项
                                    addExclusionNode(dependency, node)
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace()
                        }

                        //不考虑test
                        //考虑assembleDebug和assembleRelease
                        ArrayList<String> list = new ArrayList<>()
                        //找到执行的task名称列表
                        target.gradle.startParameter.taskRequests.each { request ->
                            list.addAll(request.args)
                        }
                        if (!list.isEmpty()) {
                            def buildType = config.pubBuildType
                            try {
                                if (buildType == 'release' && list.contains('assembleRelease')) {
                                    target.configurations.releaseApi.allDependencies.each { dependency ->
                                        def node = addDependencyNode(depNode, dependency)
                                        if (node != null) {
                                            node.appendNode('scope', 'compile')
                                            addExclusionNode(dependency, node)
                                        }
                                    }
                                } else if (buildType != 'release' && list.contains('assembleDebug')) {
                                    target.configurations.debugApi.allDependencies.each { dependency ->
                                        def node = addDependencyNode(depNode, dependency)
                                        if (node != null) {
                                            node.appendNode('scope', 'compile')
                                            addExclusionNode(dependency, node)
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

            repositories {
                maven {
                    url { target.uri(getPubMavenCentral()) }
                    def info = config.pubMavenAccount
                    if (info != null && info.size() >= 2) {
                        credentials {
                            username info[0]
                            password info[1]
                        }
                    } else {
                        //预设一个maven仓库url，如果匹配，则使用预设的账户，以通过认证
//                        if (centralUrl.startsWith(CREDENTIAL_CENTRAL_URL)) {
//                            //println "默认maven仓库，使用预置的账号认证"
//                            credentials {
//                                username CREDENTIAL_USER
//                                password CREDENTIAL_PASSWORD
//                            }
//                        }
                    }
                    allowInsecureProtocol true
                }
            }
        }
    }

    private void createPackagingTask(Project target) {
        if (target.getTasksByName("javadocs", false).isEmpty()) {
            javadocsTask = target.task([type: Javadoc], "javadocs") {
                options {
                    encoding('utf-8')
                    links 'http://docs.oracle.com/javase/8/docs/api'
                }
                failOnError = false
                def dirs = config.pubSrcDirs
                if (dirs != null) {
                    source dirs
                }
            }
        }

        if (target.getTasksByName("javadocsJar", false).isEmpty()) {
            println '创建javadoc任务...'
            javadocsJarTask = target.task([type: Jar, dependsOn: javadocsTask, group: 'extension'], "javadocsJar") {
                getArchiveClassifier().set('javadoc')
                from javadocsTask.destinationDir
            }
        }

        if (target.getTasksByName("sourceJar", false).isEmpty()) {
            println '创建源码打包任务...'
            sourceJarTask = target.task([type: Jar, group: 'extension'], "sourceJar") {
                getArchiveClassifier().set('sources')

                def dirs = config.pubSrcDirs
                if (dirs != null) {
                    from dirs
                }
            }
        }
    }

    /**
     * 排除的依赖项
     * <dependency>
     *  ...
     *  <exclusions>
     *      <exclusion>
     *          <groupId>xxx</groupId>
     *          <artifactId>xxx</artifactId>
     *      </exclusion>
     *  </exclusions>
     *  ...
     * </dependency>
     */
    private def addExclusionNode(Dependency dependency, def dependencyNode) {
        HashSet<DefaultExcludeRule> set = dependency.excludeRules
        if (set != null && !set.isEmpty()) {
            def exclusionNode = dependencyNode.appendNode('exclusions')
            set.each { rule ->
                //println "添加依赖排除项：${rule.group}:${rule.module}"
                def excludeNode = exclusionNode.appendNode('exclusion')
                excludeNode.appendNode('groupId', rule.group)
                excludeNode.appendNode('artifactId', rule.module)
            }
        }
    }

    private def addDependencyNode(def dependencyNode, Dependency dependency) {
        def node = null
        if (dependency.name != 'unspecified' && dependency.group != null
                && dependency.version != null && dependency.version != 'unspecified') {
            //println "dept name=${dependency.name}, group=${dependency.group}, version=${dependency.version}"
            node = dependencyNode.appendNode('dependency')
            node.appendNode('groupId', dependency.group)
            node.appendNode('artifactId', dependency.name)
            node.appendNode('version', dependency.version)
        }
        node
    }
}