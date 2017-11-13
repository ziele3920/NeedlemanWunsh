package pl.edu.ziele3920.needelmanwunsh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_insertion.*


class DotPlotActivity : AppCompatActivity() {

    private var firstSeq: String = ""
    private var secondSeq: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dot_plot)
        firstSeq = intent.getStringExtra("firstSeq")
        secondSeq = intent.getStringExtra("secondSeq")
        drawTable()
    }

    fun drawTable() {
        var row = TableRow(this.applicationContext)
        val tlayout = TableLayout(this.applicationContext)

        tlayout.gravity = Gravity.CENTER
        tlayout.setBackgroundResource(R.color.background_material_dark)

        row = TableRow(this)

        var text1 = TextView(this.applicationContext)
        var text2 = TextView(this.applicationContext)
        var text3 = TextView(this.applicationContext)

        text1.setText("t1")
        row.addView(text1)
        text2.setText("t2")
        row.addView(text2)
        text3.setText("t3")
        row.addView(text3)
        tlayout.addView(row)
        var i = 0
        while (i < 10) {
            row = TableRow(this.applicationContext)
            text1 = TextView(this.applicationContext)
            text2 = TextView(this.applicationContext)
            text3 = TextView(this.applicationContext)


            text1.setText("d" + i)
            row.addView(text1)
            text2.text = "du" + i
            row.addView(text2)
            text3.text = "dup" + i
            row.addView(text3)
            tlayout.addView(row)
            i++
        }
    }
}
