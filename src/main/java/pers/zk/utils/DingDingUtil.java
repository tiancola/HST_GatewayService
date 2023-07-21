package pers.zk.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @date:2023/6/8 19:55
 * @author:Mr.zhao
 * @Description:
 **/
public class DingDingUtil {

    private static final Logger log = LoggerFactory.getLogger(DingDingUtil.class);
    //钉钉上添加机器人的时候会有密钥
    @Value("${dingding.robot.secretCode}")
    private String secret;

    //注册机器人的时候会有webhook
    @Value("${dingding.robot.url}")
    private String webhook;

    public Long send(String content) throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException,
            InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData), "UTF-8"), "UTF-8");
        String signResult = "&timestamp=" + timestamp + "&sign=" + sign;
        // 得到拼接后的 URL
        String url = webhook + signResult;
        System.out.println(url);
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendResponse response = client.execute(request);
        log.info("发动钉钉返回结果：{}", response.getErrcode());
        return response.getErrcode();

    }


    public static void main(String[] args) {
//        pers.zk.utils.DingDingUtil dingDingUtil = new pers.zk.utils.DingDingUtil();
//        String context = "测试--长安大学";
//        try {
//            Long errorCode = dingDingUtil.pers.zk.utils.send(context);
//            System.out.println("钉钉消息返回码: " + errorCode);
//        } catch (Exception e) {
//            System.out.println("钉钉消息发送失败: " + e.getMessage());
//        }
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
            req.setAgentId(2622520924L);
            req.setToAllUser(true);
            OapiMessageCorpconversationAsyncsendV2Request.Msg obj1 = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            obj1.setMsgtype("text");
            OapiMessageCorpconversationAsyncsendV2Request.Text obj2 = new OapiMessageCorpconversationAsyncsendV2Request.Text();
            obj2.setContent("早上好");
            obj1.setText(obj2);
            req.setMsg(obj1);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, "19c046b9a62a363fbfe12ea63b945439");
            System.out.println(rsp.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }
}