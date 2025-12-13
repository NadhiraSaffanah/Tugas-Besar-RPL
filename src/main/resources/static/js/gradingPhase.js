document.addEventListener('DOMContentLoaded', () => {
    // === DOM ELEMENTS ===
    const overlay = document.getElementById('overlay');
    
    // --- POPUPS ---
    const addPhasePopup = document.getElementById('popup-add-phase');
    const rubricPopup = document.getElementById('popup-show-rubric');
    const editPhasePopup = document.getElementById('popup-edit-phase');
    
    // --- ADD PHASE ELEMENTS ---
    const btnAdd = document.getElementById('btn-add-phase-trigger');
    const closeAddPhase = document.getElementById('closeAddPhase');
    const formAdd = document.getElementById('addPhaseForm');
    
    // --- SHOW RUBRIC ELEMENTS ---
    const closeShowRubric = document.getElementById('closeShowRubric');
    const rubricTextContent = document.getElementById('rubric-text-content');
    const rubricPopupTitle = document.getElementById('rubric-popup-title');
    const showRubricButtons = document.querySelectorAll('.showRubricBtn');

    // --- EDIT PHASE ELEMENTS ---
    const closeEditPhase = document.getElementById('closeEditPhase');
    const cancelEditPhase = document.getElementById('cancelEditPhase');
    const editPhaseButtons = document.querySelectorAll('.editPhaseBtn');
    const editPhaseForm = document.getElementById('editPhaseForm'); 


    // ===============================
    // 0. HELPER FUNCTIONS
    // ===============================
    const hideOverlay = () => { overlay.style.display = 'none'; };
    const showOverlay = () => { overlay.style.display = 'block'; };
    
    // Fungsi untuk menutup SEMUA pop-up (seperti logika togglePopup(false) yang diperluas)
    const closeAllPopups = () => {
        hideOverlay();
        if (addPhasePopup) addPhasePopup.style.display = 'none';
        if (rubricPopup) rubricPopup.style.display = 'none';
        if (editPhasePopup) editPhasePopup.style.display = 'none';
    };


    // ===============================
    // FITUR UTAMA: KLIK OVERLAY (Menutup semua)
    // ===============================
    if (overlay) {
        overlay.addEventListener('click', closeAllPopups);
    }


    // ===============================
    // 1. ADD PHASE LOGIC (Diadaptasi dari kode asli Anda)
    // ===============================
    if (btnAdd) {
        btnAdd.addEventListener('click', (e) => {
            e.preventDefault();
            // Buka Popup Add (Close all, lalu buka Add)
            closeAllPopups(); // Tutup yang lain dulu
            showOverlay();
            if (addPhasePopup) addPhasePopup.style.display = 'flex';
        });
    }

    if (closeAddPhase) {
        closeAddPhase.addEventListener('click', closeAllPopups); // Close button memanggil closeAll
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


    // ===============================
    // 2. SHOW RUBRIC LOGIC
    // ===============================
    showRubricButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const card = btn.closest('.grading-card');
            
            // Ambil data dari data-attribute yang sudah kita tambahkan di HTML
            const rubricContent = card.dataset.rubrik || "Rubrik tidak tersedia."; 
            const phaseTitle = card.dataset.nama || "Assessment Rubric";

            rubricPopupTitle.innerText = "Rubric: " + phaseTitle;
            rubricTextContent.innerText = rubricContent; 
            
            closeAllPopups(); // Tutup yang lain dulu
            showOverlay();
            if (rubricPopup) rubricPopup.style.display = 'flex';
        });
    });

    if (closeShowRubric) {
        closeShowRubric.addEventListener('click', closeAllPopups);
    }


    // ===============================
    // 3. EDIT PHASE LOGIC
    // ===============================
    editPhaseButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const card = btn.closest('.grading-card');
            
            // Ambil data dan isi form
            document.getElementById('edit-id').value = card.dataset.id;
            document.getElementById('edit-namaTahap').value = card.dataset.nama;
            document.getElementById('edit-deskripsi').value = card.dataset.desc;
            document.getElementById('edit-rubrik').value = card.dataset.rubrik;
            document.getElementById('edit-tanggalAkhir').value = card.dataset.tanggal;
            
            closeAllPopups(); // Tutup yang lain dulu
            showOverlay();
            if (editPhasePopup) editPhasePopup.style.display = 'flex';
        });
    });

    if (closeEditPhase || cancelEditPhase) {
        const handler = () => closeAllPopups();
        if(closeEditPhase) closeEditPhase.addEventListener('click', handler);
        if(cancelEditPhase) cancelEditPhase.addEventListener('click', handler);
    }
    
    // Submit Edit Phase (API sudah tersedia)
    if (editPhaseForm) {
        editPhaseForm.addEventListener('submit', async (e) => {
             e.preventDefault();
             
             const formData = new FormData(editPhaseForm);

             try {
                 const response = await fetch('/dosen/course/grading/phase/update-api', {
                     method: 'POST',
                     body: formData
                 });

                 if (response.ok) {
                     alert("Phase successfully updated!");
                     window.location.reload(); 
                 } else {
                     alert("Failed to update phase. Please check server logs.");
                 }
             } catch (error) {
                 console.error('Error updating phase:', error);
                 alert("System error during phase update.");
             }
        });
    }

});