let participantList = []; // List of participants to be added (from popup)
let currentParticipants = []; // Current participants displayed on the page

// Tab switching
function switchTab(role) {
    window.currentRole = role;
    
    // Save selected tab to localStorage
    localStorage.setItem('participantTab', role);
    
    // Update tab buttons
    document.getElementById('dosen-tab').classList.toggle('active', role === 'dosen');
    document.getElementById('mahasiswa-tab').classList.toggle('active', role === 'mahasiswa');
    
    // Show/hide participant sections
    const dosenSection = document.getElementById('dosen-participants');
    const mahasiswaSection = document.getElementById('mahasiswa-participants');
    
    if (role === 'dosen') {
        dosenSection.style.display = 'block';
        mahasiswaSection.style.display = 'none';
    } else {
        dosenSection.style.display = 'none';
        mahasiswaSection.style.display = 'block';
    }
    
    // Update search placeholder
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.placeholder = `Search added ${role === 'dosen' ? 'lecturers' : 'students'}`;
    }
}

// Popup functions
function openForm() {
    participantList = []; // Reset when opened
    document.getElementById("participantForm").style.display = "flex";
    document.getElementById("csv-file-input").value = "";
    document.getElementById("dropdown-search-input").value = "";
    document.getElementById("search-available-users").classList.remove("show");
    updateParticipantListDisplay();
}

function closeForm() {
    participantList = []; // Reset when closed
    document.getElementById("participantForm").style.display = "none";
    document.getElementById("participant-list-popup").innerHTML = "";
    document.getElementById("dropdown-search-input").value = "";
    document.getElementById("search-available-users").classList.remove("show");
    document.getElementById("csv-file-input").value = "";
}

// Dropdown functions
let allDropdownUsers = []; // Store all users for filtering

function loadDropdownUsers() {
    const searchTerm = document.getElementById("dropdown-search-input")?.value.trim() || "";
    const dropdown = document.getElementById("search-available-users");
    const userListDiv = document.getElementById("dropdown-user-list");
    
    if (searchTerm.length < 2) {
        // If no search term, show all users
        const url = `/admin/users/search?role=${window.currentRole}&searchTerm=%`;
        
        fetch(url)
            .then(response => response.json())
            .then(users => {
                allDropdownUsers = users;
                displayDropdownUsers(users);
                dropdown.classList.add("show");
            })
            .catch(error => {
                console.error('Error loading users:', error);
                userListDiv.innerHTML = "<div class='dropdown-user-item empty'>Error loading users</div>";
            });
    } else {
        // If there's a search term, trigger search
        filterDropdownUsers();
    }
}

function displayDropdownUsers(users) {
    const userListDiv = document.getElementById("dropdown-user-list");
    userListDiv.innerHTML = "";
    
    if (users.length === 0) {
        userListDiv.innerHTML = "<div class='dropdown-user-item empty'>No users found</div>";
        return;
    }
    
    users.forEach(user => {
        // Check if user is already in participantList
        const alreadyAdded = participantList.some(p => p.id === user.id);
        if (alreadyAdded) return; // Skip if already added
        
        const item = document.createElement("div");
        item.className = "dropdown-user-item";
        item.onclick = () => addUserFromDropdown(user);
        
        const avatar = document.createElement("div");
        avatar.className = "dropdown-user-avatar";
        avatar.textContent = user.nama ? user.nama.charAt(0).toUpperCase() : 'A';
        
        const info = document.createElement("div");
        info.className = "dropdown-user-info";
        
        const name = document.createElement("div");
        name.className = "dropdown-user-name";
        name.textContent = user.nama || 'Nama';
        
        const email = document.createElement("div");
        email.className = "dropdown-user-email";
        email.textContent = user.email || 'email@unpar.ac.id';
        
        info.appendChild(name);
        info.appendChild(email);
        item.appendChild(avatar);
        item.appendChild(info);
        userListDiv.appendChild(item);
    });
}

