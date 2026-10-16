package com.pockettrack.modelo

import java.util.Calendar

// java.util.Calendar em vez de java.time: funciona desde o minSdk 24 sem desugaring.
actual fun hoje(): Data {
    val agora = Calendar.getInstance()
    return Data(agora.get(Calendar.YEAR), agora.get(Calendar.MONTH) + 1, agora.get(Calendar.DAY_OF_MONTH))
}
