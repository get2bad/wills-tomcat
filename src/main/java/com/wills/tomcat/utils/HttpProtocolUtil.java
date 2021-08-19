package com.wills.tomcat.utils;

/**
 * http协议工具类，主要是提供响应头信息，这里我们只提供200和404的情况
 */
public class HttpProtocolUtil {

    /**
     * 为响应码200提供请求头信息
     * @return
     */
    public static String getHttpHeader200(long contentLength) {
        return "HTTP/1.1 200 OK \n" +
                "Content-Type: text/plain;charset=UTF-8 \n" +
                "Content-Length: " + contentLength + " \n" +
                "\r\n";
    }

    /**
     * 为响应码404提供请求头信息(此处也包含了数据内容)
     * @return
     */
    public static String getHttpHeader404() {
        String str404 = "<h1>404 😭 为找到目标资源文件！</h1>";
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/plain;charset=UTF-8 \n" +
                "Content-Length: " + str404.getBytes().length + " \n" +
                "\r\n" + str404;
    }
}
