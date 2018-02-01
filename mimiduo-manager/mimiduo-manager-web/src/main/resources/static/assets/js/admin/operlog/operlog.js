NB.ns("app.admin").operlog = (function() {
	var url = $('#operationLog_dg').data("search-url");
	function init() {
		$('#operationLog_dg').datagrid({
           	url: url,
            required : true
         });
    }
	function searcherLog(){
//		$('#searche_fm').form('submit', {
//            url : url,
//            success : function(result) {       	
//                result = eval("(" + result + ")");               
//                $('#operationLog_dg').datagrid({
//                	data : result
//                });
//            }
//        });
		 $('#operationLog_dg').datagrid('load', {
			 userName : $('#userName').val(),
			 userId : $('#userId').val(),
			 sysCode : $('#subSysCode').combotree('getText'),
			 start : $('#start').datebox('getValue'),
			 end : $('#end').datebox('getValue')
         });
	}
	
	return {
		init : init,
		searcherLog : searcherLog
	}
	
})()