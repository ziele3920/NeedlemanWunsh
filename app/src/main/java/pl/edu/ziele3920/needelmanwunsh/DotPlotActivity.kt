package pl.edu.ziele3920.needelmanwunsh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import kotlinx.android.synthetic.main.activity_dot_plot.*


class DotPlotActivity : AppCompatActivity() {

    private var firstSeq: String = ""
    private var secondSeq: String = ""
    private lateinit var dotPlotArray: Array<Array<String>>
    private lateinit var row: Array<TableRow>
    private lateinit var textColumn: Array<Array<TextView>>
    private lateinit var nw: NeedelmanWunsh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dot_plot)
        firstSeq = intent.getStringExtra("firstSeq")
        secondSeq = intent.getStringExtra("secondSeq")
        nw = NeedelmanWunsh(firstSeq, secondSeq);
        val table: Array<Array<String>> = nw.getResultStringArray()
        drawTable(table)
    }

    fun drawTable(table: Array<Array<String>>) {
        val tlayout = tabLaay
        val llayout = linearLayoutDot
        dotPlotArray = table

        tlayout.gravity = Gravity.CENTER
        llayout.gravity = Gravity.CENTER

        tlayout.setBackgroundResource(R.color.background_material_dark)
        llayout.setBackgroundResource(R.color.background_material_dark)

        row = Array(table.size, {TableRow(this.applicationContext)})
        textColumn = Array(row.size, {Array(table[0].size, { TextView(this.applicationContext) })})
        for(r in 0.until(row.size)) {
            for (c in 0.until(textColumn[r].size)) {
                textColumn[r][c].setText(table[r][c])
                row[r].addView(textColumn[r][c])
            }
            tlayout.addView(row[r])
        }
        var rowA1 = TableLayout(this.applicationContext)
        var rowA2 = TableLayout(this.applicationContext)
        var textA1 = TextView(this.applicationContext)
        var textA2 = TextView(this.applicationContext)
        var alignments = nw.getAlignment()
        textA1.setText(alignments[0])
        textA2.setText(alignments[1])
        rowA1.addView(textA1)
        rowA2.addView(textA2)
        tlayout.addView(rowA1)
        tlayout.addView(rowA2)
    }
}