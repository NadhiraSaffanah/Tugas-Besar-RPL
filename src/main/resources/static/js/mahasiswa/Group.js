//JS untuk menunjukkan anggota kelompok 
document.addEventListener('DOMContentLoaded', function () {
    const toggleButton = document.getElementById('show-selected-group-btn');
    const memberWrappers = document.querySelectorAll('.member-wrapper');
    let isShowing = false;

    memberWrappers.forEach(wrapper => {
        wrapper.style.display = 'none';
    });

    // Logika Toggle Visibility
    toggleButton.addEventListener('click', function () {
        isShowing = !isShowing; 

        memberWrappers.forEach(wrapper => {
            wrapper.style.display = isShowing ? 'block' : 'none';
        });

        // Ubah teks tombol
        toggleButton.textContent = isShowing ? 'Hide Group Members' : 'Show Group Members';
    });

    const radioButtons = document.querySelectorAll('input[name="selectedGroup"]');

    radioButtons.forEach(radio => {
        const row = radio.closest('tr');

        if (radio.checked) {
            row.classList.add('is-selected');
        }

        radio.addEventListener('change', function () {
            document.querySelectorAll('tbody tr').forEach(tr => {
                tr.classList.remove('is-selected');
            });

            if (this.checked) {
                this.closest('tr').classList.add('is-selected');
            }
        });
    });
});