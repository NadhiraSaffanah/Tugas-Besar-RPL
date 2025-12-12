document.addEventListener("DOMContentLoaded", () => {
    const overlay = document.getElementById('overlay');
    const popupAdd = document.getElementById('popup-add-phase');
    const btnAdd = document.getElementById('btn-add-phase-trigger');
    const closeBtn = document.getElementById('closeAddPhase');
    const formAdd = document.getElementById('addPhaseForm');

    function togglePopup(show) {
        const display = show ? 'flex' : 'none';
        overlay.style.display = display === 'flex' ? 'block' : 'none';
        popupAdd.style.display = display;
    }

    if (btnAdd) {
        btnAdd.addEventListener('click', (e) => {
            e.preventDefault();
            togglePopup(true);
        });
    }

    if (closeBtn) {
        closeBtn.addEventListener('click', () => togglePopup(false));
    }

    if (overlay) {
        overlay.addEventListener('click', () => togglePopup(false));
    }

    if (formAdd) {
        formAdd.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(formAdd);
            try {
                const response = await fetch('/dosen/course/grading/phase/create-api', {
                    method: 'POST',
                    body: formData
                });

                if (response.ok) {
                    alert("Success!");
                    window.location.reload();
                } else {
                    alert("Failed to create.");
                }
            } catch (err) {
                console.error(err);
                alert("Error connecting.");
            }
        });
    }
});
