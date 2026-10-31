// Small client-side search for the document list.
const searchBox = document.getElementById("searchBox");
if (searchBox) {
    searchBox.addEventListener("input", function () {
        const searchText = this.value.toLowerCase();
        const rows = document.querySelectorAll("#documentTable tbody tr");

        rows.forEach(function (row) {
            const rowText = row.innerText.toLowerCase();
            row.style.display = rowText.includes(searchText) ? "" : "none";
        });
    });
}
