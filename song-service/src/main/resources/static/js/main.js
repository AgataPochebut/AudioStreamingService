$(document).ready(function () {

    $("#btnUpload").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        ajax_upload();
    });

    $("#btnUploadZip").click(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        ajax_upload_zip();
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

    $("#btnUpload").prop("disabled", true);

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
            $("#btnUpload").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnUpload").prop("disabled", false);

        }
    });

}

function ajax_upload_zip() {

    //Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    $("#btnUploadZip").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/song-service/songs/upload/zip",
        data: data,
        processData: false,//prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,

        success: function (data) {

            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnUploadZip").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnUploadZip").prop("disabled", false);

        }
    });

}

function ajax_download() {

    //Get form
    var url = $(this).attr('href');

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