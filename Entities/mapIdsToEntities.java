package com.sadeem.smap.util;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SMAPUtil {

    public static <T> Set<T> mapIdsToEntities(Set<Long> ids, Function<Long, Optional<T>> finder) {
        return ids.stream()
                .map(finder)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}