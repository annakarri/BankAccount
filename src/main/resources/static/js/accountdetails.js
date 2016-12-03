
$("#btn_showBalance").click(function () {
    clearErrors();
    $.get("/accounts/1", function (data) {
        //console.log(JSON.stringify(data));
        $("#span_balance").text(data.balance);
    }).fail(function(data){
        $("#error_balance").text(data.responseText);
    });
});

$("#btn_showAllTransactions").click(function () {
    clearErrors();
    $.get("/accounts/1/transactions", function (data) {
        //console.log(JSON.stringify(data));
        showTransactions(data);
    }).fail(function(data){
        $("#error_findAll").text(data.responseText);
    });
});

$("#btn_findByType").click(function () {
    clearErrors();
    var ttype = $("#in_transactionType")[0].value;

    if (isBlank(ttype)) {
        $("#error_findByType").text("Please enter valid type.");
        return;
    }
    var url = "/accounts/1/transactions?type=" + ttype;

    $.get(url, function (data) {
        //console.log(JSON.stringify(data));
        showTransactions(data);
    }).fail(function(data){
        $("#error_findByType").text("Unknown transaction type.");
    });
});

$("#btn_findByDateRange").click(function () {
    clearErrors();
    var url = "/accounts/1/transactions";
    var dateFrom = $("#in_dateFrom")[0].value;
    var dateTo = $("#in_dateTo")[0].value;

    if (isBlank(dateFrom) || isBlank(dateTo)) {
        $("#error_findByDateRange").text("Please enter valid date range.");
        return;
    }

    url = url + '?datefrom=' + dateFrom + '&dateto=' + dateTo;

    $.get(url, function (data) {
        //console.log(JSON.stringify(data));
        showTransactions(data);
    }).fail(function(data){
        $("#error_findByDateRange").text("Error encountered. Please check the query.");
    });
});

$("#ajax_upload").click(function(){
    clearErrors();
    uploadFile();
});

function clearErrors() {
    $("span.error").text("");
}

function isBlank(data) {
    return typeof data === "undefined" || data === null || data === "";
}

function showTransactions(data) {
    $("#div_transactions").removeClass("hidden");
    var table = $("#tab_transactions tbody");
    table.empty();
    if (isBlank(data) || data.length === 0) {
        table.append('<tr>' +
            '<td colspan="4">No records found.</td>' +
            '</tr>');
        $("#span_tranCount").text("");
    } else {
        $.each(data, function (index, item) {
            table.append('<tr>' +
                '<td>' + item.balance + '</td>' +
                '<td>' + item.date + '</td>' +
                '<td>' + item.type + '</td>' +
                '<td>' + item.description + '</td>' +
                '</tr>');
        });
        $("#span_tranCount").text(data.length +" record(s) found");
    }
}

function uploadFile() {
    $("#span_fileUpload").removeClass("error");

    $.ajax({
        url: "/uploadFile",
        type: "POST",
        data: new FormData($("#form_uploadFile")[0]),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (data) {
            // Handle upload success
            //console.log(data);
            $("#span_fileUpload").text(data);
        },
        error: function (data) {
            // Handle upload error
            //console.log(data)
            $("#span_fileUpload").text(data.responseText);
            $("#span_fileUpload").addClass("error");
        }
    });
}