package com.XHxinhe.aliveandwell.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;

public class HTTPUtils {

    /**
     * 发送json数据的post请求
     * 不支持https
     *
     * @param url      路径
     * @param sendData 参数(json串)
     * @return 返回调接口返回信息
     */
    public static String sendJsonPost(String url, String sendData) {
        String body = "";
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);

            //装填参数
            StringEntity s = new StringEntity(sendData, StandardCharsets.UTF_8);
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            //设置参数到请求对象中
            httpPost.setEntity(s);
            //设置header信息
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //执行请求操作，并拿到结果（同步阻塞）
            HttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
            //为防止频繁调用一个接口导致接口爆掉，每次调用完成后停留100毫秒
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println("JSON数据发送失败，异常：" + e.getMessage());
        }
        return body;
    }
}
