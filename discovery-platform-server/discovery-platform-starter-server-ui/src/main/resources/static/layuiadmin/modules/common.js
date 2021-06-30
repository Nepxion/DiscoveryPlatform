layui.define(function (e) {
    const i = (layui.$, layui.layer, layui.laytpl, layui.setter, layui.view, layui.admin);
    const $ = layui.$, admin = layui.admin;
    i.events.logout = function () {
        admin.quit();
    };


    admin.SYSTEM_PROMPT = '系统提示';
    admin.OPT_SUCCESS = '操作成功';
    admin.OPT_FAILURE = '操作失败';
    admin.BUTTONS = ['确定', '取消'];
    admin.DEL_ERROR = '请先勾选要删除的项';
    admin.DEL_QUESTION = '确定要删除所选项吗?';
    admin.DEL_SUCCESS = '所选项已全部成功删除';
    admin.ACCESS_TOKEN = "n-d-access-token";

    admin.beforeRequest = function (jqXHR, settings) {
        admin.addTokenHeader(jqXHR);
    }

    admin.afterResponse = function (data, textStatus, jqXHR) {
        admin.cacheToken(jqXHR);
    }

    admin.postQuiet = function (url, data, success, error, async) {
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            async: async,
            type: 'POST',
            data: data,
            cache: false,
            beforeSend: function (xhr, settings) {
                admin.beforeRequest(xhr, settings);
            },
            complete: function (xhr) {
                admin.afterResponse(null, null, xhr);
                if (xhr.responseText.indexOf("<div class=\"layadmin-user-login-main\">") > -1) {
                    admin.toLogin();
                } else {
                    const result = xhr.responseJSON;
                    if (result && result.ok) {
                        if (success) success(result);
                    } else {
                        if (error) {
                            error(result);
                        } else {
                            admin.error('系统错误', result.error);
                        }
                    }
                }
            }
        });
    };

    admin.post = function (url, data, success, error, async) {
        layer.load();
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            async: async,
            type: 'POST',
            data: data,
            cache: false,
            beforeSend: function (xhr, settings) {
                admin.beforeRequest(xhr, settings);
            },
            complete: function (xhr) {
                admin.afterResponse(null, null, xhr);
                if (xhr.responseText.indexOf('<div class="layadmin-user-login-main">') > -1) {
                    admin.toLogin();
                } else {
                    const result = xhr.responseJSON;
                    if (result && result.ok) {
                        if (success) success(result);
                    } else {
                        if (error) {
                            error(result);
                        } else {
                            admin.error('系统错误', result.error);
                        }
                    }
                }
                layer.closeAll('loading');
            }
        });
    };

    admin.getQuiet = function (url, success, error, async, dataType) {
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            async: async,
            type: 'GET',
            dataType: dataType ? dataType : 'json',
            cache: false,
            beforeSend: function (xhr, settings) {
                admin.beforeRequest(xhr, settings);
            },
            complete: function (xhr) {
                admin.afterResponse(null, null, xhr);
                if (xhr.responseText.indexOf("<div class=\"layadmin-user-login-main\">") > -1) {
                    admin.toLogin();
                } else {
                    const result = xhr.responseJSON;
                    if (result && result.ok) {
                        if (success) success(result);
                    } else {
                        if (error) {
                            error(result);
                        } else {
                            admin.error('系统错误', result.error);
                        }
                    }
                }
            }
        });
    };

    admin.get = function (url, success, error, async, dataType) {
        layer.load();
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: url,
            async: async,
            type: 'GET',
            dataType: dataType ? dataType : 'json',
            cache: false,
            beforeSend: function (xhr, settings) {
                admin.beforeRequest(xhr, settings);
            },
            complete: function (xhr) {
                admin.afterResponse(null, null, xhr);
                if (xhr.responseText.indexOf('<div class="layadmin-user-login-main">') > -1) {
                    admin.toLogin();
                } else {
                    const result = xhr.responseJSON;
                    if (result && result.ok) {
                        if (success) success(result);
                    } else {
                        if (error) {
                            error(result);
                        } else {
                            admin.error('系统错误', result.error);
                        }
                    }
                }
                layer.closeAll('loading');
            }
        });
    };

    admin.success = function (title, content, callback) {
        layer.open({
            title: title,
            content: content,
            icon: 1,
            end: function () {
                if (callback) callback();
            }
        });
    };

    admin.error = function (title, content, callback) {
        layer.open({
            title: title,
            content: content,
            icon: 2,
            end: function () {
                if (callback) callback();
            }
        });
    };

    admin.toJson = function (obj) {
        return JSON.parse(JSON.stringify(obj));
    };

    admin.quit = function () {
        admin.post('do-quit', {}, function () {
            window.localStorage.removeItem(admin.ACCESS_TOKEN);
            admin.toLogin();
        });
    };

    admin.initPage = function () {
        if (window.top !== window.self) {
            top.location.href = location.href;
        }
    };

    admin.toLogin = function () {
        admin.initPage();
        location.href = '/'
    }

    admin.getCheckedData = function (table, obj, field) {
        const checkStatus = table.checkStatus(obj.config.id);
        const data = checkStatus.data;
        const result = [];
        if (data.length > 0) {
            for (let j = 0, len = data.length; j < len; j++) {
                result.push(data[j][field]);
            }
        }
        return result;
    }

    admin.closeDelete = function (table, obj, index) {
        table.reload(obj.config.id, {
            done: function (result, page) {
                if (table.cache.grid.length < 1 && page > 1) {
                    table.reload(obj.config.id, {
                        page: {curr: 1}
                    });
                }
                layer.close(index);
            }
        });
    }

    admin.cacheToken = function(jqXHR) {
        const accessToken = jqXHR.getResponseHeader(admin.ACCESS_TOKEN);
        if (accessToken) {
            window.localStorage.setItem(admin.ACCESS_TOKEN, accessToken);
        }
    }

    admin.addTokenHeader = function(jqXHR) {
        const accessToken = window.localStorage.getItem(admin.ACCESS_TOKEN);
        if (accessToken) {
            jqXHR.setRequestHeader(admin.ACCESS_TOKEN, accessToken);
        }
    }

    e("common", {});

});