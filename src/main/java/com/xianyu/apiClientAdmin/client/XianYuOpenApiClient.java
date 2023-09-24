package com.xianyu.apiClientAdmin.client;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xianyu.apiClientAdmin.model.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用接口的客户端（项目内部）
 * 发出HTTP请求调用模拟的接口
 *
 * @author happyxianfish
 */
public class XianYuOpenApiClient {
    private String accessKey;
    private String sign;

    /**
     * 网关地址（本地）
     */
    private static final String HOST = "http://localhost:8090";

    public XianYuOpenApiClient(String accessKey, String sign) {
        this.accessKey = accessKey;
        this.sign = sign;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(HOST + "/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(HOST + "/api/name/url", paramMap);
        System.out.println(result);
        return result;
    }

    /**
     ** API签名认证
     *  存ak、sign（sk）、body、nonce、timestamp请求头
     *  请求头是一组k-v的结构，而map也是一组k-v的结构
     *  所以在设置请求头时，可以传入map来设置请求头
     * @return
     */
//    private Map<String, String> getHeaderMapOnline(String body) {
//        Map<String, String> hashMap = new HashMap<>();
//        //实现标准的API签名认证需6个参数，其中密钥参数一定不能直接发送
//        hashMap.put("accessKey", accessKey);
////        hashMap.put("secretKey",secretKey);
//        //body：用户请求参数
//        hashMap.put("body", body);
//        //生成100以内的随机数（偷懒模拟下）
//        hashMap.put("nonce", RandomUtil.randomNumbers(5));
//        //时间戳.（偷懒模拟下）（String.valueOf转字符串）  System.currentTimeMillis() / 1000 ???
//        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        //签名。使用签名生成算法生成签名
//        hashMap.put("sign", genSign(body, secretKey));
//        return hashMap;
//    }

    private Map<String, String> getHeaderMap(Long id,String body) {
        Map<String, String> hashMap = new HashMap<>();
        //实现标准的API签名认证需6个参数，其中密钥参数一定不能直接发送
        hashMap.put("accessKey", accessKey);
//        hashMap.put("secretKey",secretKey);
        //body：用户请求参数
        hashMap.put("body", body);
        //生成100以内的随机数（偷懒模拟下）
        hashMap.put("nonce", RandomUtil.randomNumbers(9));
        //时间戳.（偷懒模拟下）（String.valueOf转字符串）  System.currentTimeMillis() / 1000 ???
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        //签名。使用签名生成算法生成签名
        hashMap.put("sign",sign);
        hashMap.put("id",id.toString());
        return hashMap;
    }

    public String getUserNameByPost(Long id,User user) {
        String json = JSONUtil.toJsonStr(user);
        //todo 这里发送http请求时，请求参数user不支持中文，会乱码
        HttpResponse httpResponse = HttpRequest.post(HOST + "/api/name/json")
                .addHeaders(getHeaderMap(id,json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    /**
     * 通用的调用接口请求（发向网关）
     * path根据请求的接口动态变化
     * @param userParameter 用户请求参数
     * @return
     */
    public String getApiServiceByPost(Long id,String userParameter,String path,String GATEWAY_HOST) {
        //todo 这里发送http请求时，请求参数user不支持中文，会乱码
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + path)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(id,userParameter))
                .body(userParameter)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

    /**
     * 通用的调用接口请求（发向网关）
     * path根据请求的接口动态变化
     * @param userParameter 用户请求参数
     * @return
     */
    public String getApiServiceByGet(Long id,String userParameter,String path,String GATEWAY_HOST) {
        //todo 这里发送http请求时，请求参数user不支持中文，会乱码
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + path)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(id,userParameter))
                .body(userParameter)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }

}
