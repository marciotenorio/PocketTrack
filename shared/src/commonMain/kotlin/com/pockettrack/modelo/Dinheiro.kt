package com.pockettrack.modelo

// Regras de dinheiro como funções puras: sem Compose, testáveis com kotlin.test.

private const val CENTAVOS_POR_REAL = 100
private const val DIGITOS_POR_GRUPO = 3
private const val MAXIMO_DIGITOS_REAIS = 9
private val FORMATO_VALOR = Regex("""^\d{1,$MAXIMO_DIGITOS_REAIS}([.,]\d{1,2})?$""")

// Aceita "12", "12,5", "12,50" ou "12.50". Devolve o valor em centavos, ou null se inválido.
fun interpretarValor(texto: String): Long? {
    val limpo = texto.trim()
    if (!FORMATO_VALOR.matches(limpo)) return null
    val partes = limpo.split(',', '.')
    val reais = partes[0].toLong()
    val centavos = partes.getOrNull(1)?.padEnd(2, '0')?.toLong() ?: 0L
    return reais * CENTAVOS_POR_REAL + centavos
}

// 123456 -> "R$ 1.234,56"; -1050 -> "-R$ 10,50"
fun formatarMoeda(centavos: Long): String {
    val sinal = if (centavos < 0) "-" else ""
    val absoluto = if (centavos < 0) -centavos else centavos
    val reais = (absoluto / CENTAVOS_POR_REAL).toString()
    val resto = (absoluto % CENTAVOS_POR_REAL).toString().padStart(2, '0')
    val agrupado =
        reais
            .reversed()
            .chunked(DIGITOS_POR_GRUPO)
            .joinToString(".")
            .reversed()
    return "${sinal}R$ $agrupado,$resto"
}

// Converte centavos de volta para o texto do campo de valor: 1250 -> "12,50".
fun valorParaCampo(centavos: Long): String {
    val resto = (centavos % CENTAVOS_POR_REAL).toString().padStart(2, '0')
    return "${centavos / CENTAVOS_POR_REAL},$resto"
}
