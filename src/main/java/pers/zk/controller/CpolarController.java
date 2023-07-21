package pers.zk.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
public class CpolarController {

    @PostMapping("/")
    public String processJson(@RequestBody String json) {
        System.out.println("Received JSON: " + json);

        // 在这里处理接收到的JSON数据，你可以解析JSON数据和处理其他逻辑
        String mType = parseMType(json);
        System.out.println("MType: " + mType);

        // 根据MType值调用对应的Python脚本
        callPythonScript(mType, json);

        return "JSON received and processed successfully.";
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
            ProcessBuilder processBuilder = new ProcessBuilder("E:\\conda\\envs\\hst\\python.exe", "F:\\work_space\\Hst_GatewayService\\" + mType + ".py", json);
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
