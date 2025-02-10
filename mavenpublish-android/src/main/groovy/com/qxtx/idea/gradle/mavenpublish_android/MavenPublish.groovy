package com.qxtx.idea.gradle.mavenpublish_android

import com.qxtx.idea.gradle.mavenpublish_android.extension.MavenPublishExtension
import com.qxtx.idea.gradle.mavenpublish_android.impl.MavenPublishImpl
import org.gradle.api.Project

/**
 * @author QXTX-WIN
 * <p>
 * createDate 2024/12/30 21:03
 * <p>
 * Description android library 的 maven 上传实现插件
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

        impl = new MavenPublishImpl()
        impl.apply(target)
    }
}