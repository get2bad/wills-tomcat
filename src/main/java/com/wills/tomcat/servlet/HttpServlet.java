package com.wills.tomcat.servlet;

import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;

/**
 * 抽象类 HttpServlet 继承 Servlet接口类，作为一个模板
 */
public abstract class HttpServlet implements Servlet {


    public abstract void doGet(Request request,Response response);

    public abstract void doPost(Request request,Response response);


    @Override
    public void service(Request request, Response response) throws Exception {
        if("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }
}
