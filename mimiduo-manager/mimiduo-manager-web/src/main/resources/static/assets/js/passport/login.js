NB.ns("app.passport").login = (function() {
    function init() {
        $(function() {
            $('button.close').click(function() {
                $('div.alert-warning').hide();
            });
            // 确认当前location指向登录url
            if (window.location.href.indexOf("/login") == -1) {
                window.location.href = $('#loginForm').attr("action");
            }
        });
    }

    return {
        init : init
    }
})();
