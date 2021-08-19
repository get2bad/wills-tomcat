package com.wills.tomcat.filter;

import com.wills.tomcat.http.Request;
import com.wills.tomcat.http.Response;

/**
 * @ClassName Fileter
 * @Date 2021/8/19 12:48
 * @Author 王帅
 * @Version 1.0
 * @Description
 * 拦截器接口类
 */
public interface Filter {


    public boolean filter(Request request, Response response);
}
