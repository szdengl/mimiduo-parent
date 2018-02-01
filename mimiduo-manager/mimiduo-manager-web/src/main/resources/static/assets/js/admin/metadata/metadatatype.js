NB.ns("app.admin").metadatatype = (function() {
	var url;
	function init() {
		$("input").attr("autocomplete","off");
    }
		
	function refresh(){
		$('#metadatatype-tbl').datagrid('reload');
	}
	
	function close(){
		$('#metadatatype-dlg').dialog('close');
	}
	
	function newMetadataType(){
		$('#metadatatype-dlg').dialog('open').dialog('setTitle', '新增数据类型');
		$('#name').attr("readonly",false);
		url = '/admin/metaDataType/save';
		$('#metadatatype-fm').form('clear');
	}
	
	function save(){
		$('#metadatatype-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {       	
                result = eval("(" + result + ")");               
                if (!result.success) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#metadatatype-dlg').dialog('close');
                    refresh();
                    $.messager.show({
                        title : '提示',
                        msg : '操作完成!'
                    });
                }
            }
        });
		
	}
	
	
	function editMetadataType(){
		var row = $('#metadatatype-tbl').datagrid('getSelected');
        if (row) {
        	$('#metadatatype-dlg').dialog('open').dialog('setTitle', '编辑数据类型');
            $('#metadatatype-fm').form('load', row);
            $('#name').attr("readonly","readonly");
            url = '/admin/metaDataType/update';
        } else {
        	$.messager.alert("提示", "未选中要操作的行!");
        }
	}
	
	function destroyMetadataType(){
		var row = $('#metadatatype-tbl').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此配置?', function(e) {
                if (e) {
                	var index = $('#metadatatype-tbl').datagrid('getRowIndex',row);
                    $.post('/admin/metaDataType/delete', {id:row.id}, function(result) {
                        if (result.success) {
                        	$('#metadatatype-tbl').datagrid('deleteRow',index);
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    },'json');
                    
                }
            });
        } else {
        	$.messager.alert("提示", "未选中要操作的行!");
        }
	}
	
	return {
		init:init,
		refresh:refresh,
		save:save,
		close:close,
		newMetadataType:newMetadataType,
		editMetadataType:editMetadataType,
		destroyMetadataType:destroyMetadataType
	}
	
})()