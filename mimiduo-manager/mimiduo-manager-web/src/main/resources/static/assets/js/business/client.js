NB.ns("app.business").client = (function() {

	var url;
	function refreshClients() {
		$('#clientdg').datagrid('load', {});
	}

	function userName(val) {
		var userJSON = $('#clientdg').attr("userJSON");
		userJSON = $.parseJSON(userJSON);
		for (var i = 0; i < userJSON.length; i++) {
			if (val == userJSON[i].id)
				return userJSON[i].name;
		}
	}

	function formatStatus(val) {
		var statusJSON = $('#clientdg').attr("statusJSON");
		statusJSON = $.parseJSON(statusJSON);
		if (val == 1) {
			return "<font color='red'>" + statusJSON[val] + "</font>";
		} else {
			return "<font color='blue'>" + statusJSON[val] + "</font>";
		}
	}

	
	function formatToolBtns(val, row, index) {
		var t = '<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.client.editClient(${index})" title="编辑">编辑</a>'
				+ '&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.client.destroyClient()" title="删除">删除</a>';
		return _.template(t, {
			id : row.id,
			index : index,
		});
	}
	
	function onSearch() {
		$('#clientdg').datagrid('load', {
			f_LIKE_name : $('#f_name').val()
		});
	}

	function newClient() {
		$('#clients-dlg').dialog('open').dialog('setTitle', '新增客户');
		$('#clients-fm').form('clear');
		_loadBusUserData();
		url = $('#clientdg').data("save-url");
	}

	function editClient(index) {

		var row = $('#clientdg').datagrid('getRows')[index];
		if (row) {
			$('#clients-dlg').dialog('open').dialog('setTitle', '编辑客户');
			_loadBusUserData();

			$('#clients-fm').form('load', row);
            $('#busUserId').combobox('select',row.busUserId);
			url = $('#clientdg').data("update-url") + '?id=' + row.id;
		} else {
			alert("未选中记录");
		}
	}
	
	function _loadBusUserData() {
		var userJSON = $('#clientdg').attr("userJSON");
		userJSON = $.parseJSON(userJSON);
		$('#busUserId').combobox({
			data : userJSON,
			valueField : 'id',
			textField : 'name'
		});
	}

	

	function saveClient() {
		$('#clients-fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				debugger;
				result = eval("(" + result + ")");
				if (!result.result) {
					$.messager.alert('出错了', result.message, 'error');
				} else {
					alert(result.message);
					$('#clients-dlg').dialog('close'); // close the dialog
					$('#clientdg').datagrid('reload'); // reload the privilege
					// data

					$.messager.show({
						title : '提示',
						msg : '操作完成!'
					});
				}
			}
		});
	}

	

	function destroyClient(index) {
		var row = $('#clientdg').datagrid('getRows')[index];
		if (row) {
			if (confirm("确认删除当前记录?")) {
				url = $('#clientdg').data("delete-url") + '?id=' + row.id;
				$.ajax({
					url : url,
					method : "POST",
					dataType : 'json',
					success : function(result) {
						if (result.result) {
							alert(result.message);
							$('#clientdg').datagrid('reload');
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
		refreshClients : refreshClients,
		userName : userName,
		onSearch : onSearch,
		newClient : newClient,
		saveClient : saveClient,
		editClient : editClient,
		destroyClient : destroyClient,
		formatStatus : formatStatus,
		formatToolBtns:formatToolBtns
	};
})();