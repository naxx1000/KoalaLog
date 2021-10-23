package io.github.naxx1000.koalalog.extensions

import androidx.appcompat.app.AppCompatActivity
import io.github.naxx1000.koalalog.KoalaLog

fun AppCompatActivity.koalaInit() {
    KoalaLog.init(baseContext)
}

fun AppCompatActivity.koalaCancel() {
    KoalaLog.cancel(baseContext)
}

fun AppCompatActivity.koalaLog() {
    KoalaLog.log(baseContext, this.javaClass.simpleName)
}