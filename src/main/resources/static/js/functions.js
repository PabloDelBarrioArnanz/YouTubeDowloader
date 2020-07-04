function addNewURL() {
    var table = document.getElementById("table");
    var tr = document.createElement("tr");
    tr.className = 'table-bordered';
    var td_url = document.createElement("td");
    var td_format = document.createElement("td");
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
    var table = document.getElementById("table");
    for (var i = 0, row = table.rows[i].getElementsByTagName("td"); i < table.rows.length; i++) {
        fetch('/download/', {
            body: JSON.stringify({"url": row.item(0).textContent, "format": row.item(1).textContent}),
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            },
        })
            .then(response => {
                let data = response.blob();
                const blob = new Blob([data], {type: "video\/mp4"});
                const downloadUrl = URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = downloadUrl;
                a.download = response.headers.get('name').split('%20').join(' ');
                document.body.appendChild(a);
                a.click();
            })
    }
}

function emptyField() {
    document.getElementById("newURL").value = "";
}