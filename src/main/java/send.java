/**
 * @date:2023/5/25 14:54
 * @author:Mr.zhao
 * @Description:
 * 发送JSON的功能网关
 * 监听python端发布的json，控制情报板及雾灯
 **/
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;


public class send {
    public static void main(String[] args) throws IOException {
        int port = 8000; // 监听的端口号
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("JsonListener started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Received connection from " + clientSocket.getInetAddress().getHostAddress());
            Thread clientThread = new ClientThread(clientSocket);
            clientThread.start();
        }
    }

    static class ClientThread extends Thread {
        private final Socket clientSocket;

        public ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                // 读取请求数据
                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isEmpty()) {
                        // 空行表示请求头结束
                        break;
                    }
                    requestBuilder.append(line).append("\r\n");
                }
                String request = requestBuilder.toString().trim();
                System.out.println("Received request:\n" + request);

                // 解析JSON数据
                // 读取请求体部分
                StringBuilder requestBodyBuilder = new StringBuilder();
                while (reader.ready()) {
                    requestBodyBuilder.append((char) reader.read());
                }
                String json = requestBodyBuilder.toString().trim();
                System.out.println("Received JSON data:\n" + json);

                // 在这里可以对JSON数据进行处理
                // 将dev_id和content字段替换到预定义的JSON数据中
                JSONObject originalJson = new JSONObject(json);
                JSONObject predefinedJson = new JSONObject();
                JSONObject metaInfo = new JSONObject();
                metaInfo.put("msg_type", 0);
                metaInfo.put("sid", "111");
                metaInfo.put("dev_id", originalJson.getString("dev_id"));
                metaInfo.put("dev_type", "screen");
                metaInfo.put("ctl_type", "set");
                metaInfo.put("app_id", "app001");
                metaInfo.put("ts", "1605861849007");
                metaInfo.put("sign", "7f6fe4f920d4bf4b2635642f1ae3be981743adfc23e4ef737d83b657b700ac02");
                predefinedJson.put("_meta", metaInfo);
                JSONObject dataJson = new JSONObject();
                JSONArray listJson = new JSONArray();
                JSONObject itemJson = new JSONObject();
                JSONObject textJson = new JSONObject();
                textJson.put("content", originalJson.getString("content"));
                textJson.put("color", "green");
                textJson.put("fonttype", "s");
                textJson.put("fontsize", "24");
                itemJson.put("image", new JSONObject().put("base64", "data:image/jpg;base64,/9j/4AAQSkZA"));
                itemJson.put("text", textJson);
                itemJson.put("stayTime", 3);
                listJson.put(itemJson);
                dataJson.put("list", listJson);
                predefinedJson.put("data", dataJson);
                String modifiedJson = predefinedJson.toString();
                System.out.println("发送的json为：" + modifiedJson);

                // 转发JSON数据到指定URL
                String targetUrl = "http://47.103.184.192:80/api/gateway/screen/set";
//                forwardJson(modifiedJson, targetUrl);

                // 返回响应给Python
                String response = "JSON received and forwarded successfully";
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Length: " + response.getBytes().length);
                writer.println();
                writer.println(response);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void forwardJson(String json, String targetUrl) throws IOException {
            // 发送POST请求到目标URL
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 将JSON数据写入请求体
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(json.getBytes(StandardCharsets.UTF_8));
            requestBody.close();

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 处理成功响应
                InputStream responseBody = connection.getInputStream();
                // 读取和处理响应数据
                responseBody.close();
            } else {
                // 处理失败响应
                InputStream errorStream = connection.getErrorStream();
                // 读取和处理错误响应数据
                errorStream.close();
            }
            connection.disconnect();
        }
    }
}


