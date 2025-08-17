/**
 * Cambia la visibilidad de los caracteres de un input password.
 * @param {String} buttonID ID del botón que dispara el evento para la función.
 */
function togglePasswordVisible(buttonID) {
    const btn = document.getElementById(buttonID);
    const inputID = btn.getAttribute('for');
    const input = document.getElementById(inputID);
    if (input.getAttribute('type') == 'password') {
        btn.classList.remove('fa-eye');
        btn.classList.add('fa-eye-slash');
        input.setAttribute('type', 'text');
    } else {
        btn.classList.remove('fa-eye-slash');
        btn.classList.add('fa-eye');
        input.setAttribute('type', 'password');
    }
}