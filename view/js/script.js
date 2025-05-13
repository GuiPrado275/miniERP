function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("error-message");
    const criarContaBtn = document.getElementById("criar-conta-btn");

    // Resetando as mensagens de erro
    errorMessage.textContent = "";

    const data = { email: email, password: password };

    fetch('http://localhost:8080/login', { // URL de login (ajuste conforme necessário)
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Credenciais inválidas');
            }
            return response.json();
        })
        .then(data => {
            // Se o login for bem-sucedido, redireciona para o dashboard
            window.location.href = 'dashboard.html'; // Ajuste a URL para a página do dashboard
        })
        .catch(error => {
            errorMessage.textContent = error.message; // Exibe a mensagem de erro
            criarContaBtn.style.display = 'inline'; // Exibe o botão para criar conta
        });
}
