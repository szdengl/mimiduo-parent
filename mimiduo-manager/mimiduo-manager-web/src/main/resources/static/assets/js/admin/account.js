NB.ns("app.business").account = (function() {

    function init() {

    }

    function doChangePassword() {
//    	var newPassword = $('#newPassword').val();
//    	var s = /^[0-9]*$/;
//    	var flag = s.test(newPassword);
//    	if(flag){
//    		$.messager.alert('信息提示', "密码不能全部都是数字!");
//    		return;
//    	}
//    	var s1 = /^[A-Za-z]+$/;
//    	flag = s1.test(newPassword);
//    	if(flag){
//    		$.messager.alert('信息提示', "密码不能全部是英文字符!");
//    		return;
//    	}
        $('#cp-fm').form('submit', {
            // url : $('#cp-fm').attr("action")
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.success) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $.messager.alert('信息提示', "修改密码成功!");
                }
            }
        });
    }

    return {
        init : init,
        doChangePassword : doChangePassword
    };
})();
