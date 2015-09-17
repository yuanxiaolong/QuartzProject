package com.yxl.demo.quartzProject.util;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Velocity工具类
 * author: xiaolong.yuanxl
 * date: 2015-06-08 下午1:22
 */
public class VelocityHelpler {

    private static final Logger LOG = LoggerFactory.getLogger(VelocityHelpler.class);

    private static Properties props = new Properties();

    static {
        props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        props.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        props.setProperty(Velocity.RESOURCE_LOADER, "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(props);
    }

    private static VelocityHelpler instance = null;

    //返回单例
    public static VelocityHelpler getInstance(){
        if (instance == null){
            instance = new VelocityHelpler();
        }
        return instance;
    }

    private VelocityHelpler(){
        // forbid
    }


    /**
     * 合并渲染
     * @param template 模板
     * @param param 参数及值
     * @return 渲染后的字符串
     */
    public String merge(String template,Map<String,Object> param){
        String ret = "";
        if (StringUtils.isBlank(template)){
            LOG.warn("merge velocity ,input illegal template: " + template + " param: " + param);
            return ret;
        }

        try{
            if (param == null){
                param = new HashMap<String, Object>();
            }
            VelocityContext context = new VelocityContext();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                context.put(entry.getKey(), entry.getValue());
            }
            StringWriter writer = new StringWriter();
            Velocity.evaluate(context, writer, "", template);
            return writer.toString();
        }catch (Exception e){
            LOG.error("merge velocity error ,input args template: " + template + " param: " + param,e);
        }
        return ret;

    }

}
