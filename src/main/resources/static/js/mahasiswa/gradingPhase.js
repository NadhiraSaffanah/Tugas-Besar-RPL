document.addEventListener('DOMContentLoaded', () => {
    // tombol komentar
    const viewCommentBtns = document.querySelectorAll('.view-comment-btn');
    const popupComment = document.getElementById('popup-comment');
    const commentOverlay = document.getElementById('comment-overlay');
    const commentText = document.getElementById('comment-text');
    const closeBtns = document.querySelectorAll('.closeComment');

    viewCommentBtns.forEach(btn => {
        btn.addEventListener('click', async () => {
            const tahapId = btn.getAttribute('data-tahapid');
            try {
                const response = await fetch(`/mahasiswa/course/tahap/comment?tahapId=${tahapId}`);
                const comment = await response.text();
                commentText.textContent = comment;

                // tampilkan modal
                popupComment.style.display = 'flex';
                commentOverlay.style.display = 'block';
            } catch (err) {
                console.error('Error fetching comment:', err);
            }
        });
    });

    // close modal saat klik tombol close
    closeBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            popupComment.style.display = 'none';
            commentOverlay.style.display = 'none';
        });
    });

    // close modal saat klik overlay
    commentOverlay?.addEventListener('click', () => {
        popupComment.style.display = 'none';
        commentOverlay.style.display = 'none';
    });
});