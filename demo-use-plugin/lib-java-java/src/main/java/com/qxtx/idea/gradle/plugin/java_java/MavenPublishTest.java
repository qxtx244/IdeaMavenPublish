package com.qxtx.idea.gradle.plugin.java_java;

/**
 * @author QXTX-WIN
 * <p>
 * <b>Create Date</b><p> 2022/11/3 21:07
 * <p>
 * <b>Description</b>
 * <pre>
 *   仅作库的发布演示，无其它意义
 * </pre>
 */
public class MavenPublishTest {

    /** 数值 */
    private int num = 0;

    /** 名称 */
    public String name = "demo";

    /**
     * 展示名称
     * @param s 附加的文本
     */
    public void display(String s) {
        System.out.println(name + ": " + s);
    }

    /**
     * 获取名称
     * @return 名称
     */
    public String getName() {
        return name;
    }
}
