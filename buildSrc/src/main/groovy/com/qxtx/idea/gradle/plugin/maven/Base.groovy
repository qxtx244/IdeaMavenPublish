package com.qxtx.idea.gradle.plugin.maven

import com.qxtx.idea.gradle.plugin.maven.extension.MavenPublishExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

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
     * @param project module的org.gradle.api.Project对象
     */
    abstract void start(Project target)
}