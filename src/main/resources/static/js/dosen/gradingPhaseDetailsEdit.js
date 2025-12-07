// FUNGSI UNTUK KEMBALI KE HALAMAN SEBELUMNYA
backBtn = document.getElementById('back-icon');

backBtn.addEventListener('click', () => {
    window.location.href = '/dosen/course/nav/grading/phase/details';
});


// FUNGSI UNTUK MENAMPILKAN EDIT NILAI INDIVIDUAL
inputNilai = document.querySelectorAll('.editScore');
editNilaiBtn = document.querySelectorAll('.editNilaiBtn');

editNilaiBtn.forEach(btn => {
    btn.addEventListener('click', () => {
        const td = btn.closest('td');           
        const input = td.querySelector('.editScore');
        
        const currentDisplay = window.getComputedStyle(input).display;
        
        if (currentDisplay === 'none') {
            input.style.display = 'block';
        } else {
            input.style.display = 'none';
        }

        // hide nilai lama
        const p = td.querySelector('p');

        if (currentDisplay === 'none') {
            input.style.display = 'block';
            p.style.display = 'none';
        } else {
            input.style.display = 'none';
            p.style.display = 'block';
        }

    });
});


// TAMPILKAN POP UP SUCCES EDITED
popupSuccess = document.querySelector('#popup-success');
closeButton = document.querySelector('#popup-success img');
saveBtn = document.getElementById('save');

saveBtn.addEventListener('click', () => {
    const popUpSuccesDisplay = window.getComputedStyle(popupSuccess).display;

    if (popUpSuccesDisplay === 'none') {
        popupSuccess.style.display = 'flex';
    }
    
    closeButton.addEventListener('click', () => {
        window.location.href = '/dosen/course/nav/grading/phase/details/edit';
    });
});