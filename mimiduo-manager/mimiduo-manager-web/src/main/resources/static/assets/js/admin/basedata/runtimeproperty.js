NB.ns("app.runtimeproperty").t = (function() {
    var url;
    function newRuntime() {
    	if($('#key').attr("readonly")=="readonly"){
    		$('#key').attr("readonly",false);
        }
        $('#runtime-dlg').dialog('open').dialog('setTitle', '新增运行配置');
        $('#runtime-fm').form('clear');

        $('#runtime-fm #readOnly').attr("disabled", true);
        url = $('#runtimeProperty').data("save-url");
    }

    function editRuntime() {
        var row = $('#runtimeProperty').datagrid('getSelected');
        if (row) {
            $('#runtime-dlg').dialog('open').dialog('setTitle', '编辑运行配置');

            $('#runtime-fm').form('load', row);
            $('#key').attr("readonly","readonly");
            $('#runtime-fm #readOnly').attr("disabled", true);
            url = $('#runtimeProperty').data("update-url") + '?id=' + row.id;
        } else {
        	$.messager.alert("提示", "未选中要操作的行!");
        }
    }

    function saveRuntime() {
        $('#runtime-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.success) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#runtime-dlg').dialog('close');
                    $('#runtimeProperty').datagrid('reload');
                    
                    $.messager.show({
                        title : '提示',
                        msg : '操作完成!'
                    });
                }
            }
        });
    }

    function destroyRuntime() {
        var row = $('#runtimeProperty').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此配置?', function(r) {
                if (r) {
                    $.post($('#runtimeProperty').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.success) {
                            $('#runtimeProperty').datagrid('reload');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        } else {
        	$.messager.alert("提示", "未选中要操作的行!");
        }
    }

    function refreshRuntimes() {
        $('#runtimeProperty').datagrid('load', {});
    }

    function reloadDatagrid() {
        $('#runtimeProperty').datagrid('load', {});
    }

    function formatToolBtns(val, row, index) {
        return "";
    }

    function formatReadOnly(val, row) {
        return val ? "是" : "否";
    }

    function onLoadSuccess(data) {
        $('.easyui-linkbutton').linkbutton();
    }

    function onSearch() {
        $('#runtimeProperty').datagrid('load', {
            f_LIKE_name : $('#f_key').val(),
            f_EQ_type : $('#f_type').combo('getValue')
        });
    }

    function formatType(val) {
        var typesJSON = $('#runtimeProperty').attr("typesJSON");
        typesJSON = $.parseJSON(typesJSON);
        return typesJSON[val];
    }

    function init() {
    	$("input").attr("autocomplete","off");
    	 $('#runtimeProperty').datagrid({
         	url: '/admin/basedata/runtimeproperty/data/runtimeproperty.json?f_EQ_isDeleted=0',
            required : true
         });
    }

    return {
        newRuntime : newRuntime,
        saveRuntime : saveRuntime,
        editRuntime : editRuntime,
        destroyRuntime : destroyRuntime,
        refreshRuntimes : refreshRuntimes,
        formatToolBtns : formatToolBtns,
        formatReadOnly : formatReadOnly,
        formatType : formatType,
        onLoadSuccess : onLoadSuccess,
        onSearch : onSearch,
        init : init
    }
})();
