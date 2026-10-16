package com.pockettrack.modelo

private const val TAMANHO_MINIMO_SENHA = 6
private val FORMATO_EMAIL = Regex("""^[^@\s]+@[^@\s]+\.[^@\s]+$""")

data class FormularioEntrada(
    val email: String = "",
    val senha: String = "",
)

data class ErrosEntrada(
    val email: String? = null,
    val senha: String? = null,
) {
    val valido: Boolean get() = email == null && senha == null
}

fun emailValido(email: String): Boolean = FORMATO_EMAIL.matches(email.trim())

fun validarEntrada(formulario: FormularioEntrada): ErrosEntrada =
    ErrosEntrada(
        email =
            when {
                formulario.email.isBlank() -> "Informe o e-mail"
                !emailValido(formulario.email) -> "E-mail inválido"
                else -> null
            },
        senha = if (formulario.senha.isEmpty()) "Informe a senha" else null,
    )

data class FormularioCadastro(
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val confirmacao: String = "",
)

data class ErrosCadastro(
    val nome: String? = null,
    val email: String? = null,
    val senha: String? = null,
    val confirmacao: String? = null,
) {
    val valido: Boolean get() = listOf(nome, email, senha, confirmacao).all { it == null }
}

fun validarCadastro(formulario: FormularioCadastro): ErrosCadastro =
    ErrosCadastro(
        nome = if (formulario.nome.isBlank()) "Informe seu nome" else null,
        email =
            when {
                formulario.email.isBlank() -> "Informe o e-mail"
                !emailValido(formulario.email) -> "E-mail inválido"
                else -> null
            },
        senha =
            if (formulario.senha.length < TAMANHO_MINIMO_SENHA) {
                "A senha precisa de pelo menos $TAMANHO_MINIMO_SENHA caracteres"
            } else {
                null
            },
        confirmacao = if (formulario.confirmacao != formulario.senha) "As senhas não conferem" else null,
    )

// Até a Sprint 2 (Firebase Authentication), o nome exibido vem da parte local do e-mail.
fun usuarioDoEmail(email: String): Usuario = Usuario(nome = email.trim().substringBefore('@'), email = email.trim())
