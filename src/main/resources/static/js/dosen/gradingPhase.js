// FUNGSI UNTUK MENAMPILKAH POPUP SHOW RUBRIC
popUpShowRubric = document.getElementById('popup-show-rubric');
showRubricBtn = document.querySelectorAll('.showRubricBtn');
closePopUpRubric = document.querySelectorAll('.closeShowRubric');

showRubricBtn.forEach(btn => {
    btn.addEventListener('click', () => {
        const displayNow = window.getComputedStyle(popUpShowRubric).display;

        if (displayNow === 'none') {
            popUpShowRubric.style.display = 'flex'
        }
    });

    closePopUpRubric.forEach(btn => {
        btn.addEventListener('click', () => {
            popUpShowRubric.style.display = 'none';
        });
    });
});


// FUNGSI UNTUK MENAMPILKAN POP UP EDIT PHASE
editPhasePopUp = document.getElementById('edit-phase-container');
editPhaseBtn = document.querySelectorAll('.editPhaseBtn');
overlay = document.getElementById('overlay');
backBtn = document.getElementById('back-icon');

editPhaseBtn.forEach(btn => {
    btn.addEventListener('click', () => {
        overlay.style.display = 'flex';
        const displayNow = window.getComputedStyle(editPhasePopUp).display;

        if (displayNow === 'none') {
            editPhasePopUp.style.display = 'flex';
        } 
    });

    backBtn.addEventListener('click', () => {
        window.location.href = '/dosen/course/nav/grading/phase';
    });
});


// FUNGSI UNTUK MENAMPILKAN POP UP EDIT SUCCESS 
editPhaseForm = document.getElementById('editPhaseForm')
popupSuccess = document.querySelector('#popup-success');
closeButton = document.querySelector('#popup-success img');

editPhaseForm.addEventListener('submit', (e) => {
    e.preventDefault();
    popupSuccess.style.display = 'flex'
});

closeButton.addEventListener('click', () => {
    popupSuccess.style.display = 'none';
});


// FUNGSI UNTUK MENAMPILKAN POP UP SHOW DETAILS 
showDetailsBtn = document.querySelectorAll('.showDetailsBtn');

showDetailsBtn.forEach(btn => {
    btn.addEventListener('click', () => {
        window.location.href = '/dosen/course/nav/grading/phase/details';
    });
});