document.getElementById("showTrailerBtn").addEventListener("click", function (e) {
    if (window.innerWidth < 640) window.open(this.href, "_blank"); else {
        e.preventDefault(), document.getElementById("cboxOverlay").style.display = "block", document.getElementById("colorbox").style.display = "block";
        var t = document.getElementById("movieTrailer");
        t.src = t.src
    }
}), document.getElementById("cboxClose").addEventListener("click", function () {
    document.getElementById("cboxOverlay").style.display = "none", document.getElementById("colorbox").style.display = "none";
    var e = document.getElementById("movieTrailer");
    e.src = e.src
}), document.getElementById("cboxOverlay").addEventListener("click", function () {
    document.getElementById("cboxOverlay").style.display = "none", document.getElementById("colorbox").style.display = "none";
    var e = document.getElementById("movieTrailer");
    e.src = e.src
})

function toggleDescription() {
    var e = document.getElementById("description"), t = document.getElementById("show-more-btn");
    "none" === e.style.maxHeight ? (e.style.maxHeight = "9em", t.textContent = "Xem thêm") : (e.style.maxHeight = "none", t.textContent = "Thu gọn")
}

document.addEventListener("DOMContentLoaded", function () {
    var e = document.getElementById("description"), t = document.getElementById("show-more-btn");
    e.scrollHeight > e.clientHeight && (t.style.display = "block")
})