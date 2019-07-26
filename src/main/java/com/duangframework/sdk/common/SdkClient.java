package com.duangframework.sdk.common;

import com.duangframework.encrypt.core.EncryptFactory;
import com.duangframework.encrypt.core.EncryptUtils;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.HttpMethod;
import com.duangframework.sdk.http.HttpRequest;
import com.duangframework.sdk.http.HttpResult;
import com.duangframework.sdk.utils.DuangId;
import com.duangframework.sdk.utils.JsonUtils;
import com.duangframework.sdk.utils.SdkUtils;
import com.duangframework.encrypt.core.EncryptDto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by laotang on 2018/12/30.
 * @since 1.0
 */
public class SdkClient {

    // 站点，即域名或IP部分，不包含URI
    private URI endPoint;
    // 安全请求对象
    private CredentialsProvider credentialsProvider;
    // 配置对象
    private ClientConfiguration configuration;
    // 是否参数加密码后发送请求(默认开启)
    private static boolean isParamEncrypt = true;
    // 回调地址
    private String callBackUrl = "";

    private static SdkClient  _sdkClient;

    public SdkClient(String endpoint, String appKey, String appSecret) {
        this(endpoint, new CredentialsProvider(appKey, appSecret), isParamEncrypt, "", new ClientConfiguration());
    }

    public SdkClient(String endpoint, String appKey, String appSecret, String callBackUrl) {
        this(endpoint, new CredentialsProvider(appKey, appSecret), isParamEncrypt, callBackUrl, new ClientConfiguration());
    }

    public SdkClient(String endpoint, String appKey, String appSecret, Boolean isParamEncrypt, String callBackUrl) {
        this(endpoint, new CredentialsProvider(appKey, appSecret), isParamEncrypt, callBackUrl, new ClientConfiguration());
    }

    public SdkClient(String endpoint, CredentialsProvider credentialsProvider, Boolean isParamEncrypt, String callBackUrl, ClientConfiguration  config) {
        setEndPoint(endpoint);
        this.credentialsProvider = credentialsProvider;
        this.configuration = config;
        SdkClient.isParamEncrypt = isParamEncrypt;
        this.callBackUrl = callBackUrl;
        _sdkClient = this;
    }

    public static SdkClient getInstance() {
        return _sdkClient;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public URI getEndPoint() {
        return endPoint;
    }

    private void setEndPoint(String endpoint) {
        this.endPoint = toURI(endpoint);
    }

    public CredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
    }

    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
    }

    private synchronized URI toURI(String endpoint) {
        if (!endpoint.contains("://")) {
            endpoint = "http://" + endpoint;
        }
        try {
            return new URI(endpoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * header头信息
     * @return
     */
    private Map<String, String> builderHeader(String appKey, AbstractSdkRequest request) {
        Map<String, String> headers = request.getHeaderMap();  // 自定义的请求头
        Map<String, String> headerMap = new HashMap<String, String>();     // 框架需要的请求头
        headerMap.put(HttpHeaderNames.AUTHORIZATION, Constant.FRAMEWORK_OWNER + "-" + appKey);
        headerMap.put(HttpHeaderNames.USER_AGENT, SdkUtils.getDefaultUserAgent());
        headerMap.put(HttpHeaderNames.ACCEPT, request.getContentType());
        headerMap.put(HttpHeaderNames.CONTENT_TYPE, request.getContentType());
        headerMap.put(HttpHeaderNames.DATE, System.currentTimeMillis()+"");
        headerMap.put(HttpHeaderNames.NONCE, EncryptUtils.getRandomStr());
        headerMap.put(Constant.REQUEST_ID, new DuangId().toString());
        if(null != headers && !headers.isEmpty()) {
            for(Iterator<Map.Entry<String,String>> iterator = headers.entrySet().iterator(); iterator.hasNext();){
                Map.Entry<String,String> entry = iterator.next();
                String value = entry.getValue();
                if(null != value && !value.isEmpty()) {
                    headerMap.put(entry.getKey(), value);
                }
            }
        }
        return headerMap;
    }

    public SdkResponse execute(AbstractSdkRequest request) {
        String appKey = credentialsProvider.getAppKey();
        String appSecret = credentialsProvider.getAppSecret();
        HttpRequest httpRequest = null;
        String api = request.getRequestApi();
        api = (api.startsWith("/") ? api : "/"+ api);
        String url = endPoint.toString() + api;
        Map<String,Object> paramsMap = request.getParamMap();
        paramsMap.put(Constant.CALLBACK_URL_FIELD, getCallBackUrl());
        Map<String,String> headerMap = builderHeader(appKey, request);
        // 请求类型
        HttpMethod method = request.getMethod();
        String body = method.equals(HttpMethod.GET)? HttpRequest.append(url, paramsMap) : JsonUtils.toJson(paramsMap);
        // 参数加密后发送请求
        if(isParamEncrypt) {
            body = EncryptFactory.encrypt(body, appSecret);
            headerMap.put(Constant.DUANG_ENCRYPT, isParamEncrypt+"");
        } else {
            // 签名
            EncryptDto dto = new EncryptDto(api, headerMap, paramsMap);
            // Sha256签名方式
            String signString = EncryptFactory.signSha256(dto, appSecret);
            // 将签名字符串设置到header头
            headerMap.put(Constant.DUANG_HEADER_SIGN_KEY, signString);
        }
//        System.out.println("@@@@@@@@@@@: " + body);
        if(method.equals(HttpMethod.GET)) {
            httpRequest = HttpRequest.get(body, true).trustAllCerts().trustAllHosts().headers(headerMap);
        } else if (method.equals(HttpMethod.POST)) {
            httpRequest = HttpRequest.post(url, true).trustAllCerts().trustAllHosts().headers(headerMap).send(body.getBytes());
        } else if (method.equals(HttpMethod.OPTIONS)) {
            httpRequest = HttpRequest.options(url).trustAllCerts();
        } else {
            throw new IllegalArgumentException("暂不支持[\"" + method.name() +"\"]请求！");
        }

        // 请求返回信息
        HttpResult httpResult = new HttpResult(httpRequest);
        return new SdkResponse(httpResult.getCode(), httpResult.getResult(), httpResult.getHeaders());
    }
}
