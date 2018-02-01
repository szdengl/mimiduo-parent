NB.ns("app.business").project = (function() {

	function init() {
		// alert("hellor")
		_loadClientData();
	}

	var url;
	function refreshProject() {
		onSearch();
	}

	function onSearch() {
		debugger;
		$('#projectdg')
				.datagrid(
						'load',
						{
							f_LIKE_name : $('#f_name').val(),
							f_EQ_clientId : $('#f_client_id').combobox(
									'getValue') != -1 ? $('#f_client_id')
									.combobox('getValue') : "",
						});
	}
	function _loadClientData() {
		var clientJSON = $('#projectdg').attr("clientJSON");
		clientJSON = $.parseJSON(clientJSON);
		$('#f_client_id').combobox({
			data : clientJSON,
			valueField : 'id',
			textField : 'name'
		});
	}

	function formatToolBtns(val, row, index) {
		var t = '<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.project.editProject(${index})" title="编辑">编辑</a>'
				+ '&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.project.destroyProject(${index})" title="删除">删除</a>';
		return _.template(t, {
			id : row.id,
			index : index,
		});
	}
	function newProject() {
		$('#projects-dlg').dialog('open').dialog('setTitle', '新增项目');
		$('#projects-fm').form('clear');
		var client_id = $('#f_client_id').combo('getValue');
		if (client_id == "") {
			$('#projects-dlg').dialog('close'); // close the dialog
			$.messager.alert('出错了', '客户不能为空！', 'error');
		}
		$('#clientId').val(client_id);
		url = $('#projectdg').data("save-url");
	}

	function formatStatus(val) {
		var statusJSON = $('#projectdg').attr("statusJSON");
		statusJSON = $.parseJSON(statusJSON);
		if (val == 1) {
			return "<font color='red'>" + statusJSON[val] + "</font>";
		} else {
			return "<font color='blue'>" + statusJSON[val] + "</font>";
		}
	}

	function saveProject() {
		$('#projects-fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				result = eval("(" + result + ")");
				if (!result.result) {
					$.messager.alert('出错了', result.message, 'error');
				} else {
					alert(result.message);
					$('#projects-dlg').dialog('close'); // close the dialog
					$('#projectdg').datagrid('reload'); // reload the privilege
														// data

					$.messager.show({
						title : '提示',
						msg : '操作完成!'
					});
				}
			}
		});
	}

	function editProject(index) {
		var row = $('#projectdg').datagrid('getRows')[index];
		if (row) {
			$('#projects-dlg').dialog('open').dialog('setTitle', '编辑项目');
			$('#projects-fm').form('load', row);
			url = $('#projectdg').data("update-url") + '?id=' + row.id;
		} else {
			alert("未选中记录");
		}
	}

	function destroyProject(index) {
		var row = $('#projectdg').datagrid('getRows')[index];
		if (row) {
			if (confirm("确认删除当前记录?")) {
				url = $('#projectdg').data("delete-url") + '?id=' + row.id;
				$.ajax({
					url : url,
					method : "POST",
					dataType : 'json',
					success : function(result) {
						if (result.result) {
							alert("ok");
							$('#projectdg').datagrid('reload');
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
		init : init,
		refreshProject : refreshProject,
		onSearch : onSearch,
		newProject : newProject,
		saveProject : saveProject,
		editProject : editProject,
		destroyProject : destroyProject,
		formatStatus : formatStatus,
		formatToolBtns : formatToolBtns
	};
})();