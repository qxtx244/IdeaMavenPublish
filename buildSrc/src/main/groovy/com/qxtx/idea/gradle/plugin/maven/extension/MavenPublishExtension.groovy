package com.qxtx.idea.gradle.plugin.maven.extension

class MavenPublishExtension {

    /** maven仓库地址 */
    String pubMavenCentral = null

    /** maven账户信息，格式：['USER', 'PASSWORD'] */
    ArrayList<String> pubMavenAccount = null

    /** 组id。通常使用module包名 */
    String pubGroupId = null

    /** artifactId。通常使用module名称，即project.name */
    String pubArtifactId = null

    /** 版本名称。发布到snapshots仓库需要在末尾加上'-SNAPSHOT' */
    String pubVersionName = "1.0.0-${System.currentTimeMillis()}"

    /** 描述信息 */
    String pubDesc = null

    /** 发布的构建版本，String类型。取值为“debug”或“release”，对应debug和release的jar/aar包 */
    String pubBuildType = 'release'

    /**
     * 库打包文件的绝对路径。如"xxx/1.jar"，"xxx/xx/1.aar"。
     * 如果配置此项，则pomBuildType配置将会被忽略；
     * 如果未配置此项，则自动寻找library输出的pomBuildType版本的aar文件。
     */
    String pubBuildFilePath = null

    /** 源码目录，格式：['a/b/x', 'a/b', ...] */
    ArrayList<String> pubSrcDirs = null

    /** 是否发布javaDoc，取值为true或false */
    boolean pubJavaDocEnable = false

    /** 是否发布源码，取值为true或false */
    boolean pubSourceEnable = true

    def pubMavenCentral(String url) {
        checkNull(url)
        this.pubMavenCentral = url
    }

    def pubMavenAccount(ArrayList<String> account) {
        this.pubMavenAccount = new ArrayList<>(account)
    }

    def pubGroupId(String groupId) {
        checkNull(groupId)
        this.pubGroupId = groupId
    }

    def pubArtifactId(String artifactId) {
        checkNull(artifactId)
        this.pubArtifactId = artifactId
    }

    def pubVersionName(String versionName) {
        checkNull(versionName)
        this.pubVersionName = versionName
    }

    def pubDesc(String desc) {
        this.pubDesc = desc
    }

    def pubBuildType(String buildType) {
        checkNull(buildType)
        this.pubBuildType = buildType == "release" ? "release" : "debug"
    }

    def pubBuildFilePath(String path) {
        this.pubBuildFilePath = path
    }

    def pubSrcDirs(ArrayList<String> srcDirs) {
        this.pubSrcDirs = new ArrayList<>(srcDirs)
    }

    def pubJavaDocEnable(boolean b) {
        this.pubJavaDocEnable = b
    }

    def pubSourceEnable(boolean b) {
        this.pubSourceEnable = b
    }

    private void checkNull(String s) {
        if (s == null) {
            throw IllegalArgumentException("错误的参数：" + s)
        }
    }
}