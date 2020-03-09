$(function() {
    //console.log("------")
    $(".single-activity-image>img").click(function() {
        buildMaskImage($(this));
    });

    $("#image-mask-div").on("swipeleft",function(){
        const target = $(this).find(".image-mask-img").attr("preCount");
        onOpt("pre", target);
     // console.log("------left::"+target)
      //console.log(e)
    });
    $("#image-mask-div").on("swiperight",function(){
        const target = $(this).find(".image-mask-img").attr("nextCount");
        onOpt("next", target);
        //console.log("------right::"+target)
        //console.log(e)
    });
});


function buildMaskImage(obj) {
    const imgUrl = $(obj).attr("src");
    const title = $(obj).attr("title");
    const date = $(obj).attr("holdTime");
    const recordId = $(obj).attr("recordId");
    const curCount = $(obj).attr("imgCount"); //index_left_2
    const array = curCount.split("_");
    const curFlag = array[1]; const curIndex = parseInt(array[2]);
    let preFlag = '', nextFlag = ''; let preIndex = 0, nextIndex = 0;
    if(curFlag=='left') {
        preFlag = nextFlag = 'right';
        preIndex = curIndex - 1; nextIndex = curIndex;
    } else if(curFlag=='right') {
        preFlag = nextFlag = 'left';
        preIndex = curIndex; nextIndex = curIndex+1;
    }
    const preCount = "index_"+preFlag+"_"+preIndex;
    const nextCount = "index_"+nextFlag+"_"+nextIndex;
    //console.log("cur::", curCount);
    //console.log("pre::", preCount);
    //console.log("next:", nextCount);
    const html = buildHtml(imgUrl, preCount, nextCount, title, date, recordId);

    //console.log(html)

    showImage(html);
}

function buildHtml(imgUrl, preCount, nextCount, title, date, recordId) {
    var html = '' +
        '<div class="image-mask-img" preCount="'+preCount+'" nextCount="'+nextCount+'"><img onClick="onOpt(\'img\', \'\')" src="'+imgUrl+'"/></div>'+
        '<div class="image-mask-operate">'+
            '<div class="pre-btn" onClick="onOpt(\'pre\', \''+preCount+'\')"></div>'+
            '<div class="ope-content">'+
                '<div class="title" onClick="onOpt(\'detail\', \''+recordId+'\')">'+title+'</div>'+
                '<div class="date">'+date+'</div>'+
            '</div>'+
            '<div class="next-btn" onClick="onOpt(\'next\', \''+nextCount+'\')"></div>'+
        '</div>';
    return html;
}

function onOpt(flag, target) {
    if(flag=='img') {
        hideImage();
    } else if(flag=='pre') {
        findTarget(target);
    } else if(flag=='next') {
        findTarget(target);
    } else if(flag=='detail') {
        window.location.href = '/weixin/recordImage?recordId='+target;
    }
    //console.log(flag, target);
}

function findTarget(target) {
    const obj = $("img[imgCount='"+target+"']");
    //console.log($(obj).attr("src"));
    if($(obj).attr("src")) {
        buildMaskImage(obj);
    } else {
        showDialog("没有更多了...", "操作提示");
    }
}

function showImage(html) {
    $("#image-mask-div").fadeIn(300);
    $(".image-content-main-div").css({"display": "none"});
    $("#image-mask-div").html(html);
}

function hideImage() {
    $("#image-mask-div").fadeOut(200);
    $(".image-content-main-div").css({"display": "block"});
}