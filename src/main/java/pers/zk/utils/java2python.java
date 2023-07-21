package pers.zk.utils; /**
 * @date:2023/6/21 14:48
 * @author:Mr.zhao
 * @Description:
 **/
import java.io.*;

public class java2python {
    public static void main(String[] args) {
        try {
            // 构建命令
            ProcessBuilder pb = new ProcessBuilder("python", "pridect.py", "test", "test2");
//
            // 设置工作目录（如果需要）
            pb.directory(new File("E:\\work_space\\gatewayServe\\src\\main\\java\\"));

            // 启动进程
            Process process = pb.start();

            // 获取进程的输入流
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // 读取输出内容
            String line;
            String lastLine = null;  // 保存最后一行的变量
            while ((line = reader.readLine()) != null) {
                System.out.println("Python输出: " + line);
                lastLine = line;  // 更新最后一行
            }

            // 等待进程执行结束
            int exitCode = process.waitFor();

            // 检查退出码
            if (exitCode == 0) {
                System.out.println("Python脚本执行成功");
            } else {
                System.out.println("Python脚本执行失败");
            }

            // 打印最后一行
            if (lastLine != null) {
                System.out.println("Python输出的最后一行: " + lastLine);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
