package pl.edu.ziele3920.needelmanwunsh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import android.widget.ArrayAdapter
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
        val reward: Int? = intent.getStringExtra("reward").toIntOrNull()
        val indelPunish: Int? = intent.getStringExtra("indelPunish").toIntOrNull()
        val mismatchPunish: Int? = intent.getStringExtra("mismatchPunish").toIntOrNull()
        nw = NeedelmanWunsh(firstSeq, secondSeq, reward, indelPunish, mismatchPunish);
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
                textColumn[r][c].text = " " + table[r][c] + " "
                row[r].addView(textColumn[r][c])
            }
            tlayout.addView(row[r])
        }


        var alignments = nw.getAlignment()
        var textAlignments =  ArrayList<String>()

        alignments.mapTo(textAlignments) { it.first + "\n" + it.second + "   Score " + it.third}

        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1 , textAlignments)
        listViewAlignments.adapter = adapter

    }
}