package com.duangframework.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author laotang
 */
public class ToolsKit {

    private static final Logger logger = LoggerFactory.getLogger(ToolsKit.class);

    public final static Map<String, String> HTML_CHAR = new HashMap<String, String>();

    /***
     * 判断传入的对象是否为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,为空或等于0时返回true
     */
    public static boolean isEmpty(Object obj) {
        return checkObjectIsEmpty(obj, true);
    }

    /***
     * 判断传入的对象是否不为空
     *
     * @param obj
     *            待检查的对象
     * @return 返回的布尔值,不为空或不等于0时返回true
     */
    public static boolean isNotEmpty(Object obj) {
        return checkObjectIsEmpty(obj, false);
    }

    @SuppressWarnings("rawtypes")
    private static boolean checkObjectIsEmpty(Object obj, boolean bool) {
        if (null == obj) {
            return bool;
        }
        else if (obj == "" || "".equals(obj)) {
            return bool;
        }
        else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            try {
                Double.parseDouble(obj + "");
            } catch (Exception e) {
                return bool;
            }
        } else if (obj instanceof String) {
            if (((String) obj).length() <= 0) {
                return bool;
            }
            if ("null".equalsIgnoreCase(obj+"")) {
                return bool;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).size() == 0) {
                return bool;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 0) {
                return bool;
            }
        }
        return !bool;
    }

    /**
     *  将字符串日期根据format格式化字段转换成日期类型
     * @param stringDate    字符串日期
     * @param format           格式化日期
     * @return
     */
    public static Date parseDate(String stringDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查该类是否支持实例化
     * <p>如果是抽象类或接口类则返回false</p>
     *
     * @param clazz
     * @return
     */
    public static boolean supportInstance(Class<?> clazz) {
        if(null == clazz) {
            return false;
        }
        //
        if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            return false;
        }
        return true;
    }

    /**
     * HTML字符转换表
     */
    public static final StringBuilder toHTMLChar(String str) {
        if (str == null) {
            return new StringBuilder();
        }
        StringBuilder sb = new StringBuilder(str);
        char tempChar;
        String tempStr;
        for (int i = 0; i < sb.length(); i++) {
            tempChar = sb.charAt(i);
            if (HTML_CHAR.containsKey(Character.toString(tempChar))) {
                tempStr = HTML_CHAR.get(Character.toString(tempChar));
                sb.replace(i, i + 1, tempStr);
                i += tempStr.length() - 1;
            }
        }
        return sb;
    }

    public static final String htmlChar2String(String htmlChar) {
        if (isEmpty(htmlChar)) {
            return "";
        }
        for (Iterator<Map.Entry<String, String>> it = HTML_CHAR.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            htmlChar = htmlChar.replace(entry.getValue(), entry.getKey());
        }
        return htmlChar;
    }

    /**
     * 验证是否为MongoDB 的ObjectId
     *
     * @param str
     *            待验证字符串
     * @return  如果是则返回true
     */
    public static boolean isValidDuangId(String str) {
        if (ToolsKit.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        if (len != 24) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ((c < '0') || (c > '9')) {
                if ((c < 'a') || (c > 'f')) {
                    if ((c < 'A') || (c > 'F')) {
                        logger.warn(str + " is not DuangId!!");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
