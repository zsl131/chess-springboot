package com.zslin.test;

import com.zslin.basic.tools.ConfigTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 修改图片尺寸工具类
 */
@Component
public class ImageHandleTools {

    @Autowired
    private ConfigTools configTools;

    private static final String UPLOAD_PATH_PRE = "/publicFile/upload";

    public void process(Integer width) throws Exception {
        File outFile = new File(configTools.getUploadPath(UPLOAD_PATH_PRE));
        System.out.println("---------->"+outFile.getAbsolutePath());
        width = width==null?500:width;
        File [] files = outFile.listFiles();
        for(File f : files) {
            System.out.println(f.getAbsolutePath());
            File [] subList = f.listFiles();
            for(File s : subList) {
                System.out.println(s.getAbsolutePath());
                if(s.isFile()) {
                    rebuild(new FileInputStream(s), width, s.getName(), s);
                }
            }
        }
    }

    private void rebuild(InputStream is, Integer w, String fileName, File destFile) throws Exception {
        BufferedImage newImage = zoomImage(is, w);
        if(newImage!=null) {
            ImageIO.write(newImage, fileName.substring(fileName.lastIndexOf(".") + 1), destFile);
        }
    }

    private BufferedImage zoomImage(InputStream is, int w) throws Exception {
        try {
            BufferedImage srcImg = ImageIO.read(is); //读取图片
            return zoomImage(srcImg, w);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage zoomImage(BufferedImage srcImg, int w) {
        try {
            int sw = srcImg.getWidth();
            if(sw<=w) {return null;} //如果宽度小，则不需要调整
            int sh = srcImg.getHeight();

            int dh = (int)(w*sh/sw); //设置新高度

            BufferedImage newImage=new BufferedImage(w, dh,BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(srcImg.getScaledInstance(w, dh, Image.SCALE_SMOOTH), 0, 0, null);

            return newImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
