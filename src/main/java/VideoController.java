import com.sun.deploy.net.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @date:2023/5/30 15:11
 * @author:Mr.zhao
 * @Description:
// **/
//public class VideoController {
//
//    private VideoLoginInfo videoInfo = SpringUtils.getBean(VideoLoginInfo.class);
//
//    @Autowired
//    private EhTaskService ehTaskService;

    /**
     * 视频登陆
     *
     * @return
     */
//    public String login() {
//        String loginUrl = videoInfo.getIpPort() + VideoUrlConst.VIDEO_LOGIN_URL;
//        String param = "username=" + videoInfo.getUserName() + "&password=" + videoInfo.getUserPsw() + "&mac="
//                + videoInfo.getUserMac();
//        String response = HttpRequest.sendPost(loginUrl, param, "utf-8", "");
//
//        JSONObject jsonObject = JSON.parseObject(response);
//        String code = jsonObject.getString("code");
//        String accessToken = "";
//        if ("0".equals(code)) {
//            String rows = jsonObject.getString("rows");
//            JSONObject rowsObject = JSON.parseObject(rows);
//            String token = rowsObject.getString("token");
//            JSONObject jsonToken = JSON.parseObject(token);
//            accessToken = jsonToken.getString("accessToken");
//        }
//        return accessToken;
//    }
//
//    /**
//     * 请求视频实时播放
//     *
//     * @return
//     */
//    public String videoStart(String resId, String accessToken) {
//        String startUrl = videoInfo.getIpPort() + VideoUrlConst.VIDEO_START_URL;
//        String param = "rateType=0&resId=" + resId;
//        String response = HttpRequest.sendPost(startUrl, param, "utf-8", accessToken);
//
//        JSONObject jsonObject = JSON.parseObject(response);
//        String code = jsonObject.getString("code");
//        String sessionId = "";
//        if ("0".equals(code)) {
//            String rows = jsonObject.getString("rows");
//            JSONObject rowsObject = JSON.parseObject(rows);
//            sessionId = rowsObject.getString("sessionId");
//        }
//
//        return sessionId;
//
//    }
//
//    /**
//     * 视频实时播放
//     *
//     * @return
//     */
//    public Map<String, Object> videoStatus(String sessionId, String accessToken) {
//        String statusUrl = videoInfo.getIpPort() + VideoUrlConst.VIDEO_STATUS_URL;
//        String param = "sessionId=" + sessionId;
//        Map<String, Object> resultMap = new HashMap<>(16);
//        // 最多请求1分钟，无响应直接返回
//        for (int j = 0; j < 30; j++) {
//            try {
//                Thread.sleep(10);
//            }
//            catch (InterruptedException e) {
//                log.error(e.getMessage());
//            }
//            String response = HttpRequest.sendPost(statusUrl, param, "utf-8", accessToken);
//            JSONObject jsonObject = JSON.parseObject(response);
//            String code = jsonObject.getString("code");
//            if ("0".equals(code)) {
//                String rows = jsonObject.getString("rows");
//                JSONObject rowsObject = JSON.parseObject(rows);
//                String flv = rowsObject.getString("flv");
//                String rtmp = rowsObject.getString("rtmp");
//                if (flv != null && !"".equals(flv) && rtmp != null && !"".equals(rtmp)) {
//                    resultMap.put("flv", flv);
//                    resultMap.put("rtmp", rtmp);
//                    return resultMap;
//                }
//            }
//        }
//        return resultMap;
//    }
//
//    /**
//     * 视频停止实时播放
//     *
//     * @return
//     */
//    public Map<String, Object> videoStop(String sessionId, String accessToken) {
//        String stopUrl = videoInfo.getIpPort() + VideoUrlConst.VIDEO_STOP_URL;
//        String param = "sessionId=" + sessionId;
//        String response = HttpRequest.sendPost(stopUrl, param, "utf-8", accessToken);
//        JSONObject jsonObject = JSON.parseObject(response);
//        String code = jsonObject.getString("code");
//        Map<String, Object> result = new HashMap<String, Object>();
//        if ("0".equals(code)) {
//            log.debug("停止视频播放成功。");
//            result.put("Flag", true);
//        }
//        else {
//            log.debug("停止视频播放失败。");
//            result.put("Flag", false);
//        }
//        return result;
//    }
//}