/**
 * @date:2023/6/19 14:44
 * @author:Mr.zhao
 * @Description:
 **/
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private static final int PORT = 8000; // 指定监听的端口号

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // 处理客户端请求
                handleClientRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 跳过请求头部分
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Skip headers
            }

            // 读取客户端发送的JSON报文
            StringBuilder jsonBuilder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String json = jsonBuilder.toString();
            System.out.println("Received JSON: " + json);

            // 解析JSON报文，获取MType的值
            String mType = parseMType(json);
            System.out.println("MType: " + mType);

            // 根据MType值调用对应的Python脚本
            callPythonScript(mType, json);

            // 发送响应给客户端
            out.println("JSON received and processed successfully.");

            // 关闭连接
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseMType(String json) {
        // 解析JSON报文，获取MType的值
        try {
            JSONObject jsonObject = new JSONObject(json);
            String mType = jsonObject.getJSONObject("message").getString("MType");
            return mType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static void callPythonScript(String mType, String json) {
        // 根据MType值调用相应的Python脚本
        // 使用Java的ProcessBuilder执行外部命令调用Python脚本
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "F:\\work_space\\Hst_GatewayService\\" + mType + ".py", json);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // 读取Python脚本的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待Python脚本执行完成
            int exitCode = process.waitFor();

            System.out.println("Python script exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
