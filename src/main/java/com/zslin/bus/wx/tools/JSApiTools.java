package com.zslin.bus.wx.tools;

import com.zslin.bus.common.tools.RandomTools;
import com.zslin.bus.tools.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Formatter;

/**
 * 微信JS的处理工具类
 * Created by zsl on 2018/11/22.
 */
@Component
public class JSApiTools {

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private WxConfigTools wxConfigTools;

    public JsonResult buildJSTicket(String url) {
        String jsToken = accessTokenTools.getJsTicket();

        String timeStamp = ""+(System.currentTimeMillis()/1000);//时间戳
        String nonceStr = RandomTools.randomString(16).toUpperCase();//随机字符串，不长于32位
//        String[] arr = new String[] { nonceStr, jsToken, timeStamp, url };
        // 将token、timestamp、nonce三个参数进行字典序排序
//        Arrays.sort(arr);

        String string1 = "jsapi_ticket=" + jsToken +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timeStamp +
                "&url=" + url;

        String tmpStr = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(string1.getBytes("UTF-8"));
            tmpStr = byteToHex(md.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.getInstance().set("timestamp", timeStamp)
                .set("nonceStr", nonceStr)
                .set("signature", tmpStr)
                .set("appid", wxConfigTools.getWxConfig().getAppid());
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    /*private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }*/

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    /*private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }*/
}
