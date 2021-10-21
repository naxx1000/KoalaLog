package dk.jdc.koalalog

import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed

class LogActivity : AppCompatActivity() {

    private lateinit var logTextView: TextView
    private lateinit var logScrollView: ScrollView
    private lateinit var btnClear: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        logTextView = findViewById(R.id.tv_log)
        logScrollView = findViewById(R.id.sv_log)
        btnClear = findViewById(R.id.btn_clear)

        getLog()

        logScrollView.post {
            logScrollView.fullScroll(View.FOCUS_DOWN)
        }

        btnClear.setOnClickListener {
            Runtime.getRuntime().exec("logcat -b all -c")
            logTextView.text = ""
            btnClear.postDelayed(500) {
                getLog()
            }
        }
    }

    private fun getLog() {
        val errorColorHex =
            String.format("#%06X", (0xFFFFFF and baseContext.getColor(R.color.red300)))
        val pid = intent.getIntExtra("pid", -1)
        val tag = intent.getStringExtra("tag")
        val showFullLog = intent.getBooleanExtra("full_log", false)

        val command = if (tag == "FATAL") {
            "logcat *:E -v time -d --pid=$pid"
        } else {
            "logcat ${if (!showFullLog) "-s $tag:D ${tag?.replace("Fragment", "ViewModel")}:D" else ""} -v time -d --pid=$pid"
//            "logcat ${if (!showFullLog) "$tag:D *:S" else ""} -v time -d --pid=$pid"
        }

        val process =
            Runtime.getRuntime().exec(command)
        val lines = process.inputStream.bufferedReader().readLines()
        val log = StringBuilder()

        lines.forEachIndexed { i, line ->
            if (line.contains("E/AndroidRuntime")) {
                println("should be error")
                log.append("<font color=$errorColorHex>$line</font>${if (i != lines.size - 1) "<br>" else ""}")
            } else {
                log.append("$line${if (i != lines.size - 1) "<br>" else ""}")
            }
        }

        logTextView.movementMethod = ScrollingMovementMethod()
        logTextView.text = Html.fromHtml(log.toString(), Html.FROM_HTML_MODE_COMPACT)
    }
}