package pers.zk.utils; /**
 * @date:2023/6/19 15:37
 * @author:Mr.zhao
 * @Description:
 **/
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestSender {
    public static void main(String[] args) {
        String jsonText = "{\"data\":{\"DeviceName\":\"zk_test\"},\"body\":{\"DeviceNo\":\"test\",\"Category\":\"test\",\"SubCategory\":\"2\",\"Location\":\"123\",\"ETime\":\"test\"},\"headers\":{\"ss\":\"tt\"},\"message\":{\"MId\":\"消息编号\",\"MType\":\"accident\",\"MTimestamp\":\"消息发生时间\",\"MStatus\":\"设备状态描述\",\"MCode\":\"设备状态编号\"}}";
        String serverUrl = "http://8.142.73.253:6000/MessageServer";

        try {
            String receipt = sendHttpRequest(jsonText, serverUrl);
            System.out.println("Receipt from server: " + receipt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String sendHttpRequest(String jsonText, String serverUrl) throws Exception {
        String receipt = "";

        try {
//            String[] arr = new String[1];
            String arr = jsonText;
            URL imsServerURL = new URL(serverUrl);
            HttpURLConnection imsConnection = (HttpURLConnection) imsServerURL.openConnection();
            imsConnection.setDoInput(true);
            imsConnection.setDoOutput(true);
            imsConnection.setUseCaches(false);
            imsConnection.setDefaultUseCaches(false);
            imsConnection.setRequestProperty("Content-Type", "application/octet-stream");
            ObjectOutputStream oos = new ObjectOutputStream(imsConnection.getOutputStream());
            oos.writeObject(arr);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(imsConnection.getInputStream());
            receipt = (String) ois.readObject();
            ois.close();
            imsConnection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return receipt;
    }
}
