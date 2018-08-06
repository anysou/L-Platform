package com.platform.sdk.wx.pay;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WXUtil {
    public final static String getMD5(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param params     请求业务参数
     * @param app_secure 微信开放平台的私钥
     * @return
     */
    public static String getSign(TreeMap<String, String> params, String app_secure) {
        //排除可能引起参数错误的key-value
        params.remove("sign");
        params.remove("key");
        String sign = null;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("key=").append(app_secure);
        sign = getMD5(sb.toString().getBytes()).toUpperCase();
        return sign;
    }

    /**
     * @param length 指定字符串长度  不超过32位
     * @return 一定长度的随机字符串
     */
    public static String getRandomStr(int length) {
        String base = "iundsi983sbu83hdiw1isdhcbs910skd";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 参数进行XML化
     */
    public static String parseMap2Xml(TreeMap<String, String> map, String sign) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append("<" + k + ">")
                    .append(v)
                    .append("</" + k + ">");
        }
        sb.append("<sign>").append(sign).append("</sign>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 返回格式化好的时间字符串
     *
     * @param formatStr 格式化 入"yyyy-MM-dd"
     * @return
     */
    public static String getCurrentTime(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * @param strSource 源字符串：<xml>hello</xml>
     * @param tagName   tag名称：xml
     * @return 返回：hello
     */
    public static String getXMLContent(String strSource, String tagName) {
        //去掉空格和换行符
        strSource = strSource.replaceAll("\n", "").trim();
        String regex = "(?<=<(" + tagName + ")>).*(?=</\\1>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strSource);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 解析形如 <return_msg><![CDATA[OK]]></return_msg>里面的ok字段
     *
     * @param strSource 源字符串：<return_msg><![CDATA[OK]]></return_msg>
     * @param tagName   tag名称：return_msg
     * @return 返回：OK
     */
    public static String getTAGContent(String strSource, String tagName) {
        //去掉空格和换行符
        strSource = strSource.replaceAll("\n", "").trim();
        String regex = "(?<=<(" + tagName + ")><!\\[CDATA\\[).*(?=]]></\\1>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strSource);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 将形如下面的字符串(这是微信文档上的返回示例)解析成key-value的形式放入到map中
     * 这个方法是上面两个分步骤解析的整合
     * "<xml>" +
     * "   <return_code><![CDATA[SUCCESS]]></return_code>" +
     * "   <return_msg><![CDATA[OK]]></return_msg>" +
     * "   <appid><![CDATA[wx2421b1c4370ec43b]]></appid>" +
     * "   <mch_id><![CDATA[10000100]]></mch_id>" +
     * "   <nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>" +
     * "   <sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>" +
     * "   <result_code><![CDATA[SUCCESS]]></result_code>" +
     * "   <prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>" +
     * "   <trade_type><![CDATA[APP]]></trade_type>" +
     * "</xml>"
     *
     * @param strSource
     * @return
     */
    public static Map<String, String> getTAGContent(String strSource) {
        //去掉空格和换行符
        strSource = strSource.replaceAll("\n", "").trim();
        String regex = "<([a-z_]*)><!\\[CDATA\\[(.*)(?=]]></\\1>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strSource);
        Map<String, String> params = new TreeMap<>();
        while (matcher.find()) {
            int count = matcher.groupCount();
            if (count != 2) continue;
            params.put(matcher.group(1), matcher.group(2));
        }
        return params;
    }

}
