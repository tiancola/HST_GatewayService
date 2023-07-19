/**
 * @date:2023/5/26 10:01
 * @author:Mr.zhao
 * @Description:
 **/

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sha256Util {
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
//计算时间戳
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        ZoneOffset offset = ZoneOffset.ofHours(8);
        long epochMilli = now.toEpochSecond(offset) * 1000;
        return String.valueOf(epochMilli);
    }

    public static void main(String[] args) {
        // 测试计算签名的示例数据
        String app_id = "hst_cadx";
        String app_secret = "6c03430ssd374809a8ff7b67cd0ec328";
//        时间戳
        String ts = getCurrentTimestamp();
//        String ts = "1685930602000";
        System.out.println(ts);
        String dev_id = "DEV0133060401035";
        String dev_type = "screen";
        String ctl_type = "get";
        String sid = "111";
        String data = "";
        String str = "";
        str = app_id + app_secret + ts + dev_id + dev_type + ctl_type + sid + data;
//接口文档提供的加密算法
        String sign = getSHA256StrJava(str);
        System.out.println("Sign: " + sign);
    }
}
