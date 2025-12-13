document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('noMemberModal');
    const closeButton = document.getElementById('closeNoMemberModal');

    document.querySelectorAll('.edit-nilai-intercept').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            const memberCount = parseInt(this.getAttribute('data-member-count'));
            
            if (memberCount === 0) {
                modal.style.display = 'flex';
            } else {
                const groupId = this.getAttribute('data-group-id');
                const tahapId = this.getAttribute('data-tahap-id');
                
                window.location.href = `/dosen/course/nav/grading/phase/edit?tahapId=${tahapId}&groupId=${groupId}`;
            }
        });
    });

    closeButton.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
});