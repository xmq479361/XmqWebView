var qjs = {}
qjs.os = {}
qjs.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
qjs.os.isAndroid = !qjs.os.isIOS;
qjs.callbackname = function() {
    return "qjs_callback_"+(new Date()).getTime()+"_"+Math.floor(Math.random() * 10000);
};
qjs.callbacks = {};
qjs.addCallback = function(name, func, userdata) {
    delete qjs.callbacks[name];
    qjs.callbacks[name] = {callback: func, userdata: userdata};
}

qjs.callback = function(callbackname, params) {
    console.log("web:qjs.js callback: "+callbackname+", "+ params);
    let callbackobject = qjs.callbacks[callbackname];
    if (undefined != callbackobject) {
        if (undefined != callbackobject.userdata) {
            callbackobject.userdata.callbackData = params;
        }
        if (undefined != callbackobject.callback) {
            let result = callbackobject.callback(params, callbackobject.userdata);
            if (result == false) return;
            delete qjs.callbacks[params.callbackname];
        }
    }
}

qjs.jsCallNative = function(cmd, params) {
    if (qjs.os.isIOS) {
        let message = {};
        message.meta = {cmd: cmd}
        message.params = params;
        window.webview.post(message);
    } else if (qjs.os.isAndroid) {
        window.webview.post(cmd, JSON.stringify(params));
    }
}
qjs.jsCallNative = function(cmd, params, callback) {
    qjs.jsCallNative(cmd, params, callback, {});
}
qjs.jsCallNative = function(cmd, params, callback, userdata) {
    var callbackname = qjs.callbackname();
    qjs.addCallback(callbackname, callback, userdata);
    if (qjs.os.isIOS) {
        let message = {};
        message.meta = {
            cmd: cmd,
            callback: callbackname
        }
        message.params = params;
        window.webview.post(message);
    } else if (qjs.os.isAndroid) {
        params.callback = callbackname;
        console.log("Web: jsCallNative")
        window.webview.post(cmd, JSON.stringify(params));
    }
}

qjs.dispatchEvent = function(params) {
    if (!params) {
        params = {"name":"webviewLoadComplete"};
    }
    var evt = {};
    try {
        evt = new Event(para.name);
        evt.para = para.para;
    } catch(e) {
        evt = document.createEvent("HTMLEvents");
        evt.initEvent(para.name, false, false);
    }
    window.dispatchEvent(evt);
}
qjs.addEventListener = window.addEventListener;

qjs.testFun = function(){
    try{
        window.qjs.jsCallNative("verifySubmitSuccess",{});
    } catch (e) {
        console.log(e);
    }
};
qjs.stringify = function(obj){
    var type = typeof obj;
    if (type == "object"){
        return JSON.stringify(obj);
    }else {
        return obj;
    }
};
qjs.nativecallback = function(obj){
    if(qjs.os.isIOS){
        return qjs.stringify(obj.data);
    }else if(window.qjs.os.isAndroid){
        window.webview.post(obj.callback, qjs.stringify(obj));
    }
};


qjs.http = function(envcmd,options){
    var para = {
        url:options.url,
        type:options.type || "get",
        timeout:options.timeout || 60000, // 60 second
        data:options.data,
        contentType:options.contentType,
        responseType:options.responseType,
        headers:options.headers
    }
    var ud = {
        envcmd:envcmd,
        para:para,
        callbacks:{
            success:options.success,
            complete:options.complete,
            beforeSend:options.beforeSend,
            error:options.error
        }
    }
    ud.callbacks.beforeSend ? ud.callbacks.beforeSend() : ""
    qjs.jsCallNative(envcmd,para,function(para,ud){
        if (para.success){
            ud.callbacks.success ? ud.callbacks.success(para.data) : ""
        }else{
            ud.callbacks.error ? ud.callbacks.error(null,para.errorReason) : ""
        }
        ud.callbacks.complete ? ud.callbacks.complete() : ""
    },ud)
}

qjs.djApi = function(options){
    return qjs.http("djapi",options)
}


qjs.toast = function(msg) {
  qjs.jsCallNative("showToast",{message:msg})
}
qjs.alert = function(para) {
    var title = para.title || ""
    var content = para.content || ""
    qjs.jsCallNative("showDialog",{title:title,content:content,buttons:[{title:"知道了"}]},function(paras){})
}
window.qjs = qjs;