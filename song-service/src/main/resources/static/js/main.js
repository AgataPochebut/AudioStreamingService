$(document).ready(function () {

    $("#btnSubmit").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        ajax_upload();
    });

    $("#btnDownload").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        ajax_download();
    });

});

function ajax_upload() {

    //Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    data.append("CustomField", "This is some extra data, testing");

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/song-service/songs/upload",
        data: data,
        processData: false,//prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,

        success: function (data) {

            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

function ajax_download() {

    //Get form
    var url = $(this).attr('href');

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "GET",
        // enctype: 'multipart/form-data',
        url: url,
        // data: data,
        processData: false,//prevent jQuery from automatically transforming the data into a query string
        contentType: "application/octet-stream",
        cache: false,
        timeout: 600000,

        success: function (data) {

            $("#result").text(data);
            console.log("SUCCESS : ", data);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);

        }
    });

}