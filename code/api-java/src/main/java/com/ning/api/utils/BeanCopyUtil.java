package com.ning.api.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author NingWest
 * @date 2022/7/26 17:16
 * <p>
 * Bean copy的工具类
 */
public class BeanCopyUtil {

    /**
     * 单个Bean的拷贝
     *
     * @param source 原数据
     * @param clazz  目标数据的字节码对象
     * @param <V>    实际类型
     * @return 返回实际类型的参数
     */
    public static <V> V copyBean(Object source, Class<V> clazz) {
        V v = null;
        try {
            v = clazz.newInstance();
            BeanUtils.copyProperties(source, v);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return v;
    }

    /**
     * 返回集合Bean的拷贝
     *
     * @param source 原数据集合
     * @param clazz  目标数据的class字节码对象
     * @param <V>    泛型数据 clazz
     * @param <S>    泛型数据 source
     * @return 返回的泛型数据集合
     */
    public static <S, V> List<V> copyBeans(List<S> source, Class<V> clazz) {
        return source.stream().map(s -> {
            if (Objects.isNull(s)) {
                return null;
            }
            return copyBean(s, clazz);
        }).collect(Collectors.toList());
    }

}
