# wills-tomcat

> Wills 自制简易版 tomcat，仅仅是处理基本的 web.xml配置的servlet以及过滤器filter，如果servlet没有配置，自动返回resources目录下的指定的html文件

## 关键类

[测试类](src/main/java/com/wills/tomcat/example/Example.java)

[启动器配置文件](src/main/resources/tomcat.properties)

[web.xml](src/main/resources/web.xml)

[启动类](src/main/java/com/wills/tomcat/bootstrap/BootStrap.java)

[测试过滤器类](src/main/java/com/wills/tomcat/example/filter/WillsFilter.java)

[测试Servlet](src/main/java/com/wills/tomcat/example/servlet/WillsServlet.java)

## 如何开启？

执行一下 com.wills.tomcat.bootstrap.BootStrap 中 main 方法即可



## 如何更改相关参数？

修改 tomcat.properties 配置文件下的内容即可

### 相关参数解读

```properties
# 启动使用的端口
server.port=8080
# 核心线程容量
server.corePoolSize=10
# 最大线程池容量
server.maximumPoolSize=100
# 保持时间
server.keepAliveTime=100
# 保持时间的时间单位 1秒 2分钟 3小时 4天 5毫秒
# 1: SECONDS 2: MINUTES 3: HOURS 4: DAYS 5: MILLISECONDS
server.keepAliveTime.timeUnit=1
```



## 如何增加过滤器？

1. 在 web.xml 中添加以下内容

   ```xml
   <filter>
       <filter-name>过滤器名字</filter-name>
       <filter-class>过滤器的引用路径(com.wills.tomcat.example.filter.WillsFilter)</filter-class>
   </filter>
   ```

2. 新建一个类，实现 Filter接口，重写其filter方法

   [测试过滤器类](src/main/java/com/wills/tomcat/example/filter/WillsFilter.java)

3. 启动项目，自动加载并自动过滤



## 相关效果截图

### 请求内部html静态资源效果图

![](http://image.tinx.top/20210819135412.png)

### 访问内部 servlet 效果图

![](http://image.tinx.top/20210819135436.png)

### 请求被过滤效果图

![](http://image.tinx.top/20210819135550.png)

![](http://image.tinx.top/20210819135837.png)
