/**
 * manage.js
 */
NB.ns("app").manage = (function() {
    // @see: https://github.com/jpillora/jquery.rest
    var client = new $.RestClient('/manage/');
    var apis = [ "autoconfig", "beans", "configprops", "dump", "env", "health", "info", "metrics", "mappings", "trace" ];
    _.each(apis, function(api) {
        client.add(api);
    });
    client.add('_polling_server_time');
    client.add('_polling_logs');

    function vAsObject(v) {
        return _.isPlainObject(v) ? v : {
            values : v
        };
    }

    function loadLogs() {
        client._polling_logs.read({
            n : $("#log-lines").val()
        }).done(function(data) {
            $('#log-show').html(data.data);
        });
    }

    function updateTime() {
        client._polling_server_time.read().done(function(data) {
            $('#server-time').html(data.data);
        });
    }

    function remoteJsonView(target, api) {
        api.read().done(function(data) {
            try {
                $(target).JSONView(vAsObject(data), {
                    collapsed : false
                });
            } catch (e) {
                alert(e);
            }
        });
    }

    function init() {
        $("#btn-fresh-logs").click(loadLogs);

        loadLogs();

        _.each(apis, function(p) {
            remoteJsonView("#" + p, client[p]);
        });

        setInterval(updateTime, 1000);
    }

    return {
        init : init
    };
})();
