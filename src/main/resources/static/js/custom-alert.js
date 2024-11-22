const modal = document.getElementById("customAlert");

const errorReport = "[[${errorReport}]]";
if (errorReport) {
    modal.style.display = "block";
}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}