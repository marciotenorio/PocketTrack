package com.pockettrack.modelo

private const val TAMANHO_MAXIMO_CATEGORIA = 30

fun validarNomeCategoria(
    nome: String,
    existentes: List<Categoria>,
): String? {
    val limpo = nome.trim()
    return when {
        limpo.isEmpty() -> "Informe um nome"
        limpo.length > TAMANHO_MAXIMO_CATEGORIA -> "Use até $TAMANHO_MAXIMO_CATEGORIA caracteres"
        existentes.any { it.nome.equals(limpo, ignoreCase = true) } -> "Já existe uma categoria com esse nome"
        else -> null
    }
}

fun List<Categoria>.comNova(nome: String): List<Categoria> =
    this + Categoria((maxOfOrNull { it.id } ?: 0) + 1, nome.trim())

fun List<Categoria>.semCategoria(id: Int): List<Categoria> = filterNot { it.id == id }

// Uma categoria só pode ser removida se nenhuma transação a usa.
fun categoriasEmUso(transacoes: List<Transacao>): Set<Int> = transacoes.mapTo(mutableSetOf()) { it.categoriaId }
