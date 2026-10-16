package com.pockettrack.modelo

private const val MESES_NO_ANO = 12
private val NOMES_MESES =
    listOf(
        "janeiro",
        "fevereiro",
        "março",
        "abril",
        "maio",
        "junho",
        "julho",
        "agosto",
        "setembro",
        "outubro",
        "novembro",
        "dezembro",
    )

// Data de calendário, sem fuso. Evita uma dependência de data/hora só para isso.
data class Data(
    val ano: Int,
    val mes: Int,
    val dia: Int,
) : Comparable<Data> {
    val mesDoAno: Mes get() = Mes(ano, mes)

    override fun compareTo(other: Data): Int = compareValuesBy(this, other, Data::ano, Data::mes, Data::dia)

    // dd/mm/aaaa
    fun formatada(): String = "${dia.doisDigitos()}/${mes.doisDigitos()}/$ano"

    fun emDiasEpoca(): Long = diasDesdeEpoca(ano, mes, dia)

    companion object {
        fun deDiasEpoca(dias: Long): Data = civilDeDias(dias)
    }
}

data class Mes(
    val ano: Int,
    val mes: Int,
) {
    fun anterior(): Mes = if (mes == 1) Mes(ano - 1, MESES_NO_ANO) else Mes(ano, mes - 1)

    fun proximo(): Mes = if (mes == MESES_NO_ANO) Mes(ano + 1, 1) else Mes(ano, mes + 1)

    fun nome(): String = "${NOMES_MESES[mes - 1]} de $ano"
}

private fun Int.doisDigitos(): String = toString().padStart(2, '0')

// Algoritmo de Howard Hinnant (days_from_civil / civil_from_days), o mesmo usado pelo
// DatePicker, que trabalha com milissegundos em UTC.
private const val DIAS_ERA = 146_097L
private const val ANOS_ERA = 400L
private const val DESLOCAMENTO_EPOCA = 719_468L

@Suppress("MagicNumber")
private fun diasDesdeEpoca(
    ano: Int,
    mes: Int,
    dia: Int,
): Long {
    val y = (if (mes <= 2) ano - 1 else ano).toLong()
    val era = (if (y >= 0) y else y - 399) / ANOS_ERA
    val yoe = y - era * ANOS_ERA
    val mp = (mes + 9) % 12
    val doy = (153 * mp + 2) / 5 + dia - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    return era * DIAS_ERA + doe - DESLOCAMENTO_EPOCA
}

@Suppress("MagicNumber")
private fun civilDeDias(dias: Long): Data {
    val z = dias + DESLOCAMENTO_EPOCA
    val era = (if (z >= 0) z else z - (DIAS_ERA - 1)) / DIAS_ERA
    val doe = z - era * DIAS_ERA
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146_096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val dia = (doy - (153 * mp + 2) / 5 + 1).toInt()
    val mes = (if (mp < 10) mp + 3 else mp - 9).toInt()
    val ano = (yoe + era * ANOS_ERA).toInt() + if (mes <= 2) 1 else 0
    return Data(ano, mes, dia)
}

const val MILIS_POR_DIA = 86_400_000L

// Data de hoje no fuso do aparelho. Cada plataforma sabe ler o relógio local.
expect fun hoje(): Data
