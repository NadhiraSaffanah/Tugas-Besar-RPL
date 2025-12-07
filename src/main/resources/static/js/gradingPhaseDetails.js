// FUNGSI UNTUK MENAMPILKAN EDIT GRADING PHASE PER GROUP
// SAAT TOMBOL GRADED DI KOLOM STATUS DITEKAN
editPhaseGroupBtn = document.querySelectorAll('.editPhaseGroupBtn');

editPhaseGroupBtn.forEach(btn => {
    btn.addEventListener('click', () => {
        window.location.href = '/dosen/course/nav/grading/phase/details/edit';
    });
});