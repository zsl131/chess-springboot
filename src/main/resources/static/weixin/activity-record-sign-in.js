
function findByPhone() {
    var phone = $("#phone").val();
    console.log(phone);
    if(!phone || phone.length!=11) {
        $.toast("手机号错误", "cancel");
    } else {
        var recordId = $("input[name='recordId']").val();
        window.location.href = "/wx/activityRecord/signIn?recordId="+recordId+"&phone="+phone;
    }
}

function onCheck(obj) {
    var objId = $(obj).attr("objId");
    $.post("/wx/activityRecord/signPost", {id: objId}, function(res) {
        if(res=='1') {
            $.toast("签到成功", function() {
              window.location.reload();
            });
        } else {
            alert(res);
        }
    });
}