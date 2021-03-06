function addNewURL() {
    var table = document.getElementById("table");
    var tr = document.createElement("tr");
    tr.className = 'table-bordered';
    var td_url = document.createElement("td");
    var td_format = document.createElement("td");
    var td_button = document.createElement("td");
    td_button.className = 'table-bordered';
    td_url.className = 'table-bordered';
    td_format.className = 'table-bordered';
    var button = document.createElement("button");
    button.className = 'glyphicon-remove-circle';
    button.addEventListener("click", function () {
        removeURL(this);
    });
    var newURL = document.getElementById("newURL");
    var format = document.getElementById("format");
    td_url.appendChild(document.createTextNode(newURL.value));
    td_format.appendChild(document.createTextNode(format.options[format.selectedIndex].value));
    tr.appendChild(td_url);
    tr.appendChild(td_format);
    td_button.appendChild(button);
    tr.appendChild(td_button);
    table.appendChild(tr);
    newURL.value = "";
}

function removeURL(row) {
    var i = row.parentNode.parentNode.rowIndex;
    document.getElementById("table").deleteRow(i);
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
    }).then((transfer) => {
        return transfer.blob();
    }).then((bytes) => {
        let elm = document.createElement('a');
        elm.href = URL.createObjectURL(bytes);
        elm.setAttribute('download', 'ENJOY.zip');
        elm.click()
    }).catch((error) => {
        console.log(error);
    })
}

function emptyField() {
    document.getElementById("newURL").value = "";
}