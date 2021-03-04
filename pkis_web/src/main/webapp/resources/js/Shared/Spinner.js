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
                    $contents.find("div[class='navbar-header']").find("button").remove();
                    $contents.find("div[class*='navbar-brand ']").css("position", "relative");
                    $contents.find("div[class='site-menubar']").remove();

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

BoxViewReport = (function () {
    var boxBg = $('#box_viewReport');
    return {
        show: function (status, url) {
            boxBg.css({ 'display': 'block', "opacity": "0.7", "width": "800", "height": "500" });
            boxBg.fadeIn(function () {
                $("#iframe_viewReport").attr("src", url);
                $("#iframe_viewReport").css({ "width": "100%", "height": "100%" });
                $("#iframe_viewReport").on('load', function () {
                    var $iframe = $(this);
                    var $contents = $iframe.contents();

                    //舊CRM
                    if (url.indexOf('CRM/WorkForms/Case/CaseDetail_Main.aspx') > -1) {
                        // 將系統頁帶入的menu(左選單)、header(標頭)隱藏
                    }
                    else {
                        // 將系統頁帶入的menu(左選單)、header(標頭)隱藏
                        //$contents.find("nav[class*='site-navbar']").remove();
                        $contents.find("div[class='navbar-header']").find("button").remove();
                        $contents.find("div[class*='navbar-brand ']").css("position", "relative");
                        $contents.find("div[class='site-menubar']").remove();
                    }

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

$(function () {
    $('body').click(function () {
        $('#box_form_detail').fadeOut();
    });
    $('body').dblclick(function () {
        if ($(window.parent.document).find("#box_viewReport").length > 0) BoxViewReport.hide();
    });
});