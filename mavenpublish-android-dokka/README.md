mavenpublish-android
====================

一个用于将 android library 上传到 maven 仓库的插件库。

# 使用方法
1. 获取插件

2. 添加插件到项目

3. 添加插件配置

4. 发布项目库到 maven 仓库

* 实现上传到 maven 仓库，仅适用于 android library，不支持 kotlin 语言。
* 
* 此脚本仅适用于 Gradle1.3 或更高版本
* 
* # 基本概念
* android library：引入了 com.android.application 或 com.android.library 插件的 module
* 
* # 使用步骤：
* 由于需要使用 android 插件中的变量，必须将以下代码添加到 android{} 之后，建议放到 build.gradle 末尾。
* 1. 在 module 的 build.gradle 最后面添加如下代码：
* ```
* ext.pubMavenCentral = 'maven 仓库的 url'                         //必须配置
* ...
* ```
* 或
* ```
* ext {
*        pubMavenCentral = 'maven 仓库的 url'                         //必须配置
*        ...
* }
* 
* apply from: "本文件路径（在 IDE 中右键脚本文件》CopyPath 即可复制文件绝对路径"   //必须添加这行，以将脚本应用到 module
* ```
* 2. 检查 build.gradle 中的上传配置是否正确（如 pubMavenCentral 等）；
* 3. sync 工程，然后执行 assemble 打包项目；
* 4. 点击展开 AS 右侧的 "Gradle" 面板，依次展开 module名称》publishing，双击执行“publish”任务，等待执行完成。
* 注意 1：发布到 snapshots 仓库，版本名称必须在末尾加上“-SNAPSHOT”后缀，否则发布请求将会被远程仓库拒绝。
* 注意 2：相同的 release 版本禁止重复发布。
* 
* # 参数配置说明：
* - pubMavenCentral   maven 仓库地址，String 类型。格式：'http://xxx.xxx.xxx'
* - pubMavenAccount   maven 账户信息，ArrayList<String> 类型，格式：['账户名', '账户密码']
* - pubGroupId        组 id，String类型。通常使用 module 包名。格式：'x.y.z'
* - pubArtifactId     artifactId，String 类型。通常使用 module 名称，即 project.name。格式：'xxx'
* - pubVersion        库版本，String 类型。如果是发布到 snapshots 仓库需要在末尾加上 '-SNAPSHOT' 修饰。格式：'1.0.0'，'1.0.0-SNAPSHOT'
* - pubDesc           描述信息，String 类型
* - pubSrcDirs        源码目录路径，String 类型。如果该配置有效，则打包和发布源代码。可配置一个或多个。ArrayList<String> 类型。格式：["src/main/groovy", "xxx/src/main/java", ...]
* - pubSourceEnable   是否发布源码，boolean 类型
* - pubJavaDocEnable  是否生成并发布 javaDoc，boolean 类型
* - pubArchivePath    归档文件的绝对路径，String 类型。格式：'xx/xx/xxx.jar','xx/xx.aar'
* - pubExtraFiles     额外的文件路径列表，将这些文件打包成 zip 并发布，List<String> 类型。格式：['x/xx.txt', 'x/xxx.a', 'xx/xx.html', 'x/x/x.zip', ...]
* 
* 注意，javadoc功能可能会导致Gradle报错（注释格式不规范或其它原因等），这时候可以尝试将pomJavaDoc置为false，不输出javadoc文档