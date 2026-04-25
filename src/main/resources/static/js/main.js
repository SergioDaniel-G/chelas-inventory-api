document.addEventListener("DOMContentLoaded", function() {
    console.log("Sistema ERP Micheladas - Scripts cargados correctamente");

    actualizarGranTotal();

    const eyeButtons = document.querySelectorAll('#togglePassword, .eye-btn');

    eyeButtons.forEach(btn => {
        btn.addEventListener('click', function() {

            const passwordInput = this.parentElement.querySelector('input');

            if (passwordInput) {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);

                this.style.color = type === 'text' ? '#3b82f6' : '#64748b';
                this.style.opacity = type === 'text' ? '0.7' : '1';
            }
        });
    });

    // 3. LÓGICA DE CERRAR SESIÓN
    const logoutLink = document.querySelector('.navbar-link[href*="logout"], .btn-logout');

    if (logoutLink) {
        logoutLink.addEventListener('click', function(event) {
            event.preventDefault();
            const confirmacion = confirm(
                "SISTEMA DE GESTIÓN ERP - MICHELADAS\n\n" +
                "¿Está seguro de que desea cerrar su sesión actual?\n" +
                "Asegúrese de haber finalizado sus procesos pendientes."
            );
            if (confirmacion) {
                window.location.href = this.getAttribute('href');
            }
        });
    }
});

// --- FUNCIONES DE TABLA ---

function buscarEnTabla() {
    const input = document.querySelector('input[name="keyword"]');
    if (!input) return;

    const filtro = input.value.trim().toLowerCase();
    if (filtro === "") {
        alert("Por favor, ingresa un dato en el recuadro para buscar.");
        input.focus();
        return;
    }

    const filas = document.querySelectorAll('#table2 tbody tr');
    filas.forEach(fila => {
        const celdaMarca = fila.getElementsByTagName('td')[1];
        if (celdaMarca) {
            const texto = celdaMarca.textContent || celdaMarca.innerText;
            fila.style.display = texto.toLowerCase().includes(filtro) ? "" : "none";
        }
    });
    actualizarGranTotal();
}

function actualizarGranTotal() {
    let suma = 0;
    const filas = document.querySelectorAll('#table2 tbody tr');

    filas.forEach(fila => {
        if (fila.style.display !== "none") {
            const celdaTotal = fila.querySelector('td:nth-child(5)');
            if (celdaTotal) {
                let texto = celdaTotal.textContent.replace('$', '').replace(',', '').trim();
                let valor = parseFloat(texto);
                if (!isNaN(valor)) suma += valor;
            }
        }
    });

    const elementoTotal = document.getElementById('sumaTotal');
    if (elementoTotal) elementoTotal.textContent = '$ ' + suma.toFixed(2);
}

function limpiarCampos() {
    const input = document.querySelector('input[name="keyword"]');
    if (!input || input.value.trim() === "") {
        alert("El recuadro ya está vacío.");
        return;
    }
    input.value = "";
    mostrarDatos();
}

function ocultarDatos() {
    document.querySelectorAll('#table2 tbody tr').forEach(f => f.style.display = "none");
    actualizarGranTotal();
}

function mostrarDatos() {
    document.querySelectorAll('#table2 tbody tr').forEach(f => f.style.display = "");
    actualizarGranTotal();
}