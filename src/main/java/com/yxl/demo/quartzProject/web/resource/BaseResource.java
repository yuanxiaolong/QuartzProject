package com.yxl.demo.quartzProject.web.resource;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * 共有资源,用于抽象公共逻辑
 *
 * author: xiaolong.yuanxl
 * date: 2015-06-04 下午4:46
 */
public class BaseResource {

    /**
     * 这里添加options方法,不然浏览器请求options方法会报500错误
     * 原理: 由于PUT,DELETE 不是常用方法,因此浏览器发送这些方法时,会先发送一个OPTIONS方法用于获取服务器所支持的HTTP方法列表
     */
    @OPTIONS
    public Response options(@Context final Request requestContext){
        return Response.ok().build();
    }


}
