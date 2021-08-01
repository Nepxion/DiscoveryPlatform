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
    admin.SESSION_STATUS = 'n-d-session-status';

    function getContextPath() {
        return $('#contextPath').val();
    }

    admin.beforeRequest = function (jqXHR) {
        admin.addTokenHeader(jqXHR);
    }

    admin.afterResponse = function (data, textStatus, jqXHR) {
        admin.dealAuthFailureStatus(jqXHR);
        admin.cacheToken(jqXHR);
    }

    admin.loading = function (action) {
        layer.load();
        if (action) {
            action();
        }
        layer.closeAll('loading');
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

    admin.warning = function (title, content, callback, cancel) {
        layer.open({
            title: title,
            content: content,
            icon: 0,
            btn: '确定',
            yes: function () {
                if (callback) callback();
            },
            cancel: function () {
                if (cancel) cancel();
            }
        });
    };

    admin.toJson = function (obj) {
        return JSON.parse(JSON.stringify(obj));
    };

    admin.quit = function () {
        admin.post(getContextPath() + '/do-quit', {}, function () {
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
        location.href = getContextPath() + '/'
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

    admin.dealAuthFailureStatus = function (jqXHR) {
        let status = jqXHR.getResponseHeader(admin.SESSION_STATUS);
        if (status) {
            admin.removeToken();
            if ('expired' === status) {
                admin.warning('系统提示', '由于长时间未操作，账号已自动退出，请重新登录！',
                    () => admin.quit(), () => admin.quit());
            } else {
                admin.warning('系统提示', '认证失败！',
                    () => admin.quit(), () => admin.quit());
            }
        }
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

    admin.cacheToken = function (jqXHR) {
        const accessToken = jqXHR.getResponseHeader(admin.ACCESS_TOKEN);
        if (accessToken) {
            admin.putCache(admin.ACCESS_TOKEN, accessToken);
        }
    }

    admin.addTokenHeader = function (jqXHR) {
        const accessToken = admin.getCache(admin.ACCESS_TOKEN);
        if (accessToken) {
            jqXHR.setRequestHeader(admin.ACCESS_TOKEN, accessToken);
        }
    }

    admin.removeToken = function () {
        admin.removeCache(admin.ACCESS_TOKEN);
    }

    admin.putCache = function (key, value) {
        return window.localStorage.setItem(key, value);
    }

    admin.getCache = function (key) {
        return window.localStorage.getItem(key);
    }

    admin.removeCache = function (key) {
        return window.localStorage.removeItem(key);
    }

    admin.getRoutes = function () {
        const set = new Set(), routeNameList = [];
        admin.postQuiet(getContextPath() + '/common/do-list-route-names', {}, function (result) {
            $.each(result.data, function (index, item) {
                const name = $.trim(item);
                if (!set.has(name)) {
                    set.add(name);
                    routeNameList.push($.trim(name));
                }
            });
        }, null, false);
        routeNameList.sort();
        return routeNameList;
    }

    admin.getGatewayName = function () {
        const set = new Set(), gatewayNameList = [];
        admin.postQuiet(getContextPath() + '/common/do-list-gateway-names', {}, function (result) {
            $.each(result.data, function (index, item) {
                const name = $.trim(item);
                if (!set.has(name)) {
                    set.add(name);
                    gatewayNameList.push($.trim(name));
                }
            });
        }, null, false);
        gatewayNameList.sort();
        return gatewayNameList;
    }

    admin.getServiceName = function () {
        const set = new Set(), serviceNameList = [];
        admin.postQuiet(getContextPath() + '/common/do-list-service-names', {}, function (result) {
            $.each(result.data, function (index, item) {
                const name = $.trim(item);
                if (!set.has(name)) {
                    set.add(name);
                    serviceNameList.push($.trim(name));
                }
            });
        }, null, false);
        serviceNameList.sort();
        return serviceNameList;
    }

    admin.getServiceMetadata = function (serviceName, metadataName) {
        const set = new Set(), serviceMetadataList = [];
        admin.post(getContextPath() + '/common/do-list-service-metadata', {'serviceName': serviceName}, function (result) {
            $.each(result.data, function (index, item) {
                const key = item[metadataName];
                const name = $.trim(key);
                if (!set.has(name)) {
                    set.add(name);
                    serviceMetadataList.push(name);
                }
            });
        }, null, false);
        serviceMetadataList.sort();
        return serviceMetadataList;
    }

    admin.getMetadataName = function (strategyType) {
        if (strategyType == 1) {
            return 'version';
        } else if (strategyType == 2) {
            return 'region';
        }
    }

    e("common", {});
});