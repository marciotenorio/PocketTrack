package com.pockettrack.modelo

// Regras do formulário, da lista e do resumo de transações, como funções puras.

private const val TAMANHO_MAXIMO_DESCRICAO = 60

data class FormularioTransacao(
    val tipo: TipoTransacao = TipoTransacao.DESPESA,
    val descricao: String = "",
    val valor: String = "",
    val categoriaId: Int? = null,
    val data: Data,
)

data class ErrosTransacao(
    val descricao: String? = null,
    val valor: String? = null,
    val categoria: String? = null,
) {
    val valido: Boolean get() = descricao == null && valor == null && categoria == null
}

fun validarTransacao(formulario: FormularioTransacao): ErrosTransacao {
    val descricao = formulario.descricao.trim()
    val valor = interpretarValor(formulario.valor)
    return ErrosTransacao(
        descricao =
            when {
                descricao.isEmpty() -> "Informe uma descrição"
                descricao.length > TAMANHO_MAXIMO_DESCRICAO -> "Use até $TAMANHO_MAXIMO_DESCRICAO caracteres"
                else -> null
            },
        valor =
            when {
                formulario.valor.isBlank() -> "Informe o valor"
                valor == null -> "Use só números, com até dois decimais (ex.: 12,50)"
                valor == 0L -> "O valor precisa ser maior que zero"
                else -> null
            },
        categoria = if (formulario.categoriaId == null) "Escolha uma categoria" else null,
    )
}

// Converte o formulário em transação, ou null se ainda houver erro.
fun FormularioTransacao.paraTransacao(id: Int): Transacao? {
    val valorCentavos = interpretarValor(valor)
    val categoria = categoriaId
    return if (validarTransacao(this).valido && valorCentavos != null && categoria != null) {
        Transacao(id, descricao.trim(), valorCentavos, tipo, categoria, data)
    } else {
        null
    }
}

fun Transacao.paraFormulario(): FormularioTransacao =
    FormularioTransacao(
        tipo = tipo,
        descricao = descricao,
        valor = valorParaCampo(valorCentavos),
        categoriaId = categoriaId,
        data = data,
    )

fun List<Transacao>.proximoId(): Int = (maxOfOrNull { it.id } ?: 0) + 1

// Insere ou substitui pelo id.
fun List<Transacao>.salvando(transacao: Transacao): List<Transacao> =
    if (any { it.id == transacao.id }) {
        map { if (it.id == transacao.id) transacao else it }
    } else {
        this + transacao
    }

fun List<Transacao>.removendo(id: Int): List<Transacao> = filterNot { it.id == id }

// Mais recentes primeiro; no mesmo dia, a última registrada primeiro.
fun List<Transacao>.ordenadasPorData(): List<Transacao> =
    sortedWith(
        compareByDescending<Transacao> {
            it.data
        }.thenByDescending { it.id },
    )

fun List<Transacao>.doMes(mes: Mes): List<Transacao> = filter { it.data.mesDoAno == mes }

fun resumoDoMes(
    transacoes: List<Transacao>,
    categorias: List<Categoria>,
    mes: Mes,
): Resumo {
    val doMes = transacoes.doMes(mes)
    val receitas = doMes.filter { it.tipo == TipoTransacao.RECEITA }.sumOf { it.valorCentavos }
    val despesas = doMes.filter { it.tipo == TipoTransacao.DESPESA }
    val totalDespesas = despesas.sumOf { it.valorCentavos }
    val porCategoria =
        despesas
            .groupBy { it.categoriaId }
            .mapNotNull { (categoriaId, itens) ->
                val categoria = categorias.find { it.id == categoriaId } ?: return@mapNotNull null
                val total = itens.sumOf { it.valorCentavos }
                TotalCategoria(categoria, total, if (totalDespesas == 0L) 0f else total.toFloat() / totalDespesas)
            }.sortedByDescending { it.totalCentavos }
    return Resumo(receitas, totalDespesas, porCategoria)
}
