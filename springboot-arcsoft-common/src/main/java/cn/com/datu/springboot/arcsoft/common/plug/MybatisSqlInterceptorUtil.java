package cn.com.datu.springboot.arcsoft.common.plug;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@DependsOn("mybatisSqlInterceptorConfig")
public class MybatisSqlInterceptorUtil {

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static final String SQL_SUFFIX=";"+System.getProperty("line.separator");

    public static String file_PREFIX = MybatisSqlInterceptorConfig.getFile_prefix();

    private static String filePath = MybatisSqlInterceptorConfig.getSaveFilePath();

    public static void  saveSqlToFile(String str){

        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        //日期格式
        String fileName=df.format(new Date())+".sql";
        File newFile=new File(filePath);
        if(!newFile.exists()) {
            newFile.mkdir();
        }
        File file=new File(filePath,file_PREFIX+fileName);
        try {
            //创建文件
            if (!file.exists()) {
                file.createNewFile();
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            }else {
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8);
            }

             bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(str);
        } catch (IOException e) {
            throw new RuntimeException("文件创建失败:{}",e);
        }finally {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStreamWriter.close();

            } catch (IOException e) {
                throw new RuntimeException("文件流关闭失败:{}",e);
            }
        }
    }

}
