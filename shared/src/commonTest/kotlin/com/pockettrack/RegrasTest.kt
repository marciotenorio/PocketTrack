package com.pockettrack

import com.pockettrack.modelo.Categoria
import com.pockettrack.modelo.Data
import com.pockettrack.modelo.FormularioCadastro
import com.pockettrack.modelo.FormularioTransacao
import com.pockettrack.modelo.Mes
import com.pockettrack.modelo.TipoTransacao
import com.pockettrack.modelo.Transacao
import com.pockettrack.modelo.formatarMoeda
import com.pockettrack.modelo.interpretarValor
import com.pockettrack.modelo.paraFormulario
import com.pockettrack.modelo.paraTransacao
import com.pockettrack.modelo.resumoDoMes
import com.pockettrack.modelo.salvando
import com.pockettrack.modelo.validarCadastro
import com.pockettrack.modelo.validarNomeCategoria
import com.pockettrack.modelo.validarTransacao
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RegrasTest {
    private val outubro = Data(2026, 10, 7)
    private val categorias = listOf(Categoria(1, "Alimentação"), Categoria(2, "Salário"))

    @Test
    fun interpretaValoresComVirgulaOuPonto() {
        assertEquals(1250L, interpretarValor("12,50"))
        assertEquals(1250L, interpretarValor("12.5"))
        assertEquals(1200L, interpretarValor(" 12 "))
        assertNull(interpretarValor("12,345"))
        assertNull(interpretarValor("abc"))
        assertNull(interpretarValor(""))
    }

    @Test
    fun formataMoedaEmReais() {
        assertEquals("R$ 0,05", formatarMoeda(5))
        assertEquals("R$ 1.234,56", formatarMoeda(123_456))
        assertEquals("-R$ 10,50", formatarMoeda(-1_050))
    }

    @Test
    fun dataIdaEVoltaPelosDiasDaEpoca() {
        assertEquals(0L, Data(1970, 1, 1).emDiasEpoca())
        listOf(Data(2026, 10, 7), Data(2024, 2, 29), Data(1999, 12, 31)).forEach {
            assertEquals(it, Data.deDiasEpoca(it.emDiasEpoca()))
        }
    }

    @Test
    fun mesAnteriorEProximoViramOAno() {
        assertEquals(Mes(2025, 12), Mes(2026, 1).anterior())
        assertEquals(Mes(2027, 1), Mes(2026, 12).proximo())
    }

    @Test
    fun formularioDeTransacaoSoEhValidoCompleto() {
        val vazio = FormularioTransacao(data = outubro)
        val erros = validarTransacao(vazio)
        assertFalse(erros.valido)
        assertNotNull(erros.descricao)
        assertNotNull(erros.valor)
        assertNotNull(erros.categoria)
        assertNull(vazio.paraTransacao(1))

        val completo = vazio.copy(descricao = " Mercado ", valor = "45,90", categoriaId = 1)
        assertTrue(validarTransacao(completo).valido)
        assertEquals(
            Transacao(1, "Mercado", 4_590, TipoTransacao.DESPESA, 1, outubro),
            completo.paraTransacao(1),
        )
    }

    @Test
    fun valorZeroEhInvalido() {
        val formulario = FormularioTransacao(descricao = "x", valor = "0", categoriaId = 1, data = outubro)
        assertNotNull(validarTransacao(formulario).valor)
    }

    @Test
    fun edicaoVoltaAoMesmoFormulario() {
        val transacao = Transacao(3, "Ônibus", 2_250, TipoTransacao.DESPESA, 1, outubro)
        assertEquals(transacao, transacao.paraFormulario().paraTransacao(3))
    }

    @Test
    fun salvarSubstituiPeloId() {
        val original = Transacao(1, "A", 100, TipoTransacao.DESPESA, 1, outubro)
        val editada = original.copy(descricao = "B")
        assertEquals(listOf(editada), listOf(original).salvando(editada))
    }

    @Test
    fun resumoSomaSoOMesPedido() {
        val transacoes =
            listOf(
                Transacao(1, "Salário", 300_000, TipoTransacao.RECEITA, 2, outubro),
                Transacao(2, "Mercado", 50_000, TipoTransacao.DESPESA, 1, outubro),
                Transacao(3, "Mercado", 10_000, TipoTransacao.DESPESA, 1, Data(2026, 9, 30)),
            )
        val resumo = resumoDoMes(transacoes, categorias, Mes(2026, 10))
        assertEquals(300_000L, resumo.receitasCentavos)
        assertEquals(50_000L, resumo.despesasCentavos)
        assertEquals(250_000L, resumo.saldoCentavos)
        assertEquals(1f, resumo.despesasPorCategoria.single().fracao)
    }

    @Test
    fun nomeDeCategoriaRepetidoOuVazioEhRecusado() {
        assertNotNull(validarNomeCategoria("  ", categorias))
        assertNotNull(validarNomeCategoria("alimentação", categorias))
        assertNull(validarNomeCategoria("Lazer", categorias))
    }

    @Test
    fun cadastroExigeSenhasIguais() {
        val formulario = FormularioCadastro("Ana", "ana@exemplo.com", "segredo", "segredo")
        assertTrue(validarCadastro(formulario).valido)
        assertNotNull(validarCadastro(formulario.copy(confirmacao = "outra")).confirmacao)
        assertNotNull(validarCadastro(formulario.copy(email = "ana@")).email)
    }
}
