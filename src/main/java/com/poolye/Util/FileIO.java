package com.poolye.Util;

import java.io.File;
import java.io.FileWriter;


public class FileIO {


    public void writeContentFile(String content,String book_id, String uuid){
        String path = "/yoko/poolye/data/"+book_id+"/"+uuid+".tmp";
        try{
            File file = new File(path);
            if(!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                //System.out.println("目标文件所在目录不存在，准备创建它！");
                if(!file.getParentFile().mkdirs()) {
//                    System.out.println("创建目标文件所在目录失败！");
                }
            }

            FileWriter fw = new FileWriter(path);

            fw.write(content);

            fw.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
