package com.zslin;

import com.zslin.bus.finance.model.FinanceDetail;
import com.zslin.bus.finance.model.FinanceRecord;
import com.zslin.bus.finance.model.FinanceTicket;
import com.zslin.bus.finance.tools.MoneyTools;
import com.zslin.bus.finance.tools.PDFTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2019/1/5.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class PDFTest {

    @Autowired
    private PDFTools pdfTools;

    @Test
    public void test04() {
        System.out.println(MoneyTools.digitUppercase(0.12)); // 壹角贰分
        System.out.println(MoneyTools.digitUppercase(123.34)); // 壹佰贰拾叁元叁角肆分
        System.out.println(MoneyTools.digitUppercase(97001.34)); // 壹佰贰拾叁元叁角肆分
        System.out.println(MoneyTools.digitUppercase(1000000.56)); // 壹佰万元伍角陆分
        System.out.println(MoneyTools.digitUppercase(100000001.78)); // 壹亿零壹元柒角捌分
        System.out.println(MoneyTools.digitUppercase(1000000000.90)); // 壹拾亿元玖角
        System.out.println(MoneyTools.digitUppercase(1234567890.03)); // 壹拾贰亿叁仟肆佰伍拾陆万柒仟捌佰玖拾元叁分
        System.out.println(MoneyTools.digitUppercase(1001100101.00)); // 壹拾亿零壹佰壹拾万零壹佰零壹元整
        System.out.println(MoneyTools.digitUppercase(110101010.10)); // 壹亿壹仟零壹拾万壹仟零壹拾元壹角
    }

    @Test
    public void test03() throws Exception {
        FileOutputStream fos = new FileOutputStream(new File("D:/temp/1234567.pdf"));
        FinanceRecord fr = new FinanceRecord();
        fr.setRecordDate("20190108");
        fr.setTicketNo("2019010008");
        fr.setRecordName("zsl_钟述林");
        fr.setVerifyName("lz_李振");
        fr.setFlag("-1");
        List<FinanceDetail> list = new ArrayList<>();
        for(int i=0;i<4;i++) {
            FinanceDetail fd = new FinanceDetail();
            fd.setTitle("红外发射二极管/三极管/杜邦线"+i);
            fd.setCount(1+i);
            fd.setPrice(12.4f+i*0.8f);
            fd.setAmount((12.4f+i*0.8f)*(1+i));
            fd.setTicketCount(0+i);
            fd.setRecordDate((20181211+i)+"");
            fd.setCateName("实验耗材");
            list.add(fd);
        }
        List<FinanceTicket> tlist = new ArrayList<>();
        tlist.add(new FinanceTicket());
        tlist.add(new FinanceTicket());
        tlist.add(new FinanceTicket());
        pdfTools.buildPDF(fos, fr, list, tlist);
    }

    @Test
    public void test02() throws Exception {
        /*FileOutputStream fos = new FileOutputStream(new File("D:/temp/1234567.pdf"));
        PDFTools.createPDF(fos, "2019010001", "2019010002");*/
    }

    @Test
    public void test01() throws Exception {
        /*FileOutputStream fos = new FileOutputStream(new File("D:/temp/123.pdf"));
        PDFTools.download(fos);*/
    }
}
