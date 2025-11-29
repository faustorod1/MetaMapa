/**
 * Cambia la visibilidad de los caracteres de un input password.
 * @param {String} buttonID ID del botón que dispara el evento para la función.
 */
function togglePasswordVisible(buttonID) {
    const btn = document.getElementById(buttonID);
    const inputID = btn.getAttribute('for');
    const input = document.getElementById(inputID);
    if (input.getAttribute('type') === 'password') {
        btn.classList.remove('fa-eye');
        btn.classList.add('fa-eye-slash');
        input.setAttribute('type', 'text');
    } else {
        btn.classList.remove('fa-eye-slash');
        btn.classList.add('fa-eye');
        input.setAttribute('type', 'password');
    }
}

function toggleAdminCode(isAdmin) {
    const adminCodeContainer = document.getElementById('admin-code-container');
    const codigoAdminInput = document.getElementById('codigoAdmin');

    if (isAdmin) {
        // Mostrar el campo de código
        adminCodeContainer.style.display = 'flex';

        // Hacer que el campo sea obligatorio para un Administrador
        codigoAdminInput.required = true;

    } else {
        // Ocultar el campo de código
        adminCodeContainer.style.display = 'none';

        // No es obligatorio, y limpiamos su valor para no enviarlo accidentalmente
        codigoAdminInput.required = false;
        codigoAdminInput.value = null;
    }
}

// Opcional: Asegurar que, al cargar la página, el estado sea el correcto (oculto)
document.addEventListener('DOMContentLoaded', () => {
    // Si el Contribuyente está marcado por defecto (checked), el campo debe estar oculto
    const adminRadio = document.querySelector('input[name="tipoUsuario"][value="ADMINISTRADOR"]:checked');
    toggleAdminCode(adminRadio !== null); // Debería ser 'false' si Contribuyente está checked
});