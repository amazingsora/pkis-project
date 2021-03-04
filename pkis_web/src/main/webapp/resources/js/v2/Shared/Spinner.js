Spinner = (function () {
    var $spinner = $('#circle_spinner');
    return {
        show: function () {
            $spinner.fadeIn();
        },
        hide: function () {
            //$spinner.hide();
            $spinner.fadeOut();
        },
        status: function () {
            return $spinner.is(":hidden");
        }
    };
})();


//【取病歷資訊】
BoxPatientInfo = (function () {
    var boxBg = $('#box_patient');
    return {
        show: function (status, url) {
            boxBg.css({ 'display': 'block', "opacity": "1", "width": "700", "height": "500" });
            boxBg.fadeIn(function () {
                $("#iframe_patient").attr("src", url);
                $("#iframe_patient").css({ "width": "100%", "height": "100%" });
                $("#iframe_patient").on('load', function () {
                    var $iframe = $(this);
                    var $contents = $iframe.contents();

                    // 將系統頁帶入的menu(左選單)、header(標頭)隱藏
                    //$contents.find("nav[class*='site-navbar']").remove();
                    /*
                    $contents.find("div[class='navbar-header']").find("button").remove();
                    $contents.find("div[class*='navbar-brand']").css("position", "relative");
                    $contents.find("div[class='site-menubar']").remove();
                    */
                    
                    $contents.find(".site-navbar").remove();
                    $contents.find(".site-menubar").remove();
                    $contents.find(".page-container-top").remove();
                    $contents.find(".page-container-slide").remove();
                    $contents.find(".site-body").css({ "margin-top": "-68px" });

                    // 執行 iframe 中定義的方法
                    //$contents.find("input:button:contains('關閉查詢窗格')").click(function () {
                });
            });
        },
        hide: function () {
            boxBg.fadeOut();
        }
    };
})();


//個案資料查詢
BoxCaseInfo = (function () {
    var boxBg = $('#box_case');
    return {
        show: function (status, url) {
            boxBg.css({ 'display': 'block', "opacity": "1", "width": "770", "height": "500" });
            boxBg.fadeIn(function () {
                $("#iframe_case").attr("src", url);
                $("#iframe_case").css({ "width": "100%", "height": "100%" });
                $("#iframe_case").on('load', function () {
                    var $iframe = $(this);
                    var $contents = $iframe.contents();

                    // 將系統頁帶入的menu(左選單)、header(標頭)隱藏
                    //$contents.find("div[class*='navbar-container']").remove();
                    //$contents.find("div[class='navbar-header']").find("button").remove();
                    //$contents.find("div[class*='navbar-brand ']").css("position", "relative");
                    //$contents.find("div[class*='site-menubar']").remove();

                    $contents.find(".site-navbar").remove();
                    $contents.find(".site-menubar").remove();
                    $contents.find(".page-container-top").remove();
                    $contents.find(".page-container-slide").remove();
                    $contents.find(".site-body").css({ "margin-top": "-68px" });
                });
            });
        },
        hide: function () {
            boxBg.fadeOut();
        }
    };
})();

BoxDialog = (function () {
    var boxBg = $('#box_dialog');
    var iframe = $("#iframe_dialog");
    return {
        show: function (title, url, parameters, initFunction, height) {
            boxBg.find("#box_title").text(title);
            boxBg.find("#modal-confirm").unbind('click');
            iframe.attr("src", url);
            iframe.css({ "width": "100%", "height": height + "px" });
            iframe.unbind('load');
            iframe.on('load', function () {
                var $iframe = $(this);
                var $contents = $iframe.contents();

                // 將系統頁帶入的menu(左選單)、header(標頭)隱藏
                //$contents.find("nav[class*='site-navbar']").remove();
                $contents.find(".site-navbar").remove();
                $contents.find(".site-menubar").remove();
                $contents.find(".page-container-top").remove();
                $contents.find(".page-container-slide").remove();
                //$contents.find(".site-body").css({ "margin-top": "-68px" });

                //執行客製Function 2019.04.01 -Ben
                if (typeof eval(`${initFunction}`) === "function") {
                    eval(`${initFunction}($contents,parameters,$(this));`);
                }
            });
            boxBg.fadeIn();
        },
        hide: function () {
            boxBg.fadeOut();
        }
    };
})();

//側邊選單按鈕
BoxViewReport = (function () {
    var boxBg = $('#box_viewReport');
    boxBg.on('dblclick', function () { BoxViewReport.hide(); });
    return {
        show: function (status, url) {
            console.log(url);
            boxBg.css({ "top": "0px", "left": "0px" });
            boxBg.fadeIn(function () {
                $("#iframe_viewReport").attr("src", url);
                $("#iframe_viewReport").css({ "padding-top": "35px" });
                $("#iframe_viewReport").on('load', function () {
                    var $iframe = $(this);
                    var $contents = $iframe.contents();
                    
                    //將系統頁帶入的menu(左選單)、header(標頭)隱藏
                    //$contents.find("nav[class*='site-navbar']").remove();
                    $contents.find("div[class='navbar-header']").find("button").remove();
                    $contents.find("div[class*='navbar-brand ']").css("position", "relative");
                    $contents.find("div[class='site-menubar']").remove();

                    //執行 iframe 中的 Click & Hide
                    if (getURLParameterByName("CLUSTER", url) === "TP") {
                        $contents.find("td#td_TagHome table tr td:nth-child(8) a").click();
                        $contents.find("td#td_TagHome").hide();
                    }

                    //執行 iframe 中定義的方法
                    //$contents.find("input:button:contains('關閉查詢窗格')").click(function () {
                    //關閉窗格
                    $contents.on('dblclick', function () { BoxViewReport.hide(); });
                });
            });
        },
        hide: function () {
            boxBg.fadeOut();
        }
    };
})();


//唯讀頁
BoxReadOnlyReport = (function () {
    var boxBg = $('#box_viewReadOnlyReport');
    boxBg.on('dblclick', function () {
        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_AjccUnReceivePlan, "isClearAll": "false" });
        MainProcess();
        BoxReadOnlyReport.hide();
    });
    return {
        show: function (status, url) {
            console.log(url);
            boxBg.css({ "top": "0px", "left": "0px" });
            boxBg.fadeIn(function () {
                $("#iframe_viewReadOnlyReport").attr("src", url);
                $("#iframe_viewReadOnlyReport").css({ "padding-top": "35px" });
                $("#iframe_viewReadOnlyReport").on('load', function () {
                    //關閉窗格
                    $(this).contents().on('dblclick', function () {
                        SetMainProcessPara("clearLocalStorage", { "key": LocalStorage_Key_AjccUnReceivePlan, "isClearAll": "false" });
                        MainProcess();
                        BoxReadOnlyReport.hide();
                    });
                });
            });
        },
        hide: function () {
            boxBg.fadeOut();
        }
    };
})();