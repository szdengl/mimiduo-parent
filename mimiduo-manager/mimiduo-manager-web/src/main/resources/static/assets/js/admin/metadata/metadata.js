NB.ns("app.admin").metadata = (function() {
	
	var url;
	
	function newMetadata(){
		var node = $('#metaDataType-tree').tree('getSelected');
		if(node){
			$('#metaData-dlg').dialog('open').dialog('setTitle', '新增基础数据');
			url = '/admin/metaData/save';
		}else{
			$.messager.alert("提示", "请选择类型!");
		}
	}
	
	function save(){
		$('#metaData-fm').form('submit', {
            url : url,
            onSubmit : function() {                
            	return $(this).form('validate');
            },
            success : function(result) {       	
            	result = JSON.parse(result);              
                if (!result.success) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#metaData-dlg').dialog('close');
                    
                    if(url.indexOf('update')>0){
						var index = $('#metaData_tbl').datagrid('getRowIndex',result.data.id);
						console.log(index);
						console.log(result.data);
						$('#metaData_tbl').datagrid('updateRow',{
							index:index,
							row:result.data
						});
						
					}else{
						$('#metaData_tbl').datagrid('appendRow',result.data);	
					}
                    
                    
                    $.messager.show({
                        title : '提示',
                        msg : '操作完成!'
                    });
                }
            }
        });
	}
	function editMetadata(){
		var row = $('#metaData_tbl').datagrid('getSelected');
        if (row) {
        	$('#metaData-dlg').dialog('open').dialog('setTitle', '编辑基础数据');
            $('#metaData-fm').form('load', row);
            url = '/admin/metaData/update';
        } else {
        	$.messager.alert("提示", "未选中要操作的行!");
        }
	}
	
	function destroyMetadata(){
		var row = $('#metaData_tbl').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此配置?', function(e) {
                if (e) {
                	var index = $('#metaData_tbl').datagrid('getRowIndex',row);
                    $.post('/admin/metaData/delete', {id:row.id}, function(result) {
                        if (result.success) {
                        	$('#metaData_tbl').datagrid('deleteRow',index);
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
	
	function onTreeSelect(){
		var node = $('#metaDataType-tree').tree('getSelected');
		$.post('/admin/metaData/findMetaDataByType', {
			id : node.id
		}, function(result) {
			if (result.success) {
				$('#metaData_tbl').datagrid({data:result.data});
			} else {
				$.messager.alert('出错了', result.message, 'error');
			}
		}, 'json');
	}
	
	function init() {
		$("input").attr("autocomplete","off");
		
    }
		
	
	function close(){
		$('#metaData-dlg').dialog('close');
	}
	
	function onSelectedType(newValue, oldValue){
//		var node = $('#metaDataType-tree').tree('find',newValue);
//		$('#metaDataType-tree').tree('select',node.target);
	}
	
	function onDialogOpen(){
		$('#metaData-fm').form('clear');
		var node = $('#metaDataType-tree').tree('getSelected');
		$('#metaDataTypeCombobox').combobox('setValue',node.id);
		$('#metaDataTypeCombobox').combobox('setText',node.text);
		$('#metaDataTypeCombobox').combo('readonly', true)
	}
	
	function onTreeLoadSuccess(){
		
	}
	
	return {
		init:init,
		newMetadata:newMetadata,
		editMetadata:editMetadata,
		destroyMetadata:destroyMetadata,
		close:close,
		save:save,
		onTreeSelect:onTreeSelect,
		onSelectedType:onSelectedType,
		onDialogOpen:onDialogOpen
	}
	
})()