let courseList = []; // menyimpan list kelas yang akan ditambahkan

//buat buka tutup popup bikin semester
function openForm() {
    document.getElementById("courseForm").style.display = "flex";
    courseList = []; // Reset list when opening
    updateCourseListDisplay();
}

function closeForm() {
    document.getElementById("courseForm").style.display = "none";
    courseList = []; // Reset list when closing
    document.getElementById("course-list").innerHTML = "";
    document.getElementById("nama_matkul_manual").value = "";
    document.getElementById("kelas_matkul_manual").value = "";
    document.getElementById("add-with-file-btn").value = "";
}

// mekanisme add course kalau manual
function addManualCourse() {
    const namaMatkul = document.getElementById("nama_matkul_manual").value.trim();
    const kelasMatkul = document.getElementById("kelas_matkul_manual").value.trim();
    
    if (namaMatkul && kelasMatkul) {
        addCourseToList(namaMatkul, kelasMatkul);
        document.getElementById("nama_matkul_manual").value = "";
        document.getElementById("kelas_matkul_manual").value = "";
        document.getElementById("nama_matkul_manual").focus(); // Focus back to first input
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

// Trigger file input when "Add with file" is clicked
document.getElementById("add-with-file-btn")?.addEventListener("click", function() {
    document.getElementById("csv-file-input").click();
});

// Also trigger when clicking the file upload label
document.querySelector(".file-upload-label")?.addEventListener("click", function(e) {
    e.preventDefault();
    document.getElementById("csv-file-input").click();
});

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
    
    // Parse data rows
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

// Handle form submission
document.getElementById("add-course-btn")?.addEventListener("click", function() {
    if (courseList.length === 0) {
        alert('Please add at least one course');
        return;
    }
    
    // Get semester ID from the page
    const idSemester = /*[[${idSemester}]]*/ null;
    
    if (!idSemester) {
        alert('Semester ID not found');
        return;
    }
    
    // Create form data
    const formData = new FormData();
    formData.append('idSemester', idSemester);
    courseList.forEach((course) => {
        formData.append('courses', course.namaMatkul + ',' + course.kelasMatkul);
    });
    
    // Send to server
    fetch('/admin/courses/create', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok || response.redirected) {
            window.location.reload(); // Refresh page
        } else {
            alert('Error creating courses');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating courses');
    });
});
