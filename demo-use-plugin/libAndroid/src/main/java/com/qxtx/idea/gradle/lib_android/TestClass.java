package com.qxtx.idea.gradle.lib_android;

/**
 * @author QXTX-WIN
 * <p>
 * <b>Create Date</b><p> 2024年12月31日 16:24
 * <p>
 * <b>Description</b>
 * <pre>
 *   仅作库的发布演示，无其它意义
 * </pre>
 */
public class TestClass {
    /**
     * 类名称字段
     */
    public String name = getClass().getSimpleName();

    /**
     * 返回类名称
     * @return 名称字段
     */
    public String get() {
        return name;
    }

    /**
     * 设置名称字段
     * @param name 目标名称
     */
    public void set(String name) {
        this.name = name;
    }
}
