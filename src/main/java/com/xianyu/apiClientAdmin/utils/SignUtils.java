package com.xianyu.apiClientAdmin.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**签名工具
 * @author happyxianfish
 */
public class SignUtils {
    /**（这里为了方便，把hashmap换成了body）
     *生成签名
     * @param body
     * @param secretKey
     * @return
     */
    static public String genSign(String body, String secretKey) {
        //将其他参数hashMap和密钥拼接在一块儿，然后再使用签名生成算法进行加密
        String content = body + secretKey;
        //以Hutool的摘要加密功能实现SHA256签名生成算法加密
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        return md5.digestHex(content);
    }
}
