/*******************************************************************************
 * 用户管理
 * 
 ******************************************************************************/
NB.ns("app.admin").users = (function() {
    var url;
    var selOrg = {
        //id : 0
    };

    function init() {
        $('#users-fm #orgId').combotree({
            url : $('#users-fm #orgId').data('url')+'?f_EQ_isDeleted=0&f_EQ_isActived=1',
            required : true
        });
        
        $('#user_dg').datagrid({
            url : $('#user_dg').data("load-url")+'?f_EQ_isDeleted=0',
            required : true
        });
    }

    function onTreeSelect(node) {
        selOrg = node;

        refreshUsers(selOrg.id);
    }
    
    function formatPrivilegesType(val) {
        var typesJSON = $('#user_dg').attr("typesJSON");
        typesJSON = $.parseJSON(typesJSON);
        return typesJSON[val];
    }

    function newUser() {
        $('#passwordHelp').hide();

        $('#users-dlg').dialog('open').dialog('setTitle', '新增用户');
        $('#users-fm').form('clear');

        $('#div-password').show();
        $('#users-fm #plainPassword').validatebox("enableValidation")
        $('#users-fm #plainPassword').val("123456");
        $('#users-fm #orgId').combotree("setValue", selOrg.id || 0);

        url = $('#user_dg').data("save-url");
    }

    function changePassword() {
        var row = $('#user_dg').datagrid('getSelected');
        if (row) {
            $('#users-change-password-dlg input[name=newPassword]').val("");
            $('#users-change-password-dlg').dialog('open').dialog('setTitle', '修改密码:' + row.name);
        }
    }

    function doChangePassword() {
        var row = $('#user_dg').datagrid('getSelected');
        if (row) {
            $('#users-change-password-fm input[name=id]').val(row.id);
            $('#users-change-password-fm').form('submit', {
                onSubmit : function() {
                    return $(this).form('validate');
                },
                success : function(result) {
                    result = eval("(" + result + ")");
                    if (!result.result) {
                        $.messager.alert('出错了', result.message, 'error');
                    } else {
                        $.messager.alert('提示信息', '修改密码成功!');
                        $('#users-change-password-dlg').dialog('close');
                    }
                }
            });
        }
    }

    function editUser() {
        var row = $('#user_dg').datagrid('getSelected');
        if (row) {
            $('#users-fm #plainPassword').validatebox("disableValidation")
            $('#users-fm #plainPassword').val("");
            $('#div-password').hide();

            $('#users-dlg').dialog('open').dialog('setTitle', '编辑用户');
            $('#users-fm').form('load', row);

            $.getJSON($('#user_dg').data("user-org-url") + "?id=" + row.id, function(result) {
                if (result.result) {
                    $('#users-fm #orgId').combotree("setValue", result.data.id);
                }
            });
            url = $('#user_dg').data("update-url") + '?id=' + row.id;
        }
    }

    function saveUser() {
        $('#users-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.result) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#users-dlg').dialog('close'); // close the dialog
                    $('#user_dg').datagrid('reload'); // reload the user data
                }
            }
        });
    }

    function destroyUser() {
        var row = $('#user_dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此用户?', function(r) {
                if (r) {
                    $.post($('#user_dg').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.result) {
                            $('#user_dg').datagrid('reload');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function refreshUsers(orgId) {
        orgId = orgId || selOrg.id;
        if ($.type(orgId) === "undefined") {
            $('#user_dg').datagrid('load', {})
        } else {
            orgId = orgId || 0;
            $('#user_dg').datagrid('load', {
                orgId : orgId
            });
        }

        // 刷新detail info
        _loadDetailDataByUserId("");
    }

    function formatToolBtns(val, row, index) {
        if (row.id == 1) {// 超级管理员
            return "";
        }

        var t = '<a href="javascript:void(0)" class="easyui-linkbutton " iconCls="icon-${icon}" plain="true" onclick="app.admin.users.changeUserStatus(${id})" title="${name}">${name}</a>';
        return _.template(t, {
            id : row.id,
            icon : (row.status == 0 ? "no" : "ok"),
            name : (row.status == 0 ? "禁用" : "激活")
        });
    }

    function changeUserStatus(id) {
        $.messager.confirm('Confirm', '确认更改当前用户的状态?', function(r) {
            if (r) {
                $.post($('#user_dg').data("change-status-url"), {
                    id : id
                }, function(result) {
                    if (result.result) {
                        $('#user_dg').datagrid('reload');
                    } else {
                        $.messager.alert('出错了', result.message, 'error');
                    }
                }, 'json');
            }
        });
    }

    function onLoadSuccess(data) {
        $('.easyui-linkbutton').linkbutton();
    }

    function formatStatus(val) {
        var statusJSON = $('#user_dg').attr("statusJSON");
        statusJSON = $.parseJSON(statusJSON);
        if (val == 1) {
            return "<font color='red'>" + statusJSON[val] + "</font>";
        } else {
            return "<font color='blue'>" + statusJSON[val] + "</font>";
        }
    }

    var row = null;
    function onClickRow(index, rowData) {
        row = rowData;
        $('#user-panel-detail').panel("setTitle", rowData.name + "- 用户管理");
        _loadDetailData();
    }

    function _loadDetailData() {
        var userId = row ? row.id : "";
        _loadDetailDataByUserId(userId);
    }

    function _loadDetailDataByUserId(userId) {
        var url = $('#dg-user-roles').data("url") + "?id=" + userId;
        $('#dg-user-roles').datagrid({
            url : url
        });

        url = $('#user-roles-tree').data("url") + "?id=" + userId;
        $('#user-roles-tree').tree({
            url : url
        });

        $('#user-privileges-dg').datagrid({
            url : $('#user-privileges-dg').attr("url"),
            queryParams : {
                id : userId
            }
        });
    }

    function refreshUserRoles() {
        _loadDetailData();
    }

    function destroyUserRole() {
        var prow = $('#dg-user-roles').datagrid('getSelected');
        if (prow) {
            $.messager.confirm('Confirm', '确认从当前用户移除此角色？', function(r) {
                if (r) {
                    $.post($('#dg-user-roles').data("delete-url"), {
                        userId : row.id,
                        roleId : prow.id
                    }, function(result) {
                        if (result.result) {
                            refreshUserRoles();
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function toAddUserRole() {
        if (row) {
            $('#user-roles-dlg').dialog('open').dialog('setTitle', '添加角色');
        	$('#roles-select-dg').datagrid({
            url : $('#roles-select-dg').data("load-url")+'?f_EQ_isDeleted=0&f_EQ_isActived=1',
            required : true
        	});
            
        } else {
            alert("请先选中要管理的用户");
        }
    }

    function addUserRoles() {
        var prow = $('#roles-select-dg').datagrid("getSelected");
        if (prow) {
            $.messager.confirm('Confirm', '确认添加此角色？', function(r) {
                if (r) {
                    $.post($('#dg-user-roles').data("save-url"), {
                        userId : row.id,
                        roleId : prow.id
                    }, function(result) {
                        if (result.result) {
                            refreshUserRoles();
                            $('#user-roles-dlg').dialog('close');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        } else {
            alert("请选中要添加的角色");
        }
    }

    function treeLoadFilter(data, parent) {
        data.push({
            id : 0,
            text : "其他（未关联机构）"
        });
        return data;
    }

    function treeLoadFilterForSelect(data) {
        data.unshift({
            id : 0,
            text : "无"
        });
        return data;
    }

    return {
        init : init,
        formatPrivilegesType :formatPrivilegesType,
        newUser : newUser,
        saveUser : saveUser,
        editUser : editUser,
        destroyUser : destroyUser,
        refreshUsers : refreshUsers,
        formatStatus : formatStatus,
        formatToolBtns : formatToolBtns,
        onLoadSuccess : onLoadSuccess,
        changeUserStatus : changeUserStatus,
        onClickRow : onClickRow,
        destroyUserRole : destroyUserRole,
        refreshUserRoles : refreshUserRoles,
        toAddUserRole : toAddUserRole,
        addUserRoles : addUserRoles,
        onTreeSelect : onTreeSelect,
        treeLoadFilter : treeLoadFilter,
        treeLoadFilterForSelect : treeLoadFilterForSelect,
        changePassword : changePassword,
        doChangePassword : doChangePassword
    }
})();

/*******************************************************************************
 * 角色管理
 * 
 ******************************************************************************/
NB.ns("app.admin").roles = (function() {
    var url = $('#role_dg').data("load-url");
    function newRole() {
        $('#roles-dlg').dialog('open').dialog('setTitle', '新增角色');
        $('#roles-fm').form('clear');

        url = $('#role_dg').data("save-url");
    }

    function userName(field) {
        return function(val) {
            if ($.isPlainObject(val)) {
                return val[field];
            }
            return val;
        };
    }
    
    function editRole() {
        var row = $('#role_dg').datagrid('getSelected');
        if (row) {
            $('#roles-dlg').dialog('open').dialog('setTitle', '编辑角色');
            $('#roles-fm').form('load', row);

            //
            _initParentIdOnEdit(row);

            $('#roles-fm #readOnly').attr("disabled", true);
            url = $('#role_dg').data("update-url") + '?id=' + row.id;
        } else {
            $.messager.show({
                title : '提示信息',
                msg : '未选中要操作的行!',
                showType : 'show'
            });
        }
    }

    function _initParentIdOnEdit(row) {
        $('#parent_id').combogrid("setValue", row.parent);
    }

    function saveRole() {
        $('#roles-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.result) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#roles-dlg').dialog('close'); // close the dialog
                    $('#role_dg').datagrid('reload'); // reload the customer data

                    $.messager.show({
                        title : '提示',
                        msg : '操作完成!'
                    });
                }
            }
        });
    }

    function destroyRole() {
        var row = $('#role_dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('确认信息', '确认删除此角色?', function(r) {
                if (r) {
                    $.post($('#role_dg').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.result) {
                            $('#role_dg').datagrid('reload');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function refreshRoles() {
        $('#role_dg').datagrid('load', {});
        $('#roles_search_form')[0].reset();
    }

    function reloadDatagrid() {
        $('#role_dg').datagrid('load', {});
    }

    function formatParent(val, row) {
        var rolesJSON = $('#role_dg').attr("rolesJSON");
        rolesJSON = $.parseJSON(rolesJSON);
        return rolesJSON[val];
    }

    function onLoadSuccess(data) {
        $('.easyui-linkbutton').linkbutton();
    }

    function onSearch() {
        $('#role_dg').datagrid('load', {
            f_LIKE_name : $('#roles_search_form #f_name').val(),
            f_EQ_realm : $('#roles_search_form #f_realm').val()
        });
    }

    var row = null;
    function onClickRow(index, rowData) {
        row = rowData;
        $('#role-panel-detail').panel("setTitle", rowData.name + "- 角色管理");
        _loadPrivileges();
    }

    function _loadPrivileges() {
        if (row) {
            var url = ($('#dg-privileges').data("url") + "?id=" + row.id);
            $('#dg-privileges').datagrid({
                url : url
            });

            url = ($('#role-privileges-tree').data("url") + "?id=" + row.id);
            $('#role-privileges-tree').tree({
                url : url
            });
        }
    }

    function init() {
    	 $('#role_dg').datagrid({
         	url: url+'?f_EQ_isDeleted=0',
             required : true
         });
    }

    function refreshRolePrivileges() {
        _loadPrivileges();
    }

    function destroyRolePrivilege() {
        var prow = $('#dg-privileges').datagrid('getSelected');
        if (prow) {
            $.messager.confirm('Confirm', '确认从当前角色移除此权限？', function(r) {
                if (r) {
                    $.post($('#dg-privileges').data("delete-url"), {
                        roleId : row.id,
                        privilegeId : prow.id
                    }, function(result) {
                        if (result.result) {
                            refreshRolePrivileges();
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function toAddRolePrivilege() {
        if (row) {
            $('#role-privileges-dlg').dialog('open').dialog('setTitle', '添加权限');
            $('#privileges-select-dg').datagrid({
                url : $('#privileges-select-dg').data("load-url")+'?f_EQ_isDeleted=0&f_EQ_isActived=1',
                required : true
            	});
            $('#privileges-select-dg').datagrid("unselectAll");
        } else {
            $.messager.show({
                title : '提示信息',
                msg : '未选中要操作的角色!',
                showType : 'show'
            });
        }
    }

    function addRolePrivileges() {
        var prows = $('#privileges-select-dg').datagrid("getSelections");
        if (prows && prows.length > 0) {
            var ps = _.map(prows, function(r) {
                return "<li>" + r.name + "</li>";
            }).join("");

            $.messager.confirm('确认', "确认添加如下权限？ <ol>" + ps + "</ol>", function(r) {
                if (r) {
                    var pids = _.map(prows, function(p) {
                        return p.id;
                    }).join(",");

                    $.post($('#dg-privileges').data("save-url"), {
                        roleId : row.id,
                        privilegeIds : pids
                    }, function(result) {
                        if (result.result) {
                            refreshRolePrivileges();
                            $('#role-privileges-dlg').dialog('close');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });

        } else {
            alert("请选中要添加的权限");
        }
    }
    
    function formatPrivilegsType(val) {
        var typesJSON = $('#role_dg').attr("typesJSON");
        typesJSON = $.parseJSON(typesJSON);
        return typesJSON[val];
    }

    return {
    	formatPrivilegsType : formatPrivilegsType,
    	userName : userName,
        newRole : newRole,
        saveRole : saveRole,
        editRole : editRole,
        destroyRole : destroyRole,
        refreshRoles : refreshRoles,
        formatParent : formatParent,
        onLoadSuccess : onLoadSuccess,
        onSearch : onSearch,
        onClickRow : onClickRow,
        destroyRolePrivilege : destroyRolePrivilege,
        refreshRolePrivileges : refreshRolePrivileges,
        toAddRolePrivilege : toAddRolePrivilege,
        addRolePrivileges : addRolePrivileges,
        init : init
    }
})();

/*******************************************************************************
 * 权限管理
 * 
 ******************************************************************************/
NB.ns("app.admin").privileges = (function() {
    var url = $('#privilege_dg').data("load-url");
    function newPrivilege() {
        $('#privileges-dlg').dialog('open').dialog('setTitle', '新增权限');
        $('#privileges-fm').form('clear');

        $('#privileges-fm #readOnly').attr("disabled", true);

        url = $('#privilege_dg').data("save-url");
    }

    function editPrivilege() {
        var row = $('#privilege_dg').datagrid('getSelected');
        if (row) {
            $('#privileges-dlg').dialog('open').dialog('setTitle', '编辑权限');

            $('#privileges-fm').form('load', row);

            $('#privileges-fm #readOnly').attr("disabled", true);
            url = $('#privilege_dg').data("update-url") + '?id=' + row.id;
        } else {
            $.messager.show({
                title : '提示信息',
                msg : '未选中要操作的行!',
                showType : 'show'
            });
        }
    }

    function savePrivilege() {
        $('#privileges-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.result) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#privileges-dlg').dialog('close');
                    $('#privilege_dg').datagrid('reload');

                    $.messager.show({
                        title : '提示',
                        msg : '操作完成!'
                    });
                }
            }
        });
    }

    function destroyPrivilege() {
        var row = $('#privilege_dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此权限?', function(r) {
                if (r) {
                    $.post($('#privilege_dg').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.result) {
                            $('#privilege_dg').datagrid('reload');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        } else {
            $.messager.show({
                title : '提示信息',
                msg : '未选中要操作的行!',
                showType : 'show'
            });
        }
    }

    function refreshPrivileges() {
        $('#privilege_dg').datagrid('load', {});
        $('#privielges_search_form')[0].reset();
        $('#f_type').combobox("clear");
    }

    function reloadDatagrid() {
        $('#privilege_dg').datagrid('load', {});
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
        $('#privilege_dg').datagrid('load', {
            f_LIKE_name : $('#privielges_search_form #f_key').val(),
            f_EQ_type : $('#privielges_search_form #f_type').combo('getValue')
        });
    }

    function formatType(val) {
        var typesJSON = $('#privilege_dg').attr("typesJSON");
        typesJSON = $.parseJSON(typesJSON);
        return typesJSON[val];
    }

    function init() {

       
        $.getJSON($("#privileges-dlg #method").data("url"), function(result) {
            var s = _.map(result, function(it) {
                return it.text + ":" + it.value;
            }).join(",");
            $('#methodTip').html(s);
        });
        $('#privilege_dg').datagrid({
           	url: url+'?f_EQ_isDeleted=0',
               required : true
           });
    }

    return {
        newPrivilege : newPrivilege,
        savePrivilege : savePrivilege,
        editPrivilege : editPrivilege,
        destroyPrivilege : destroyPrivilege,
        refreshPrivileges : refreshPrivileges,
        formatToolBtns : formatToolBtns,
        formatReadOnly : formatReadOnly,
        formatType : formatType,
        onLoadSuccess : onLoadSuccess,
        onSearch : onSearch,
        init : init
    }
})();

/*******************************************************************************
 * 组织机构管理
 * 
 ******************************************************************************/
NB.ns("app.admin").organizations = (function() {
    var url = $('#organization_dg').data("load-url");
    function newOrganization() {
    	var row = $('#organization_dg').datagrid('getSelected');
    	if(row && row.isActived == 0){
    		$.messager.alert('出错了', '不能给无效机构增加子机构', 'warning');
    		return;
    	}
        $('#organizations-dlg').dialog('open').dialog('setTitle', '新增机构');
        $('#organizations-fm').form('clear');
        $('#org_parent_id').combotree(
        		{
        			url: $('#org_parent_id').data("list-url"),
        			required: true
        		});

        _initParentIdOnNew();
        url = $('#organization_dg').data("save-url");
    }

    function editOrganization() {
        var row = $('#organization_dg').datagrid('getSelected');
        if (row) {
            $('#organizations-dlg').dialog('open').dialog('setTitle', '编辑机构');
            $('#organizations-fm').form('load', row);

            $('#org_parent_id').combotree(
            		{
            			url: $('#org_parent_id').data("load-url")+'?f_EQ_isDeleted=0&f_EQ_isActived=1&id=' + row.id,
            			required: true
            		});
            
            _initParentIdOnEdit(row);

            $('#organizations-fm #readOnly').attr("disabled", true);
            url = $('#organization_dg').data("update-url") + '?id=' + row.id;
        }
    }

    function _initParentIdOnNew() {
        var row = $('#organization_dg').datagrid('getSelected');
        if (row) {
            $('#org_parent_id').combotree("setValue", row.id);
        }else{
        	$('#org_parent_id').combotree("setValue", 0);
        }
    }

    function _initParentIdOnEdit(row) {
    	if(row.parent){
    		$('#org_parent_id').combotree("setValue", row.parent);
    	}else{
    		$('#org_parent_id').combotree("setValue", 0);
    	}
        
    }

    function saveOrganization() {
        $('#organizations-fm').form('submit', {
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
                    $('#organizations-dlg').dialog('close'); // close the
                    // dialog
                    $('#organization_dg').treegrid('reload');
                    _reloadParents();
                }
            }
        });
    }

    function _reloadParents() {
        $('#org_parent_id').combotree("reload");
    }

    function destroyOrganization() {
        var row = $('#organization_dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此机构?', function(r) {
                if (r) {
                    $.post($('#organization_dg').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.result) {
                            $('#organization_dg').treegrid('reload');
                            $('#organization_dg').treegrid('clearSelections');
                            _reloadParents();
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function refresh() {
        $('#organization_dg').treegrid('load', {});
        $('#organizations_search_form')[0].reset();
    }

    function formatParent(val, row) {
    	
        var orgsJSON = $('#organization_dg').attr("orgsJSON");
        console.log($('#organization_dg'));
        
        orgsJSON = $.parseJSON(orgsJSON);
        return orgsJSON[val];
    }

    function onLoadSuccess(data) {
        $('.easyui-linkbutton').linkbutton();
    }

    function onSearch() {
        $('#organization_dg').treegrid('load', {
            f_LIKE_name : $('#organizations_search_form #f_name').val()
        });
    }

    // Q: easyui combotree怎么在树中自定义属性 比如我要在id,text后面在加个type这个属性，怎么扩展？
    function combotreeLoadFilter(data) {
    	var row = $('#organization_dg').datagrid('getSelected');
        data.unshift({
            id : "0",
            text : "无"
        });
        return data;
    }

    function init() {
        $('#organization_dg').treegrid({
        	url: url+'?f_EQ_isDeleted=0',
            required : true
        });
    }
    return {
    	init : init,
        newOrganization : newOrganization,
        saveOrganization : saveOrganization,
        editOrganization : editOrganization,
        destroyOrganization : destroyOrganization,
        refresh : refresh,
        formatParent : formatParent,
        onLoadSuccess : onLoadSuccess,
        onSearch : onSearch,
        combotreeLoadFilter : combotreeLoadFilter
    }
})();

/*******************************************************************************
 * 导航菜单管理
 * 
 ******************************************************************************/
NB.ns("app.admin").menus = (function() {
    var url = $('#menus_dg').data("load-url");
    function newMenu() {
    	var row = $('#menus_dg').datagrid('getSelected');
    	if(row && row.isActived == 0){
    		$.messager.alert('出错了', '不能给无效菜单增加子菜单', 'warning');
    		return;
    	}
        $('#menus-dlg').dialog('open').dialog('setTitle', '新增菜单');
        $('#menus-fm').form('clear');
        $('#menus_parent_id').combotree(
        		{
        			url: $('#menus_parent_id').data("load-url")+'?f_EQ_isDeleted=0&f_EQ_isActived=1',
        			required: true
        		});
        
        _initParentIdOnNew();

        url = $('#menus_dg').data("save-url");
    }

    function editMenu() {
        var row = $('#menus_dg').datagrid('getSelected');
        if (row) {
            $('#menus-dlg').dialog('open').dialog('setTitle', '编辑菜单');
            $('#menus-fm').form('load', row);

            $('#menus_parent_id').combotree(
            		{
            			url: $('#menus_parent_id').data("list-url")+'?f_EQ_isDeleted=0&f_EQ_isActived=1&id=' + row.id,
            			required: true
            		});
            _initParentIdOnEdit(row);

            $('#menus-fm #readOnly').attr("disabled", true);
            url = $('#menus_dg').data("update-url") + '?id=' + row.id;
        }
    }

    function _initParentIdOnNew() {
        var row = $('#menus_dg').datagrid('getSelected');
        if (row) {
            $('#menus_parent_id').combotree("setValue", row.id);
        }else{
    		$('#menus_parent_id').combotree("setValue", 0);
    	}
    }

    function _initParentIdOnEdit(row) {
    	if(row.parent){
    		$('#menus_parent_id').combotree("setValue", row.parent);
    	}else{
    		$('#menus_parent_id').combotree("setValue", 0);
    	}
       
    }

    function saveMenu() {
        $('#menus-fm').form('submit', {
            url : url,
            onSubmit : function() {
                return $(this).form('validate');
            },
            success : function(result) {
                result = eval("(" + result + ")");
                if (!result.result) {
                    $.messager.alert('出错了', result.message, 'error');
                } else {
                    $('#menus-dlg').dialog('close'); // close the
                    // dialog
                    $('#menus_dg').treegrid('reload');
                    _reloadParents();
                }
            }
        });
    }

    function _reloadParents() {
        $('#menus_parent_id').combotree("reload");
    }

    function destroyMenu() {
        var row = $('#menus_dg').datagrid('getSelected');
        if (row) {
            $.messager.confirm('Confirm', '确认删除此菜单?', function(r) {
                if (r) {
                    $.post($('#menus_dg').data("delete-url"), {
                        id : row.id
                    }, function(result) {
                        if (result.result) {
                            $('#menus_dg').treegrid('reload');
                        } else {
                            $.messager.alert('出错了', result.message, 'error');
                        }
                    }, 'json');
                }
            });
        }
    }

    function refresh() {
        $('#menus_dg').treegrid('load', {});
        $('#menus_search_form')[0].reset();
    }

    function formatParent(val, row) {
        var orgsJSON = $('#menus_dg').attr("orgsJSON");
        orgsJSON = $.parseJSON(orgsJSON);
        return orgsJSON[val];
    }

    function onLoadSuccess(data) {
       
    }

    function onSearch() {
        $('#menus_dg').treegrid('load', {
            f_LIKE_name : $('#menus_search_form #f_name').val()
        });
    }

    // Q: easyui combotree怎么在树中自定义属性 比如我要在id,text后面在加个type这个属性，怎么扩展？
    function combotreeLoadFilter(data) {
        data.unshift({
            id : "0",
            text : "无"
        });
        return data;
    }
    function init() {
        $('#menus_dg').treegrid({
        	url: url+'?f_EQ_isDeleted=0',
            required : true
        });
    }
    return {
    	init : init,
        newMenu : newMenu,
        saveMenu : saveMenu,
        editMenu : editMenu,
        destroyMenu : destroyMenu,
        refresh : refresh,
        formatParent : formatParent,
        onLoadSuccess : onLoadSuccess,
        onSearch : onSearch,
        combotreeLoadFilter : combotreeLoadFilter
    }
})();


