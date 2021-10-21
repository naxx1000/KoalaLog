package dk.jdc.koalalog.extensions

import androidx.fragment.app.Fragment
import dk.jdc.koalalog.KoalaLog

fun Fragment.koalaLog() {
    KoalaLog.log(requireContext(), this.javaClass.simpleName)
}