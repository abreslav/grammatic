package org.abreslav.models.util;

/**
 * @author abreslav
 */
public class CastUtils {
    public static <T> T cast(Object o, Class<T> clazz, String message) {
        if (!clazz.isInstance(o)) {
            String found = " (found: " + (o != null ? o.getClass().getSimpleName() : "null") + ")";
            throw new IllegalArgumentException(message + found);
        }
        @SuppressWarnings({"unchecked", "UnnecessaryLocalVariable"})
        T result = (T) o;
        return result;
    }
}
