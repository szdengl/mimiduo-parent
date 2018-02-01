NB.ns("app.business").company = (function() {

	var url;
	function refreshCompany() {
		$('#dg').datagrid('load', {

		});
	}

	function onSearch() {
		$('#dg').datagrid('load', {
			f_LIKE_name : $('#f_name').val()
		});
	}



	function formatStatus(val) {
		var statusJSON = $('#dg').attr("statusJSON");
		statusJSON = $.parseJSON(statusJSON);
		if (val == 1) {
			return "<font color='red'>" + statusJSON[val] + "</font>";
		} else {
			return "<font color='blue'>" + statusJSON[val] + "</font>";
		}
	}
	
	
	function formatToolBtns(val, row, index) {
		var t = '<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.company.editCompany(${index})" title="编辑">编辑</a>'
				+ '&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.company.destroyCompany(${index})" title="删除">删除</a>'
				+'&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.company.copyCompany(${index})" title="获取同步地址">获取同步地址</a>';
		return _.template(t, {
			id : row.id,
			index : index,
		});
	}
	
	function newCompany() {
		$('#companys-dlg').dialog('open').dialog('setTitle', '新增客户');
		$('#companys-fm').form('clear');
		url = $('#dg').data("save-url");
	}
	
	function saveCompany() {
		$('#companys-fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				result = eval("(" + result + ")");
				if (!result.success) {
					$.messager.alert('出错了', result.message, 'error');
				} else {
					alert(result.message);
					$('#companys-dlg').dialog('close'); // close the dialog
					$('#dg').datagrid('reload'); // reload the privilege data

					$.messager.show({
						title : '提示',
						msg : '操作完成!'
					});
				}
			}
		});
	}

	function editCompany(index) {
		var row = $('#dg').datagrid('getRows')[index];
		if (row) {
			$('#companys-dlg').dialog('open').dialog('setTitle', '编辑客户');
			$('#companys-fm').form('load', row);
			url = $('#dg').data("update-url") + '?id=' + row.id;
		} else {
			alert("未选中记录");
		}
	}

	function copyCompany(index) {
		var row = $('#dg').datagrid('getRows')[index];
		var content = "";
		var str = new Array();
		str = row.setting.split("|");
		if (str != "") {
			if (str[0] == "SMS") {
				content = "上行同步地址：" + window.location.host + "/sms/" + row.id
						+ "/mo?";

				if (str[7] != undefined) {
					debugger;
					content += ",<br/>下行同步地址：" + window.location.host + "/sms/"
							+ +row.id + "/mr?";
				}
			} else if (str[0] == "IVR") {
				content = "IVR同步地址:" + window.location.host + "/sms/" + row.id
						+ "/ivr?";
			} else if (str[0] == "IVR2") {
				content = "IVR同步地址:" + window.location.host + "/sms/" + row.id
						+ "/ivr2?";
			}
		}
		if (row) {
			$('#companys-setting').dialog({
				closed : false,
				cache : false,
				modal : true,
				closable : false,
				content : content,
				buttons : [ {
					text : '确定',
					handler : function() {
						$('#companys-setting').dialog('close');
					}
				} ]
			});
		} else {
			alert("未选中记录");
		}
	}

	function destroyCompany(index) {
		var row = $('#dg').datagrid('getRows')[index];
		if (row) {
			if (confirm("确认删除当前记录?")) {
				url = $('#dg').data("delete-url") + '?id=' + row.id;
				$.ajax({
					url : url,
					method : "POST",
					dataType : 'json',
					success : function(result) {
						if (result.success) {
							alert("ok");
							$('#dg').datagrid('reload');
						} else {
							alert("出错了" + result.message);
						}
					}
				});
			}
		} else {
			alert("未选中记录");
		}
	}
	return {
		init : function() {
			// alert("hello");
		},
		refreshCompany : refreshCompany,
		onSearch : onSearch,
		newCompany : newCompany,
		saveCompany : saveCompany,
		editCompany : editCompany,
		copyCompany : copyCompany,
		destroyCompany : destroyCompany,
		formatStatus : formatStatus,
		formatToolBtns:formatToolBtns
	};
})();