package io.sidecar.util;


import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;


import java.util.List;
import java.util.Set;

public class CollectionUtils {

    public static <T> ImmutableList<T> filterNulls(List<T> orig) {
        if (orig == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf(Iterables.filter(orig, new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t != null;
            }
        }));
    }


    public static <T> ImmutableSet<T> filterNulls(Set<T> orig) {
        if (orig == null) {
            return ImmutableSet.of();
        }
        return ImmutableSet.copyOf(Iterables.filter(orig, new Predicate<T>() {
            @Override
            public boolean apply(T t) {
                return t != null;
            }
        }));
    }

}
