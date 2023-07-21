package pers.zk.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class sendTest {
    public static void main(String[] args) {
        String url = "http://10.168.160.22/dev-tool/ocp/api/gateway/screen/get";
        JSONObject jsonObject = new JSONObject();

        JSONObject metaInfo = new JSONObject();
        metaInfo.put("msg_type", 0);
        metaInfo.put("sid", "111");
        metaInfo.put("dev_id", "DEV0133060401035");
        metaInfo.put("dev_type", "screen");
        metaInfo.put("ctl_type", "get");
        metaInfo.put("app_id", "hst_cadx");
        metaInfo.put("ts", "1685930602000");
        metaInfo.put("sign", "18239c21d79ac99e10bf8e719fce4b3256ab31e4e6a777edb37c4bbcd434898c");

        JSONObject data = new JSONObject();
        JSONObject image = new JSONObject();
        image.put("base64", "data:image/jpg;base64,/9j/4AAQSkZA");

        JSONObject text = new JSONObject();
        text.put("content", "安全⽂明出⾏");
        text.put("color", "green");
        text.put("fonttype", "s");
        text.put("fontsize", "24");

        JSONObject list = new JSONObject();
        list.put("image", image);
        list.put("text", text);
        list.put("stayTime", 3);

        data.put("list", list);
//        data

        jsonObject.put("_meta", metaInfo);
        jsonObject.put("data", "");

        String jsonPayload = jsonObject.toString();
        System.out.println(jsonPayload);

        String response = sendPostRequest(url, jsonPayload);
        System.out.println("Response: " + response);
    }

    public static String sendPostRequest(String url, String jsonPayload) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(jsonPayload, ContentType.TEXT_PLAIN);
            httpPost.setEntity(requestEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    return EntityUtils.toString(responseEntity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
