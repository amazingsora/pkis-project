$(function () {
    // 幫 #menu li 加上 hover 事件
    $('#skhcq_menu_ul li').hover(function () {
        // 先找到 li 中的子選單
        var _this = $(this),
            _subnav = _this.children('ul');
        // 變更目前母選項的背景顏色
        // 同時顯示子選單(如果有的話)

        _this.css('backgroundColor', 'green');
        _subnav.css('display', 'block');

    }, function () {
        // 變更目前母選項的背景顏色
        // 同時隱藏子選單(如果有的話)
        // 也可以把整句拆成上面的寫法
        $(this).css('backgroundColor', '').children('ul').css('display', 'none');
    });

    // 取消a的虛線框
    $('a').focus(function () {
        this.blur();
    });

    $('#skhcq_menu_ul #jobs_btn').hover(function () {
        $(this).css('backgroundColor', 'black');
    });


    $('#logout').on("click", function () {
        logOut();
    });


    $('#skhcq_todo_btn').click(function () {
        var el = $('#skhcq_header_left_todo');
        if (el.css('display') === 'block') {
            el.css('display', 'none');
        } else {
            el.css('display', 'block');
        }
    });

});

