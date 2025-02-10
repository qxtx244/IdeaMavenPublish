package com.qxtx.idea.gradle.mavenpublish_java

import com.qxtx.idea.gradle.mavenpublish_java.impl.MavenPublishImpl
import org.gradle.api.Project

/**
 * @author QXTX-WIN
 * <p>
 * createDate 2022/11/13 21:08
 * <p>
 * Description  java/groovy library的maven上传实现插件
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