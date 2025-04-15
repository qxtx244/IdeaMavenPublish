# Documentation
===============
一个用于将 java/groovy/android library 上传到 maven 仓库的插件库，仅适用于 Gradle1.3 或更高版本的工程。

## 基本概念

| groovy library  | 引入了 groovy 插件的 module，支持使用 groovy 语言和 java 语言实现              |
|:----------------|:------------------------------------------------------------------------|
| java library    | 引入了 java-library / groovy 插件的 module，使用 java / groovy 语言编写的项目 |
| android library | 引入了 com.android.application 或 com.android.library 插件的 module       |


# 使用方法
**1. 获取和使用插件**
在对应 module 层级的 `build.gradle` 中的最顶端，添加插件仓库和环境变量：
```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'io.github.qxtx244.gradle:MavenPublishJava:2.0.0'
  }
}
```
接着，`sync` 工程。  
成功后，应用 `idea-publish` 插件，并配置插件字段（推荐添加到文件末尾）：
```groovy
apply plugin: "idea-publish"
MavenPublish {
  pubMavenCentral = "仓库地址"
  pubGroupId = "group id"        //通常为包名
  pubArtifactId = "artifact id"  //如缺省，则默认使用 module 名称
  pubVersion = "发布的版本名称"
  pubArchivePath = "打包的 .jar 包路径"
  //...
}
```

**2. 编译和打包模块**  
执行 `Build 》 Rebuild Project`，等待构建完成即可。

**3. 发布模块到 Maven 仓库**  
在 AS 右侧的 “Gradle” 面板中可以找到对应 module 的 `publishing` 任务组，点击其中的 `publish` 任务，等待发布完成即可。

**注意**  
如果需要将 module 发布到 snapshots 仓库，版本名称必须在末尾加上“-SNAPSHOT”后缀，否则发布请求将会被远程仓库拒绝。


# 插件字段说明
- pubMavenCentral   maven 仓库地址，String 类型。格式：'http://xxx.xxx.xxx'
- pubMavenAccount   maven 账户信息，ArrayList<String> 类型，格式：['账户名', '账户密码']
- pubGroupId        组 id，String类型。通常使用 module 包名。格式：'x.y.z'
- pubArtifactId     artifactId，String 类型。通常使用 module 名称，即 project.name。格式：'xxx'
- pubVersion        库版本，String 类型。如果是发布到 snapshots 仓库需要在末尾加上 '-SNAPSHOT' 修饰。格式：'1.0.0'，'1.0.0-SNAPSHOT'
- pubDesc           描述信息，String 类型
- pubSrcDirs        源码目录路径，String 类型。如果该配置有效，则打包和发布源代码。可配置一个或多个。ArrayList<String> 类型。格式：["src/main/groovy", "xxx/src/main/java", ...]
- pubSourceEnable   是否发布源码，boolean 类型
- pubJavaDocEnable  是否生成并发布 javaDoc，boolean 类型。取值为 'true' 或 'false'
- pubGroovyDocEnable 是否生成并发布groovyDoc，boolean 类型。取值为 'true' 或 'false'
- pubArchivePath    归档文件的绝对路径，String 类型。格式：'xx/xx/xxx.jar','xx/xx.aar'
- pubExtraFiles     额外的文件路径列表，将这些文件打包成 zip 并发布，List<String> 类型。格式：['x/xx.txt', 'x/xxx.a', 'xx/xx.html', 'x/x/x.zip', ...]

注意，javadoc 功能可能会导致 Gradle 报错（注释格式不规范或其它原因等），这时候可以尝试将 pubJavaDoc 置为 false，不输出 javadoc 文档