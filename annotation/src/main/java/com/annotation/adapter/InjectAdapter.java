package com.annotation.adapter;

/**
 * Created by renxl
 * On 2017/7/22 18:26.
 * <p>
 * 在 ButterKnife 中实现注入时，调用一个所有 Adapter 都实现的方法为入口，如果没有改接口声明的入口，那么得到辅助类时也不知道调用哪个方法实现注入
 */

public interface InjectAdapter<T> {
    /**
     * 实现注入的入口方法
     *
     * @param target 被注解的元素所在的类型对象，也就是生成的辅助类的父类对象，通过该父类对象可以实例化辅助类
     */
    void bind(T target);
}
