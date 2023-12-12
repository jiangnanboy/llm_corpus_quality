package com.sy.util;

import java.util.*;

/**
 * @author sy
 * @date 2023/12/7 22:33
 */
public class CollectionUtil<E, T> {

    public static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> List<E> newArrayList(int N) {
        return new ArrayList<>(N);
    }

    public static <E> List<E> newArrayList(Set<E> entry) {
        return new ArrayList<>(entry);
    }

    public static <E> Set<E> newHashset() {
        return new HashSet<>();
    }

    public static <E,T> Map<E, T> newHashMap() {
        return new HashMap<>();
    }

    public static <E,T> Map<E, T> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    public static <E,T> Map<E, T> newTreeMap() {
        return new TreeMap<>();
    }

}
