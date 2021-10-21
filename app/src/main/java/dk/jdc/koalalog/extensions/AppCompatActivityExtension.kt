package dk.jdc.koalalog.extensions

import androidx.appcompat.app.AppCompatActivity
import dk.jdc.koalalog.KoalaLog

fun AppCompatActivity.koalaInit() {
    KoalaLog.init(baseContext)
}

fun AppCompatActivity.koalaCancel() {
    KoalaLog.cancel(baseContext)
}

fun AppCompatActivity.koalaLog() {
    KoalaLog.log(baseContext, this.javaClass.simpleName)
}