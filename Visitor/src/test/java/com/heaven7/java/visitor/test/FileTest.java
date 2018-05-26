package com.heaven7.java.visitor.test;

import org.junit.Test;

import java.io.File;

public class FileTest {

    @Test
    public void read(){
        String path = "D:\\360安全浏览器下载\\untitled folder\\wedding\\churchIn";
        //String path = "D:\\360安全浏览器下载\\untitled folder\\wedding\\churchOut";
        //String path = "D:\\360安全浏览器下载\\untitled folder\\wedding\\dinner";
       //String path = "D:\\360安全浏览器下载\\untitled folder\\wedding\\empty";
        //String path = "D:\\360安全浏览器下载\\untitled folder\\wedding\\character";
        File file = new File(path);
        File[] files = file.listFiles();
        StringBuilder sb = new StringBuilder();

        for(int i = 0 , len = files.length ; i < len ; i++){
            String name = files[i].getName();
            if(name.contains(".")){
                name = name.substring(0, name.indexOf("."));
            }
            sb.append(name);
            if(i != len - 1){
                sb.append(",");
            }
        }
        System.out.println(sb.toString());
        System.out.println(" 1".replace(" ",""));
    }
}
