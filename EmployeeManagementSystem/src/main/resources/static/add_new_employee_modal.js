$(document).ready(function () {
    console.log("intra");
    $(".modal-ctrl-btn").on("click", function (){
        let modal = $("#add_new_employee_modal");
        modal.css("display", "block");
    });

    $(".close").on("click", function (){
        let modal = $("#add_new_employee_modal");
        modal.css("display", "none");
    });
})
