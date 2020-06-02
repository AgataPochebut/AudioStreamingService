$("#uploadDocument").on("click", function (e) {

    var formData = new FormData(document.forms["frmPdfUpload"]);

    $.ajax({
        url: '/documents',
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {

        },
        error: function (data) {
        }
    });
    return false;
});