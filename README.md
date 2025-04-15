# IdeaMavenPublish
提供将库发布到maven仓库的实现方案，以简化库发布的流程和难度。


## 为什么要使用 maven 仓库？
· `方便管理`。便于对项目库进行保存和整合；团队可通过简单的依赖拉取共享代码；  
· `节省时间`。从仓库中拉取下来的库不需要编译，而直接导入代码则每次构建都需要；  
· `代码一致性`。相对于直接导入源码，maven 仓库拉取的方式避免了因使用者的失误而对代码进行了不合适的修改，导致产生多份不同的代码；  
· `依赖聚合`。借助pom文件，多个版本库可以实现自动关联依赖，从而能够提供十分简洁的使用方式，如需要引入多个库，使用 maven 拉取只需要添加一条依赖即可。


## 使用脚本/插件有什么好处？
要将项目库发布到 maven 仓库，需要熟悉 maven 插件的配置方法，并且不同版本的 maven 插件在使用上也有差异。
现在，我们有足够简单的 Gradle 脚本来轻易地实现将自己的库发布到 maven 仓库，无论是本地仓库还是远程仓库，无论是 java 还是 kotlin。
这些 Gradle 脚本适用于大部分场景，并且参数配置也比较灵活。


## 开发环境配置
**Android Studio 版本**：2021.3.1  
**Gradle 版本**：7.2  
**AGP 版本**：7.1.3  
**Kotlin 版本**：1.6.21  
**JDK 版本**：1.8  
版本差异不大即可。


## 使用脚本
不同的 Gradle 插件版本支持的 maven 插件版本不同，不同的 maven 插件用法也有所差异：  
· `Gradle1.2 及更早版本`，仅支持 'maven' 插件；  
· `Gradle1.3 ~ 6.1.1 版本`，同时支持 'maven' 插件和 'maven-publish' 插件；  
· `Gradle6.2 及更高版本`，仅支持 'maven-publish' 插件。

脚本文件：对应版本的脚本使用方法请参考[scripts][1]目录内的[README.md][2]。

### demo
* [lib-android-java][3] 演示了如何使用提供的脚本将使用纯 java 编写的 android library 发布到指定的 maven 仓库；
* [lib-android-kt][4] 演示了如何使用提供的脚本将使用纯 kotlin 编写的 android library 发布到指定的 maven 仓库；
* [lib-android-java-kt][5] 演示了如何使用提供的脚本将使用 java 和 kotlin 混合编写的 android library 发布到指定的 maven 仓库；
* [lib-java][6] 演示了如何使用提供的脚本将使用纯 java 编写的 java library 发布到指定的 maven 仓库。


## 使用插件
当前插件仅支持 Gradle 1.3 及更高的版本。对于不支持的 Gradle 版本，以 [Gradle 脚本][1] 的方式提供支持。

### 发布 android library 到 maven 仓库
支持发布 java & kotlin 语言混合编写的 android library，同时支持生成 javadoc、htmldoc 等文档。  
详细说明见 [插件说明][10]。

### 发布 java library 到 maven 仓库
仅支持发布使用 java 或 groovy 语言编写的 library，支持生成 javadoc、groovydoc 等文档。  
详细说明见 [插件说明][11]。

### demo
* [libJava][7] 演示了如何使用 gradle 插件将使用纯 java 编写的 java library 发布到指定的 maven 仓库。
* [libAndroid][8] 演示了如何使用 gradle 插件将使用 java 编写的 android library 发布到指定的 maven 仓库。
* [libAndroidDokka][9] 演示了如何使用 gradle 插件配合 Dokka 插件将使用 java & kotlin 编写的 android library 发布到指定的 maven 仓库。

[1]: scripts
[2]: scripts/README.md
[3]: demo-use-script/lib-android-java
[4]: demo-use-script/lib-android-kt
[5]: demo-use-script/lib-android-java-kt
[6]: demo-use-script/lib-java
[7]: demo-use-plugin/libJava
[8]: demo-use-plugin/libAndroid
[9]: demo-use-plugin/libAndroidDokka
[10]: mavenpublish-android-dokka/README.md
[11]: mavenpublish-java/README.md
