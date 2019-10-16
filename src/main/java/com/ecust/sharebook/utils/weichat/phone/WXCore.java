package com.ecust.sharebook.utils.weichat.phone;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;


/**
 * 封装对外访问方法
 * @author liuyazhuang
 *
 */
public class WXCore {

    private static final String WATERMARK = "watermark";
    private static final String APPID = "appid";
    /**
     * 解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(String appId, String encryptedData, String sessionKey, String iv){
        String result = "";
        try {
            AES aes = new AES();
            byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                result = new String(WxPKCS7Encoder.decode(resultByte));
                JSONObject jsonObject = JSONObject.fromObject(result);
                String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
                if(!appId.equals(decryptAppid)){
                    result = "";
                }
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws Exception{
        String appId = "wx2bc9a08ee68d7dcc";
        String encryptedData = "\"b0x0cdi6qjHFHapKg9rzJhw3KncRtoeKHMvlxQarbSUhK0iCyBELsPV1z1oeTuFybOFn35aTWDlFk8EW/xlJCDaHEsE3+6sPByiNBX7jlHjS//YzmDxTFEiaPvenql6wBEVCNLTPQcHMDotgJmLQZ33wt7Dg+ivJwTqPAStEyA9gB3gpdKhZnM8TzsrtcFtlgSy6XIvPy7uuAiMNiqoIohgvh2oe6RXnjaMfGpN03z/usn52eatAJzml792YuP4OC6Tvn5huEF5Z2KQnbvp+uK6xDQGQ3dGMW8CaUKkHTo9Dtd0cpxFmbm1/bWvLl3niMvU1UL4Dkj1Fr+Bo8tCaSTTb6bzEFTtRPY9vSOdlS7uu6m2kio3uQQ0NLJDYGB+SKHiadQ/Z4LU8DVDyf8NMYt90e6ZrWQMbBNKxQATc3i3kdm7yCuIJkDh3UoY9uK/wuDuBXZ2aak7l1vi7sEQeOEWqYkctmdt84lTBJOkWlpA=";
        String iv = "hfzWtPBBSWProOG5IgpKhQ==";
        String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
     //   String iv = "r7BXXKkLb8qrSNn05n0qiA==";
        System.out.println(decrypt(appId, encryptedData, sessionKey, iv));
    }
}


