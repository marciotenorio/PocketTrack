package com.pockettrack.modelo

// Modelos de dados do app. Nesta sprint os dados ficam em memória; persistência local e
// sincronização com o Firebase chegam na Sprint 2.

enum class TipoTransacao(
    val rotulo: String,
) {
    RECEITA("Receita"),
    DESPESA("Despesa"),
}

data class Categoria(
    val id: Int,
    val nome: String,
)

// Valor em centavos para não acumular erro de ponto flutuante em dinheiro.
data class Transacao(
    val id: Int,
    val descricao: String,
    val valorCentavos: Long,
    val tipo: TipoTransacao,
    val categoriaId: Int,
    val data: Data,
)

data class Usuario(
    val nome: String,
    val email: String,
)

enum class PreferenciaTema(
    val rotulo: String,
) {
    SISTEMA("Seguir o sistema"),
    CLARO("Claro"),
    ESCURO("Escuro"),
}

data class Preferencias(
    val tema: PreferenciaTema = PreferenciaTema.SISTEMA,
    val lembreteAtivo: Boolean = false,
    val horarioLembrete: String = HORARIOS_LEMBRETE.first(),
)

val HORARIOS_LEMBRETE = listOf("08:00", "12:00", "20:00", "22:00")

// Resumo de um mês: totais e despesas agrupadas por categoria.
data class Resumo(
    val receitasCentavos: Long,
    val despesasCentavos: Long,
    val despesasPorCategoria: List<TotalCategoria>,
) {
    val saldoCentavos: Long get() = receitasCentavos - despesasCentavos
}

data class TotalCategoria(
    val categoria: Categoria,
    val totalCentavos: Long,
    val fracao: Float,
)

// Todo o estado do app, elevado acima da navegação: todas as telas enxergam os mesmos dados.
data class DadosApp(
    val usuario: Usuario? = null,
    val transacoes: List<Transacao> = transacoesIniciais,
    val categorias: List<Categoria> = categoriasIniciais,
    val preferencias: Preferencias = Preferencias(),
) {
    fun categoria(id: Int): Categoria? = categorias.find { it.id == id }

    fun transacao(id: Int): Transacao? = transacoes.find { it.id == id }

    fun nomeCategoria(id: Int): String = categoria(id)?.nome ?: "Sem categoria"
}
