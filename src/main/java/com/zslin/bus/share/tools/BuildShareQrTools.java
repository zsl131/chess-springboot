package com.zslin.bus.share.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
import com.sun.javafx.iio.common.ImageTools;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.model.ActivityRecord;
import com.zslin.bus.share.dao.IShareUserDao;
import com.zslin.bus.share.dao.IShareUserQrDao;
import com.zslin.bus.share.model.ShareUser;
import com.zslin.bus.share.model.ShareUserQr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

/**
 * 生成推广二维码工具类
 */
@Component
public class BuildShareQrTools {

    @Autowired
    private IShareUserQrDao shareUserQrDao;

    @Autowired
    private IShareUserDao shareUserDao;

    @Autowired
    private IActivityRecordDao activityRecordDao;

    private static Integer QRCODE_SIZE = 300;

    @Autowired
    private ConfigTools configTools;

    /**
     * 生成所有用户的二维码
     * @param recordId
     */
    public void buildQr(Integer recordId) {
        List<ShareUser> userList = shareUserDao.findAll();
        ActivityRecord ar = activityRecordDao.findOne(recordId);
        for(ShareUser user: userList) {
            buildQr(ar, user);
        }
    }

    /**
     * 生成推广二维码
     * @param recordId
     * @param userId
     */
    public void buildQr(Integer recordId, Integer userId) {
        /*ShareUserQr suq = shareUserQrDao.findByRecordIdAndUserId(recordId, userId);
        if(suq==null) {
            suq = new ShareUserQr();
            ActivityRecord ar = activityRecordDao.findOne(recordId);
            ShareUser su = shareUserDao.findOne(userId);
            suq.setName(su.getName());
            suq.setPhone(su.getPhone());
            suq.setActivityTitle(ar.getActTitle());
            suq.setRecordId(recordId);
            suq.setUserId(userId);
            suq.setQrPath(buildQrPath(recordId, userId));
            shareUserQrDao.save(suq);
        }*/
        buildQr(activityRecordDao.findOne(recordId), shareUserDao.findOne(userId));
    }

    private void buildQr(ActivityRecord record, ShareUser user) {
        ShareUserQr suq = shareUserQrDao.findByRecordIdAndUserId(record.getId(), user.getId());
        if(suq==null) {
            Integer recordId = record.getId();
            Integer userId = user.getId();
            suq = new ShareUserQr();
            suq.setName(user.getName());
            suq.setPhone(user.getPhone());
            suq.setActivityTitle(record.getActTitle());
            suq.setRecordId(recordId);
            suq.setUserId(userId);
            suq.setQrPath(buildQrPath(recordId, userId));
            shareUserQrDao.save(suq);
        }
    }

    public String buildQrPath(Integer recordId, Integer userId) {
        try {
            String url = "http://server.qswkx.com/wx/activityRecord/signUp?recordId="+recordId+"&userId="+userId; //TODO 这里需要生成url地址
            BufferedImage bi = createImage(url);
            String fileName = "share_"+userId+"_"+recordId+".jpg";
            String path = configTools.getUploadPath("publicFile/shareQr")+ fileName;
//            System.out.println("========"+ path);
            boolean writeFlag = ImageIO.write(bi, "jpg", new File(path));
//            System.out.println("-----"+writeFlag);
            return path.replace(configTools.getUploadPath(), "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public BufferedImage createImage(String url) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}