function filterDropdownUsers() {
    const searchTerm = document.getElementById("dropdown-search-input").value.trim();
    const dropdown = document.getElementById("search-available-users");
    
    if (searchTerm.length === 0) {
        // If search term is empty, hide dropdown
        dropdown.classList.remove("show");
        return;
    }
    
    if (searchTerm.length < 2) {
        // If search term is too short, don't search yet
        dropdown.classList.remove("show");
        return;
    }
    
    // Show dropdown and search
    dropdown.classList.add("show");
    
    // Debounce search
    clearTimeout(window.searchTimeout);
    window.searchTimeout = setTimeout(() => {
        const url = `/admin/users/search?role=${window.currentRole}&searchTerm=${encodeURIComponent(searchTerm)}`;
        
        fetch(url)
            .then(response => response.json())
            .then(users => {
                allDropdownUsers = users;
                displayDropdownUsers(users);
            })
            .catch(error => {
                console.error('Error searching users:', error);
                const userListDiv = document.getElementById("dropdown-user-list");
                userListDiv.innerHTML = "<div class='dropdown-user-item empty'>Error searching users</div>";
            });
    }, 300);
}

function addUserFromDropdown(user) {
    // Check if already in list
    if (participantList.some(p => p.id === user.id)) {
        alert('User already added');
        return;
    }
    
    participantList.push(user);
    updateParticipantListDisplay();
    
    // Close dropdown and clear search
    document.getElementById("search-available-users").classList.remove("show");
    document.getElementById("dropdown-search-input").value = "";
    allDropdownUsers = [];
}

// CSV file upload
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

