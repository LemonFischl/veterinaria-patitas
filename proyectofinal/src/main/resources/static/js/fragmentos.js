document.addEventListener('DOMContentLoaded', function() {
    const navbarToggle = document.getElementById('navbar-toggle');
    const navbarMenu = document.getElementById('navbar-menu');

    // Toggle del menú hamburguesa
    navbarToggle.addEventListener('click', function() {
        navbarMenu.classList.toggle('active');
    });

    // Manejar los dropdowns
    const dropdownBtns = document.querySelectorAll('.dropdown-btn');

    dropdownBtns.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();

            const dropdown = this.closest('.dropdown');
            const dropdownMenu = dropdown.querySelector('.dropdown-menu');

            document.querySelectorAll('.dropdown-menu').forEach(menu => {
                if (menu !== dropdownMenu && menu.classList.contains('active')) {
                    menu.classList.remove('active');
                }
            });

            dropdownMenu.classList.toggle('active');
        });
    });

    document.addEventListener('click', function(e) {
        if (!e.target.closest('.dropdown')) {
            document.querySelectorAll('.dropdown-menu').forEach(menu => {
                menu.classList.remove('active');
            });
        }

        if (navbarMenu.classList.contains('active') &&
            !e.target.closest('.navbar-menu') &&
            !e.target.closest('.navbar-toggle')) {
            navbarMenu.classList.remove('active');
        }
    });

    document.querySelectorAll('.dropdown-menu').forEach(menu => {
        menu.addEventListener('click', function(e) {
            e.stopPropagation();
        });
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