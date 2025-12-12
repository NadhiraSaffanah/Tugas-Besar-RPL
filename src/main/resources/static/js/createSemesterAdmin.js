// buat buka tutup popup bikin semester
function openForm() {
    document.getElementById("semesterForm").style.display = "flex";
    // Reset form fields
    document.getElementById("startDate").value = "";
    document.getElementById("endDate").value = "";
    document.getElementById("jenis").value = "ganjil";
}

function closeForm() {
    document.getElementById("semesterForm").style.display = "none";
    // Reset form fields
    document.getElementById("startDate").value = "";
    document.getElementById("endDate").value = "";
    document.getElementById("jenis").value = "ganjil";
}

// menangani submit semester
document.getElementById("submit-semester-btn")?.addEventListener("click", function() {
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const jenis = document.getElementById("jenis").value;
    
    // Validasi input
    if (!startDate || !endDate || !jenis) {
        alert('Please fill in all fields');
        return;
    }
    
    // Validasi tanggal: end date harus setelah start date
    if (new Date(endDate) <= new Date(startDate)) {
        alert('End date must be after start date');
        return;
    }
    
    // Buat form data untuk dikirim ke server
    const formData = new FormData();
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    formData.append('jenis', jenis);
    
    // Kirim ke server dengan POST
    fetch('/admin/semesters/create', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok || response.redirected) {
            closeForm(); // Tutup popup sebelum reload
            window.location.reload(); // Refresh page
        } else {
            alert('Error creating semester');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating semester: ' + error.message);
    });
});

// Toggle dropdown menu
function toggleMenu(event, element) {
    event.preventDefault();
    event.stopPropagation();
    
    // Close all other menus
    document.querySelectorAll('.dropdown-menu').forEach(menu => {
        if (menu !== element.nextElementSibling) {
            menu.classList.remove('show');
        }
    });
    
    // Toggle current menu
    const menu = element.nextElementSibling;
    if (menu) {
        menu.classList.toggle('show');
    }
}

// Prevent link navigation when clicking menu icon
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.menu-icon').forEach(icon => {
        icon.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });
    
    document.querySelectorAll('.menu-container').forEach(container => {
        container.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
        });
    });
});

// Close menu when clicking outside
document.addEventListener('click', function(event) {
    if (!event.target.closest('.menu-container')) {
        document.querySelectorAll('.dropdown-menu').forEach(menu => {
            menu.classList.remove('show');
        });
    }
});

// Delete semester function
function deleteSemester(event, element) {
    event.preventDefault();
    event.stopPropagation();
    
    const idSemester = element.getAttribute('data-semester-id');
    console.log(idSemester);

    if (!idSemester) {
        alert('Semester ID not found');
        return;
    }
    
    // Confirm deletion
    if (!confirm('Are you sure you want to delete this semester? This action cannot be undone.')) {
        return;
    }
    
    // Create form data
    const formData = new FormData();
    formData.append('id', idSemester);
    
    // Send delete request
    fetch(`/admin/semesters/${idSemester}/delete`, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok || response.redirected) {
            window.location.reload(); // Refresh page
        } else {
            alert('Error deleting semester');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error deleting semester: ' + error.message);
    });
}