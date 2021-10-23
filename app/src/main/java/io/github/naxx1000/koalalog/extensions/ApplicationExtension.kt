package io.github.naxx1000.koalalog.extensions

import android.app.Application
import io.github.naxx1000.koalalog.KoalaLog

fun Application.koalaInit() {
    KoalaLog.init(baseContext)
}

fun Application.koalaCancel() {
    KoalaLog.cancel(baseContext)
}