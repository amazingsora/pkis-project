var userName, apipara, cluster;

$(function () {
    userName = localStorage.getItem("userName");
    $("#skhcq_name").text(userName);

    cluster = $('#cluster').val();
    getSmartMenu(renderSmartMenu, `${getApiUrl()}${path_API_Version}main-access`);
});

function getSmartMenu(callback, url) {
    if (localStorage.getItem("menuPara")) {
        callback(JSON.parse(localStorage.getItem("menuPara")));
    } else {
        var obj = {
            "token": getToken(),
            "userId": getUserId(),
            "accessCode": "MENU"
        };
        obj = JSON.stringify(obj);

        $.ajax({
            url: url,
            data: obj,
            contentType: "application/json",
            type: "POST",
            dataType: 'json',
            success: function (response) {
                if (response.rtnCode === 0) {
                    var jObject = JSON.parse(response.jsonData.data);
                    localStorage.setItem("menuPara", response.jsonData.data);
                    callback(jObject);
                } else {
                    Spinner.hide();
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                }
            },
            error: function (xhr, status, errorThrown) {
                Spinner.hide();
                //FixSysMsg.danger(`Status: ${status} ${errorThrown}`);
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            }
        });
    }
}

function renderSmartMenu(obj) {
    console.log(obj.data[0].menu);

    var ul = $(`<ul id="main-menu" class="sm sm-vertical sm-blue" style="display: inline-block; width:100%"></ul>`);
    jQuery.each(obj.data[0].menu, function (i, val) {
        var li = $(`<li></li>`), _class = "";
        if (val.submenu) {
            //次選單
            //加入判斷無cluster時以此條件(val.api && location.href.indexOf(val.api) > -1)為主
            if (val.submenu.filter(function (val) { return (cluster === val.itemcode || val.api && location.href.indexOf(val.api) > -1) }).length > 0) _class = `highlighted`;
            li.append(`<a href="javascript:void(0)"${_class !== "" ? ` class="${_class}"` : ``}>${val.menuitem}</a>`);

            var tmp = $(`<ul></ul>`);
            jQuery.each(val.submenu, function (j, valSub) {
                tmp.append(renderSubSmartMenu(valSub));
            });

            li.append(tmp);
        }
        else {
            if (val.api && (cluster === val.itemcode || location.href.indexOf(val.api) > -1)) _class = `highlighted`;
            li.append(`<a href="${val.api ? `..${val.api}` : `javascript:void(0)`}"${_class !== "" ? ` class="${_class}"` : ``}>${val.menuitem}</a>`);
        }

        ul.append(li);
    });

    $('#skhcq_smartmenu').append(ul);

    $('#main-menu').smartmenus({
        subMenusSubOffsetX: 1,
        subMenusSubOffsetY: -8
    });

    $('a.nav-link').click(function () {
        if ($("#main-menu").attr("style").indexOf("float") > -1) {
            $("#main-menu").removeAttr("style").css({
                "display": "inline-block",
                "width": "100%"
            });

            $(".sm-blue > li > a").removeAttr("style");
            $("#main-menu li a > span.sub-arrow").show();
        } else {
            $("#main-menu").removeAttr("style").css({
                "float": "left",
                "width": "130px"
            });

            $(".sm-blue > li > a").css("padding", "0 10px");
            $("#main-menu li a > span.sub-arrow").hide();
        }
    });
}

function renderSubSmartMenu(obj) {
    var li = $(`<li></li>`);
    li.append(`<a href="${obj.submenu || obj.api === "" ? `javascript:void(0)` : `..${obj.api}`}">${obj.menuitem}</a>`);

    if (obj.submenu) {
        var ul = $(`<ul></ul>`);
        jQuery.each(obj.submenu, function (k, val) {
            //console.log(obj.itemcode + ":" + obj.menuitem + ":" + val.menuitem + ":" + val.api);
            if ((obj.spec === "REC" && val.menuitem === "查詢") ||
                (obj.spec === "PAT" && val.menuitem === "新增") ||
                (obj.spec === "SCREENING" && val.menuitem === "新增") ||
                (obj.spec === "DOC" && val.menuitem === "新增"))
                return;

            ul.append(renderSubSmartMenu(val));
        });

        li.append(ul);
    }

    return li;
}