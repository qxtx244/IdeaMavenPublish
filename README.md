IdeaMavenPublish
================
提供将库发布到maven仓库的实现方案，以简化库发布的流程和难度。


## 为什么要使用maven仓库？
· `方便管理`。便于对项目库进行保存和整合；团队可通过简单的依赖拉取共享代码；  
· `节省时间`。从仓库中拉取下来的库不需要编译，而直接导入代码则每次构建都需要；  
· `代码一致性`。相对于直接导入源码，maven仓库拉取的方式避免了因使用者的失误而对代码进行了不合适的修改，导致产生多份不同的代码；  
· `依赖聚合`。借助pom文件，多个版本库可以实现自动关联依赖，从而能够提供十分简洁的使用方式，如需要引入多个库，使用maven拉取只需要添加一条依赖即可。


## 为什么要使用脚本？
要将项目库发布到maven仓库，需要熟悉maven插件的配置方法，并且不同版本的maven插件在使用上也有差异。
现在，我们有足够简单的Gradle脚本来轻易地实现将自己的库发布到maven仓库，无论是本地仓库还是远程仓库，无论是java还是kotlin。
这些Gradle脚本适用于大部分场景，并且参数配置也比较灵活。


## 使用脚本
不同的Gradle插件版本支持的maven插件版本不同，不同的maven插件用法也有所差异：  
· `Gradle6.x或更早版本`，支持的maven插件为'maven'；  
· `Gradle7.x或更高版本`，支持的maven插件为'maven-publish'。

脚本文件：对应版本的脚本使用方法请参考[scripts][1]目录内的[README.md][2]。

### demo
* [lib-android-java][3] 演示了如何使用提供的脚本将使用纯java编写的android library发布到指定的maven仓库；
* [lib-android-kt][4] 演示了如何使用提供的脚本将使用纯kotlin编写的android library发布到指定的maven仓库；
* [lib-android-java-kt][5] 演示了如何使用提供的脚本将使用java和kotlin混合编写的android library发布到指定的maven仓库；
* [lib-java-java][6] 演示了如何使用提供的脚本将使用纯java编写的java library发布到指定的maven仓库。


## 使用插件
当前插件仅支持 Gradle 7.x 或更高版本。
### 发布 java library 到 maven 仓库 
1. 在 module 的 build.gradle 中，添加 gradle 插件依赖：
```groovy
//注意，buildscript{} 代码块应位于任何其它代码之上，也就是文件最前面
buildscript {
    repositories {
        mavenCentral()  //maven 中央仓库
    }
    dependencies {
        classpath 'com.qxtx.idea.gradle:IdeaMavenPublishJava:1.0.0'  //插件依赖
    }
}
```
3. 在 module 的 build.gradle 中，引入插件：
```groovy
apply plugin: 'idea-maven-publish-java'  //引入插件
MavenPublish {                            //发布配置
    pubGroupId 'group id，一般为包名'   //必须配置。插件依赖地址的组成部分（implementation 'groupId：artifactId：versionName'）
    pubVersionName '版本名称'            //必须配置。插件依赖地址的组成部分
    pubSrcDirs = ["src/main/java"]    //必须配置。java 源码（相对于 module的）目录
    pubArtifactId '插件名称，通常为 module 名称'                     //可选。插件依赖地址的组成部分，如缺省则默认使用 module 名称
    pubJavaDocEnable true                                            //可选。是否同时发布 javadoc.jar
    pubMavenCentral "${rootDir.absolutePath}/.mavenCentral"  //可选。maven 仓库地址，如缺省，默认发布到 工程\.mavenCentral
}
```
4. 构建/编译 module。可以选择 build / rebuild / assemble 当前 module。
5. 上传module。依次在 Android Studio 的 'Gradle' 面板中展开 工程名》module名》publishing，双击执行 'publish' 任务，即可完成发布。

#### demo
* [lib-java-java][7] 演示了如何使用 gradle 插件将使用纯 java 编写的 java library 发布到指定的maven仓库。

[1]: scripts
[2]: scripts/README.md
[3]: demo-use-script/lib-android-java
[4]: demo-use-script/lib-android-kt
[5]: demo-use-script/lib-android-java-kt
[6]: demo-use-script/lib-java-java
[7]: demo-use-plugin/lib-java-java
