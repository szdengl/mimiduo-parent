NB.ns("app.business").channel = (function() {
	var url;
	function refreshChannel() {
		onSearch();
	}

	function onSearch() {
		var status = $('#f_isstatus').combo('getValue');
		var company=$('#f_company_id').combobox('getValue');
        $('#channeldg')
				.datagrid(
						'load',
						{
							f_LIKE_name : $('#f_name').val(),
							f_EQ_companyId : company!= "-1" ? company : "",
							f_EQ_isstatus :  status!="-1"?status:"",
                            f_LIKE_spnumber :$('#f_spnumber').val()
						});
	}

	function formatStatus(val) {
		var actionJSON = $('#channeldg').attr("actionJSON");
        actionJSON = $.parseJSON(actionJSON);
		if (val == 1) {
			return "<font color='red'>" + actionJSON[val] + "</font>";
		} else if (val == 0) {
			return "<font color='blue'>" + actionJSON[val] + "</font>";
		} else {
			return "<font color='greed'>" + actionJSON[val] + "<font>";
		}
	}

	function attributeStatus(val) {
		var statusJSON = $('#channeldg').attr("attributeJSON");
		statusJSON = $.parseJSON(statusJSON);
		for (var i = 0; i < statusJSON.length; i++) {
			if (val == statusJSON[i].id)
				return statusJSON[i].name;
		}

	}

	function companyName(val) {
        var companyJSON=$('#channeldg').attr("companyJSON");
        companyJSON=$.parseJSON(companyJSON);
        for(var i=0;i<companyJSON.length;i++){
            if(val==companyJSON[i].id)
                return companyJSON[i].name;
        }
    }

    function formatToolBtns(val, row, index) {
        var t = '<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.channel.editChannel(${index})" title="编辑">编辑</a>'
            + '&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.channel.destroyChannel(${index})" title="删除">删除</a>'
		    + '&nbsp;||&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton " plain="true" onclick="app.business.channel.onClickRow(${index})" title="开通省份">开通省份</a>'
        return _.template(t, {
            id : row.id,
            index : index,
        });
    }

    function newChannel() {
        $('#channels-dlg').dialog('open').dialog('setTitle', '新增业务');
        $('#channels-fm').form('clear');
        var company_id = $('#f_company_id').combo('getValue');
        if (company_id == "") {
            $('#channels-dlg').dialog('close'); // close the dialog
            $.messager.alert('出错了', '通道商不能为空！', 'error');
        }
        $('#companyId').val(company_id);
        $('#visitType').combobox("setValue","1");
        $('#isExec').combobox("setValue","0");
        $('#isSycn').combobox("setValue","0");
        $('#contentType').combobox("setValue","1");
        $('#isThirdCmd').combobox("setValue","0");
        $('#isGet').combobox("setValue","1");
        $('#visitType2').combobox("setValue","1");
        $('#isExec2').combobox("setValue","0");
        $('#isSycn2').combobox("setValue","0");
        $('#contentType2').combobox("setValue","1");
        $('#isThirdCmd2').combobox("setValue","0");
        $('#isGet2').combobox("setValue","1");
        $('#visitType1').combobox("setValue","1");
        $('#isExec1').combobox("setValue","0");
        $('#isSycn1').combobox("setValue","0");
        $('#contentType1').combobox("setValue","1");
        $('#isThirdCmd1').combobox("setValue","0");
        $('#isGet1').combobox("setValue","1");
        url = $('#channeldg').data("save-url");
    }

    function editChannel(index) {
        var row = $('#channeldg').datagrid('getRows')[index];
        debugger;
        if (row) {
            $('#channels-dlg').dialog('open').dialog('setTitle', '编辑项目');
            $('#channels-fm').form('load', row);
            $('#visitType').combobox("select",row.visitType);
            $('#isExec').combobox("select",row.isExec);
            $('#isSycn').combobox("select",row.isSycn);
            $('#contentType').combobox("select",row.contentType);
            $('#isGet').combobox("select",row.isGet);
            $('#visitType1').combobox("select",row.visitType1);
            $('#isExec1').combobox("select",row.isExec1);
            $('#isSycn1').combobox("select",row.isSycn1);
            $('#contentType1').combobox("select",row.contentType1);
            $('#isGet1').combobox("select",row.isGet1);
            $('#visitType2').combobox("select",row.visitType2);
            $('#isExec2').combobox("select",row.isExec2);
            $('#isSycn2').combobox("select",row.isSycn2);
            $('#contentType2').combobox("select",row.contentType2);
            $('#isGet2').combobox("select",row.isGet2);
            url = $('#channeldg').data("update-url") + '?id=' + row.id;
        } else {
            alert("未选中记录");
        }
    }

	function saveChannel() {
		$('#channels-fm').form('submit', {
			url : url,
			onSubmit : function() {
				debugger;
				return $(this).form('validate');
			},
			success : function(result) {
				result = eval("(" + result + ")");
				if (!result.result) {
					$.messager.alert('出错了', result.message, 'error');
				} else {
					alert(result.message);
					$('#channels-dlg').dialog('close'); // close the dialog
					$('#channeldg').datagrid('reload'); // reload the privilege data
					$.messager.show({
						title : '提示',
						msg : '操作完成!'
					});
				}
			}
		});
	}

    function destroyChannel(index) {
        var row = $('#channeldg').datagrid('getRows')[index];
        if (row) {
            if (confirm("确认删除当前记录?")) {
                url = $('#channeldg').data("delete-url") + '?id=' + row.id;
                $.ajax({
                    url : url,
                    method : "POST",
                    dataType : 'json',
                    success : function(result) {
                        if (result.result) {
                            alert("ok");
                            $('#channeldg').datagrid('reload');
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


	function onClickRow(index) {
        var row = $('#channeldg').datagrid('getRows')[index];
		$('#easyui-layout-content').layout('expand','south');
		$('#province-channelId').text(row.id);
		// _loadDetailData();
		var url = $('#channeldg-channel-province').data("url") + "?channelId=" 	+ row.id;
		$('#channeldg-channel-province').datagrid({
			url : url
		});
	}



	/** **********************省份管理************************************ */
	function newProvince() {
		debugger;
		var channelId=$('#province-channelId').text();
		if (channelId!="" && channelId!="0") {
			$('#channel-province-dlg').dialog('open') .dialog('setTitle', '开通省份');
			$('#provinceId').combobox({
                disabled:false
            });
			$('#channels-province-fm').form('clear');
			$('#channelId').val(channelId);
			url = $('#channeldg-channel-province').data("save-url");
		} else {
			alert("请先选中要业务");
		}
	}

	function editProvince() {
		var row = $('#channeldg-channel-province').datagrid('getSelected');
		if (row) {
			$('#channel-province-dlg').dialog('open').dialog('setTitle', '配置省份');
			$('#provinceId').combobox({
                disabled:true
            });
			$('#channels-province-fm').form('load', row);
			url = $('#channeldg-channel-province').data("update-url") + '?id=' + row.id;
		} else {
			alert("未选中记录");
		}
	}

	function saveProvince() {
		$('#channels-province-fm').form('submit', {
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
					$('#channel-province-dlg').dialog('close'); // close the
					// dialog
					$('#channeldg-channel-province').datagrid('reload'); // reload
					// the
					// privilege
					// data
					$.messager.show({
						title : '提示',
						msg : '操作完成!'
					});
				}
			}
		});
	}

	function destroyProvince() {
		var row = $('#channeldg-channel-province').datagrid('getSelected');
		if (row) {
			if (confirm("确认删除当前记录?")) {
				url = $('#channeldg-channel-province').data("delete-url") + '?id='
						+ row.id;
				$.ajax({
					url : url,
					method : "POST",
					dataType : 'json',
					success : function(result) {
						if (result.result) {
							alert("ok");
							$('#channeldg-channel-province').datagrid('reload');
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
		refreshChannel : refreshChannel,
		onSearch : onSearch,
		newChannel : newChannel,
		saveChannel : saveChannel,
		editChannel : editChannel,
		destroyChannel : destroyChannel,
		formatStatus : formatStatus,
		attributeStatus : attributeStatus,
        formatToolBtns:formatToolBtns,
        companyName:companyName,
		onClickRow : onClickRow,
		newProvince : newProvince,
		saveProvince : saveProvince,
		editProvince : editProvince,
		destroyProvince : destroyProvince
	};
})();