var userName, apipara;

$(function () {
    userName = getUserName();
    $("#skhcq_name").text(userName);
    module = $('#module').val();
    cluster = $('#cluster').val();
    ajcctype = $('#ajcctype').val();
    masterSpec = $('#spec').val();

    getFlatAccordionMenu(renderFlatAccordionMenu);
});

function getFlatAccordionMenu(callback) {
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
            url: getAPIURL("MENU/Get"),
            type: "POST",
            contentType: "application/json",
            data: obj,
            success: function (response) {
                if (response.rtnCode === 0) {
                    var jObject = JSON.parse(response.jsonData.data);
                    localStorage.setItem("menuPara", response.jsonData.data);
                    callback(jObject);
                } else {
                    FixSysMsg.danger(`${response.rtnCode}-${response.message}`);
                }
            },
            error: function (xhr, status, errorThrown) {
                console.log(`Status:${status} ${errorThrown}`);
                FixSysMsg.danger("Ajax發生錯誤!");
            },
            complete: function (xhr, status) {
                Spinner.hide();
            }
        });
    }
}

function getMenuTitle(obj) {
    //var arrObj = obj.split("");
    //for (var i = 5; i < arrObj.length; i += 6) arrObj[i] += "<br />";
    //return arrObj.join("");
    return obj;
}

function renderFlatAccordionMenu(obj) {
    console.log(obj.data[0].menu);

    var ul = $(`<ul></ul>`), _title = "";
    $.each(obj.data[0].menu, function (i, val) {
        var li = $(`<li></li>`);
        _title = getMenuTitle(val.menuitem);

        if (val.submenu) {
            //次選單
            li.addClass("has-sub");
            li.append(`<a href="javascript:void(0)">${_title}</a>`);

            var tmp = $(`<ul></ul>`);
            val.submenu.forEach(function (item) {
                if (item.module === module && `|${cluster}|${masterSpec}`.indexOf(`|${item.itemcode}|`) > -1) li.addClass("open");
                tmp.append(renderSubFlatAccordionMenu(item));
            });

            li.append(tmp);
        }
        else {
            li.append(`<a href="${val.api ? `../..${val.api}` : `javascript:void(0)`}">${_title}</a>`);
        }

        ul.append(li);
    });

    $('.FlatAccordionMenu').append(ul);

    $("a.nav-link").click(function () {
        if ($(window).width() < 768 || $(window).width() > 1199) {
            $(".navbar-header, .navbar-brand, .navbar-container, .site-menubar, .page").toggleClass("hamburger");
            $("ul.nav.navbar-toolbar, li.nav-item.dropdown").toggleClass("normal");
            $(`.css-menubar [data-toggle="menubar"] .hamburger-arrow-left`).toggleClass("hided");
        }
    });

    $(".FlatAccordionMenu li.has-sub > ul").css({ "display": "none" });
    $(".FlatAccordionMenu li.active").addClass("open").children("ul").show();
    $(".FlatAccordionMenu li.has-sub > a").on("click", function () {
        $(this).removeAttr("href");
        var elm = $(this).parent("li");
        if (elm.hasClass("open")) {
            elm.removeClass("open");
            elm.find("li").removeClass("open");
            elm.find("ul").slideUp(200);
        }
        else {
            elm.addClass("open");
            elm.children("ul").slideDown(200);
            elm.siblings("li").children("ul").slideUp(200);
            elm.siblings("li").removeClass("open");
            elm.siblings("li").find("li").removeClass("open");
            elm.siblings("li").find("ul").slideUp(200);
        }
    });

    if ($(window).width() <= 767) $(".site-menubar").addClass("site-menubar-hide");

    $(`button[data-toggle="menubar"]`).click(function () {
        if ($(window).width() <= 767) {
            $(".site-menubar").toggleClass("site-menubar-hide");
        }
    });

    //實作依參數來展開MENU
    //console.log(`menuObj: ${ul.children()[0].outerHTML}`);
    OpenMenu(ul.children());

    MenuResize();
}

function renderSubFlatAccordionMenu(obj) {
    var li = $(`<li></li>`), _title = getMenuTitle(obj.menuitem);
    li.append(`<a href="${obj.submenu || obj.api === "" ? `javascript:void(0)` : `../..${obj.api}`}">${_title}</a>`);

    if (obj.submenu) {
        //menuitem -> submenu.itemcode
        //診療計畫書(NEW|QRY|TODO)
        //個案審查-選案(REC)
        if ("|NEW|QRY|TODO|REC|TMY|".indexOf(`|${obj.itemcode}|`) > -1 ||
            obj.module === "CM" && obj.cluster === "RPT" && obj.itemcode.match(/CEM|COL|MST/)) {
            if (obj.api === "") {
                obj.submenu.forEach(function (item) {
                    //console.log(`${obj.itemcode}.${obj.spec}.${obj.menuitem}.${val.menuitem}=>${val.api}`);
                    if (
                        //個案審查[選案&派案]
                        obj.spec.match(/(DIVIDE|ASSIGN)DOCTOR/) && item.menuitem === "新增" ||
                        //個管[收案模組]、[多專科專案團隊會議]、[腫瘤心理個案]、[癌症診療計畫]
                        obj.shortdisp === "NEW" && item.menuitem === "查詢" ||
                        obj.shortdisp.match(/QRY|TODO/) && item.menuitem === "新增" ||
                        //個管[報表模組]
                        obj.spec.match(/AUDITLIST|CANCERYEAR|AGESTATISTICS|ANNUALSTATISTICS/) && item.menuitem === "查詢"
                    )
                        return;

                    li.find("a").attr("href", item.api === "" ? "javascript:void(0)" : `../..${item.api}`);
                });
            }
            else
                li.find("a").attr("href", `../..${obj.api}`);
        }
        else {
            var ul = $(`<ul></ul>`);

            obj.submenu.forEach(function (item) {
                ul.append(renderSubFlatAccordionMenu(item));
            });

            li.addClass("has-sub");
            if (obj.module === module && (obj.itemcode === cluster || obj.cluster === cluster && obj.itemcode === ajcctype && obj.spec === masterSpec))
                li.addClass("open");
            li.append(ul);
        }
    }

    return li;
}

function OpenMenu(obj) {
    $.each(obj, function (i, val) {
        $val = $(val);

        if ($val.hasClass("open")) {
            $val.children("ul").show();
            OpenMenu($val.children("ul").children());
        }
    });
}

/**
 * 選單縮窄
 */
function MenuResize() {
    var MENURESIZE;
    SetMainProcessPara("getLocalStorage", { "key": LocalStorage_Key_MENURESIZE });
    MENURESIZE = MainProcess();
    if (MENURESIZE === undefined) {
        //設定一開始為小漢堡
        if (!$(".nav-link > i").hasClass("hided")) $(".nav-link").click();
        MENURESIZE = $(".nav-link > i").hasClass("hided");

        SetMainProcessPara("setLocalStorage", { "key": LocalStorage_Key_MENURESIZE, "value": MENURESIZE });
        MainProcess();
    } else if ($.parseJSON(MENURESIZE) && !$(".nav-link > i").hasClass("hided"))
        $(".nav-link").click();
}