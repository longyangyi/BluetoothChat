/*******************************************************************************
Copyright ArxanFintech Technology Ltd. 2018 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

                 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*******************************************************************************/


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Tools {
    final static String NEWLINE = "\r\n";
    final static String BOUNDARYPREFIX = "--";

    public static String GetMultipartData(String boundary, String pathFile, String poeID, Boolean readOnly) {
        try {
            String shortFileName = new File(pathFile.trim()).getName();
            String fileContent = GetFileContent(pathFile);
            if (fileContent == null) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(BOUNDARYPREFIX);
            sb.append(boundary);
            sb.append(NEWLINE);
            sb.append("Content-Disposition: form-data; name=\"poe_file\"; filename=\"" + shortFileName + "\"");
            sb.append(NEWLINE);
            sb.append("Content-Type: application/octet-stream");
            sb.append(NEWLINE);
            sb.append(NEWLINE);
            sb.append(fileContent);
            sb.append(NEWLINE);

            sb.append(BOUNDARYPREFIX);
            sb.append(boundary);
            sb.append(NEWLINE);
            sb.append("Content-Disposition: form-data; name=\"poe_id\"");
            sb.append(NEWLINE);
            sb.append(NEWLINE);
            sb.append(poeID);
            sb.append(NEWLINE);

            sb.append(BOUNDARYPREFIX);
            sb.append(boundary);
            sb.append(NEWLINE);
            sb.append("Content-Disposition: form-data; name=\"read_only\"");
            sb.append(NEWLINE);
            sb.append(NEWLINE);
            sb.append(String.valueOf(readOnly));
            sb.append(NEWLINE);

            sb.append(BOUNDARYPREFIX);
            sb.append(boundary);
            sb.append(BOUNDARYPREFIX);

            return sb.toString();
        } catch (Exception e) {
            System.out.println("GetMultipartData error: " + e);
            return null;
        }
    }

    public static String GetFileContent(String fileName) {

        // 定义�?个输入流对象
        FileInputStream fis = null;

        // 定义�?个存放输入流的缓冲对�?
        BufferedInputStream bis = null;

        // 定义�?个输出流，相当StringBuffer（），会根据读取数据的大小，调整byte的数组长�?
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // 把文件路径和文件名作为参�? 告诉读取�?
            fis = new FileInputStream(fileName);

            // 把文件读取流对象传�?�给缓存读取流对�?
            bis = new BufferedInputStream(fis);

            // 获得缓存读取流开始的位置
            int len = bis.read();
            System.out.println("len=" + len);

            // 定义�?个容量来盛放数据
            byte[] buf = new byte[1024];

            while ((len = bis.read(buf)) != -1) {
                // 如果有数据的话，就把数据添加到输出流
                // 这里直接用字符串StringBuffer的append方法也可以接�?
                baos.write(buf, 0, len);
            }

            // 把文件输出流的数据，放到字节数组
            byte[] buffer = baos.toByteArray();

            // 返回输出
            return new String(buffer);

        } catch (Exception e) {
            System.out.println("Get File " + fileName + " error: " + e);
            return null;
        } finally {
            try {
                // 关闭�?有的�?
                baos.close();
                bis.close();
                fis.close();
            } catch (IOException e) {
            }

        }
    }
}