// Parse CSV file - expects column: email
function parseCSV(csv) {
    console.log('Current Role:', window.currentRole); // Debug
    const lines = csv.split(/\r?\n/).filter(line => line.trim());
    if (lines.length < 2) {
        alert('CSV file must have at least a header row and one data row');
        return;
    }
    
    // Parse header row
    const headerLine = lines[0];
    const headers = parseCSVLine(headerLine).map(h => h.trim().toLowerCase().replace(/"/g, ''));
    
    // Find email column index
    const emailIndex = headers.indexOf('email');
    
    if (emailIndex === -1) {
        alert('CSV must contain an "email" column');
        return;
    }
    
    // For each row, search for user by email
    const promises = [];
    let processedCount = 0;
    let notFoundCount = 0;
    const totalRows = lines.length - 1;
    
    for (let i = 1; i < lines.length; i++) {
        if (lines[i].trim()) {
            const values = parseCSVLine(lines[i]);
            const email = values[emailIndex]?.trim().replace(/"/g, '');
            
            if (email) {
                // Search for user by email
                const promise = fetch(`/admin/users/search?role=${window.currentRole}&searchTerm=${encodeURIComponent(email)}`)
                    .then(response => response.json())
                    .then(users => {
                        console.log(`Search for email "${email}" returned:`, users); // Debug
                        if (users.length > 0) {
                            const user = users[0]; // Take first match
                            if (!participantList.some(p => p.id === user.id)) {
                                participantList.push(user);
                                processedCount++;
                            }
                        } else {
                            notFoundCount++;
                        }
                    })
                    .catch(error => {
                        console.error('Error searching user:', error);
                        notFoundCount++;
                    });
                promises.push(promise);
            }
        }
    }
    
    // Wait for all searches to complete
    Promise.all(promises).then(() => {
        updateParticipantListDisplay();
        if (notFoundCount > 0) {
            alert(`${notFoundCount} user(s) not found in database or does not have the correct role`);
        }
    });
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

// Update participant list display in popup
function updateParticipantListDisplay() {
    const listDiv = document.getElementById("participant-list-popup");
    listDiv.innerHTML = "";
    
    if (participantList.length === 0) {
        listDiv.innerHTML = "<div style='padding: 1em; text-align: center; color: #999;'>No participants added yet</div>";
        return;
    }
    
    participantList.forEach((participant, index) => {
        const item = document.createElement("div");
        item.className = "course-list-item";
        
        // Avatar
        const avatar = document.createElement("div");
        avatar.className = "participant-avatar";
        avatar.textContent = participant.nama ? participant.nama.charAt(0).toUpperCase() : 'A';
        
        // Info container
        const info = document.createElement("div");
        info.className = "participant-info";
        
        // Name
        const name = document.createElement("div");
        name.className = "participant-name";
        name.textContent = participant.nama || 'Nama';
        
        // Email
        const email = document.createElement("div");
        email.className = "participant-email";
        email.textContent = participant.email || 'email@unpar.ac.id';
        
        // Remove button
        const removeBtn = document.createElement("button");
        removeBtn.type = "button";
        removeBtn.className = "remove-btn";
        removeBtn.textContent = "×";
        removeBtn.onclick = () => {
            participantList.splice(index, 1);
            updateParticipantListDisplay();
        };
        
        // Assemble
        info.appendChild(name);
        info.appendChild(email);
        item.appendChild(avatar);
        item.appendChild(info);
        item.appendChild(removeBtn);
        listDiv.appendChild(item);
    });
}

// Submit participants
document.getElementById("submit-participant-btn")?.addEventListener("click", function() {
    if (participantList.length === 0) {
        alert('Please add at least one participant');
        return;
    }
    
    const userIds = participantList.map(p => p.id);
    
    // Convert to URL-encoded format
    const params = new URLSearchParams();
    userIds.forEach(id => {
        params.append('userIds', id);
    });
    
    fetch(`/admin/semesters/${window.idSemester}/courses/${window.idMatkul}/participants/add`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        credentials: 'include',
        body: params.toString()
    })
    .then(response => {
        if (response.ok || response.redirected) {
            // Navigate to the redirect URL instead of reloading
            // This ensures the session is properly maintained
            closeForm();
            window.location.href = `/admin/semesters/${window.idSemester}/courses/${window.idMatkul}/participants`;
        } else {
            alert('Error adding participants');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error adding participants: ' + error.message);
    });
});

// Remove participant from main list
function removeParticipant(event, element) {
    event.preventDefault();
    event.stopPropagation();
    
    const userId = element.getAttribute('data-user-id');
    const idMatkul = window.idMatkul;
    const idSemester = window.idSemester;
    
    if (!userId || !idMatkul || !idSemester) {
        alert('User ID, Matkul ID, or Semester ID not found');
        return;
    }
    
    if (!confirm('Are you sure you want to remove this participant?')) {
        return;
    }
    
    fetch(`/admin/semesters/${idSemester}/courses/${idMatkul}/participants/${userId}/remove`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        }
    })
    .then(response => {
        if (response.ok || response.redirected) {
            window.location.reload();
        } else {
            alert('Error removing participant');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error removing participant: ' + error.message);
    });
}

// Search in main page (filter displayed participants)
function searchUsers() {
    const searchTerm = document.getElementById("search-input").value.toLowerCase();
    const items = document.querySelectorAll(".participant-item");
    
    items.forEach(item => {
        const name = item.querySelector(".participant-name")?.textContent.toLowerCase() || "";
        const email = item.querySelector(".participant-email")?.textContent.toLowerCase() || "";
        
        if (name.includes(searchTerm) || email.includes(searchTerm)) {
            item.style.display = "flex";
        } else {
            item.style.display = "none";
        }
    });
}

// Close dropdown when clicking outside
document.addEventListener('click', function(event) {
    const dropdown = document.getElementById("search-available-users");
    const searchInput = document.getElementById("dropdown-search-input");
    
    if (dropdown && searchInput && 
        !dropdown.contains(event.target) && 
        !searchInput.contains(event.target)) {
        dropdown.classList.remove("show");
    }
});

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Get saved tab from localStorage, or use default from window.currentRole, or default to 'dosen'
    const savedTab = localStorage.getItem('participantTab');
    const initialRole = savedTab || window.currentRole || 'dosen';
    switchTab(initialRole);
});

