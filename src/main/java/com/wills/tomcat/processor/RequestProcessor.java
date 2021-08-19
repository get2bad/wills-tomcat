package com.wills.tomcat.processor;

import com.wills.tomcat.bootstrap.BootStrap;
import com.wills.tomcat.filter.Filter;
import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;
import com.wills.tomcat.servlet.HttpServlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;

public class RequestProcessor extends Thread {

    private Socket socket;
    private Map<String, HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket = socket;
        this.servletMap = servletMap;
    }

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();

            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());

            // 执行前，先执行一下 filterChain 进行过滤
            boolean filterRes = filter(request,response);
            if(!filterRes){
                return;
            }
            // 静态资源处理
            if(servletMap.get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            }else{
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 根据链路的Filter进行过滤
    public boolean filter(Request request, Response response){
        Map<String, Filter> filterChainMap = BootStrap.filterChainMap;
        for (Map.Entry<String, Filter> filter : filterChainMap.entrySet()) {
            Filter filterClazz = filter.getValue();
            boolean res = filterClazz.filter(request, response);
            if(!res){
                return res;
            }
        }
        return true;
    }
}
