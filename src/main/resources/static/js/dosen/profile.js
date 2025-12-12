const editProfileBtn = document.querySelector('#profile-button button:first-child');
const changePasswordBtn = document.querySelector('#profile-button button:last-child');
const formEditProfile = document.getElementById('form-edit-profile');
const password = document.querySelector('#body-right > p:nth-of-type(4)');

// TAMPILKAN FORM EDIT PROFILE
editProfileBtn.addEventListener('click', () => {
    const isHidden = getComputedStyle(formEditProfile).display === 'none';

    if (isHidden) {
        formEditProfile.style.display = 'flex';
        password.style.display = 'none';
    } else {
        formEditProfile.style.display = 'none';
        password.style.display = 'block';
    }
});

// SUBMIT FORM EDIT PROFILE
changePasswordBtn.addEventListener('click', (e) => {
    e.preventDefault();

    const isVisible = getComputedStyle(formEditProfile).display !== 'none';
    const hasValue =
        formEditProfile.oldPassword.value.trim() !== '' &&
        formEditProfile.newPassword.value.trim() !== '' &&
        formEditProfile.oldPasswordRetype.value.trim() !== '';

    if (isVisible && hasValue) {
        formEditProfile.submit();
        alert('Password changed successfully!');
        formEditProfile.style.display = 'none';
        password.style.display = 'block';
    } else {
        alert('Fill the fields first!');
    }
});


// LOGOUT BUTTON
const logoutBtn = document.querySelector('#nav-right li:last-child');
logoutBtn.addEventListener('click', () => {
    alert('Logged out successfully!');
    window.location.href = 'login-page.html';
});