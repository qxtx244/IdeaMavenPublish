Documentation
=============
一个用于将 android library 上传到 maven 仓库的插件库，Java + Kotlin 的混合开发项目，支持使用 Dokka 插件生成 kotlin KDoc。  
如果项目中包含 kotlin 代码，必须添加 Dokka 插件，才能正常生成 javadoc。

## 基本概念

| groovy library  | 引入了 groovy 插件的 module，支持使用 groovy 语言和 java 语言实现              |
|:----------------|:------------------------------------------------------------------------|
| java library    | 引入了 java-library / groovy 插件的 module，使用 java / groovy 语言编写的项目 |
| android library | 引入了 com.android.application 或 com.android.library 插件的 module       |

## 环境要求
**dokka 版本**：不高于 1.9.0  
**kotlin 版本**：不高于 2.0.0  
**gradle 版本**：1.3 ~ 8.9  
以上版本均已验证支持。
需要注意的是：dokka 版本与 kotlin 版本相关，两者版本不能相差太远。已知：kotlin1.7.0 兼容 dokka1.6.20；kotlin2.0.0 兼容 dokka 1.9.0。  
同时，gradle 版本对 kotlin 版本可能也有影响，gradle 版本太低可能不支持 kotlin 插件。

## 使用方法
**1. 获取和使用插件**
<details open>
<summary>Kotlin DSL</summary>

在对应 module 层级的 `build.gradle.kts` 中添加如下内容:  
添加插件的环境变量：
```kotlin
buildscript {
  repositories {
      mavenCentral()
  }
  dependencies {
    classpath("io.github.qxtx244.gradle:MavenPublishAndroid:2.0.0")
  }
}
```
接着，请确保 module 已经添加 `kotlin` 和 `dokka` 插件：
```kotlin
plugins {
  id("org.jetbrains.dokka").version("1.6.20")
}
```
`sync` 工程。成功后，应用 `idea-publish` 插件，并配置插件（最好位于 `android{}` 之后，推荐添加到文件末尾）：
```kotlin
apply(plugin = "idea-publish")
configure<MavenPublishExtension> {
  pubMavenCentral = "仓库地址"
  pubGroupId = "group id"        //通常为包名
  pubArtifactId = "artifact id"  //如缺省，则默认使用 module 名称
  pubVersion = "发布的版本名称"
  //...
}
```
</details>


<details>
<summary>Groovy DSL</summary>

在对应 module 层级的 `build.gradle` 中的最顶端，添加插件仓库和环境变量：
```groovy
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'io.github.qxtx244.gradle:MavenPublishAndroid:2.0.0'
  }
}
```
接着，请确保 module 已经添加 `kotlin` 和 `dokka` 插件：
```groovy
plugins {
  id 'org.jetbrains.dokka' version '1.6.20'
}
```
`sync` 工程。  
成功后，应用 `idea-publish` 插件，并配置插件字段（需要 `android{}` 之后，推荐添加到文件末尾）：
```groovy
apply plugin: "idea-publish"
MavenPublish {
  pubMavenCentral = "仓库地址"
  pubGroupId = "group id"        //通常为包名
  pubArtifactId = "artifact id"  //如缺省，则默认使用 module 名称
  pubVersion = "发布的版本名称"
  //...
}
```

</details>


**2. 编译和打包模块**  
执行 `Build 》 Rebuild Project`，等待构建完成即可。

**3. 发布模块到 Maven 仓库**  
在 AS 右侧的 “Gradle” 面板中可以找到对应 module 的 `publishing` 任务组，点击其中的 `publish` 任务，等待发布完成即可。

**注意**  
如果需要将 module 发布到 snapshots 仓库，版本名称必须在末尾加上“-SNAPSHOT”后缀，否则发布请求将会被远程仓库拒绝。

## 插件字段说明
- pubMavenCentral   maven 仓库地址，String 类型。格式：'http://xxx.xxx.xxx'
- pubMavenAccount   maven 账户信息，ArrayList<String> 类型，格式：['账户名', '账户密码']
- pubGroupId        组 id，String类型。通常使用 module 包名。格式：'x.y.z'
- pubArtifactId     artifactId，String 类型。通常使用 module 名称，即 project.name。格式：'xxx'
- pubVersion        库版本，String 类型。如果是发布到 snapshots 仓库需要在末尾加上 '-SNAPSHOT' 修饰。格式：'1.0.0'，'1.0.0-SNAPSHOT'
- pubDesc           描述信息，String 类型
- pubSrcDirs        源码目录路径，String 类型。如果该配置有效，则打包和发布源代码。可配置一个或多个。ArrayList<String> 类型。格式：["src/main/groovy", "xxx/src/main/java", ...]
- pubSourceEnable   是否发布源码，boolean 类型
- pubJavaDocEnable  是否生成并发布 javaDoc，boolean 类型
- pubArchivePath    归档文件的绝对路径，String 类型。格式：'xx/xx/xxx.jar','xx/xx.aar'
- pubExtraFiles     额外的文件路径列表，将这些文件打包成 zip 并发布，List<String> 类型。格式：['x/xx.txt', 'x/xxx.a', 'xx/xx.html', 'x/x/x.zip', ...]