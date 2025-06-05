package com.wolfhouse.journeydaily.common.util.proxy;

/**
 * @author linexsong
 */
public interface Proxy<T> {
    /**
     * 获取原始对象
     *
     * @return 原始对象
     */
    T getOrigin();

    /**
     * 设置原始对象
     *
     * @param obj 原始对象
     */
    void setObj(T obj);
}
