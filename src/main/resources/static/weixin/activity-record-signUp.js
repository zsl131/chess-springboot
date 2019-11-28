$(function() {
    $(".record_input").each(function() {
        var stuId = $(this).val();
        var status = $(this).attr("status");
        var s = status=='0'?"<b style='color:#888'>未审核</b>":(status=='1'?"<b style='color:#00F;'>通过</b>":"驳回");
        $(("#act_id_")+stuId).html("（已报名，"+s+"）");
    });

    $(".add-student").click(function() {
        $.get("/public/json/onAddStudent", {}, function(res) {
            console.log(res);
            var ageHtml = '';
            res.result.ageList.forEach(function(obj) {ageHtml += '<option value="'+obj.id+'">'+obj.name+'</option>';});
            var schoolHtml ='';
            res.result.schoolList.forEach(function(obj) {schoolHtml += '<option value="'+obj.id+'">'+obj.name+'</option>';});
            //console.log(ageHtml);
            var html =
            '<div class="weui-cells weui-cells_form">'+
                  '<div class="weui-cell">'+
                    '<div class="weui-cell__hd"><label class="weui-label">学生姓名</label></div>'+
                    '<div class="weui-cell__bd">'+
                      '<input class="weui-input" name="name" type="text" placeholder="请输入学生姓名">'+
                    '</div>'+
                  '</div>'+
                  '<div class="weui-cell weui-cell_vcode">'+
                    '<div class="weui-cell__hd">'+
                      '<label class="weui-label">手机号码</label>'+
                    '</div>'+
                    '<div class="weui-cell__bd">'+
                      '<input class="weui-input" type="tel" name="phone" placeholder="请输入手机号码">'+
                    '</div>'+
                  '</div>'+
                  '<div class="weui-cell weui-cell_select weui-cell_select-after">'+
                      '<div class="weui-cell__hd">'+
                        '<label for="" class="weui-label">性别</label>'+
                      '</div>'+
                      '<div class="weui-cell__bd">'+
                        '<select class="weui-select" name="sex">'+
                          '<option value="1">男</option>'+
                          '<option value="2">女</option>'+
                        '</select>'+
                      '</div>'+
                    '</div>'+

                    '<div class="weui-cell weui-cell_select weui-cell_select-after">'+
                      '<div class="weui-cell__hd">'+
                        '<label for="" class="weui-label">年龄段</label>'+
                      '</div>'+
                      '<div class="weui-cell__bd">'+
                        '<select class="weui-select" name="age">'+
                          ageHtml+
                        '</select>'+
                      '</div>'+
                    '</div>'+

                    '<div class="weui-cell weui-cell_select weui-cell_select-after">'+
                      '<div class="weui-cell__hd">'+
                        '<label for="" class="weui-label">就读学校</label>'+
                      '</div>'+
                      '<div class="weui-cell__bd">'+
                        '<select class="weui-select" name="school">'+
                          schoolHtml+
                        '</select>'+
                      '</div>'+
                    '</div>'+

                '</div>';
            var addStuDialog = confirmDialog(html, "<span class='fa fa-plus'></span> 添加学生", function() {
                var name = $(addStuDialog).find("input[name='name']").val();
                var phone = $(addStuDialog).find("input[name='phone']").val();
                var sex = $(addStuDialog).find("select[name='sex']").val();
                var age = $(addStuDialog).find("select[name='age']").val();
                var school = $(addStuDialog).find("select[name='school']").val();

                console.log(name+","+phone+", "+sex+", "+age+", "+school);
                //$(addStuDialog).remove();
                if(!name || !phone) {
                    $.toast("请完善信息", "cancel", function(toast) {});
                } else {
                    $(addStuDialog).remove();
                    $.post("/wx/activityRecord/addStudent", {name: name, phone: phone, age: age, sex: sex, school: school}, function(res) {
                        if(res=='1') {
                            $.toast("添加成功", function() {
                              window.location.reload();
                            });
                        } else {
                            alert(res);
                        }
                    });
                }
            });
        });
    });
});

function optStu(flag) {
    var stuList = $(".student-list").find("input[type='checkbox']:checked");
    if(stuList.length<=0) {
        $.toast("请选择学生", "cancel", function(toast) {});
    } else {
        var names = "", ids = "";
        for(var i=0;i<stuList.length; i++) {
            var target = stuList[i];
            names += $(target).attr("stuName")+"，";
            ids += $(target).attr("stuId")+",";
        }
        //console.log(names+"---"+ids);
        names = names.indexOf("，")>0?names.substring(0, names.length-1):names;
        $.confirm("您确定"+(flag=='0'?"删除":"报名")+"["+names+"]吗?", "确认"+(flag=='0'?"删除":"报名")+"?", function() {
          if(flag=='0') {
            $.post("/wx/activityRecord/deleteStus", {ids: ids}, function(res) {
                if(res=='1') {
                    $.toast("删除成功", function() {
                        window.location.reload();
                    });
                }
              });
          } else {
            var from = $("input[name='from']").val();
            var recordId = $("input[name='recordId']").val();
            //alert(from+"---"+recordId);
            $.post("/wx/activityRecord/addStudentActivity", {ids: ids, from: from, recordId: recordId}, function(res) {
                if(res=='1') {
                    $.toast("报名成功", function() {
                        window.location.reload();
                    });
                } else {
                    alert(res);
                }
            });
          }
        }, function() {
          //取消操作
        });
    }
}