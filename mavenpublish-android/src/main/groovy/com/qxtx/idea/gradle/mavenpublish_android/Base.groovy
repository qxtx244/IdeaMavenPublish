package com.qxtx.idea.gradle.mavenpublish_android

import com.qxtx.idea.gradle.mavenpublish_android.extension.MavenPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author QXTX-WIN
 * createDate 2024/12/30 20:52
 * Description 插件基类
 */
abstract class Base implements Plugin<Project> {

    protected def EXT_NAME = Consts.EXTENSION_NAME
    protected def TAG = Consts.TAG
    protected MavenPublishExtension config = null

    @Override
    void apply(Project target) {
        def extension = target.getExtensions()
        config = extension.findByName(EXT_NAME)
        if (config == null) {
            config = extension.create(EXT_NAME, MavenPublishExtension)
        }
        target.afterEvaluate {
            start(target)
        }
    }

    /**
     * 功能实现
     * @param target module 的 org.gradle.api.Project 对象
     */
    abstract void start(Project target)
}