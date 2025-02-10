package com.qxtx.idea.gradle.script.android_kt

/**
 * @author QXTX-WIN
 *
 * **Create Date** 2022/11/3 22:19
 *
 * **Description**
 *
 * 仅作库的发布演示，无其它意义
 */
class MavenPublishTest {

    /** 数值  */
    private val num = 0

    /** 名称  */
    @JvmField
    var name = "demo"

    /**
     * 展示名称
     * @param s 附加的文本
     */
    fun display(s: String) {
        println("$name: $s")
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    fun getName(): String {
        return name
    }
}