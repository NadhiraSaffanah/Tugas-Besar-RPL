// TAMPILKAN POP UP CREATE COURSE
popupSuccess = document.querySelector('#popup-success');
closeButton = document.querySelector('#popup-success img');

closeButton.addEventListener('click', () => {
    popupSuccess.style.display = 'none';
    window.location.href = 'course-details.html';
});

formCreateCourse = document.querySelector('#createCourseForm')
formCreateCourse.addEventListener('submit', (e) => {
    e.preventDefault();
    popupSuccess.style.display = 'flex';

    // backend process
});