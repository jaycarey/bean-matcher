package com.jay.beanmatcher;

import java.util.List;

/**
 * @author jaycarey
 */
public class StringUtils {

    public static String join(List<String> strings, String separator) {
        StringBuilder joined = new StringBuilder();
        for (String string : strings) {
            joined.append(string).append(separator);
        }
        return joined.substring(0, joined.lastIndexOf(separator));
    }
}
