function addNewURL() {
    var table = document.getElementById("table");
    var tr = document.createElement("tr");
    tr.className = 'table-bordered';
    var td_url = document.createElement("td");
    var td_format = document.createElement("td");
    td_url.className = 'table-bordered';
    td_format.className = 'table-bordered';
    var newURL = document.getElementById("newURL");
    var format = document.getElementById("format");
    td_url.appendChild(document.createTextNode(newURL.value));
    td_format.appendChild(document.createTextNode(format.options[format.selectedIndex].value));
    tr.appendChild(td_url);
    tr.appendChild(td_format);
    table.appendChild(tr);
    newURL.value = "";
}

function downloadList() {
    var body = '{"videoInfoList": [';
    var table = document.getElementById("table");
    for (var i = 0; i < table.rows.length; i++) {
        var row = table.rows[i].getElementsByTagName("td");
        body += '{"url" : ' + '"' + row.item(0).textContent.trim() + '", "format": ' + '"' + row.item(1).textContent.trim() + '"}';
        if (i < table.rows.length - 1) {
            body += ","
        }
    }
    body += "]}";
    console.log(body);
    fetch('/download/', {
        body: body,
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        },
    })
        .then(response => {
            let data = response.blob();
            const blob = new Blob([data], {type: "application/zip"});
            const downloadUrl = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = downloadUrl;
            a.download = response.headers.get('name');
            document.body.appendChild(a);
            a.click();
        })
}

function emptyField() {
    document.getElementById("newURL").value = "";
}