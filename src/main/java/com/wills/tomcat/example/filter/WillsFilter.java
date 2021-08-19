package com.wills.tomcat.example.filter;

import com.wills.tomcat.filter.Filter;
import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;

import java.io.IOException;

/**
 * @ClassName WillsFilter
 * @Date 2021/8/19 12:53
 * @Author 王帅
 * @Version 1.0
 * @Description
 * 自定义 Filter
 */
public class WillsFilter implements Filter {

    @Override
    public boolean filter(Request request, Response response){
        System.out.println("过滤器方法被调用");
        String requestUrl = request.getUrl();
        if(! "/wills".equals(requestUrl) && !"/index.html".equals(requestUrl)){
            String data = "😭对不起！您的请求被过滤！请重试！";
            try {
                response.output(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
