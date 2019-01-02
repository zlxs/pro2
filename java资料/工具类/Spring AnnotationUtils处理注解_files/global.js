/**
 * 全局公用js
 */

// 显示详情
function show_post_detail (post_id) {
    post_detail = $('#post-item-'+post_id).find('.post-detail');
    if (post_detail.hasClass('hidden')) {
        $('#post-item-'+post_id).find('.close_btn').removeClass('hidden');
        $('#post-item-'+post_id).find('.play_btn').addClass('hidden');
        post_detail.removeClass('hidden');
    } else {
        $('#post-item-'+post_id).find('.close_btn').addClass('hidden');
        $('#post-item-'+post_id).find('.play_btn').removeClass('hidden');
        post_detail.addClass('hidden');
    }
    // 加载图片
    img_lazyload();
}

// 重新发送激活邮件
function resend_verify_mail (obj) {
    $(obj).addClass('disabled');
    $.get('/user/send_verify_email',function(html){
        html && alert(html);
        $(obj).removeClass('disabled');
    });
}

$(function() {

    // 更多推荐
    $("#more_recommend").click(function(){
        $('#more_recommend').html('加载中...');
        $.ajax({
            type: "GET",
            url: "/index/ajax_more_recommend",
            data: {'page': $('#more_recommend').data('next-page')},
            dataType: 'json',
            success: function(result) {
                // 浏览更多按钮设置
                $('#more_recommend').blur();
                $('#more_recommend').html('浏览更多');
                $('#more_recommend').data('next-page', result.next_page);
                // 判断如果更多内容为空，或下一页页码为0，则把浏览更多按钮隐藏，并显示更多推荐按钮
                if (!result.html || !result.next_page) {
                    $('.more_recommend_box').addClass('hidden');
                    $('.refresh_box').removeClass('hidden');
                };
                // 显示更多内容
                $('.more_recommend_box').before(result.html);
                // 加载图片
                img_lazyload();
            },
            error: function(){
                alert("加载失败");
            }
        });
    });

    /* 帖子点击统计 */
    $('.js-post-link').click(function() {
        post_id = $(this).data('id');
        timestamp = new Date().getTime();
        view_posts = parseInt(timestamp/1000)+'.'+post_id;
        if ($.cookie('view_posts')) {
            view_posts += ','+$.cookie('view_posts');
        }

        $.cookie('view_posts', view_posts, { expires: 7, path: '/' });

        // ajax调用点击回调接口
        $.get('/ajax/click_post', function(){});
    });
    /* /帖子点击统计 */
    
    /* 标签关注滑动效果 */
    $('.post-follow-topic.unfollow').mouseover(function(){
        tag = $(this).data('tag');
        // 设置关注标签选中状态
        $(this).parent().find('.tag-'+tag).addClass('hover-follow');
        $(this).addClass('hover');
    }).mouseout(function(){
        tag = $(this).data('tag');
        // 设置关注标签选中状态
        $(this).removeClass('hover');
        $(this).parent().find('.tag-'+tag).removeClass('hover-follow');
    });
    /* /标签关注滑动效果 */

    // 删除搜索框的__hash__字段
    $('.js-search-form input[name="__hash__"]').remove();

    /* 鼠标滑过显示下拉菜单 */
    $('.dropdown-hover').mouseover(function() {
        $(this).addClass('open');
    }).mouseout(function() {
        $(this).removeClass('open');
    });
    /* /鼠标滑过显示下拉菜单 */
    /* 点击下拉菜单不隐藏 给点击对象加上属性data-stopPropagation='true'即可 */
    $(".dropdown-menu").on("click", "[data-stopPropagation]", function(even) {
        even.stopPropagation();
    });
    /* /点击下拉菜单不隐藏 */

    /*禁用bootstrap全局过度效果*/
    $.support.transition = false;

    /*全局启用bootstrap tooltip*/
    $('[data-toggle="tooltip"]').tooltip();

    /*用户表单输入时删除错误提示*/
    $("body").delegate("form input","keydown",function(){
        $(this).next(".has-error .help-block").remove();
        $(this).parents(".form-group").removeClass("has-error");
    });

    /*验证码重新加载*/
    $("#reloadCaptcha").click(function(){
        var new_src = $(this).find("img").attr("src")+'&'+Math.random();
        $(this).find("img").attr("src",new_src);
    });

    $("#top-search-form span").click(function(){
        $("#top-search-form").submit();
    });

    /*消息提示框自动隐藏*/
    $(".alert-message").delay(5000).hide(0);


    /*激活邮件发送*/
    $(".send-email-token").click(function(){
        $.get('/email/sendToken',function(msg){
            if( msg === 'tooFast'){
                alert('发送太频繁，请一分钟后再试.');
            }
        });
        $(".send-email-tips").show();
    });


    /*加载更多分页*/
    $(document).on("click",".load-more",function(){
        var $btn = $(this).button('loading');
        var loading_btn = $(this).button('loading');
        var source_type = $(this).data('source_type');
        var source_id = $(this).data('source_id');
        var next_page_url = $(this).data('next_page_url');
        $.get(next_page_url,function(html){
            $("#comments-"+source_type+"-"+source_id+" .widget-comment-list").append(html);
            loading_btn.parent().remove();
        });
    });


    $(document).on("click",".comment-reply",function(){

        var message = $(this).data('message');
        var source_type = $(this).data('source_type');
        var source_id = $(this).data('source_id');
        var to_user_id = $(this).data('to_user_id');

        $("#comment-"+source_type+"-content-"+source_id).attr('placeholder',message);
        $("#"+source_type+"-comment-"+source_id+"-btn").data('to_user_id',to_user_id);
        return false;

    });


    $(".collapse-cancel").click(function(){
        var collapse_id = $(this).data("collapse_id");
        $("#"+collapse_id).collapse('hide');
        return false;
    });



    /*私信模块处理*/

    $('#sendTo_message_model').on('show.bs.modal', function (event) {

        var button = $(event.relatedTarget);
        var to_user_id = button.data('to_user_id');
        var to_user_name = button.data('to_user_name');
        var modal = $(this);
        modal.find('#to_user_id').val(to_user_id);
        modal.find('#to_user_name').text(to_user_name);
    });


    $("#sendTo_submit").click(function(){
        $.ajax({
            type: "POST",
            url: "/message/store",
            data: $('#sendTo_message_form').serialize(),
            success: function(msg){
                alert('消息发送成功');
                $("#sendTo_message_model").modal('hide');
            },
            error: function(){
                alert("发送失败！");
            }
        });
    });


    /*关注模块处理，关注话题，用户等*/
    $(".follow-btn").click(function(){
        if(!check_login())
        {
            return;
        }
        var follow_btn = $(this);
        var type = $(this).data('type');
        var id = $(this).data('id');
        var unfollow = $(this).data('unfollow');

        $.get('/follow/'+type+'/id/'+id,function(msg){
            follow_btn.removeClass('disabled');
            follow_btn.removeAttr('disabled');

            // 帖子列表关注按钮逻辑
            if (follow_btn.hasClass('post-follow-topic')) {
                tag = follow_btn.data('tag');
                if(msg =='followed'){
                    $('.tag-'+tag+' .fa').attr('class', 'fa fa-check-circle');
                    follow_btn.addClass('followed');
                    follow_btn.removeClass('unfollow');
                    // 设置关注标签选中状态
                    $('.tag-'+tag).addClass('followed');
                }else{
                    $('.tag-'+tag+' .fa').attr('class', 'fa fa-plus-circle');
                    follow_btn.removeClass('followed');
                    follow_btn.addClass('unfollow');
                    // 设置关注标签选中状态
                    follow_btn.removeClass('hover');
                    $('.tag-'+tag).removeClass('followed');
                    $('.tag-'+tag).removeClass('hover-follow');
                }
            }
            // 普通关注按钮逻辑
            else {
                if(msg =='followed'){
                    follow_btn.html('已关注');
                    follow_btn.addClass('active');
                }else{
                    follow_btn.html(unfollow);
                    follow_btn.removeClass('active');
                }
            }
        });

    });
    $(".follow-btn.big").mouseover(function() {
        $(this).hasClass('active') && $(this).html('取消关注');
    }).mouseout(function() {
        $(this).hasClass('active') && $(this).html('已关注');
    });
    /*/关注模块处理，关注话题，用户等*/

    /*投票模块处理 赞/踩贴子、评论等*/
    $(".vote-btn").click(function(){
        if(!check_login())
        {
            return;
        }
        var vote_btn = $(this);
        // 投票对象
        var obj = $(this).data('obj');
        var type = $(this).data('type');
        var id = $(this).data('id');
        // 原始投票数
        var og_vote = vote_btn.parent().find('.vote-count').data('vote');
        // 原始赞数
        var og_up_vote = vote_btn.parent().find('.up-count').data('vote');
        // 用户点赞数
        var user_up_vote = vote_btn.parent().find('.up-count').data('user-vote');
        // 原始踩数
        var og_down_vote = vote_btn.parent().find('.down-count').data('vote');
        // 用户踩数
        var user_down_vote = vote_btn.parent().find('.down-count').data('user-vote');
        // 用户投票数（在总投票或赞投票任意一项元素中设置即可）
        var user_vote = vote_btn.parent().find('.vote-count').data('user-vote');
        // 获取投票初始值
        up_default = vote_btn.parent().find('.up-count').data('default');
        down_default = vote_btn.parent().find('.down-count').data('default');
        up_default == undefined ? up_default = '' : '';
        down_default == undefined ? down_default = '' : '';

        $.get('/'+obj+'/vote/id/'+id+'/type/'+type, function(msg) {
            // 初始化所有投票按钮点击状态
            vote_btn.parent().find('.vote-btn').removeClass('active');
            // 初始化赞/踩票数
            up_count = og_up_vote-user_up_vote;
            up_count == 0 ? up_count = up_default : '';
            down_count = og_down_vote-user_down_vote;
            down_count == 0 ? down_count = down_default : '';
            // 投票
            if(msg == 'voted')
            {
                vote_btn.addClass('active');
                vote_btn.parent().removeClass('down-vote');
                vote_btn.parent().removeClass('up-vote');
                if (type == 1) {
                    vote_btn.parent().addClass('up-vote');
                } else {
                    vote_btn.parent().addClass('down-vote');
                }
                // 总投票数设置
                vote_count = og_vote*1-user_vote+type*1;
                vote_btn.parent().find('.vote-count').addClass('active');
                // 赞数统计
                if (type == 1) {
                    up_count = og_up_vote-user_up_vote+1;
                    up_count == 0 ? up_count = up_default : '';
                    vote_btn.parent().find('.up-count').addClass('active');
                }
                // 踩数统计
                else {
                    down_count = og_down_vote-user_down_vote+1;
                    down_count == 0 ? down_count = down_default : '';
                    vote_btn.parent().find('.down-count').addClass('active');
                }
            }
            // 撤销投票
            else
            {
                vote_btn.removeClass('active');
                // 总投票数设置
                vote_count = og_vote-user_vote;
                vote_btn.parent().find('.vote-count').removeClass('active');
                // 赞数统计
                if (type == 1) {
                    up_count = og_up_vote-user_up_vote;
                    up_count == 0 ? up_count = up_default : '';
                    vote_btn.parent().find('.up-count').removeClass('active');
                }
                // 踩数统计
                else {
                    down_count = og_down_vote-user_down_vote;
                    down_count == 0 ? down_count = down_default : '';
                    vote_btn.parent().find('.down-count').addClass('active');
                }
            }
            // 设置投票数据
            vote_btn.parent().find('.vote-count').html(vote_count);
            vote_btn.parent().find('.up-count').html(up_count);
            vote_btn.parent().find('.down-count').html(down_count);
        });

    });
    /*/投票模块处理 赞/踩贴子、评论等*/

    /*收藏帖子*/
    $(".collect-btn").click(function(){
        if(!check_login())
        {
            return;
        }
        var collect_btn = $(this);
        var id = $(this).data('id');

        $.get('/post/collect/id/'+id,function(msg){
            if(msg =='collected'){
                collect_btn.html('已收藏');
                collect_btn.addClass('active');
            }else{
                collect_btn.html('收藏');
                collect_btn.removeClass('active');
            }
        });

    });
    $(".collect-btn").mouseover(function() {
        $(this).hasClass('active') && $(this).html('取消收藏');
    }).mouseout(function() {
        $(this).hasClass('active') && $(this).html('已收藏');
    });
    /*/收藏帖子*/

    /*标签自动选择*/
    if( $("#select_tags").length > 0 ){
        $("#select_tags").select2({
            theme:'bootstrap',
            placeholder: "选择话题",
            ajax: {
                url: '/ajax/loadTags',
                dataType: 'json',
                delay: 250,
                data: function (params) {
                    return {
                        word: params.term
                    };
                },
                processResults: function (data) {
                    return {
                        results: data
                    };
                },
                cache: true
            },
            minimumInputLength:1,
            tags:true
        });
        $("#select_tags").change(function(){
            $("#tags").val($("#select_tags").val());
        });
    }

    /*fancybox处理*/
    $(".description .text-fmt img,.best-answer .text-fmt img,.widget-answers .text-fmt img,.widget-article .text-fmt img").each(function(){
        var image = $(this);
        image.wrap('<a data-fancybox="gallery" href="'+image.attr("src")+'"></a>');
    });

});


/**
 * 编辑器图片图片文件方式上传
 * @param file
 * @param editor
 * @param welEditable
 */
function upload_editor_image(file,editorId){
    data = new FormData();
    data.append("file", file);
    $.ajax({
        data: data,
        type: "POST",
        dataType : 'text',
        url: "/image/upload",
        cache: false,
        contentType: false,
        processData: false,
        success: function(url) {
            console.log(url)
            if(url == 'error'){
                alert('图片上传失败！');
                return false;
            }
            $('#'+editorId).summernote('insertImage', url, function ($image) {
                $image.css('width', $image.width() / 2);
                $image.addClass('img-responsive');
            });
        },
        error:function(){
            alert('图片上传失败，请压缩图片大小再进行上传 :)');
        }
    });
}


/*检查用户登录情况*/
function check_login(){
    if(!is_login){
        document.location = '/login';
        return false;
    }

    return true;
}



