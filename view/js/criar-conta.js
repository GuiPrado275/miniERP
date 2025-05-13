function criarConta() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmarPassword = document.getElementById("confirmar-password").value;
    const errorMessage = document.getElementById("error-message");

    // Resetando mensagens de erro
    errorMessage.textContent = "";

    if (password !== confirmarPassword) {
        errorMessage.textContent = "As senhas não coincidem!";
        return;
    }

    const data = { email: email, password: password };

    fetch('http://localhost:8080/usuario', { // URL de criação de conta (ajuste conforme necessário)
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    // Mostra erros de validação do backend
                    errorMessage.textContent = "Erro: " + (data.errors ? data.errors.join(', ') : "Erro desconhecido");
                });
            }
            return response.json();
        })
        .then(data => {
            // Caso a conta seja criada com sucesso, redireciona para a página de login
            window.location.href = 'login.html'; // Ajuste a URL para a página de login
        })
        .catch(error => {
            errorMessage.textContent = "Erro ao criar conta. Tente novamente mais tarde.";
        });
}
