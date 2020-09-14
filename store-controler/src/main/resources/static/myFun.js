function isEmpty(str) {
    if (str == null) {
        return true;
    }
    if (str == "") {
        return true;
    }
    return false;
}
function isNotEmpty(str) {
    return !isEmpty(str);
}
function alertInfo(info) {
    $.messager.alert('提示信息！', info, 'info');
}
function alertSuccess() {
    $.messager.alert('提示信息！', "操作成功！", 'info');
}
function alertWarn(info) {
    $.messager.alert('警告操作！', info, 'warning');
}
function loadTip() { // 弹出加载层
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("加载中。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });
}
function disloadTip() { // 关闭加载层
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}
function ajaxSynchPostJson(conectUrl, params) {
    var res = {"code": 500, "msg": "数据传输错误！"};
    if (params == null) {
        params = {};
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "POST",
        contentType: 'application/json',
        cache: false,
        data: JSON.stringify(params),
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
function ajaxSynchGet(conectUrl, params) {
    var res = {"code": 500, "msg": "数据传输错误！"};
    if (params == null) {
        params = {};
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "GET",
        cache: false,
        data: params,
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
/*
 入参格式如下：
 var fileParams = [
 {key:"myfile", value:"#excelFileUpload"},
 ];
 var otherParams = [
 {key:"param1", value:"你好啊"},
 {key:"param2", value:"我好啊"}
 ];
 并且第一个参数必须是文件，且要带#
 */
function ajaxSynchUploadFilePost(conectUrl, fileParams, otherParams) {
    var res = {"code": 500, "msg": "数据传输错误！"};
    var formData = new FormData();
    if (fileParams != null && fileParams.length > 0) {
        for(var x = 0; x < fileParams.length; x++) {
            formData.append(fileParams[x].key, $(fileParams[x].value)[0].files[0]);
        }
    }
    if (otherParams != null && otherParams.length > 0) {
        for(var x = 0; x < otherParams.length; x++) {
            formData.append(otherParams[x].key, otherParams[x].value);
        }
    }
    $.ajax({
        async: false,
        url: contextPath + conectUrl,
        type: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (result) {
            res = result;
        },
        error: function (xhr, status, error) {},
        beforeSend: function(xhr) {
            xhr.setRequestHeader("token", interceptorToken);
            xhr.setRequestHeader("userId", interceptorUserId);
        }
    });
    return res;
}
function urlHandle(url) {
    return url + '?token=' + interceptorToken + '&userId=' + interceptorUserId + '&randNum=' + Math.random();
}
function urlHandleContext(url) {
    return contextPath + url + '?token=' + interceptorToken + '&userId=' + interceptorUserId + '&randNum=' + Math.random();
}
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
function getNowDatetime() {
    return new Date().Format("yyyy-MM-dd hh:mm:ss");
}
// ------------------------ 工具方法 ------------------------
function isTreeNodeSelected(treeId) {
    var selectNode = $("#" + treeId).tree('getSelected');
    if (selectNode != null && selectNode != undefined) {
        return true;
    } else {
        return false;
    }
}
function getTreeSelectedNode(treeId) {
    return $("#" + treeId).tree('getSelected');
}