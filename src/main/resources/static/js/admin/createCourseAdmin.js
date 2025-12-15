let courseList = []; // menyimpan list kelas yang akan ditambahkan

//buat buka tutup popup bikin semester
function openForm() {
    courseList = []; // Reset waktu dibuka
    document.getElementById("courseForm").style.display = "flex";
    document.getElementById("csv-file-input").value = ""; //double reset file input
    
    updateCourseListDisplay();
}

function closeForm() {
    courseList = []; // Reset waktu ditutup
    document.getElementById("courseForm").style.display = "none";
    document.getElementById("course-list").innerHTML = "";
    document.getElementById("namaMatkul").value = "";
    document.getElementById("kelasMatkul").value = "";
    document.getElementById("csv-file-input").value = "";
}

// mekanisme add course kalau manual
function addManualCourse() {
    const namaMatkul = document.getElementById("namaMatkul").value.trim();
    const kelasMatkul = document.getElementById("kelasMatkul").value.trim();
    
    if (namaMatkul && kelasMatkul) {
        addCourseToList(namaMatkul, kelasMatkul);
        document.getElementById("namaMatkul").value = "";
        document.getElementById("kelasMatkul").value = "";
        document.getElementById("namaMatkul").focus(); // Focus back to first input
    } else {
        alert('Please fill in both Nama Matkul and Class fields');
    }
}

//kalau button add-manual-btn diklik, input akan dimunculkan dalam bentuk list
document.getElementById("add-manual-btn")?.addEventListener("click", addManualCourse);


// mekanisme add course kalau dari file csv
document.getElementById("csv-file-input")?.addEventListener("change", function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(event) {
            const csv = event.target.result;
            parseCSV(csv);
        };
        reader.readAsText(file);
    }
});

//parse csv file untuk ambil data nama matkul dan kelas matkul, harus memiliki header nama_matkul dan kelas_matkul
function parseCSV(csv) {
    const lines = csv.split(/\r?\n/).filter(line => line.trim());
    if (lines.length < 2) {
        alert('CSV file must have at least a header row and one data row');
        return;
    }
    
    // Parse header row - handle quoted values
    const headerLine = lines[0];
    const headers = parseCSVLine(headerLine).map(h => h.trim().toLowerCase().replace(/"/g, ''));
    
    // Find column indices
    const namaIndex = headers.indexOf('nama_matkul');
    const kelasIndex = headers.indexOf('kelas_matkul');
    
    if (namaIndex === -1 || kelasIndex === -1) {
        alert('CSV must contain columns: nama_matkul and kelas_matkul');
        return;
    }
    
    // Parse per row data
    for (let i = 1; i < lines.length; i++) {
        if (lines[i].trim()) {
            const values = parseCSVLine(lines[i]);
            const namaMatkul = values[namaIndex]?.trim().replace(/"/g, '');
            const kelasMatkul = values[kelasIndex]?.trim().replace(/"/g, '');
            
            if (namaMatkul && kelasMatkul) {
                addCourseToList(namaMatkul, kelasMatkul);
            }
        }
    }
}

// Helper function to parse CSV line handling quoted values
function parseCSVLine(line) {
    const result = [];
    let current = '';
    let inQuotes = false;
    
    for (let i = 0; i < line.length; i++) {
        const char = line[i];
        
        if (char === '"') {
            inQuotes = !inQuotes;
        } else if (char === ',' && !inQuotes) {
            result.push(current);
            current = '';
        } else {
            current += char;
        }
    }
    result.push(current);
    
    return result;
}

//fungsi menambah course ke list dengan penanganan duplikat
function addCourseToList(namaMatkul, kelasMatkul) {
    // Check if course already exists
    const exists = courseList.some(c => 
        c.namaMatkul === namaMatkul && c.kelasMatkul === kelasMatkul
    );
    
    if (!exists) {
        courseList.push({ namaMatkul, kelasMatkul });
        updateCourseListDisplay();
    }
}

function removeCourseFromList(index) {
    courseList.splice(index, 1);
    updateCourseListDisplay();
}

//update tampilan list pada popup
function updateCourseListDisplay() {
    const listContainer = document.getElementById("course-list");
    listContainer.innerHTML = "";
    
    courseList.forEach((course, index) => {
        const item = document.createElement("div");
        item.className = "course-list-item";
        item.innerHTML = `
            <span>${course.namaMatkul} (${course.kelasMatkul})</span>
            <input type="image" src="/images/close-green.svg" class="remove-btn" style="transform: scale(0.3);" onclick="removeCourseFromList(${index})"></button>
        `;
        listContainer.appendChild(item);
    });
}

// menangani submit 
document.getElementById("submit-course-btn")?.addEventListener("click", function() {
    if (courseList.length === 0) {
        alert('Please add at least one course');
        return;
    }
    
    // ambil idSemester dari window object (defined di matkul.html)
    const idSemester = window.idSemester;
    
    if (!idSemester) {
        alert('Semester ID not found');
        return;
    }
    
    // buat form data untuk dikirim ke server
    const formData = new FormData();
    formData.append('idSemester', idSemester);
    courseList.forEach((course) => {
        formData.append('courses', course.namaMatkul + ',' + course.kelasMatkul);
    });
    
    // kirim ke server dengan post
    fetch('/admin/courses/create', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok || response.redirected) {
            closeForm(); //tutup popup sebelum reload
            window.location.reload(); // Refresh page
        } else {
            alert('Error creating courses');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating courses: ' + error.message);
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

// Close menu when clicking outside
document.addEventListener('click', function(event) {
    if (!event.target.closest('.menu-container')) {
        document.querySelectorAll('.dropdown-menu').forEach(menu => {
            menu.classList.remove('show');
        });
    }
});

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

// Delete matkul function
function deleteMatkul(event, element) {
    event.preventDefault();
    event.stopPropagation();
    
    const idMatkul = element.getAttribute('data-matkul-id');
    const idSemester = window.idSemester;
    
    if (!idMatkul) {
        alert('Matkul ID not found');
        return;
    }
    
    if (!idSemester) {
        alert('Semester ID not found');
        return;
    }
    
    // Confirm deletion
    if (!confirm('Are you sure you want to delete this course? This action cannot be undone.')) {
        return;
    }
    
    // Close the menu
    document.querySelectorAll('.dropdown-menu').forEach(menu => {
        menu.classList.remove('show');
    });
    
    // Create form data with semester ID
    const formData = new FormData();
    formData.append('idSemester', idSemester);
    
    // Send delete request
    fetch(`/admin/courses/${idMatkul}/delete`, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        console.log('Delete response status:', response.status);
        console.log('Delete response ok:', response.ok);
        console.log('Delete response redirected:', response.redirected);
        
        // Check if it's an error status (4xx or 5xx)
        if (response.status >= 400) {
            response.text().then(text => {
                console.error('Error response body:', text.substring(0, 500));
                alert('Error deleting course. Status: ' + response.status);
            }).catch(() => {
                alert('Error deleting course. Status: ' + response.status);
            });
        } else {
            // Success - reload the page
            window.location.reload();
        }
    })
    .catch(error => {
        console.error('Fetch error:', error);
        alert('Error deleting course: ' + error.message);
    });
}
