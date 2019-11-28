$(function() {

    var domain = window.location.protocol+"//"+window.location.hostname+":"+window.location.port;
    //console.log(domain);
    var imgUrl = $(".imgUrl").val(); //已有的logo图标
    if(!imgUrl || imgUrl=='') {
        var content = $(".content").html();
        var imgReg = /<img.*?(?:>|\/>)/gi;
        var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
        var arr = content.match(imgReg);  // arr 为包含所有img标签的数组
        imgUrl = arr?arr[0].match(srcReg)[1]:"/publicFile/logo.png"; //图片地址
    }

    imgUrl = (imgUrl.indexOf("http://")>=0||imgUrl.indexOf("https://")>=0)?imgUrl:domain+imgUrl;
    //以上是获取图片路径
    //console.log(imgUrl);

//    var title=document.querySelector('meta[name="title"]').getAttribute('content');   //网页标题
    var title = $(document).attr("title");
//    var desc=document.querySelector('meta[name="description"]').getAttribute('content');   //网页描述
    var desc = document.getElementById("description").content;
    var url = window.location.href;

    $.post("/weixin/js/getTicket", {url: url}, function(res) {
        var response = res.result;
        wx.config({
            debug: false, //调试阶段建议开启，关闭就不弹提示了
            appId: response.appid,//APPID
            timestamp: response.timestamp,//上面main方法中拿到的时间戳timestamp
            nonceStr: response.noncestr,//上面main方法中拿到的随机数nonceStr
            signature: response.sign,//上面main方法中拿到的签名signature
            //需要调用的方法接口
            jsApiList: ['updateAppMessageShareData','updateTimelineShareData', 'onMenuShareAppMessage', 'onMenuShareTimeline']
        });
        //ready
        wx.ready(function () {   //需在用户可能点击分享按钮前就先调用
            shareData = {
                title: title,  // 分享标题
                desc: desc,            // 分享描述
                link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                imgUrl: imgUrl,  // 分享图标
                success: function() {
//                    alert("设置成功分享");
                  // 设置成功
                }
            };
            if(wx.updateTimelineShareData) {
                wx.updateTimelineShareData(shareData);
            } else {
                wx.onMenuShareTimeline({
                    title: title, // 分享标题
                    link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: imgUrl, // 分享图标
                    success: function () {
                    // 用户点击了分享后执行的回调函数
//                    alert("设置成功分享111");
                    }
                });
            }
            if(wx.updateAppMessageShareData) {
                // 1.4.0 新接口 (只调用这个接口在安卓下是无效的)
                wx.updateAppMessageShareData(shareData);
            } else {
                wx.onMenuShareAppMessage({
                    title: title, // 分享标题
                    desc: desc, // 分享描述
                    link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                    imgUrl: imgUrl, // 分享图标
                    type: '', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                    // 用户点击了分享后执行的回调函数
//                    alert("设置成功分享2222");
                    }
                });
            }
        });

    }, "json");
});