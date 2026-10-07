package com.pockettrack.navegacao

import kotlinx.serialization.Serializable

// Rotas tipadas: cada destino é um tipo @Serializable, e os argumentos são propriedades.
// Passamos só ids nos argumentos; a tela busca os dados no estado elevado.
object Rotas {
    // Grafo aninhado de autenticação: entra e sai como uma unidade.
    @Serializable
    data object Autenticacao

    @Serializable
    data object Entrar

    @Serializable
    data object Cadastro

    // Destinos principais (barra de navegação inferior ou trilho lateral).
    @Serializable
    data object Transacoes

    @Serializable
    data object ResumoMensal

    @Serializable
    data object Categorias

    @Serializable
    data object Ajustes

    // Destinos com argumento.
    @Serializable
    data class DetalheTransacao(
        val id: Int,
    )

    @Serializable
    data object NovaTransacao

    @Serializable
    data class EditarTransacao(
        val id: Int,
    )
}

// Deep link do detalhe: pockettrack://transacao/{id}
// O mesmo esquema e host estão no intent-filter do AndroidManifest.xml.
const val BASE_DEEP_LINK = "pockettrack://transacao"
