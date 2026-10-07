package com.pockettrack.modelo

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate

actual fun hoje(): Data {
    val componentes =
        NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = NSDate(),
        )
    return Data(componentes.year.toInt(), componentes.month.toInt(), componentes.day.toInt())
}
