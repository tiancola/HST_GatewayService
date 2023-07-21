package pers.zk.utils; /**
 * @date:2023/6/7 21:29
 * @author:Mr.zhao
 * @Description:
 **/
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class sendJson {
    public static void main(String[] args) throws IOException {
        int port1 = 8000; // 监听的端口号1
        int port2 = 8001; // 监听的端口号2
        System.out.println("JsonListener started on port " + port1 + " and " + port2);

        Thread thread1 = new ClientThread(port1);
        Thread thread2 = new ClientThread(port2);

        thread1.start();
        thread2.start();
    }

    static class ClientThread extends Thread {
        private final int port;

        public ClientThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Received connection from " + clientSocket.getInetAddress().getHostAddress() + " on port " + port);
                    handleClientRequest(clientSocket, port);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleClientRequest(Socket clientSocket, int port) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

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
            StringBuilder requestBodyBuilder = new StringBuilder();
            while (reader.ready()) {
                requestBodyBuilder.append((char) reader.read());
            }
            String json = requestBodyBuilder.toString().trim();
            System.out.println("Received JSON data:\n" + json);
            String targetUrl = "";
            JSONObject predefinedJson = new JSONObject();

            // 根据端口号进行不同的JSON解析逻辑
            if (port == 8000) {
                // 处理8000端口监听到的内容
                targetUrl = "http://10.168.160.22/dev-tool/ocp/api/gateway/screen/set";
                // 构建8000端口对应的JSON数据
                JSONObject originalJson = new JSONObject(json);
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
                System.out.println("Parsing JSON data for port 8000");
            } else if (port == 8001) {
                // 处理8001端口监听到的内容
                targetUrl = "http://10.168.160.22/dev-tool/ocp/api/8001"; // 8001对应的目标URL
                // 构建8001端口对应的JSON数据
                JSONObject originalJson = new JSONObject(json);
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
                dataJson.put("mode", originalJson.getString("mode"));
                dataJson.put("pattern", originalJson.getString("pattern"));
                dataJson.put("brightness", originalJson.getString("brightness"));
                dataJson.put("twinkle",originalJson.getString("twinkle"));
                predefinedJson.put("data", dataJson);
                System.out.println("Parsing JSON data for port 8001");
            }


            // 返回响应给客户端
            String response = "JSON received and processed successfully";
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Length: " + response.getBytes().length);
            writer.println();
            writer.println(response);
            writer.flush();

            String modifiedJson = predefinedJson.toString();
            System.out.println("发送的json为：" + modifiedJson);
            // 关闭连接
            writer.close();
            reader.close();
            clientSocket.close();

            // 转发JSON数据到指定URL
//            forwardJson(modifiedJson, targetUrl);


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



