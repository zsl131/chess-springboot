$(function() {
    $(".check-images").find("img").each(function() {
        $(this).click(function() {
            var obj = $(this);
            //console.log(obj+"----")
            var src = $(obj).attr("src");
            if(!src.startsWith("http://") && !src.startsWith("https://")) {
                src = "http://wx.qswkx.com"+src;
            }
            window.location.href = src;
            //console.log(src)
        })
    });
})