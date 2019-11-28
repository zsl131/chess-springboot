package com.zslin.bus.common.controller;

import com.zslin.bus.finance.dao.IFinanceDetailDao;
import com.zslin.bus.finance.dao.IFinanceRecordDao;
import com.zslin.bus.finance.dao.IFinanceTicketDao;
import com.zslin.bus.finance.model.FinanceDetail;
import com.zslin.bus.finance.model.FinanceRecord;
import com.zslin.bus.finance.model.FinanceTicket;
import com.zslin.bus.finance.tools.PDFTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by zsl on 2019/1/11.
 */
@RestController
@RequestMapping(value = "api/showFinancePdf")
public class ShowFinancePdfController {

    @Autowired
    private IFinanceRecordDao financeRecordDao;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IFinanceTicketDao financeTicketDao;

    @Autowired
    private PDFTools pdfTools;

    @GetMapping(value = {"", "/", "index"})
    public void index(String ticketNo, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf"); // 设置返回内容格式
            OutputStream os = response.getOutputStream();
            FinanceRecord record = financeRecordDao.findByTicketNo(ticketNo);
            List<FinanceDetail> detailList = financeDetailDao.findByTicketNo(ticketNo);
            List<FinanceTicket> ticketList = financeTicketDao.findByTicketNo(ticketNo);
            pdfTools.buildPDF(os, record, detailList, ticketList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
