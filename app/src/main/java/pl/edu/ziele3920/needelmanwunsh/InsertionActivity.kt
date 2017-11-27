package pl.edu.ziele3920.needelmanwunsh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_insertion.*


class InsertionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)
        buttonCalculate.setOnClickListener {
            val intent = Intent(this, DotPlotActivity::class.java)
            intent.putExtra("firstSeq", editTextFiirstSeq.text.toString() )
            intent.putExtra("secondSeq", editTextSecondSeq.text.toString())
            intent.putExtra("reward",  editTextReward.text.toString())
            intent.putExtra("indelPunish", editTextIndelPunish.text.toString())
            intent.putExtra("mismatchPunish", editTextMismatchPunish.text.toString())
            startActivity(intent)
        }
    }
}
