/**
 * 管理端首页js.
 * 
 */
NB.ns("app.admin").index = (function() {

    var menuData = null; // 菜单数据
    var currTab = null; // 当前tab
    var openedList = "";

    function treeLoadFilter(data, parent) {
        menuData = data;
        return data;
    }

    function init() {
    	$('#tabs').tabs()
        var curWwwPath=window.document.location.href;  
        var pathName=window.document.location.pathname;  
        var pos=curWwwPath.indexOf(pathName);  
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
        $('#t-channels').tree({
            url : $('#t-channels').attr("url"),
            loadFilter : treeLoadFilter,
            // 选中节点事件之前的处理，如果返回false，将取消onSelect的处理
            onBeforeSelect : function(node) {
                if (currTab && currTab.tabChangeCallback) { // 当前tab存在监听逻辑？
                    return currTab.tabChangeCallback();
                }
            },
            // 选中节点事件处理
            onSelect : function(node) {
            	if(!currTab || currTab == null) {
            		currTab = $('#tabs').tabs("getTab", "欢迎使用");
            	}
                if (!node.url) {
                    return;
                }

                // 尝试关闭欢迎的tab.
                if (currTab&&currTab.panel('options').title=="欢迎使用") {
                    var tabTitle = currTab.panel('options').title;
                    $('#tabs').tabs('close', tabTitle);
                }
                if(openedList.indexOf(node.text) > -1){
//                	var needClosed = $('#tabs').tabs("getTab", node.text); 
//                	var tabTitle = needClosed.panel('options').title;
                  $('#tabs').tabs('close', node.text);
                }else{
                	if(""==openedList){
                		openedList = openedList + node.text;
                	}else{
                		openedList = openedList +","+node.text;
                	}
                	
                }
                // 添加新的tab.
                $('#tabs').tabs('add', {
                    title : node.text,
                    href : projectName + node.url,
                    closable : true,
                    iconCls : node.iconCls,
                    tools : [ {
                        iconCls : 'icon-mini-refresh',
                        handler : function(e) {
                            // TODO 刷新当前tab
                        }
                    } ]
                });  
                currTab = undefined;
//                currTab = $('#tabs').tabs("getTab", node.text);
            },
            onLoadSuccess : function(node, data) { // 成功加载数据之后
                // if (data.length) {
                // var id = data[0].children[0].id; // 触发选中第一个节点事件
                // var n = $(this).tree('find', id);
                // $(this).tree('select', n.target);
                // }
            }
        });

        _checkShouldChangePassword();
    }// end init method

    function _checkShouldChangePassword() {
        $.getJSON($('#jshold-index').data("chkpwd_url"), function(should) {
            if (should) {
                $.messager.confirm('提示', '您在使用系统初始化的密码进行登录。为了保障帐户的安全性，请立即修改您的登录密码', function(r) {
                    if (r) {
                        showTabByText('帐号安全');
                    }
                });
            }
        });
    }

    function showTabByText(text) {
        if (menuData) {
            findNodeByText(menuData, text);
            if (_findNode) {
                var node = $('#t-channels').tree("find", _findNode.id);
                $('#t-channels').tree("select", node.target);
            }
        }
    }

    // 遍历找到最深的节点
    var _findNode = null;
    function findNodeByText(menus, text) {
        for (var i = 0; i < menus.length; i++) {
            var m = menus[i];
            if (m.text == text) {
                _findNode = m;
            }
            findNodeByText(m.children, text);
        }
    }

    function setCurrentTabChangeCallback(callback) {
        if (currTab) {
            currTab.tabChangeCallback = callback;
        }
    }

    return {
        init : init,
        showTabByText : showTabByText,
        setCurrentTabChangeCallback : setCurrentTabChangeCallback
    }
})();
