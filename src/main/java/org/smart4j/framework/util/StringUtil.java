package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author psl
 * @date 2020/7/4
 * 字符串工具类
 */
public final class StringUtil {
    public static final String SEPARATOR = String.valueOf((char)29);

    public static boolean isEmpty(String str){
        if (str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
