package com.zslin.bus.share.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
import com.sun.javafx.iio.common.ImageTools;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.dao.IActivityRecordDao;
import com.zslin.bus.basic.model.Activity;
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

    @Autowired
    private IActivityDao activityDao;

    private static Integer QRCODE_SIZE = 300;

    public static String BASE_URL = "http://server.qswkx.com";
    public static class QR_TYPE {
        public static String TYPE_RECORD = "ActivityRecord";
        public static String TYPE_ACTIVITY = "Activity";
        public static String TYPE_Course = "ShareCourse";
    }

    public static class QR_URL {
        public static String URL_ACTIVITY = "/wx/activity/show?id=#objId#&userId=#userId#";
        public static String URL_RECORD = "/wx/activityRecord/signUp?recordId=#objId#&userId=#userId#";
    }

    @Autowired
    private ConfigTools configTools;

    /**
     * 生成所有用户的二维码
     * @param objId
     */
    public void buildQr(String type, Integer objId) {
        List<ShareUser> userList = shareUserDao.findAll();
        if(QR_TYPE.TYPE_RECORD.equalsIgnoreCase(type)) {
            ActivityRecord ar = activityRecordDao.findOne(objId);
            for(ShareUser user: userList) {
                buildQrByActivityRecord(ar, user);
            }
        } else if(QR_TYPE.TYPE_ACTIVITY.equalsIgnoreCase(type)) {
            Activity act = activityDao.findOne(objId);
            for(ShareUser user: userList) {
                buildQrByActivity(act, user);
            }
        }

    }

    /**
     * 生成推广二维码
     * @param type
     * @param objId
     * @param userId
     */
    public void buildQr(String type, Integer objId, Integer userId) {
        ShareUser user = shareUserDao.findOne(userId);
        if(QR_TYPE.TYPE_RECORD.equalsIgnoreCase(type)) {
            buildQrByActivityRecord(activityRecordDao.findOne(objId), user);
        } else if(QR_TYPE.TYPE_ACTIVITY.equalsIgnoreCase(type)) {
            buildQrByActivity(activityDao.findOne(objId), user);
        }
    }

    private void buildQrByActivityRecord(ActivityRecord obj, ShareUser user) {
        Integer userId = user.getId();
        Integer objId = obj.getId();
        ShareUserQr suq = shareUserQrDao.findByType(QR_TYPE.TYPE_RECORD, objId, userId);
        if(suq==null) {
            suq = new ShareUserQr();
            suq.setObjId(objId);
            suq.setUserId(userId);
            suq.setQrType(QR_TYPE.TYPE_RECORD);
            suq.setQrPath(buildQrPath(QR_URL.URL_RECORD, objId, userId));
        } else {
            suq.setName(user.getName());
            suq.setPhone(user.getPhone());
            suq.setObjName(obj.getActTitle());
        }
        shareUserQrDao.save(suq);
    }

    private void buildQrByActivity(Activity obj, ShareUser user) {
        Integer userId = user.getId();
        Integer objId = obj.getId();
        ShareUserQr suq = shareUserQrDao.findByType(QR_TYPE.TYPE_ACTIVITY, objId, userId);
        if(suq==null) {
            suq = new ShareUserQr();
            suq.setObjId(objId);
            suq.setUserId(userId);
            suq.setQrType(QR_TYPE.TYPE_ACTIVITY);
            suq.setQrPath(buildQrPath(QR_URL.URL_ACTIVITY, objId, userId));
        } else {
            suq.setName(user.getName());
            suq.setPhone(user.getPhone());
            suq.setObjName(obj.getTitle());
        }
        shareUserQrDao.save(suq);
    }

    /**
     *
     * @param path 如： /wx/activityRecord/signUp?recordId=#objId#&userId=#userId#
     * @param objId 对象ID
     * @param userId 用户ID
     * @return
     */
    private String buildUrl(String path, Integer objId, Integer userId) {
        path = path.replaceAll("#objId#", String.valueOf(objId)).replaceAll("#userId#", String.valueOf(userId));
        return BASE_URL + path;
    }

    public String buildQrPath(String urlPath, Integer objId, Integer userId) {
        try {
            //String url = BASE_URL + "/wx/activityRecord/signUp?recordId="+recordId+"&userId="+userId; //TODO 这里需要生成url地址
            String url = buildUrl(urlPath, objId, userId);
            BufferedImage bi = createImage(url);
            String fileName = "share_"+userId+"_"+objId+"_"+System.currentTimeMillis()+".jpg";
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
