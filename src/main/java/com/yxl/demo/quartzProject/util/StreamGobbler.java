package com.yxl.demo.quartzProject.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 获取输入流
 * author: xiaolong.yuanxl
 * date: 2015-06-08 下午9:06
 */
public class StreamGobbler extends Thread{

    private static final Logger LOG = LoggerFactory.getLogger(StreamGobbler.class);

    private InputStream is;
    private String type;
    private StringBuffer sb = new StringBuffer();

    public StreamGobbler(InputStream is, String type,StringBuffer sb) {
        this.is = is;
        this.type = type;
        this.sb = sb;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOG.error("read from stream happened error: ",ioe);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }


}
