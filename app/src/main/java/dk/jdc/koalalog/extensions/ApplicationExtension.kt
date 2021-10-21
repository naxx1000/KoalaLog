package dk.jdc.koalalog.extensions

import android.app.Application
import dk.jdc.koalalog.KoalaLog

fun Application.koalaInit() {
    KoalaLog.init(baseContext)
}

fun Application.koalaCancel() {
    KoalaLog.cancel(baseContext)
}