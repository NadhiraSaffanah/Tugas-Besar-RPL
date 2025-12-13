document.addEventListener("DOMContentLoaded", () => {
    const currentList = document.getElementById('current-members-list');
    const availableList = document.getElementById('available-students-list');
    const capacityInput = document.getElementById('jmlAnggota');
    const saveBtn = document.getElementById('save-btn');
    const errorMsg = document.getElementById('capacity-error');
    const countBadge = document.getElementById('member-count-badge');

    updateMemberCount();
    validateCapacity();

    window.removeMember = function(btn) {
        const item = btn.parentElement;

        const newBtn = document.createElement('button');
        newBtn.type = 'button';
        newBtn.className = 'action-btn btn-add';
        newBtn.innerText = '+';
        newBtn.onclick = function() { addMember(this); };

        item.replaceChild(newBtn, btn);
        availableList.appendChild(item);

        updateMemberCount();
        validateCapacity();
    };

    window.addMember = function(btn) {
        const item = btn.parentElement;

        const newBtn = document.createElement('button');
        newBtn.type = 'button';
        newBtn.className = 'action-btn btn-remove';
        newBtn.innerText = '-';
        newBtn.onclick = function() { removeMember(this); };

        item.replaceChild(newBtn, btn);
        currentList.appendChild(item);

        updateMemberCount();
        validateCapacity();
    };

    function updateMemberCount() {
        countBadge.innerText = currentList.children.length;
    }

    function validateCapacity() {
        const capacity = parseInt(capacityInput.value) || 0;
        const currentCount = currentList.children.length;

        if (capacity < currentCount) {
            errorMsg.style.display = 'block';
            saveBtn.disabled = true;
            saveBtn.innerText = "Capacity too low";
        } else {
            errorMsg.style.display = 'none';
            saveBtn.disabled = false;
            saveBtn.innerText = "Save Changes";
        }
    }

    const form = document.getElementById('editGroupForm');
    form.addEventListener("submit", function(e) {
        e.preventDefault();

        const groupId = document.getElementById('groupId').value;
        const nama = document.getElementById('namaKelompok').value;
        const jml = document.getElementById('jmlAnggota').value;
        const tubesId = document.getElementById('tubesId').value;

        const memberIds = [];
        currentList.querySelectorAll('.student-item').forEach(item => {
            memberIds.push(item.getAttribute('data-id'));
        });

        const params = new URLSearchParams();
        params.append('groupId', groupId);
        params.append('namaKelompok', nama);
        params.append('jmlAnggota', jml);
        memberIds.forEach(id => params.append('memberIds', id));

        fetch('/dosen/course/nav/group/update-api', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        })
        .then(response => {
            if (response.ok) {
                window.location.href = "/dosen/course/nav/group?id=" + tubesId;
            } else {
                alert("Error saving group changes.");
            }
        })
        .catch(err => console.error(err));
    });

});
