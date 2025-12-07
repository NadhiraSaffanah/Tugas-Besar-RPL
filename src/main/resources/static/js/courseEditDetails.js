// TAMPILKAN POP UP EDIT COURSE DETAILS 
editButton = document.querySelector('.desc-group-header img');
popupEditDetails = document.querySelector('#course-edit-details');
overlay = document.querySelector('#overlay');
cancelEdit = document.querySelector('#back-icon');

title = document.querySelector

editButton.addEventListener('click', () => {
    popupEditDetails.style.display = 'flex';
    overlay.style.display = 'block';

    if(cancelEdit.addEventListener('click', () => {
        popupEditDetails.style.display = 'none';
        overlay.style.display = 'none';
    }));
});


// TAMPILKAN POP UP SUCCES EDITED
popupSuccess = document.querySelector('#popup-success');
closeButton = document.querySelector('#popup-success img');

closeButton.addEventListener('click', () => {
    popupSuccess.style.display = 'none';
    window.location.href = '/dosen/course/details';
});

formEditCourse = document.querySelector('#editCourseForm');
formEditCourse.addEventListener('submit', (e) => {
    e.preventDefault();
    popupSuccess.style.display = 'flex';

    // backend process
});


// TAMPILKAN GRADING PHASE JIKA SUDAH DIISI
gradingWrapper = document.querySelector('#grading-wrapper');
    // TOLONG DILENGKAPI


// TAMPILKAN POP UP SHOW RUBRICS
popUpShowRubric = document.querySelector('#popup-show-rubric');
showRubricsButton = document.querySelectorAll('.grading-card button');
closeShowRubric = document.querySelector('#closeShowRubric')

showRubricsButton.forEach(showRubricsButton => {
    showRubricsButton.addEventListener('click', () => {
        overlay.style.display = 'block';
        popUpShowRubric.style.display = 'flex';
    });

    closeShowRubric.addEventListener('click', () => {
        overlay.style.display = 'none';
        popUpShowRubric.style.display = 'none';
    });
});

