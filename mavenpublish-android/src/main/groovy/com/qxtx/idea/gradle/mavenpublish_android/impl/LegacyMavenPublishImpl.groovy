package com.qxtx.idea.gradle.mavenpublish_android.impl

import com.qxtx.idea.gradle.mavenpublish_android.Base
import com.qxtx.idea.gradle.mavenpublish_android.extension.MavenPublishExtension
import org.gradle.api.Project

/**
 * @author QXTX-WIN
 * createDate 2024/12/30 21:13
 * Description 插件功能的具体实现，适用于 Gradle1.2 或更低版本
 */
class LegacyMavenPublishImpl extends Base {

    @Override
    void start(Project target) {
        println "${getClass().getSimpleName()} is deprecated."
    }
}