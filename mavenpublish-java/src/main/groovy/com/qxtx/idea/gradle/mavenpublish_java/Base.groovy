package com.qxtx.idea.gradle.mavenpublish_java

import com.qxtx.idea.gradle.mavenpublish_java.extension.MavenPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author QXTX-WIN
 * createDate 2022/11/13 21:20
 * Description 插件基类
 */
abstract class Base implements Plugin<Project> {

    protected static final String EXT_NAME = Consts.EXTENSION_NAME
    protected static final String TAG = Consts.TAG
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