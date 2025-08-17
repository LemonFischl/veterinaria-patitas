document.addEventListener('DOMContentLoaded', function () {
    const usuarioSelect = document.getElementById('usuarioSelect');
    const usuarioHidden = document.querySelector('input[name="usuario.id_usuario"]');
    const mascotaSelect = document.getElementById('mascotaSelect');

    function obtenerUsuarioId() {
        if (usuarioSelect) return usuarioSelect.value;
        if (usuarioHidden) return usuarioHidden.value;
        return null;
    }

    function filtrarMascotasPorUsuario() {
        const usuarioId = obtenerUsuarioId();
        if (!usuarioId || !mascotaSelect) return;

        mascotaSelect.value = '';

        Array.from(mascotaSelect.options).forEach(option => {
            const userId = option.getAttribute('data-usuario-id');
            if (!userId || userId === usuarioId) {
                option.style.display = '';
            } else {
                option.style.display = 'none';
            }
        });

        const visibleOptions = Array.from(mascotaSelect.options)
            .filter(option => option.style.display !== 'none' && option.value !== '');

        mascotaSelect.value = visibleOptions.length > 0 ? visibleOptions[0].value : '';
    }

    if (usuarioSelect) {
        usuarioSelect.addEventListener('change', filtrarMascotasPorUsuario);
    }

    // Ejecutar una vez al cargar
    filtrarMascotasPorUsuario();

    // VETERINARIA: servicios y veterinarios
    const veterinariaSelect = document.getElementById('veterinaria');
    const serviciosUl = document.getElementById('serviciosVeterinaria');
    const veterinarioSelect = document.getElementById('veterinarioSelect');

    if (!veterinariaSelect) return;

    function actualizarServicios() {
        serviciosUl.innerHTML = '';

        const selectedOption = veterinariaSelect.selectedOptions[0];
        const serviciosRaw = selectedOption?.getAttribute('data-servicios');

        if (!serviciosRaw || serviciosRaw === '[]') {
            serviciosUl.innerHTML = '<li class="list-group-item text-muted">Sin servicios registrados</li>';
            return;
        }

        const servicios = serviciosRaw.replaceAll('[', '').replaceAll(']', '').split(',');

        servicios.forEach((servicio) => {
            const item = document.createElement('li');
            item.className = 'list-group-item';
            item.textContent = servicio.trim();
            serviciosUl.appendChild(item);
        });
    }

    function filtrarVeterinarios() {
        const veterinariaId = veterinariaSelect.value.trim();

        Array.from(veterinarioSelect.options).forEach(option => {
            const vetVetId = option.getAttribute('data-vet-vet');
            if (!vetVetId || vetVetId.trim() === veterinariaId) {
                option.style.display = '';
            } else {
                option.style.display = 'none';
            }
        });

        const visibleOptions = Array.from(veterinarioSelect.options)
            .filter(opt => opt.style.display !== 'none' && opt.value !== '');

        veterinarioSelect.value = visibleOptions.length > 0 ? visibleOptions[0].value : '';
    }

    veterinariaSelect.addEventListener('change', function () {
        actualizarServicios();
        filtrarVeterinarios();
    });

    // Ejecutar al cargar
    setTimeout(() => {
        actualizarServicios();
        filtrarVeterinarios();
    }, 0);
});