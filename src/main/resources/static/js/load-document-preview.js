document.querySelectorAll('[id^="pdf-container-"]').forEach(container => {
    const documentId = container.id.split('-')[2];
    const url = `/common/download/${documentId}`;
    pdfjsLib.getDocument(url).promise.then(pdf => {
        pdf.getPage(1).then(page => {
            const viewport = page.getViewport({ scale: 1 });
            const canvas = document.createElement('canvas');
            const context = canvas.getContext('2d');
            canvas.height = viewport.height;
            canvas.width = viewport.width;
            canvas.style.position = "absolute";
            canvas.style.top = "0";
            canvas.style.left = "0";
            canvas.style.width = "100%";
            canvas.style.height = "100%";
            canvas.style.objectFit = "cover";
            container.appendChild(canvas);
            const renderContext = {
                canvasContext: context,
                viewport: viewport
            };
            page.render(renderContext);
        });
    }).catch(error => {
        console.error(`Error al cargar el PDF con ID ${documentId}:`, error);
        container.innerHTML = "<p>Error al cargar el documento</p>";
    });
});