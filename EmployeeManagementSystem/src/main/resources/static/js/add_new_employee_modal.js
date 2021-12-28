function disablePastDates() {

    var dtToday = new Date();

    var month = dtToday.getMonth() + 1;
    var day = dtToday.getDate();
    var year = dtToday.getFullYear();
    if(month < 10)
        month = '0' + month.toString();
    if(day < 10)
        day = '0' + day.toString();

    var maxDate = year + '-' + month + '-' + day;
    $('#dueDate').attr('min', maxDate);
}

    $(document).ready(function () {

    $(".modal-ctrl-btn").on("click", function (){
        let modal = $("#add_new_employee_modal");
        modal.css("display", "block");
    });

    $(".close").on("click", function (){
        let modal = $("#add_new_employee_modal");
        modal.css("display", "none");
    });

})

$(document).ready(function () {
    disablePastDates();
    let assignTask_btns=document.getElementsByClassName("modal-assignTask-ctrl-btn");
    let close_btns = document.getElementsByClassName('close');
    let employee_id;

    for(let i=0; i<assignTask_btns.length; i++){
        assignTask_btns[i].addEventListener("click",function (){
            employee_id = $(this).attr('id').split('-')[1];
            let modal = $('#assignTask_modal-' + employee_id);
            modal.css("display", "block");
        });
    }

    for (let i = 0; i < close_btns.length; i++) {
        close_btns[i].addEventListener('click', function () {
            let modal_to_close = $('#assignTask_modal-' + employee_id);
            modal_to_close.css("display", "none");
        });
    }

    $(".modal-assignTask-ctrl-btn").on("click", function (){
        let modal = $("#assignTask_modal");
        modal.css("display", "block");
    });
})
