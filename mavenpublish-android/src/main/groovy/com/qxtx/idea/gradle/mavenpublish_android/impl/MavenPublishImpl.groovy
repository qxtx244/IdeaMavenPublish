package com.qxtx.idea.gradle.mavenpublish_android.impl

import com.qxtx.idea.gradle.mavenpublish_android.Base
import com.qxtx.idea.gradle.mavenpublish_android.extension.MavenPublishExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.internal.artifacts.DefaultExcludeRule
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar

/**
 * @author QXTX-WIN
 * createDate 2024/12/30 21:13
 * Description 插件功能的具体实现
 */
class MavenPublishImpl extends Base {

    private final String EXT_TASK_GROUP_NAME = 'ideaPublish'

    private LegacyMavenPublishImpl legacyImpl = null

    /**
     * 获取 maven 仓库地址
     * @return maven 仓库地址
     */
    private String getPubMavenCentral() {
        def url = config.pubMavenCentral
        if (url == null) throw new RuntimeException("无法获取 maven 仓库地址！是否已经配置参数 'pubMavenCentral'？")

        url.toString()
    }

    /**
     * 获取 groupId，如果未配置，则默认获取 AndroidManifest.xml 中定义的包名
     * @return groupId
     */
    private String getPubGroupId(Project target) {
        def groupId = null
        try {
            groupId = config.pubGroupId
            if (groupId == null) {
                def slurper = new XmlSlurper()
                def file = target.file('src/main/AndroidManifest.xml') //仅支持 main flavor
                if (file.exists()) {
                    def result = slurper.parse(file)
                    groupId = result['@package']
                }
            }
            if (groupId != null) {
                groupId = groupId.toString().replaceAll('/', '\\.').replaceAll('\\\\', '\\.')
            }
        } catch (Exception e) {
            e.printStackTrace()
        }

        if (groupId == null || groupId == '') throw new RuntimeException("无法获取 groupId！是否已经配置参数 'pubGroupId'？")

        groupId.toString()
    }

    /**
     * 获取 artifactId，如果未配置，则默认使用 module 的名称
     * @return artifactId
     */
    private String getPubArtifactId(Project target) {
        def artifactId = config.pubArtifactId
        if (artifactId == null && target) artifactId = target.name
        if (artifactId == null) throw new RuntimeException("无法获取 artifactId！")

        artifactId.toString()
    }

    /**
     * 获取 version，如果未配置，则默认获取 defaultConfig 中定义的 versionName
     * @return version
     */
    private String getPubVersion(Project target) {
        def version = config.pubVersion
        if (version == null && target) version = target.android.defaultConfig.versionName
        if (version == null) version = '1.0.0'

        version.toString()
    }

    /**
     * 获取描述信息，如果未配置，则为空
     * @return 描述信息
     */
    private String getPubDesc() {
        def desc = config.pubDesc
        if (desc == null) desc = ''

        desc.toString()
    }

    /**
     * 发布版本，取值为 “debug” 或 “release”，对应 debug 和 release 的 jar/aar 包。预设返回 “release”
     */
    private String getPubBuildType() {
        def type = config.pubBuildType
        if (type != "release" && type != "debug") type = "release"

        type
    }

    /**
     * 获取是否发布 javaDoc。如果未配置，默认返回 false
     * @return true 表示发布，否则不发布
     */
    private boolean isPubJavaDocEnable() {
        config.pubJavaDocEnable
    }

