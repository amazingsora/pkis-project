
function setCookie(cookieName, cookieValue, exdays) {
    if (document.cookie.indexOf(cookieName) >= 0) {
        var expD = new Date();
        expD.setTime(expD.getTime() + (-1 * 24 * 60 * 60 * 1000));
        var uexpires = "expires=" + expD.toUTCString();
        document.cookie = cookieName + "=" + cookieValue + "; " + uexpires;
    }
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + "; " + expires;
}

function getCookie(cookieName) {
    var name = cookieName + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1);
        if (c.indexOf(name) === 0) return c.substring(name.length, c.length);
    }
    return "";
}


function delCookie(cookieName){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = GetCookie(cookieName);
    documents.cookie = cookieName + "=" + cval + ";  expires=" + exp.toGMTString();
}