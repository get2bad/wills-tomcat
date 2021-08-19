package com.wills.tomcat.bootstrap;

import com.wills.tomcat.filter.Filter;
import com.wills.tomcat.processor.RequestProcessor;
import com.wills.tomcat.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ClassName BootStrap
 * @Date 2021/8/19 10:19
 * @Author 王帅
 * @Version 1.0
 * @Description 项目启动类
 */
public class BootStrap {

    private static Properties properties;

    // 基础的配置信息
    private static Integer port;
    private static Integer corePoolSize;
    private static Integer maximumPoolSize;
    private static Long keepAliveTime;
    private static TimeUnit timeUnit;

    // servlet 集合
    private Map<String, HttpServlet> servletMap = new HashMap<>();
    // 过滤器链路
    public static Map<String, Filter> filterChainMap = new LinkedHashMap<>();

    // 读取配置文件的内容
    static {
        properties = new Properties();
        InputStream stream = BootStrap.class.getClassLoader().getResourceAsStream("tomcat.properties");
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            port = Integer.parseInt(properties.getProperty("server.port", "8080"));
            corePoolSize = Integer.parseInt(properties.getProperty("server.corePoolSize", "10"));
            maximumPoolSize = Integer.parseInt(properties.getProperty("server.maximumPoolSize", "100"));
            keepAliveTime = Long.parseLong(properties.getProperty("server.keepAliveTime", "1000"));
            Integer timeUnitsFlag = Integer.parseInt(properties.getProperty("server.keepAliveTime.timeUnit"));
            switch (timeUnitsFlag) {
                case 1:
                    timeUnit = TimeUnit.SECONDS;
                    break;
                case 2:
                    timeUnit = TimeUnit.MINUTES;
                    break;
                case 3:
                    timeUnit = TimeUnit.HOURS;
                    break;
                case 4:
                    timeUnit = TimeUnit.DAYS;
                    break;
                case 5:
                    timeUnit = TimeUnit.MILLISECONDS;
                    break;
                default:
                    timeUnit = TimeUnit.SECONDS;
                    break;
            }
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void start() throws Exception {
        // 加载 web.xml
        loadServlet();

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(maximumPoolSize);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                workQueue,
                threadFactory,
                handler);

        ServerSocket socket = new ServerSocket(port);
        while (true) {
            Socket accept = socket.accept();
            // 访问 http://localhost:8080 返回data的文字后结束socket连接
//            String data = "欢迎👏🏻来到Wills-tomcat";
//            String responseText = HttpProtocolUtil.getHttpHeader200(data.getBytes().length) + data;
//            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));

            // 静态资源处理
//            if (servletMap.get(request.getUrl()) == null) {
////                访问 http://localhost:8080/index.html 返回了resources目录下的 index.html的文件内容
//                response.outputHtml(request.getUrl());
//            } else {
//                // 动态资源servlet请求
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request, response);
//            }
            // 创建一个 requestProcessor 线程，然后里面的 run 方法就是读取相关的 请求方式、请求路径、请求的Servlet等
            RequestProcessor requestProcessor = new RequestProcessor(accept,servletMap);
            // 放入线程池中执行
            executor.execute(requestProcessor);
        }
    }

    /**
     * 加载解析web.xml，初始化Servlet/filter
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            // 读取配置文件中的 servlet 信息
            loadServlet(rootElement);
            // 读取配置文件中的 filter 信息
            loadFilter(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadServlet(Element rootElement){
        try {
            // 读取 servlet 标签内容
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                // <servlet-class>com.wills.tomcat.example.servlet.WillsServlet</servlet-class>
                Element servletclassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletclassElement.getStringValue();


                // <servlet-name>wills</servlet-name>
                Element servletnameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletnameElement.getStringValue();
                // 根据servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                // /wills
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadFilter(Element rootElement){
        try{
            // 读取 filter 标签内容
            List<Element> nodes = rootElement.selectNodes("//filter");
            for (int i = 0; i < nodes.size(); i++) {
                Element element = nodes.get(i);
                Element filterNameNode = (Element) element.selectSingleNode("//filter-name");
                String filterName = filterNameNode.getStringValue();

                Element filterClassNode = (Element) element.selectSingleNode("//filter-class");
                String filterClass = filterClassNode.getStringValue();
                filterChainMap.put(filterName, (Filter) Class.forName(filterClass).newInstance());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
