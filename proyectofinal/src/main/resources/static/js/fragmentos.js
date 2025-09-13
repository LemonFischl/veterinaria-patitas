document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("navbar-toggle");
    const menu = document.getElementById("navbar-menu");

    toggle.addEventListener("click", () => {
        menu.classList.toggle("active");
    });
});



/*INICIO - FAQ*/

document.addEventListener('DOMContentLoaded', function() {
    const faqItems = document.querySelectorAll('.faq-item');

    faqItems.forEach(item => {
        const details = item.querySelector('details');
        const summary = item.querySelector('.faq-question');

        // Verificar estado inicial
        if (details.open) {
            summary.classList.add('active');
        }

        // Observar cambios en el estado
        details.addEventListener('toggle', function() {
            if (details.open) {
                summary.classList.add('active');
            } else {
                summary.classList.remove('active');
            }
        });
    });
});