document.addEventListener('DOMContentLoaded', () => {
    // 1. LOGIKA EDIT PROJECT (UPDATE)
    const editButton = document.querySelector('.desc-group-header img[alt="edit icon"]'); 
    const popupEditDetails = document.querySelector('#course-edit-details');
    const closeEditIcon = document.querySelector('#closeEditIcon'); 

    // Buka Popup Edit
    if (editButton) {
        editButton.addEventListener('click', () => {
            if(popupEditDetails) popupEditDetails.style.display = 'flex'; 
        });
    }

    // Tutup Popup Edit
    if (closeEditIcon) {
        closeEditIcon.addEventListener('click', () => {
            if(popupEditDetails) popupEditDetails.style.display = 'none';
        });
    }

    // Submit Form Edit
    const popupSuccess = document.querySelector('#popup-success'); // Pastikan ID ini ada di HTML (atau buat baru)
    // Kalau belum ada popup success khusus edit, bisa pakai alert atau buat div baru
    const formEditCourse = document.querySelector('#editCourseForm');

    if (formEditCourse) {
        formEditCourse.addEventListener('submit', async (e) => {
            e.preventDefault();

            const formData = new FormData(formEditCourse);
            const params = new URLSearchParams(formData);

            try {
                const response = await fetch('/dosen/course/update-api', {
                    method: 'POST',
                    body: params
                });

                if (response.ok) {
                    popupEditDetails.style.display = 'none';
                    // Tampilkan notif sukses (bisa alert atau popup)
                    alert("Project successfully updated!"); 
                    window.location.reload(); // Reload halaman
                } else {
                    alert("Gagal menyimpan data.");
                }
            } catch (error) {
                console.error('Error:', error);
                alert("Terjadi kesalahan sistem.");
            }
        });
    }


    // 2. LOGIKA SHOW RUBRICS
    const popUpShowRubric = document.querySelector('#popup-show-rubric');
    const closeRubricIcon = document.querySelector('#closeRubricIcon'); 
    
    const rubricContent = document.querySelector('#rubric-content');
    const rubricTitle = document.querySelector('#rubric-title');
    const showRubricsButtons = document.querySelectorAll('.show-rubric-btn');

    showRubricsButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const rubricText = this.getAttribute('data-rubric'); 
            const phaseName = this.getAttribute('data-phase');

            if(rubricContent) rubricContent.textContent = rubricText || "Tidak ada rubrik untuk tahap ini.";
            if(rubricTitle) rubricTitle.innerText = phaseName; 

            if(overlay) overlay.style.display = 'block';
            if(popUpShowRubric) popUpShowRubric.style.display = 'flex'; 
        });
    });

    // Tutup Popup pakai Icon X
    if (closeRubricIcon) {
        closeRubricIcon.addEventListener('click', () => {
            if(overlay) overlay.style.display = 'none';
            if(popUpShowRubric) popUpShowRubric.style.display = 'none';
        });
    }


    // 3. LOGIKA CREATE PROJECT (BARU)
    const btnCreateNew = document.querySelector('.btn-create'); 
    const popupCreate = document.querySelector('#course-create-details');
    const btnCloseCreate = document.querySelector('#closeCreateIcon'); // Tombol X
    const formCreate = document.querySelector('#createCourseForm');
    const popupSuccessCreate = document.querySelector('#popup-success-create');
    const closeSuccessCreateBtn = document.querySelector('#closeSuccessCreate');

    // Fungsi Tutup Popup Create
    const closeCreatePopup = () => {
        if(popupCreate) popupCreate.style.display = 'none';
        if(overlay) overlay.style.display = 'none';
    };

    // Buka Popup Create
    if (btnCreateNew) {
        btnCreateNew.addEventListener('click', (e) => {
            e.preventDefault();
            if(popupCreate) popupCreate.style.display = 'flex'; 
            if(overlay) overlay.style.display = 'block';
        });
    }

    // Tutup tombol Close (X)
    if (btnCloseCreate) {
        btnCloseCreate.addEventListener('click', closeCreatePopup);
    }

    // Submit Form Create (AJAX)
    if (formCreate) {
        formCreate.addEventListener('submit', async (e) => {
            e.preventDefault();

            const formData = new FormData(formCreate);
            const params = new URLSearchParams(formData);

            try {
                // Kirim ke endpoint API
                const response = await fetch('/dosen/course/create-api', {
                    method: 'POST',
                    body: params
                });

                if (response.ok) {
                    if(popupCreate) popupCreate.style.display = 'none';
                    // Tampilkan notif sukses
                    if(popupSuccessCreate) {
                        popupSuccessCreate.style.display = 'block';
                        // Reload halaman otomatis
                        setTimeout(() => {
                            window.location.reload();
                        }, 1500);
                    } else {
                        // Fallback kalau div notif gak ada
                        alert("Project successfully created!");
                        window.location.reload();
                    }
                } else {
                    alert("Failed to create project. Please try again.");
                }
            } catch (error) {
                console.error('Error:', error);
                alert("An error occurred.");
            }
        });
    }

    // Tutup Popup Success Create Manual
    if(closeSuccessCreateBtn) {
        closeSuccessCreateBtn.addEventListener('click', () => {
            if(popupSuccessCreate) popupSuccessCreate.style.display = 'none';
            if(overlay) overlay.style.display = 'none';
            window.location.reload();
        });
    }
});