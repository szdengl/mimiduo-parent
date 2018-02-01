NB.ns("app.business").charging = (function () {

    var url;

    function onDefault() {
        onSearch();
    }

    function onSearch() {
        var opts = $("#dg-day").datagrid("options");
        opts.url = "/admin/charging/data/dayList";
        $('#dg-day').datagrid('load', {
            operator: $('#f_operator').combobox('getValue')
        });
    }

    function onLoadSuccess() {
        $('#dg-day').datagrid("autoMergeCells", ["province"]);
    }

    function formatRate(val, row, index) {
        if (row.sendnums == 0) {
            return 0;
        }
        else {
            return (row.chargings / row.sendnums).toFixed(2);
        }


    }


    var row = null;

    function onClickRow(index, rowData) {
        debugger;
        row = rowData;
        var province = row ? row.province: "";
        var operator = $('#f_operator').combobox('getValue');

        // _loadDetailData();
        url = $('#select-dg').data("url") + "?operator=" + operator
            + "&province=" + province;
        url = encodeURI(url);
        $('#select-dg').datagrid({
            url: url
        });
        $('#dg-dlg').dialog('open').dialog('setTitle', '通道设置');
    }

    var editRow = undefined;

    function editProvince() {
        row = $("#select-dg").datagrid('getSelected');
        if (row != null) {
            if (editRow != undefined) {
                $("#select-dg").datagrid('endEdit', editRow);
            }
            if (editRow == undefined) {
                var index = $("#select-dg").datagrid('getRowIndex', row);
                $("#select-dg").datagrid('beginEdit', index);
                editRow = index;
                $("#select-dg").datagrid('unselectAll');
            }
        } else {

        }
    }

    function saveProvince() {
        var row = $('#select-dg').datagrid('getSelected');
        $("#select-dg").datagrid('endEdit', editRow);
        url = $('#select-dg').data("save-url");
        // 如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。

        // 使用JSON序列化datarow对象，发送到后台。
        var rows = $("#select-dg").datagrid('getChanges');

        if (rows[0] != undefined) {
            debugger;
            var channelId=rows[0].channelId;
            var province=rows[0].province;
            var orderId=rows[0].orderId;
            $.ajax({
                    url: url + "?channelId=" +  channelId+ "&province="+province+"&orderId="
                    + orderId,
                    method: "POST",
                    dataType: 'json',
                    success: function (result) {
                        if (result.result) {
                            alert(result.message);
                            $('#select-dg').datagrid('reload');
                            $('#dg-day').datagrid('reload');
                        } else {
                            alert("出错了" + result.message);
                        }
                    }
                });
        }

    }

    function onAfterEdit(rowIndex, rowData, changes) {
        editRow = undefined;
    }

    function onDblClickRow(rowIndex, rowData) {
        if (editRow != undefined) {
            $("#select-dg").datagrid('endEdit', editRow);
        }
        if (editRow == undefined) {
            $("#select-dg").datagrid('beginEdit', rowIndex);
            editRow = rowIndex;
        }
    }

    function onClickEdit(rowIndex, rowData) {
        if (editRow != undefined) {
            $("#select-dg").datagrid('endEdit', editRow);

        }
    }

    return {
        init: function () {
            // alert("hello");
        },
        onLoadSuccess: onLoadSuccess,
        onSearch: onSearch,
        onDefault: onDefault,
        onClickRow: onClickRow,
        onClickEdit: onClickEdit,
        editProvince: editProvince,
        saveProvince: saveProvince,
        onDblClickRow: onDblClickRow,
        onAfterEdit: onAfterEdit,
        formatRate: formatRate
    };

})();
