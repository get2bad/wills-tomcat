package com.wills.tomcat.example;

import com.wills.tomcat.bootstrap.BootStrap;

/**
 * @ClassName Example
 * @Date 2021/8/19 12:49
 * @Author 王帅
 * @Version 1.0
 * @Description
 */
public class Example {


    /**
     * wills-tomcat 的程序启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        BootStrap bootstrap = new BootStrap();
        try {
            // 启动wills-tomcat
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
