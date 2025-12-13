document.addEventListener('DOMContentLoaded', () => {
    const gradeAllRadio = document.getElementById('grade-all');
    const scoreInputMaster = document.getElementById('score');
    const scoreInputs = document.querySelectorAll('input[name="nilai"]');
    const gradingForm = document.getElementById('gradingForm');
    const toggleScoreButtons = document.querySelectorAll('.editNilaiBtn');

    toggleScoreButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const td = btn.closest('.score-input-wrapper');
            const input = td.querySelector('.editScore');
            const displayValue = td.querySelector('p');
            
            const isInputHidden = input.style.display === 'none' || input.style.display === '';

            if (isInputHidden) {
                input.style.display = 'block';
                displayValue.style.display = 'none';
                input.focus();
            } else {
                input.style.display = 'none';
                
                displayValue.textContent = input.value; 
                displayValue.style.display = 'block';
            }
        });
    });
    
    if (!gradingForm || !gradeAllRadio || !scoreInputMaster) {
        return; 
    }

    
    gradeAllRadio.addEventListener('change', () => {
        if (gradeAllRadio.checked) {
            scoreInputMaster.disabled = false;
            scoreInputMaster.focus();
        
            const masterValue = scoreInputMaster.value;
            if (masterValue !== "") {
                 scoreInputs.forEach(input => {
                    input.value = masterValue;
                });
            }
        } else {
            scoreInputMaster.disabled = true;
        }
    });

    scoreInputMaster.addEventListener('input', () => {
        if (gradeAllRadio.checked) {
            const masterValue = scoreInputMaster.value;
            scoreInputs.forEach(input => {
                input.value = masterValue;
            });
        }
    });
    
    // --- LOGIKA SUBMIT FORM (AJAX & Mencegah Enter) ---

    // 1. Mencegah submit form saat tombol Enter ditekan
    gradingForm.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault(); // Mencegah form submit default
        }
    });
    
    // 2. Handle Submission hanya saat tombol Save diklik (via form submit)
    gradingForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(gradingForm);
        
        try {
            const response = await fetch('/dosen/course/grading/phase/save-score-api', {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                alert("Nilai berhasil disimpan!");
                // Reload halaman untuk melihat perubahan
                window.location.reload(); 
            } else {
                const errorBody = await response.text();
                alert(`Gagal menyimpan nilai: ${errorBody}`);
            }
        } catch (error) {
            console.error('Error:', error);
            alert("Terjadi kesalahan sistem saat menyimpan nilai.");
        }
    });
});