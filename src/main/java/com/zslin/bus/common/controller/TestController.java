package com.zslin.bus.common.controller;

import com.zslin.test.ImageHandleTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    private ImageHandleTools imageHandleTools;

    @GetMapping(value = "processImage")
    public @ResponseBody
    String processImage(Integer w) throws Exception {
        System.out.println("starting---------->"+w);
        imageHandleTools.process(w);
        return "----------";
    }
}
