package com.duangframework.sdk.constant;

import java.util.concurrent.ExecutorService;

public class Constant {

    /**
     * connectionPool
     **/
    private int maxIdleConnections = 5;
    private long maxIdleTimeMillis = 60 * 1000L;
    private long keepAliveDurationMillis = 5000L;

    /**
     * timeout
     **/
    private long connectionTimeoutMillis = 15000L;
    private long readTimeoutMillis = 15000L;
    private long writeTimeoutMillis = 15000L;

    /**
     * dispatcher
     **/
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private Runnable idleCallback = null;
    private ExecutorService executorService = null;


    /**
     * 表单类型Content-Type
     */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=UTF-8";
    /**
     * 流类型Content-Type
     */
    public static final String CONTENT_TYPE_STREAM = "application/octet-stream; charset=UTF-8";
    /**
     * JSON类型Content-Type
     */
    public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    /**
     * XML类型Content-Type
     */
    public static final String CONTENT_TYPE_XML = "application/xml; charset=UTF-8";
    /**
     * 文本类型Content-Type
     */
    public static final String CONTENT_TYPE_TEXT = "application/text; charset=UTF-8";

    public static final String OPENAPI_INFO_FILE = "openapi.properties";
    public static final String ENDPOINT_FIELD = "endpoint";
    public static final String APPKEY_FIELD = "appkey";
    public static final String APPSECRET_FIELD = "appsecret";

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getMaxIdleTimeMillis() {
        return maxIdleTimeMillis;
    }

    public void setMaxIdleTimeMillis(long maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
    }

    public long getKeepAliveDurationMillis() {
        return keepAliveDurationMillis;
    }

    public void setKeepAliveDurationMillis(long keepAliveDurationMillis) {
        this.keepAliveDurationMillis = keepAliveDurationMillis;
    }

    public long getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(long connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(long readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public long getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    public void setWriteTimeoutMillis(long writeTimeoutMillis) {
        this.writeTimeoutMillis = writeTimeoutMillis;
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public int getMaxRequestsPerHost() {
        return maxRequestsPerHost;
    }

    public void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
    }

    public Runnable getIdleCallback() {
        return idleCallback;
    }

    public void setIdleCallback(Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    /*
    private void initProperties() {
        InputStream inputStream = SdkUtils.class.getClassLoader().getResourceAsStream(Constant.OPENAPI_INFO_FILE);
        Properties properties = new Properties();
        try {
            if (inputStream == null) {
                throw new IllegalArgumentException(Constant.OPENAPI_INFO_FILE + " not found on classpath");
            }
            properties.load(inputStream);

            String endPoint = properties.getProperty(Constant.ENDPOINT_FIELD);
            String appKey = properties.getProperty(Constant.APPKEY_FIELD);
            String appSecret = properties.getProperty(Constant.APPSECRET_FIELD);

        } catch (Exception e) {
            System.out.println("Unable to load version information for the running SDK: " + e.getMessage());
        }
    }
    */
}
