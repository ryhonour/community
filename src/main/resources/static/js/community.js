/**
 * 展开二级评论
 */
function SecondaryComment(e) {
    var id = e.getAttribute("data-commentId");
    var commentId = $("#comment-" + id);
    if (commentId.hasClass("in")) {
        commentId.removeClass("in");
        e.classList.remove("menu-comment-icon-color");
    } else {
        builderSecondaryComment(id);
        commentId.addClass("in");
        e.classList.add("menu-comment-icon-color");
    }
}

/**
 * 构建二级评论列表
 */
function builderSecondaryComment(id) {
    var targetDiv = $("#two-comment-list-" + id);
    targetDiv.empty();
    $.getJSON("/comment/" + id, function (data) {
        $.each(data.result, function (index, result) {
            var comment = $('' +
                '<div class="media secondary-comment-list col-lg-12 col-md-12 col-sm-12 col-xs-12">\n' +
                '    <div class="media-left">\n' +
                '        <a href="#">\n' +
                '            <img class="comment-user-image img-rounded" src="' + result.user.avatarUrl + '">\n' +
                '        </a>\n' +
                '    </div>\n' +
                '    <div class="media-body">\n' +
                '        <small class="media-heading">\n' +
                '            <span>' + result.user.name + '</span>\n' +
                '        </small>\n' +
                '        <div>\n' +
                '            <strong><span>' + result.content + '</span></strong>\n' +
                '            <small class="pull-right">' + moment(result.gmtCreate).format('YYYY-MM-DD hh:mm') + '</small>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>'
            );
            comment.appendTo(targetDiv);
        });
    });
}


/**
 * 发表一级评论
 */
function commitComment1() {
    var parentId = $("#question_id").val();
    var content = $("#content").val();
    publicCommmitComment(parentId, content, 1);
}

/**
 * 发表二级评论
 */
function commitComment2(e) {
    var parentId = e.getAttribute("data-commentId");
    var content = $("#two-comment-" + parentId).val();
    publicCommmitComment(parentId, content, 2);
}

/**
 * 发表评论的公共方法
 */
function publicCommmitComment(parentId, content, type) {
    if (!$.trim(content)) {
        swal("回复内容不能为空", "回复内容不能为空...", "warning");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(
            {
                "parentId": parentId.trim(),
                "content": content,
                "type": type
            }
        ),
        success: function (response) {
            if (response.code == 200) {
                swal(response.message, response.message, "success", {
                    buttons: false,
                    timer: 1000
                });
                setTimeout(function () {
                    $("#comment_div").hide();
                    window.location.reload();
                }, 1000);
            } else {
                swal(response.message, response.message, "warning");
            }
        },
        dataType: "json"
    });
}

/**
 * 添加标签
 */
function selectTag(e) {
    //获取id为tags的输入框中的值
    var value = e.getAttribute("data-tag");
    var previous = $("#tags").val();

    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tags").val(previous + "," + value);
        } else {
            $("#tags").val(value);
        }
    }
}

/**
 * 点击标签输入框显示标签列表集
 */
$(function () {
    $("#tags").on('click', function () {
        $('#selectTags').show();
    });
    $("#tags").on('blur', function () {
        setTimeout('$("#selectTags-hot").hide()', 500);
    });
})

/**
 * 发布问题
 */
function publish() {
    var question_id = $("#question_id").val();
    var title = $("#title").val();
    var description = $("#description").val();
    var tags = $("#tags").val();
    if (!$.trim(title)) {
        swal("标题不能为空！", title, "warning");
        return;
    }
    if (!$.trim(description)) {
        swal("描述不能为空！", description, "warning");
        return;
    }
    if (!$.trim(tags)) {
        swal("标签不能为空！", tags, "warning");
        return;
    }
    var data = {
        "id": question_id,
        "title": title,
        "description": description,
        "tags": tags
    };

    $.ajax({
        type: "POST",
        url: "/publish",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(data),
        success: function (response) {
            if (response.code == 201) {
                swal(response.message, response.message, "success", {
                    buttons: false,
                    timer: 1000
                });
                //在执行代码前先等待1000毫秒
                setTimeout(function () {
                    window.location.href = "/";
                }, 1000);
            } else {
                swal(response.message, response.message, "warning");
            }
        },
        dataType: "json"
    });
}