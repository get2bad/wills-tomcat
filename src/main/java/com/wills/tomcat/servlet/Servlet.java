package com.wills.tomcat.servlet;

import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;

public interface Servlet {

    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
