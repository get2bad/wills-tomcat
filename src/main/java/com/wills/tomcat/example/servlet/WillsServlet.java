package com.wills.tomcat.example.servlet;

import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;
import com.wills.tomcat.servlet.HttpServlet;
import com.wills.tomcat.utils.HttpProtocolUtil;

import java.io.IOException;

public class WillsServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String content = "<h1>WillsServlet get</h1>";
        try {
            response.output(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>WillsServlet post</h1>";
        try {
            response.output(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }
}
