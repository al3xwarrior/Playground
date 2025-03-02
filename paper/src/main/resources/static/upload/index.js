const form = document.querySelector("form");
const fileinput = document.querySelector('input[type="file"]');
const fancyinput = document.querySelector('label[for="upload"]');
const keyinput = document.querySelector('input[name="key"]');
let hasUploaded = false;

fileinput.addEventListener("change", _event => {
    document.getElementById("filename").innerText = fileinput.files[0].name;
    keyinput.getAttribute("value").length > 0 && !hasUploaded &&
        document.querySelector('input[type="submit"]').removeAttribute("disabled");
});

document.addEventListener("DOMContentLoaded", _event => {
    window.location.searchParams = new URLSearchParams(location.search);
    keyinput.setAttribute("value", location.searchParams.get("key") ?? "");
});

form.addEventListener("submit", _event => {
    document.querySelector(".success").innerText = "Resource pack uploaded!";
    document.querySelector('input[type="submit"]').setAttribute("disabled", "");
    hasUploaded = true;
});

fancyinput.addEventListener("dragenter", event => {
    event.stopPropagation();
    event.preventDefault();
});

fancyinput.addEventListener("dragover", event => {
    event.stopPropagation();
    event.preventDefault();
});

fancyinput.addEventListener("drop", event => {
    fileinput.files = event.dataTransfer.files;
    fileinput.dispatchEvent(new Event("change"));
    event.stopPropagation();
    event.preventDefault();
});
