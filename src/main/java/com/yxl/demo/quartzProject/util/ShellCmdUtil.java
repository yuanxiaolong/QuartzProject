package com.yxl.demo.quartzProject.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令执行工具类
 * author: xiaolong.yuanxl
 * date: 2015-06-08 下午9:30
 */
public class ShellCmdUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ShellCmdUtil.class);

    private static ShellCmdUtil instance = null;

    // 固定线程池
//    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    //返回单例
    public static ShellCmdUtil getInstance(){
        if (instance == null){
            instance = new ShellCmdUtil();
        }
        return instance;
    }

    private ShellCmdUtil(){
        // forbid
    }


    /**
     * 执行命令
     * @param shellCmd  例如 "hive"
     * @param shellArgs 例如 "-e"
     *  @param shellScript 例如 "show tables;"
     * @return 执行结果
     */
    public String exec(String shellCmd, String shellArgs, String shellScript){
        if (StringUtils.isBlank(shellCmd) || StringUtils.isBlank(shellScript)){
            LOG.warn("cannot exec shell cmd without cmd or script");
            return "";
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer sbError = new StringBuffer();
        StringBuffer sbOut = new StringBuffer();
        try{
            Process proc = Runtime.getRuntime().exec(shellCmd + " " + shellArgs + " " + shellScript);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR",sbError);
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT",sbOut);

            // 启动2个线程去读 输入流 防止程序挂起。不用线程池,否则会挂起程序
            errorGobbler.start();
            outputGobbler.start();

            int exitVal = proc.waitFor();   //success-0

            LOG.info("exec cmd " + shellCmd + " " + shellArgs + " " + shellScript + " exitVal " + exitVal);
            // 添加所有输出流 无论在 out 还是 err , hive 执行结果在 err ,其他正常的在out
            sb.append(sbError.toString());
            sb.append(sbOut.toString());
        }catch (Exception e){
            LOG.error("exec cmd " + shellCmd + " " + shellArgs + " " + shellScript ,e);
            sb.append("exec cmd " + shellCmd + " " + shellArgs + " " + shellScript + e.getMessage());//添加异常报警，以便人工介入
        }
        return sb.toString();
    }

    public String exec(String allCmd){
        if (StringUtils.isBlank(allCmd)){
            LOG.warn("cannot exec blank shell cmd ");
            return "";
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer sbError = new StringBuffer();
        StringBuffer sbOut = new StringBuffer();
        try{
            Process proc = Runtime.getRuntime().exec(allCmd);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR",sbError);
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT",sbOut);

            // 启动2个线程去读 输入流 防止程序挂起。不用线程池,否则会挂起程序
            errorGobbler.start();
            outputGobbler.start();

            int exitVal = proc.waitFor();   //success-0

            LOG.info("exec cmd " +allCmd + " exitVal " + exitVal);
            // 添加所有输出流 无论在 out 还是 err , hive 执行结果在 err ,其他正常的在out
            sb.append(sbError.toString());
            sb.append(sbOut.toString());
        }catch (Exception e){
            LOG.error("exec cmd " + allCmd ,e);
            sb.append("exec cmd " + allCmd + e.getMessage());//添加异常报警，以便人工介入
        }
        return sb.toString();
    }


//    public static void main(String[] args) {
//        String shellCmd = "/bin/bash";
//        String shellArgs = "-c";
//        String shellScript = "/Users/xiaolongyuan/Downloads/monitor.sh";
//
//        StringBuffer sb = new StringBuffer();
//        try{
//            Process proc = Runtime.getRuntime().exec(new String[]{shellCmd, shellArgs, shellScript});
//            StringBuffer sbError = new StringBuffer();
//            StringBuffer sbOut = new StringBuffer();
//
//            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR",sbError);
//            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT",sbOut);
//
////            pool.submit(errorGobbler);
////            pool.submit(outputGobbler);
//            errorGobbler.start();
//            outputGobbler.start();
//
//            int exitVal = proc.waitFor();   //success-0
//
//            LOG.info("exec cmd " + shellCmd + " " + shellArgs + " " + shellScript + " exitVal " + exitVal);
//            // 添加所有输出流 无论在 out 还是 err , hive 执行结果在 err ,其他正常的在out
//            sb.append(sbError.toString() + sbOut.toString());
//        }catch (Exception e){
//            LOG.error("exec cmd " + shellCmd + " " + shellArgs + " " + shellScript ,e);
//        }
//        System.out.println("result is: " + sb.toString());
//    }



}
