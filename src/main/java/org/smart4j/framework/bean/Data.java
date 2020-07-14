package org.smart4j.framework.bean;

/**
 * @author psl
 * @date 2020/7/6
 * 返回数据对象
 */
public class Data {
    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }
    public Object getModel() {
        return this.model;
    }
}
