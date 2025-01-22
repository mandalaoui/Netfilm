document.addEventListener("DOMContentLoaded", function () {
    const body = document.body;
    
    // Adding the header
    const header = document.createElement("div");
    header.className = "header";
    header.textContent = "Netflix"; // Header text
    body.appendChild(header);
});