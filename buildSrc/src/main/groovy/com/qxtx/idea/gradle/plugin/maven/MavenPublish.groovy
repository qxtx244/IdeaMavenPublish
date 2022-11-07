package com.qxtx.idea.gradle.plugin.maven

import com.qxtx.idea.gradle.plugin.maven.impl.MavenPublishImpl
import org.gradle.api.Project

/**
 * maven发布插件
 */
class MavenPublish extends Base {

    private MavenPublishImpl impl = null

    @Override
    void start(Project target) {
        if (config == null) {
            println("$TAG 未被配置。请修改后，重新build工程。参考示例：\n" +
                    "$EXT_NAME {\n" +
                    "\t//描述\n" +
                    "\tpubDesc '这是一个用于插件使用演示的库'\n" +
                    "\t//可选配置，maven仓库地址\n" +
                    "\tpubMavenCentral 'http://abc/maven/mycentral'\n" +
                    "\t//...\n" +
                    "}")
            return
        }

        target.apply plugin: 'maven-publish'

        impl = new MavenPublishImpl()
        impl.apply(target)
    }
}