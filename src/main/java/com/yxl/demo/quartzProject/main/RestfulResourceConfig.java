package com.yxl.demo.quartzProject.main;

import com.sun.jersey.api.core.PackagesResourceConfig;

/**
 * Restful 资源配置
 *
 * author: xiaolong.yuanxl
 * date: 2015-05-01 上午11:03
 */
public class RestfulResourceConfig extends PackagesResourceConfig {

    public RestfulResourceConfig() {
        super("com.yxl.demo.quartzProject.web.resource");
    }
}
