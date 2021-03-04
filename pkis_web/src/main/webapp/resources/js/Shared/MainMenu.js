var userName, apipara;

$(function () {
    userName = localStorage.getItem("userName");
    $("#skhcq_name").text(userName);
    getMainMenu(renderMainMenu, `${getApiUrl()}${path_API_Version}main-access`);
});

function getMainMenu(callback, url) {
    var obj = {
        "token": getToken(),
        "accessCode": "MENU",
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
                alert(response.message);
            }
        },
        error: function (xhr, status, errorThrown) {
            var err = "Status: " + status + " " + errorThrown;
            Spinner.hide();
            alert(err);
        }
    });
}

function renderMainMenu(obj) {
    console.log(obj.data[0].menu);

    var ul = $(`<ul class="site-menu scrollable-content" data-plugin="menu"></ul>`);
    jQuery.each(obj.data[0].menu, function (idx, val) {
        var li = $(`<li class="site-menu-item has-sub active"></li>`);
        if (!val.submenu) { //已經最後一層
            if (val.api && cluster === val.itemcode) li.addClass("open");
            li.append(`<a href="${val.api ? `..${val.api}` : `javascript:void(0)`}"><i class="site-menu-icon fa-dot-circle-o" aria-hidden="true"></i><span class="site-menu-title">${val.menuitem}</span><span class="site-menu-arrow"></span></a>`);
        } else {
            if (val.submenu.filter(function (val) { return cluster === val.itemcode }).length > 0) li.addClass("open");

            li.append(`<a href="javascript:void(0)"><i class="site-menu-icon fa-dot-circle-o" aria-hidden="true"></i><span class="site-menu-title">${val.menuitem}</span><span class="site-menu-arrow"></span></a>`);

            var ulSub = $(`<ul class="site-menu-sub"></ul>`);
            jQuery.each(val.submenu, function (idx, val) {
                ulSub.append(renderSubMenu(val, true));
            });

            li.append(ulSub);
        }

        ul.append(li);
    });

    $("#skhcq_menu").append(ul.html());
}

function renderSubMenu(objSub, check) {
    var api = "#";
    if (objSub.api) api = `..${objSub.api}`;
    if (check) apipara = `&cluster=${objSub.itemcode}`;

    var liSub = $(`<li class="${objSub.submenu ? "dropdown-submenu" : "site-menu-item"}"></li>`);
    if (objSub.api && cluster === objSub.itemcode) liSub.addClass("active is-shown");

    liSub.append(`<a href="${api}" id="${objSub.itemcode}"${objSub.submenu ? ` data-toggle="dropdown"` : ``}><span class="site-menu-title">${objSub.menuitem}</span></a>`);

    if (objSub.submenu) {
        var ulSub = $(`<ul class="dropdown-menu"></ul>`);
        jQuery.each(objSub.submenu, function (idx, val) {
            ulSub.append(renderSubMenu(val, false));
        });
        liSub.append(ulSub);
    }

    return liSub;
}
