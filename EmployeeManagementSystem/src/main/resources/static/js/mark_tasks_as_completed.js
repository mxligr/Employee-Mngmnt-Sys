$(document).ready(function () {

    let markAsCompleted_btns=document.getElementsByClassName("markAsCompleted");
    for(let i=0; i<markAsCompleted_btns.length; i++) {
        markAsCompleted_btns[i].addEventListener("click", function () {
            let taskId = $(this).attr('id').split('-')[1];
            if (confirm("Mark this task as completed?")){
                location.href="http://localhost:8090/taskCompleted/" + taskId;
            }else{
                location.reload();
            }

        });
    }

});
