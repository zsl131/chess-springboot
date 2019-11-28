package com.zslin.bus.yard.tools;

import it.sauronsoftware.jave.Encoder;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * 需要在仓库目录执行：mvn install:install-file -Dfile=jave-1.0.2.jar -DgroupId=jave -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar -DgeneratePom=true
 * Created by zsl on 2018/9/12.
 */
public class MyFileTools {

    public static long getVideoTimeLength(File source) {
        Encoder encoder = new Encoder();
        try {
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(source);
            long ls = m.getDuration(); //毫秒
            return ls;
        } catch (Exception e) {
            e.printStackTrace();
            return 0l;
        }finally {
        }
    }

    /**
     * 判断文件是否为图片文件
     * @param fileName
     * @return
     */
    public static Boolean isImageFile(String fileName) {
        return isSpeFile(fileName, ".jpg", ".jpeg", ".png", ".gif", ".bmp");
    }

    /**
     * 判断文件是否为视频文件
     * @param fileName
     * @return
     */
    public static Boolean isVideoFile(String fileName) {
        return isSpeFile(fileName, ".mp4", ".avi");
    }

    /**
     * 判断文件是否为PPT文件
     * @param fileName
     * @return
     */
    public static Boolean isPPTFile(String fileName) {
        return isSpeFile(fileName, ".ppt", ".pptx");
    }

    /**
     * 判断文件是否为Word文件
     * @param fileName
     * @return
     */
    public static Boolean isWordFile(String fileName) {
        return isSpeFile(fileName, ".doc", ".docx");
    }

    /**
     * 判断文件是否为PDF文件
     * @param fileName
     * @return
     */
    public static Boolean isPDFFile(String fileName) {
        return isSpeFile(fileName, ".pdf");
    }

    /**
     * 判断文件是否为指定文件
     * @param fileName
     * @return
     */
    public static Boolean isSpeFile(String fileName, String... targetFileTypes) {
        if(fileName==null || targetFileTypes==null || targetFileTypes.length<=0) {return false;}
        fileName = fileName.toLowerCase();
        for(String type : targetFileTypes) {
            if(fileName.endsWith(type)) {return true;}
        }
        return false;
    }

    public static String getFileType(String fileName) {
        if(fileName!=null && fileName.indexOf(".")>=0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }
}
