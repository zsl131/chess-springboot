package com.zslin.bus.common.controller;

import com.zslin.basic.tools.Base64Utils;
import com.zslin.bus.common.dto.AppUserDto;
import com.zslin.bus.common.iservice.IApiCodeSerivce;
import com.zslin.bus.common.iservice.IApiTokenCodeService;
import com.zslin.bus.common.iservice.IApiTokenService;
import com.zslin.bus.common.model.ApiCode;
import com.zslin.bus.common.model.ApiToken;
import com.zslin.bus.common.model.ApiTokenCode;
import com.zslin.bus.common.tools.AppUserLoginTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.test.dao.INewsDao;
import com.zslin.bus.tools.JsonParamTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.test.AesEncryptUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/7/3.
 */
@RestController
@RequestMapping(value = "api")
public class ApiController {

    @Autowired
    private INewsDao newsService;

    @Autowired
    private BeanFactory factory;

    @Autowired
    private IApiTokenService apiTokenService;

    @Autowired
    private IApiCodeSerivce apiCodeSerivce;

    @Autowired
    private IApiTokenCodeService apiTokenCodeService;

    @Autowired
    private AppUserLoginTools appUserLoginTools;

    private boolean ignoreCheck(String apiCode) {
        List<String> apiList = new ArrayList<>();
        apiList.add("login");
        apiList.add("checkUpdate");
        boolean res = false;
        for(String api:apiList) {
            if(apiCode.contains(api)) {res = true; break;}
        }
        return res;
    }

    /**
     * 此接口调用的业务接口有一个名为"handle"的方法，此方法接受两个String类型的参数action和params
     *  - action - 具体的处理业务
     *  - params - 对应的json参数
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "baseRequest")
    public JsonResult baseRequest(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("auth-token"); //身份认证token
        String apiCode = request.getHeader("api-code"); //接口访问编码
//        String loginToken = request.getHeader("login-token"); //登陆token
//        System.out.println("---------"+loginToken);
        AppUserDto userDto = JsonTools.buildUserDto(request); //用户信息
//        System.out.println(userDto);
        if(token == null || "".equals(token) || apiCode==null || "".equals(apiCode)) {
            return JsonResult.getInstance().fail("auth-token或api-code为空");
        }
       // System.out.println("ApiController--> apiCode:"+apiCode+", ignore:"+ignoreCheck(apiCode)+", userDto: "+userDto.toString());
        //检测token是否有效
        if(!ignoreCheck(apiCode) && !appUserLoginTools.checkLogin(userDto.getPhone(), userDto.getToken())) {
            return JsonResult.getInstance().fail("用户登陆失效，请重新登陆").set("login", "timeout");
        }
        try {
            String serviceName = apiCode.split("\\.")[0];
            String actionName = apiCode.split("\\.")[1];
            Object obj = factory.getBean(serviceName);
            Method method ;
            boolean hasParams = false;
            String params = request.getParameter("params");
            if(params==null || "".equals(params.trim())) {
                method = obj.getClass().getMethod(actionName);
            } else {
                params = Base64Utils.getFromBase64(params);
                params = URLDecoder.decode(params, "utf-8");
//                System.out.println("============="+params);

                params = JsonParamTools.rebuildParams(params, request);

                method = obj.getClass().getMethod(actionName, params.getClass());
                hasParams = true;
            }
            JsonResult result;
            if(hasParams) {
                result = (JsonResult) method.invoke(obj, params);
            } else {
                result = (JsonResult) method.invoke(obj);
            }
//            System.out.println("result: "+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.getInstance().fail("数据请求失败："+e.getMessage());
        }
    }

    @RequestMapping(value = "queryOrSubmit")
    public JsonResult queryOrSubmit(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("auth-token"); //身份认证token
        String apiCode = request.getHeader("api-code"); //接口访问编码
        if(token == null || "".equals(token) || apiCode==null || "".equals(apiCode)) {
            return JsonResult.getInstance().fail("auth-token或api-code为空");
        }

        ApiToken at = apiTokenService.findByToken(token);
        if(at==null || !"1".equals(at.getStatus())) {
            return JsonResult.getInstance().fail("auth-token不存在或被停用");
        }
        ApiTokenCode atc = apiTokenCodeService.findByTokenAndCode(token, apiCode);
        if("1".equals(at.getIsSuper()) || atc != null) { //有权限
            String serviceName, methodName;
            if(atc==null) {
                ApiCode ac = apiCodeSerivce.findByCode(apiCode);
                if(ac==null) {
                    return JsonResult.getInstance().fail("接口【"+apiCode+"】不存在");
                }
                serviceName = ac.getServiceName();
                methodName = ac.getMethodName();
            } else {
                serviceName = atc.getServiceName();
                methodName = atc.getMethodName();
            }
            String params = request.getParameter("params");
            try {
                Object obj = factory.getBean(serviceName);
                Method method ;
                boolean hasParams = false;
                if(params==null || "".equals(params.trim())) {
                    method = obj.getClass().getMethod(methodName);
                } else {
                    method = obj.getClass().getMethod(methodName, params.getClass());
                    hasParams = true;
                }
//                Method method = obj.getClass().getMethod(methodName, (params==null || "".equals(params.trim()))?:params.getClass());
                JsonResult result;
                if(hasParams) {
                    result = (JsonResult) method.invoke(obj, params);
                } else {
                    result = (JsonResult) method.invoke(obj);
                }
//                System.out.println("result: "+result);
                return result;
//                JsonResult result = (JsonResult) method.invoke(obj, params);
//                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return JsonResult.getInstance().fail("数据请求失败："+e.getMessage());
            }
        } else {
            return JsonResult.getInstance().fail("无权限访问【"+apiCode+"】");
        }
    }
}
