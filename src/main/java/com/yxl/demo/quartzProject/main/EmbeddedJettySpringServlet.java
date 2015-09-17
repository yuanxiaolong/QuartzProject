package com.yxl.demo.quartzProject.main;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * 将jetty的上下文classloader替换成spring的，并初始化，以便能使用spring bean
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 下午3:21
 */


public class EmbeddedJettySpringServlet extends ServletContainer implements ApplicationContextAware {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJettySpringServlet.class.getName());

    private ApplicationContext springContext;

    public EmbeddedJettySpringServlet() {
        super();
    }

    @Override
    protected void initiate(ResourceConfig rc, WebApplication wa) {
        try {
            wa.initiate(rc, new SpringComponentProviderFactory(rc, (ConfigurableApplicationContext) springContext));
        } catch (RuntimeException e) {
            LOGGER.error("Exception occurred when intializing", e);
            throw e;
        }
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        springContext = applicationContext;
    }

}
