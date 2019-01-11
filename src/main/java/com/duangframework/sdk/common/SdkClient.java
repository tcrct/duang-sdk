package com.duangframework.sdk.common;

import com.duangframework.encrypt.core.EncryptFactory;
import com.duangframework.encrypt.core.EncryptType;
import com.duangframework.encrypt.core.EncryptUtils;
import com.duangframework.sdk.constant.Constant;
import com.duangframework.sdk.enums.HttpMethod;
import com.duangframework.sdk.http.HttpRequest;
import com.duangframework.sdk.http.HttpResult;
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

    public SdkClient(String endpoint, String appKey, String appSecret) {
        this(endpoint, new CredentialsProvider(appKey, appSecret), new ClientConfiguration());
    }

    public SdkClient(String endpoint, CredentialsProvider credentialsProvider, ClientConfiguration  config) {
        setEndPoint(endpoint);
        this.credentialsProvider = credentialsProvider;
        this.configuration = config;
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
        headerMap.put(HttpHeaderNames.AUTHORIZATION, Constant.DUANG_FIELD_PREFIX + "-" + appKey);
        headerMap.put(HttpHeaderNames.DATE, Long.toString(System.currentTimeMillis()));
        headerMap.put(HttpHeaderNames.USER_AGENT, SdkUtils.getDefaultUserAgent());
        headerMap.put(HttpHeaderNames.ACCEPT, request.getContentType());
        headerMap.put(HttpHeaderNames.CONTENT_TYPE, request.getContentType());
        headerMap.put(HttpHeaderNames.DATE, System.currentTimeMillis()+"");
        headerMap.put(HttpHeaderNames.NONCE, EncryptUtils.getRandomStr());
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
        String secret = credentialsProvider.getAppSecret();
        HttpRequest httpRequest = null;
        String api = request.getRequestApi();
        api = (api.startsWith("/") ? api : "/"+ api);
        String uri = endPoint.toString() + api;
        Map<String,Object> paramsMap = request.getParamMap();
        Map<String,String> headerMap = builderHeader(appKey, request);
        EncryptDto dto = new EncryptDto(api, headerMap, paramsMap);
//        String timeStamp = SdkUtils.getRequestHeaderTimeStamp(headerMap);
//        String nonce = SdkUtils.getRequestHeaderNonce(headerMap);
//        String key = credentialsProvider.getAppKey();
        // Sha1签名方式
//        String signString = SignUtils.signSha1(dto, key, secret, timeStamp, nonce);
        // Sha256签名方式
        String signString = EncryptFactory.signSha256(dto, secret);
        paramsMap.put(Constant.DUANG_SIGN_KEY, signString);
        headerMap.put(Constant.DUANG_ENCRYPT_TYPE, EncryptType.SHA256.name());
        headerMap.put(Constant.DUANG_HEADER_SIGN_KEY, signString);

        // 请求类型
        HttpMethod method = request.getMethod();
        if(method.equals(HttpMethod.GET)) {
            httpRequest = HttpRequest.get(uri, paramsMap, true).headers(headerMap);
        } else if (method.equals(HttpMethod.POST)) {
            httpRequest = HttpRequest.post(uri, true).headers(headerMap).send(JsonUtils.toJson(paramsMap).getBytes());
        } else if (method.equals(HttpMethod.OPTIONS)) {
            httpRequest = HttpRequest.options(uri);
        } else {
            throw new IllegalArgumentException("暂不支持[\"" + method.name() +"\"]请求！");
        }

        // 请求返回信息
        HttpResult httpResult = new HttpResult(httpRequest);
        return new SdkResponse(httpResult.getCode(), httpResult.getResult(), httpResult.getHeaders());
    }
}
