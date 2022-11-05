IdeaMavenPublish
================
提供将库发布到maven仓库的实现方案，以简化库发布的流程和难度。
> *下一个版本将做重大升级：将脚本文件改为Gradle插件方案，使用更加便捷*

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

对应版本的maven插件使用方法请参考[scripts][1]目录内的[README.md][2]。

## demo
* [demo-android-java][3] 演示了如何使用提供的脚本将使用纯java编写的android library发布到指定的maven仓库；
* [demo-android-kt][4] 演示了如何使用提供的脚本将使用纯kotlin编写的android library发布到指定的maven仓库；
* [demo-android-java-kt][5] 演示了如何使用提供的脚本将使用java和kotlin混合编写的android library发布到指定的maven仓库；
* [demo-java-java][6] 演示了如何使用提供的脚本将使用纯java编写的java library发布到指定的maven仓库。

[1]: https://github.com/qxtx244/IdeaMavenPublish/tree/master/scripts
[2]: https://github.com/qxtx244/IdeaMavenPublish/blob/master/scripts/README.md
[3]: https://github.com/qxtx244/IdeaMavenPublish/tree/master/demo-android-java
[4]: https://github.com/qxtx244/IdeaMavenPublish/tree/master/demo-android-kt
[5]: https://github.com/qxtx244/IdeaMavenPublish/tree/master/demo-android-java-kt
[6]: https://github.com/qxtx244/IdeaMavenPublish/tree/master/demo-java-java
