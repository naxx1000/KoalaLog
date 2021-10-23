package io.github.naxx1000.koalalog.extensions

import androidx.fragment.app.Fragment
import io.github.naxx1000.koalalog.KoalaLog

fun Fragment.koalaLog() {
    KoalaLog.log(requireContext(), this.javaClass.simpleName)
}