package com.qxtx.idea.gradle.mavenpublish_java.extension

/**
 * @author QXTX-WIN
 * createDate 2022/11/11 21:12
 * Description 插件扩展类
 */
class MavenPublishExtension {

    /** maven 仓库地址 */
    String pubMavenCentral = null

    /** maven 账户信息，格式：['账户名称', '账户密码'] */
    ArrayList<String> pubMavenAccount = null

    /** 组id。通常使用 module 包名 */
    String pubGroupId = null

    /** artifactId。通常使用 module 名称，即 project.name */
    String pubArtifactId = null

    /** 版本名称。发布到 snapshots 仓库需要在末尾加上 '-SNAPSHOT' */
    String pubVersion = null

    /** 描述信息 */
    String pubDesc = null

    /**
     * 归档文件的绝对路径。如 "xxx/1.jar"，"xxx/xx/1.aar"。
     * 如果配置此项，则 pubBuildType 配置将会被忽略；
     * 如果未配置此项，则自动寻找 library 输出的 pubBuildType 版本的 aar 文件。
     */
    String pubArchivePath = null

    /** 附加的文件路径集，可指定多个文件，如 [ xx/xxx.a, xxx/xxx.txt, xx/xxxx/x/x.mp4, ...]，这些文件将会被打包并发布 */
    Set<String> pubExtraFiles = null

    /** 源码目录列表，格式：['src/main/java', 'xxx/xxx/xxx', ...] */
    ArrayList<String> pubSrcDirs = null

    /** 是否发布源码，取值为 true 或 false */
    boolean pubSourceEnable = false

    /** 是否发布 javaDoc，取值为 true 或 false */
    boolean pubJavaDocEnable = false

    /** 是否发布 groovyDoc，取值为 true 或 false */
    boolean pubGroovyDocEnable = false

    /** 发布的构建版本，String 类型。取值为“debug”或“release”，对应 debug 和 release 的 jar/aar 包 */
    String pubBuildType = 'release'

    def pubBuildType(String buildType) {
        checkNull(buildType)
        this.pubBuildType = buildType == "release" ? "release" : "debug"
    }

    def pubGroovyDocEnable(boolean b) {
        this.pubGroovyDocEnable = b
    }

    def pubJavaDocEnable(boolean b) {
        this.pubJavaDocEnable = b
    }

    def pubSourceEnable(boolean b) {
        this.pubSourceEnable = b
    }

    def pubSrcDirs(ArrayList<String> srcDirs) {
        checkNull(srcDirs)
        this.pubSrcDirs = new ArrayList<>(srcDirs)
    }

    def pubExtraFiles(Set<String> files) {
        checkNull(files)
        this.pubExtraFiles = new HashSet<>(files)
    }

    def pubArchivePath(String path) {
        checkNull(path)
        this.pubArchivePath = path
    }

    def pubDesc(String desc) {
        this.pubDesc = desc
    }

    def pubVersion(String version) {
        checkNull(version)
        this.pubVersion = version
    }

    def pubArtifactId(String artifactId) {
        checkNull(artifactId)
        this.pubArtifactId = artifactId
    }

    def pubGroupId(String groupId) {
        checkNull(groupId)
        this.pubGroupId = groupId
    }

    def pubMavenAccount(ArrayList<String> account) {
        checkNull(account)
        this.pubMavenAccount = new ArrayList<>(account)
    }

    def pubMavenCentral(String url) {
        checkNull(url)
        this.pubMavenCentral = url
    }

    private void checkNull(String s) {
        if (s == null) {
            throw IllegalArgumentException("错误的参数：" + s)
        }
    }
}