
        document.addEventListener('DOMContentLoaded', function() {
            // Toggle password visibility
            const togglePassword = document.getElementById('togglePassword');
            const passwordInput = document.getElementById('password');
            const toggleIcon = document.getElementById('toggleIcon');

            togglePassword.addEventListener('click', function() {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);

                // Cambiar icono
                if (type === 'password') {
                    toggleIcon.className = 'bi bi-eye';
                } else {
                    toggleIcon.className = 'bi bi-eye-slash';
                }
            });

            // Animación de entrada
            const loginCard = document.querySelector('.login-card');
            loginCard.style.opacity = '0';
            loginCard.style.transform = 'scale(0.9)';

            setTimeout(() => {
                loginCard.style.transition = 'all 0.6s ease';
                loginCard.style.opacity = '1';
                loginCard.style.transform = 'scale(1)';
            }, 100);

            // Focus effect en inputs
            const inputs = document.querySelectorAll('.form-control');
            inputs.forEach(input => {
                input.addEventListener('focus', function() {
                    this.parentNode.classList.add('shadow-sm');
                });

                input.addEventListener('blur', function() {
                    this.parentNode.classList.remove('shadow-sm');
                });
            });

            // Validación en tiempo real
            const form = document.querySelector('form');
            const submitBtn = form.querySelector('button[type="submit"]');

            function validateForm() {
                const username = document.getElementById('username').value;
                const password = document.getElementById('password').value;

                if (username.length >= 3 && password.length >= 6) {
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = '<i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión';
                } else {
                    submitBtn.disabled = true;
                    submitBtn.innerHTML = '<i class="bi bi-exclamation-circle me-2"></i>Completa los campos';
                }
            }

            inputs.forEach(input => {
                input.addEventListener('input', validateForm);
            });

            // Estado inicial
            validateForm();
        });