    /**
     * 找到指定构建类型的 aar 输出路径
     * @param buildType 构建版本，取值为 "release" 或 "debug"
     * @return aar 文件路径
     */
    private static def findAarPath(String buildType, Project target) {
        String aarPath = null
        if (target == null) return null
        try {
            target.android.libraryVariants.all { variant ->
                variant.outputs.all { output ->
                    if (variant.name == buildType && outputFileName.endsWith(".aar")) {
                        try {
                            //aarPath = variant.getPackageLibraryProvider().get()
                            aarPath = output.outputFile.absolutePath //这里仍然过时了，可能需要换个方法？
                            // variant.getPackageLibrary() 已在 gradle8.x 被移除
                            //def destDir = variant.getPackageLibrary().destinationDir
                            //aarPath = "${destDir.absolutePath}/${outputFileName}"
                        } catch (e) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        aarPath
    }

    /**
     * 获取外部指定的构建版本文件路径。如果获取到有效值，则忽略 pubBuildType 配置。
     */
    private String getPubArchivePath(Project target) {
        String path = config.pubArchivePath
        if (path == null) path = findAarPath(getPubBuildType(), target)
        if (path == null) println "无法找到归档文件！"

        path
    }

    /**
     * 获取源码目录列表，如果未配置，则取 main 中的默认资源目录
     * @return 可能为 null
     */
    private ArrayList<Object> getPubSrcDirs(Project target) {
        def dirs = config.pubSrcDirs
        if (dirs == null) dirs = target.android.sourceSets.main.java.srcDirs

        dirs
    }

    /**
     * 获取是否发布源码。如果未配置，默认返回 false
     * @return true 表示发布，否则不发布
     */
    private boolean getPubSourceEnable() {
        config.pubSourceEnable
    }

    /**
     * 获取附加的归档文件绝对路径集。
     * @return 附加归档文件绝对路径集，可能为 null
     */
    private Set<String> getPubExtraFiles() {
        config.pubExtraFiles
    }

    /**
     * 获取 maven 仓库账户信息
     * @return maven 仓库的账户信息（用户名 和 用户密码）
     */
    private ArrayList<String> getPubMavenAccount() {
        config.pubMavenAccount
    }

    @Override
    void start(Project target) {
        //检查 gradle 版本
        def gradleVer = target.gradle.gradleVersion
        println "Gradle version is $gradleVer"
        if (gradleVer.charAt(0) <= '1' && gradleVer.charAt(3) < '3') {
            println "gradle version is too low, use legacy maven plugin implementation..."
            legacyImpl = new LegacyMavenPublishImpl()
            legacyImpl.apply(target)
            return
        }

        def pubUrl = getPubMavenCentral()
        def pubGroupId = getPubGroupId(target)
        def pubArtifactId = getPubArtifactId(target)
        def pubVersion = getPubVersion(target)
        def pubJavaDocEnable = isPubJavaDocEnable()
        def pubSrcDirs = getPubSrcDirs(target)
        def pubSourceEnable = getPubSourceEnable()
        def pubDesc = getPubDesc()
        def pubExtraFiles = getPubExtraFiles()
        println("${target.name}发布配置：" +
                "\nmaven仓库：$pubUrl" +
                "\ngroupId=$pubGroupId" +
                "\nartifactId=$pubArtifactId" +
                "\nversion=$pubVersion" +
                "\nbuildType=${getPubBuildType()}" +
                "\nenable source=$pubSourceEnable" +
                "\nenable javadoc=$pubJavaDocEnable")
        if (pubUrl == null || pubGroupId == null || pubArtifactId == null || pubVersion == null) {
            println "maven 发布配置错误！"
            return
        }

        def mavenPublishPlugin = 'maven-publish'
        if (target.plugins.findPlugin(mavenPublishPlugin) == null) target.apply plugin: mavenPublishPlugin

        def publishing = target.extensions.getByType(PublishingExtension.class)
        if (publishing == null) return

        publishing.publications {publicationContainer ->
            //使用 create() 创建 MavenPublication 对象，并添加到 publications 容器中。闭包中可以配置此对象
            // 直接 xxx(MavenPublication.class) 等价于 create("xxx", MavenPublication.class)，此种写法更接近于 DSL 风格
            // 但是 create() 还可以指定 publication 的类型，不一定是 MavenPublication 类型

            //创建用于发布的 Publication 对象
            publicationContainer.create(target.name, MavenPublication.class) { publication ->
                //from components.release //包含 aar,sources,javadoc
                publication.groupId = pubGroupId
                publication.artifactId = pubArtifactId
                publication.version = pubVersion

                //打包归档文件 jar / aar
                def archivePath = getPubArchivePath(target)
                if (archivePath != null) {
                    //println "archive file path: ${archivePath}"
                    artifact(archivePath)
                }

                //打包源码
                if (pubSourceEnable && pubSrcDirs != null && !pubSrcDirs.isEmpty()) {
                    def sourcesJarTaskName = "jarSources"
                    def sourcesJarTask = target.tasks.findByName(sourcesJarTaskName)
                    if (sourcesJarTask == null && pubSrcDirs != null) {
                        try {
                            sourcesJarTask = target.task([type: Jar, group: EXT_TASK_GROUP_NAME], sourcesJarTaskName) {
                                getArchiveClassifier().set('sources')
                                from pubSrcDirs
                            }
                        } catch (def ignore) {}
                    }

                    //println "sources path: ${sourcesJarTask.archiveFile}"
                    artifact(sourcesJarTask.archiveFile) {
                        it.builtBy sourcesJarTask
                        it.classifier 'sources'
                        it.extension 'jar'
                    }
                }

                //打包 javadoc
                if (pubJavaDocEnable) {
                    def javadocTaskName = "generalJavadoc"
                    def javadocTask = target.tasks.findByName(javadocTaskName)
                    try {
                        if (javadocTask == null && pubSrcDirs != null) {
                            javadocTask = target.task([type: Javadoc, group: EXT_TASK_GROUP_NAME], javadocTaskName) {
                                options {
                                    encoding('utf-8')
                                    links 'http://docs.oracle.com/javase/8/docs/api'
                                }
                                failOnError = false
                                source = pubSrcDirs
                                if (target.android != null) {
                                    //为 javadoc 添加 android.jar 依赖包，防止报错找不到相关类
                                    classpath += target.files(target.android.getBootClasspath().join(File.pathSeparator))
                                    classpath += target.files("${target.android.sdkDirectory}/platforms/${target.android.compileSdkVersion}/android.jar")
                                }
                            }
                        }
                        if (javadocTask == null) throw new Exception()

                        def javadocJarTaskName = "jarJavaDoc"
                        def javadocJarTask = target.tasks.findByName(javadocJarTaskName)
                        if (javadocJarTask == null) {
                            javadocJarTask = target.task([type: Jar, group: EXT_TASK_GROUP_NAME, dependsOn: javadocTask], javadocJarTaskName) {
                                getArchiveClassifier().set('javadoc')
                                from javadocTask.destinationDir
                            }
                        }
                        if (javadocJarTask != null) {
                            artifact(javadocJarTask.archiveFile) {
                                it.builtBy javadocJarTask
                                it.classifier 'javadoc'
                                it.extension 'jar'
                            }
                        }
                    } catch (def ignore) {}
                }

                //打包附加的文件
                if (pubExtraFiles != null && !pubExtraFiles.isEmpty()) {
                    def extraFilesTask = target.task([type: org.gradle.api.tasks.bundling.Jar, group: EXT_TASK_GROUP_NAME], 'ZipExtraFiles') {
                        getArchiveClassifier().set('extraFiles')
                        from pubExtraFiles
                    }
                    artifact(extraFilesTask.archiveFile) {
                        it.builtBy extraFilesTask
                        it.classifier 'files'
                        it.extension 'zip'
                    }
                }

                //自定义 pom 文件，以添加一些三方远程依赖，以及依赖排除
                configPom(target, pom, pubDesc, pubArtifactId)
            }
        }

        //maven 仓库配置
        configRepo(target, publishing, pubUrl, getPubMavenAccount())
    }

    private static void configRepo(Project target, PublishingExtension publishing, String repoUrl, ArrayList<String> account) {
        publishing.repositories {
            maven {
                url = target.uri(repoUrl)

                if (account != null && account.size() >= 2) {
                    credentials {
                        username account[0]
                        password account[1]
                    }
                }
                allowInsecureProtocol true
            }
        }
    }

    private static void configPom(Project target, MavenPom pom, String description, String artifactId) {
        pom.withXml {
            if (description != null) asNode().appendNode('description', description)
            if (artifactId != null) asNode().appendNode('name', artifactId)

            //添加三方依赖项
            //<dependencies>
            //  ...
            //  <dependency>
            //      <groupId>x.x.x</groupId>
            //      <artifactId>yyy</artifactId>
            //      <version>z.z.z</version>
            //      <scope>compile|runtime|provided|test|system|import</scope>
            //      <type>jar/war/aar/pom等等，默认jar</type>
            //  </dependency>
            //  ...
            //</dependencies>
            def depNode = it.asNode().appendNode('dependencies')
            try {
                //1. 添加 api 方式的三方依赖
                target.configurations.api.allDependencies.each { dependency ->
                    addDependencyNode(depNode, dependency)
                }

                //2. 添加 releaseApi 方式的三方依赖
                //不考虑 test，assembleDebug，只考虑assembleRelease
                ArrayList<String> list = new ArrayList<>()
                gradle.startParameter.taskRequests.each { request -> list.addAll(request.args) }
                if (list.contains('assembleRelease')) {
                    target.configurations.releaseApi.allDependencies.each { dependency ->
                        addDependencyNode(depNode, dependency)
                    }
                }
            } catch (def ignore) {}
        }
    }

    /**
     * 排除的依赖项
     * <dependency>
     *  <groupId></groupId>
     *  <artifactId></artifactId>
     *  <version></version>
     *  <scope></scope>
     *  ...
     *  <exclusions>
     *      <exclusion>
     *          <groupId>xxx</groupId>
     *          <artifactId>xxx</artifactId>
     *      </exclusion>
     *  </exclusions>
     *  ...
     * </dependency>
     * @param depNode dependencies 标签节点
     * @param dependency
     * @return
     */
    private static Node addDependencyNode(Node depNode, Dependency dependency) {
        if (dependency.name == null || dependency.name == 'unspecified'               //artifactId
                || dependency.version == null || dependency.version == 'unspecified'  //version
                || dependency.group == null || dependency.group == 'unspecified'      //groupId
        ) return null

        def node = depNode.appendNode('dependency')
        node.appendNode('groupId', dependency.group)
        node.appendNode('artifactId', dependency.name)
        node.appendNode('version', dependency.version)

        //额外添加
        node.appendNode('scope', 'compile')

        //排除依赖规则（exclude()）
        // api(xxx) {
        //      exclude(group='xxx', module='xxx')
        //}
        HashSet<DefaultExcludeRule> set = dependency.excludeRules
        if (set != null && !set.isEmpty()) {
            def exclusionsNode = node.appendNode('exclusions')
            set.each { rule ->
                node = exclusionsNode.appendNode('exclusion')
                node.appendNode('groupId', rule.group)
                node.appendNode('artifactId', rule.module)
            }
        }

        node
    }
}