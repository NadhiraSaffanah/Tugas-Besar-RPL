// TAMPILKAN FILTER
filterButton = document.querySelector('#filter-sort button');
filterWrapper = document.querySelector('#filter-wrapper');
resetButton = document.querySelector('#filter-button > p:first-child')

filterButton.addEventListener('click', () => {
    if(filterWrapper.style.display = 'none') {
        filterWrapper.style.display = 'flex';
    } 
    
    resetButton.addEventListener('click', () => {
        filterWrapper.style.display = 'none';
    });
});