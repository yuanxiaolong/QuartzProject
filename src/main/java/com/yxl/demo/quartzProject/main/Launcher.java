package com.yxl.demo.quartzProject.main;


import com.yxl.demo.quartzProject.util.PropertyUtil;
import org.apache.commons.lang.StringUtils;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 启动包装类，包装成spring bean 托管于spring
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午3:32
 */
public class Launcher {

    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    private static String port = PropertyUtil.getInstance().getProperty("server.jetty.port");

    private final Server server;

    @Autowired
    public Launcher(EmbeddedJettySpringServlet embeddedServlet) {

        LOG.info("properties port is " + port);

        Integer vaynePort = port == null ? 8091 : Integer.parseInt(port);

        server = new Server();
        Connector connector=new SocketConnector();

        String host = PropertyUtil.getInstance().getProperty("server.host");
        connector.setHost(StringUtils.isBlank(host) ? "127.0.0.1" : host);
        connector.setPort(vaynePort);
        server.setConnectors(new Connector[]{connector});
        server.addConnector(connector);



        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);
        server.setSendServerVersion(false);
        server.setSendDateHeader(false);
        server.setStopAtShutdown(true);



        ServletHolder sh = new ServletHolder(embeddedServlet);
        sh.setInitParameter(
                "com.sun.jersey.config.property.resourceConfigClass",
                RestfulResourceConfig.class.getCanonicalName());    //指定资源路径
        sh.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters",CORSFilter.class.getCanonicalName());
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

        Context context = new Context(server, "/", Context.SESSIONS);
        context.setClassLoader(embeddedServlet.getClass().getClassLoader());
        context.addServlet(sh, "/*");
    }

    public void start() throws Exception{
        server.start();
    }

    public void stop() throws Exception{
        server.stop();
    }


}
