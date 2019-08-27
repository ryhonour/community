function postComment() {
    var questionId = $("#question_id").val();
    var content = $("#content").val();
    if (!content || content.trim() == "") {
        
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(
            {
                "parentId": questionId,
                "content": content,
                "type": 1
            }
        ),
        success: function (response) {
            if (response.code == 200) {
                swal(response.message, response.message, "success", {
                    buttons: false,
                    timer: 1000,
                });
                $("#comment_div").hide();
                window.location.reload();
            } else {
                swal(response.message, response.message, "warning");
            }
        },
        dataType: "json"
    });
}
