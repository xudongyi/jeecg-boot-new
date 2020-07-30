package org.jeecg.modules.business.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonsUtil {

    public CommonsUtil() {
    }

    public static String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static String createUUID1() {
        return createUUID().replaceAll("-", "");
    }

    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map)obj).isEmpty();
        } else if (!(obj instanceof Object[])) {
            return false;
        } else {
            Object[] object = (Object[])((Object[])obj);
            if (object.length == 0) {
                return true;
            } else {
                boolean empty = true;

                for(int i = 0; i < object.length; ++i) {
                    if (!isNullOrEmpty(object[i])) {
                        empty = false;
                        break;
                    }
                }

                return empty;
            }
        }
    }

    public static String dateCurrent(String formatStr) {
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        return sf.format(new Date());
    }

    public static String dateCurrent() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date());
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    public static String dateFormat(Date date, String formatStr) {
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);
        return sf.format(date);
    }

    public static Date dateParse(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return sf.parse(date);
        } catch (ParseException var3) {
            log.error("日期转换出错", var3);
            return null;
        }
    }

    public static Date dateParse(String date, String formatStr) {
        SimpleDateFormat sf = new SimpleDateFormat(formatStr);

        try {
            return sf.parse(date);
        } catch (ParseException var4) {
            log.error("日期转换出错", var4);
            return null;
        }
    }

    public static Date hour(int hour) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.set(11, cl.get(11) + hour);
        return cl.getTime();
    }

    public static Date hour(Date date, int hour) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.set(11, cl.get(11) + hour);
        return cl.getTime();
    }

    public static String day(int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.set(5, cl.get(5) + day);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(cl.getTime());
    }

    public static Date day(Date date, int day) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.set(5, cl.get(5) + day);
        return cl.getTime();
    }

    public static String month(int month) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(new Date());
        cl.set(2, cl.get(2) + month);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(cl.getTime());
    }

    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        } else {
            Random randGen = new Random();
            char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            char[] randBuffer = new char[length];

            for(int i = 0; i < randBuffer.length; ++i) {
                randBuffer[i] = numbersAndLetters[randGen.nextInt(62)];
            }

            return new String(randBuffer);
        }
    }

    public static final String randomNum(int length) {
        if (length < 1) {
            return null;
        } else {
            Random randGen = new Random();
            char[] numbersAndLetters = "0123456789".toCharArray();
            char[] randBuffer = new char[length];

            for(int i = 0; i < randBuffer.length; ++i) {
                randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
            }

            return new String(randBuffer);
        }
    }

    public static final String Base64EnCoder(byte[] str) {
        BASE64Encoder Encoder = new BASE64Encoder();
        return Encoder.encode(str);
    }

    public static final byte[] Base64Decoder(String str) {
        BASE64Decoder decoder = new BASE64Decoder();

        try {
            return decoder.decodeBuffer(str);
        } catch (IOException var3) {
            return null;
        }
    }

    public static final boolean isWin() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        return os.startsWith("win") || os.startsWith("Win");
    }

    public static Properties getProperties(String fileName) {
        if (fileName != null && !fileName.equals("")) {
            fileName = "resources/" + fileName;
        } else {
            fileName = "resources/constant.properties";
        }

        Properties properties = new Properties();
        InputStream inputStream = CommonsUtil.class.getClassLoader().getResourceAsStream(fileName);

        try {
            properties.load(inputStream);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return properties;
    }

    public static String toJsonStr(Object o) {
        String result = "";

        try {
            if (o instanceof Collection) {
                result = JSONArray.toJSONString(o);
            } else if (o instanceof Map) {
                result = JSONArray.toJSONString(o);
            } else if (o instanceof Object[]) {
                result = JSONArray.toJSONString(o);
            } else {
                result = JSONObject.toJSONString(o);
            }
        } catch (Exception var3) {
            log.error("对象转JSON字符串出错", var3);
        }

        return result;
    }

    public static Object toJsonObject(String str, Class<?> entity) {
        Object result = null;

        try {
            if (str != null && str.startsWith("[")) {
                if (entity != null) {
                    result = JSONArray.parseArray(str, entity);
                } else {
                    result = JSONArray.parse(str);
                }
            } else if (entity != null) {
                result = JSONObject.parseObject(str, entity);
            } else {
                result = JSONObject.parse(str);
            }
        } catch (Exception var4) {
            log.error(str + " 转JSON出错", var4);
        }

        return result;
    }

    public static String getProjectName() {
        String path = CommonsUtil.class.getResource("/").getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.lastIndexOf("/") != -1) {
            path = path.substring(0, path.lastIndexOf("/"));
        }

        if (path.lastIndexOf("/") != -1) {
            path = path.substring(0, path.lastIndexOf("/"));
        }

        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }

    public static double numberFormat(double number) {
        BigDecimal formatNumber = new BigDecimal(number);
        double result = formatNumber.setScale(2, 4).doubleValue();
        return result;
    }

    public static double numberFormat(double number, int scale) {
        BigDecimal formatNumber = new BigDecimal(number);
        double result = formatNumber.setScale(scale, 4).doubleValue();
        return result;
    }

    public static double numberFormat(double number, int scale, int roundingMode) {
        BigDecimal formatNumber = new BigDecimal(number);
        double result = formatNumber.setScale(scale, roundingMode).doubleValue();
        return result;
    }

    public static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public static boolean isNumeric(char c) {
        return Character.isDigit(c);
    }

    public static String retainNumeric(String str) {
        String result = "";
        char[] list = str.toCharArray();
        char[] var3 = list;
        int var4 = list.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            if (isNumeric(c) || c == '.') {
                result = result + c;
            }
        }

        return result;
    }

    public static <T> void mergeObject(T source, T target, String ignoreProperties, String ignoreNoProperties) {
        if (target != null && source != null) {
            if (target.getClass().equals(source.getClass())) {
                Field[] fields = source.getClass().getDeclaredFields();

                for(int i = 0; i < fields.length; ++i) {
                    try {
                        String name = fields[i].getName();
                        fields[i].setAccessible(true);
                        Object value = fields[i].get(source);
                        String type = fields[i].getGenericType().toString();
                        if (ignoreProperties.indexOf(name) != -1) {
                            fields[i].set(target, value);
                        } else if (ignoreNoProperties.indexOf(name) == -1 && null != value) {
                            if (type.equals("class java.lang.String")) {
                                if (!"".equals(value)) {
                                    fields[i].set(target, value);
                                }
                            } else if (type.equals("int")) {
                                if ((Integer)value != 0) {
                                    fields[i].set(target, value);
                                }
                            } else if (type.equals("double")) {
                                if ((Double)value != 0.0D) {
                                    fields[i].set(target, value);
                                }
                            } else if (type.equals("Long")) {
                                if ((Long)value != 0L) {
                                    fields[i].set(target, value);
                                }
                            } else if (type.equals("float")) {
                                if ((Float)value != 0.0F) {
                                    fields[i].set(target, value);
                                }
                            } else if (type.equals("class java.math.BigDecimal")) {
                                BigDecimal val = (BigDecimal)value;
                                if (val.compareTo(BigDecimal.ZERO) != 0) {
                                    fields[i].set(target, value);
                                }
                            } else {
                                fields[i].set(target, value);
                            }
                        }

                        fields[i].setAccessible(false);
                    } catch (Exception var10) {
                        System.err.println(var10);
                    }
                }

            }
        }
    }
}