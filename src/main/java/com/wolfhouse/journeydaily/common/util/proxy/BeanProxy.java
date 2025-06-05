package com.wolfhouse.journeydaily.common.util.proxy;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Function;

/**
 * Bean 对象代理类，用于增强对象功能
 *
 * @author linexsong
 */
public class BeanProxy<T> implements Proxy<T> {
    private T obj;
    private Class<T> clazz;

    @Override
    public T getOrigin() {
        return obj;
    }

    public BeanProxy(T obj) {
        this.setObj(obj);
    }

    public BeanProxy(Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.clazz = clazz;
        this.obj = clazz.getDeclaredConstructor().newInstance();
    }

    public static <E> BeanProxy<E> of(E obj) {
        return new BeanProxy<>(obj);
    }

    public static <E> BeanProxy<E> of(Class<E> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new BeanProxy<>(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setObj(T obj) {
        this.obj = obj;
        this.clazz = (Class<T>) obj.getClass();
    }

    public Boolean isAnyPropBlank() throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(null, null, null, null);
    }

    public Boolean isAnyPropBlank(String... props) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(null, null, props, null);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
        return this.isAnyPropBlank(e, null, null, null);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String[] props) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(e, null, props, null);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String[] props,
                                  Function<Object, Boolean> func) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(e, null, props, func);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String errorMsg) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(e, errorMsg, null, null);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String errorMsg,
                                  String[] props) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(e, errorMsg, props, null);
    }

    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String errorMsg,
                                  Function<Object, Boolean> func) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        return this.isAnyPropBlank(e, errorMsg, null, func);
    }


    /**
     * 检查空属性业务方法，如果指定异常类则在属性为空时抛出异常
     *
     * @param e        异常类，如存在空属性则抛出该异常
     * @param errorMsg 异常信息，若不为 null 则以该信息作为异常信息。需要异常类拥有对应的构造方法。
     * @param props    指定需要检查的属性名
     * @param func     指定用于检查的方法
     * @throws IllegalAccessException
     * @throws NoSuchMethodException     方法不存在异常
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public Boolean isAnyPropBlank(Class<? extends RuntimeException> e,
                                  String errorMsg,
                                  String[] props,
                                  Function<Object, Boolean> func) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        boolean specified = props != null && props.length > 0;

        // 若指定属性，则执行指定属性的检查方法
        String blankField = specified ? checkBlankSpecified(props, func) : checkBlank(func);

        if (blankField != null) {
            return !error(e, errorMsg == null ?
                    "[Error: Field[" + blankField + "] is Blank]" : errorMsg + "[" + blankField + "]");
        }

        // 所有属性全不为空
        return false;
    }

    /**
     * 全部属性查空核心方法
     *
     * @return 是否有空
     * @throws IllegalAccessException 属性不可访问
     */
    private String checkBlank() throws IllegalAccessException {
        return checkBlank(null);
    }

    /**
     * 全部属性查空核心方法
     *
     * @param func 指定查空方法
     * @return 是否有空
     * @throws IllegalAccessException 属性不可访问
     */
    private String checkBlank(Function<Object, Boolean> func) throws IllegalAccessException {
        for (Field field : this.clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object o = field.get(this.obj);
            if (isBlank(o, func)) {
                return field.getName();
            }
        }
        return null;
    }

    /**
     * 指定属性查空核心方法
     *
     * @param props 指定属性列表
     * @return 是否有空
     * @throws NoSuchFieldException   指定属性不存在
     * @throws IllegalAccessException 指定属性不可访问
     */
    private String checkBlankSpecified(String... props) throws NoSuchFieldException, IllegalAccessException {
        return checkBlankSpecified(props, null);
    }

    /**
     * 指定属性查空核心方法
     *
     * @param props 指定属性列表
     * @param func  指定查空方法
     * @return 是否有空
     * @throws NoSuchFieldException   指定属性不存在
     * @throws IllegalAccessException 指定属性不可访问
     */
    private String checkBlankSpecified(String[] props,
                                       Function<Object, Boolean> func) throws NoSuchFieldException, IllegalAccessException {
        for (String prop : props) {
            Field field = this.clazz.getDeclaredField(prop);
            field.setAccessible(true);

            Object o = field.get(this.obj);
            if (func != null && !func.apply(o)) {
                return prop;
            }
            if (isBlank(o, func)) {
                return prop;
            }
        }
        return null;
    }

    /**
     * 查空统一方法
     *
     * @param o 需要查空的对象
     * @return 是否为空
     */
    private Boolean isBlank(Object o) {
        return isBlank(o, null);
    }

    /**
     * 查空统一方法
     *
     * @param o    需要查空的对象
     * @param func 查空所用方法
     * @return 是否为空
     */
    private Boolean isBlank(Object o, Function<Object, Boolean> func) {
        if (func == null) {
            return Objects.isNull(o);
        }
        return func.apply(o);
    }

    /**
     * 抛出指定类型的错误信息，若不指定异常类，则返回 false
     *
     * @param e        异常类
     * @param errorMsg 异常信息
     * @return 若不指定异常类，则返回 false
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Boolean error(Class<? extends RuntimeException> e,
                          String errorMsg) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (e == null) {
            return false;
        }
        throw isBlank(errorMsg) ? e.getDeclaredConstructor().newInstance() : e.getDeclaredConstructor(String.class)
                                                                              .newInstance(errorMsg);
    }
}
