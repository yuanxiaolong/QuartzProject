package com.yxl.demo.quartzProject.util;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpClientHelper {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public enum HttpMethod {
        POST, GET, PUT, DELETE
    }

    private DefaultHttpClient client;
    private HttpResponse response;
    private HttpHost httpHost;

    public HttpClientHelper(String host, int port) {
        httpHost = new HttpHost(host, port);
        client = new DefaultHttpClient();
    }


    public HttpResponse exec(HttpMethod method, String path, Map<String, String> param)
            throws IOException {
        switch (method) {
            case GET: {
                StringBuilder pathB = new StringBuilder(path);
                if (param != null && !param.isEmpty()) {
                    String sep = "?";
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        pathB.append(sep)
                                .append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue(),
                                        HTTP.UTF_8));
                        sep = "&";
                    }
                }
                HttpGet request = new HttpGet(pathB.toString());
                response = client.execute(httpHost, request);
                break;
            }
            case DELETE: {
                StringBuilder pathB = new StringBuilder(path);
                if (param != null && !param.isEmpty()) {
                    String sep = "?";
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        pathB.append(sep)
                                .append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue(),
                                        HTTP.UTF_8));
                        sep = "&";
                    }
                }
                HttpDelete request = new HttpDelete(pathB.toString());
                response = client.execute(httpHost, request);
                break;
            }
            case PUT: {
                HttpPut request = new HttpPut(path);
//                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//                for (Map.Entry<String, String> entry : param.entrySet()) {
//                    nvps.add(new BasicNameValuePair(entry.getKey(), entry
//                            .getValue()));
//                }
//                request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    jsonObject.put(entry.getKey(),entry.getValue());
                }

                StringEntity entity = new StringEntity(jsonObject.toString(),HTTP.UTF_8);
                entity.setContentType("application/json;charset=UTF-8");
                request.setEntity(entity);
                response = client.execute(httpHost, request);
                break;
            }

            case POST: {
                HttpPost request = new HttpPost(path);
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue()));
                }
//                JSONObject jsonObject = new JSONObject();
//                for (Map.Entry<String, String> entry : param.entrySet()) {
//                    jsonObject.put(entry.getKey(),entry.getValue());
//                }
//
//                StringEntity entity = new StringEntity(jsonObject.toString(),HTTP.UTF_8);
//                System.out.println(jsonObject.toString());
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps,HTTP.UTF_8);
//                entity.setContentType("application/json;charset=UTF-8");

//                StringBuffer sb = new StringBuffer();
//                sb.append("?");
//
//                for (Map.Entry<String, String> entry : param.entrySet()) {
//                    sb.append(entry.getKey() + "=" + entry.getValue() + "&");
//                }
//                String str = sb.toString();
//                str = StringUtils.removeEnd(str,"&");
//
//                StringEntity entity = new StringEntity(str,HTTP.UTF_8);
//                request.setEntity(entity);

//                System.out.println(toString(entity.getContent()));
                request.setEntity(entity);
                response = client.execute(httpHost, request);
                break;
            }
            default:
                break;
        }

        return response;//.getStatusLine().getStatusCode();
    }

    public String getContent() throws IllegalStateException, IOException {
        return toString(response.getEntity().getContent());
    }

    private String toString(InputStream in)
            throws UnsupportedEncodingException, IOException {
        StringWriter writer = new StringWriter();
        copyLarge(new InputStreamReader(in, "utf-8"), writer);
        return writer.toString();
    }

    private static long copyLarge(Reader input, Writer output)
            throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }


    public void close() {
        if (client != null)
            client.getConnectionManager().shutdown();
    }
}
