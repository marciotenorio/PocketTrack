package com.pockettrack.modelo

// Dados de exemplo para as telas e os @Preview enquanto não há persistência (Sprint 2).

val categoriasIniciais =
    listOf(
        Categoria(1, "Alimentação"),
        Categoria(2, "Transporte"),
        Categoria(3, "Moradia"),
        Categoria(4, "Lazer"),
        Categoria(5, "Salário"),
    )

val transacoesIniciais =
    listOf(
        Transacao(1, "Salário de outubro", 450_000, TipoTransacao.RECEITA, 5, Data(2026, 10, 1)),
        Transacao(2, "Aluguel", 120_000, TipoTransacao.DESPESA, 3, Data(2026, 10, 5)),
        Transacao(3, "Supermercado", 38_790, TipoTransacao.DESPESA, 1, Data(2026, 10, 6)),
        Transacao(4, "Ônibus", 2_250, TipoTransacao.DESPESA, 2, Data(2026, 10, 6)),
        Transacao(5, "Cinema", 6_000, TipoTransacao.DESPESA, 4, Data(2026, 9, 27)),
        Transacao(6, "Salário de setembro", 450_000, TipoTransacao.RECEITA, 5, Data(2026, 9, 1)),
    )
