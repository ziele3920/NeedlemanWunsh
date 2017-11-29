package pl.edu.ziele3920.needelmanwunsh

/**
 * Created by Zioło on 2017-11-13.
 */
class NeedelmanWunsh(
        private val horizontalSeq: String,
        private val vericalSeq: String,
        reward: Int?,
        indelPunish: Int?,
        mismatchDiagonalPunish: Int?) {

    private val reward: Int
    private val defaultReward: Int = 1
    private val indelPunish: Int
    private val defaultIndelPunish: Int = -1
    private val mismatchDiagonalPunish: Int
    private val defaultMismatchDiagonalPunish = 0

    init {
        this.reward = reward ?: this.defaultReward
        this.indelPunish = indelPunish ?: this.defaultIndelPunish
        this.mismatchDiagonalPunish = mismatchDiagonalPunish ?: this.defaultMismatchDiagonalPunish
    }

    private lateinit var resultTable: Array<IntArray>
    private lateinit var paths: Array<Array<MutableList<Pair<Pair<Int, Int>, Pair<String, String>>>>>
    private lateinit var alignments: MutableList<Pair<String, String>>
    private var score: Int = 0

    fun getResultStringArray(): Array<Array<String>> {
        resultTable = Array(vericalSeq.length+1, {IntArray(horizontalSeq.length+1)})
        paths = Array(vericalSeq.length+1, { Array(horizontalSeq.length+1, { mutableListOf<Pair<Pair<Int, Int>, Pair<String, String>>>()}) })

        for (i in 0..vericalSeq.length) {
            resultTable[i][0] = -i
            if(i > 0)
                paths[i][0].add(Pair(Pair(i-1, 0), Pair("", "")))
        }

        for (i in 0..horizontalSeq.length) {
            resultTable[0][i] = -i
            if(i > 0)
                paths[i][0].add(Pair(Pair(0, i-1), Pair("", "")))
        }

        for (i in 1..vericalSeq.length) {
            for (j in 1..horizontalSeq.length) {
                val diagonalPathScore = resultTable[i - 1][j - 1] + if (vericalSeq[i-1] == horizontalSeq[j-1]) reward else mismatchDiagonalPunish
                val verticalPathScore = resultTable[i - 1][j] + indelPunish
                val horizontalPathScore = resultTable[i][j - 1] + indelPunish
                val max = Math.max(diagonalPathScore, Math.max(verticalPathScore, horizontalPathScore))
                resultTable[i][j] = max

                if(diagonalPathScore == max)
                    paths[i][j].add(Pair(Pair(i-1, j-1), Pair(vericalSeq[i-1].toString(), horizontalSeq[j-1].toString())))
                if(verticalPathScore == max)
                    paths[i][j].add(Pair(Pair(i-1, j), Pair(vericalSeq[i-1].toString(), "_")))
                if(horizontalPathScore == max)
                    paths[i][j].add(Pair(Pair(i, j-1), Pair("_", horizontalSeq[j-1].toString())))
            }
        }
        generateAlignments()
        return generateStringArray(resultTable)
    }

    fun getAlignment(): MutableList<Pair<String, String>>{
        return alignments
    }

    fun getScore(): Int{
        return score
    }

    private fun generateAlignments() {
        alignments = mutableListOf()
        var max = Int.MIN_VALUE
        var maxX = 0
        var maxY = 0
        for(i in 0 until resultTable[0].size) {
            if (resultTable[resultTable.size - 1][i] >= max) {
                max = resultTable[resultTable.size - 1][i]
                maxX = resultTable.size - 1
                maxY = i
            }
        }
        for(i in 0 until resultTable.size) {
            if (resultTable[i][resultTable[0].size-1] >= max) {
                max = resultTable[i][resultTable[0].size-1]
                maxX = i
                maxY = resultTable[0].size-1
            }
        }
        for(stepCoord in paths[maxX][maxY]) {
            var verticalStringAlignment = StringBuilder(stepCoord.second.first)
            var horizontalStringAlignment = StringBuilder(stepCoord.second.second)
            stepPath(horizontalStringAlignment, verticalStringAlignment, stepCoord.first)
        }
    }

    private fun stepPath(horizontalStringAlignment: StringBuilder, verticalStringAlignment: StringBuilder, destinationCoords: Pair<Int, Int>) {
        if(paths[destinationCoords.first][destinationCoords.second].size == 0) {
            alignments.add(Pair(horizontalStringAlignment.reverse().toString(), verticalStringAlignment.reverse().toString()))
            return
        }
        for(stepCoord in paths[destinationCoords.first][destinationCoords.second]) {
            var verticalPathAlignemnt = StringBuilder(verticalStringAlignment).append(stepCoord.second.first)
            var horizontalPathAlignment = StringBuilder(horizontalStringAlignment).append(stepCoord.second.second)
            stepPath(horizontalPathAlignment, verticalPathAlignemnt, stepCoord.first)
        }
    }

    private fun generateStringArray(resultTable: Array<IntArray>): Array<Array<String>> {
        val stringArray: Array<Array<String>> = Array(vericalSeq.length+2, { Array(horizontalSeq.length+2, {""})})
        for(i in 0.until(vericalSeq.length))
            stringArray[i+2][0] = vericalSeq[i].toString()
        for(i in 0.until(horizontalSeq.length))
            stringArray[0][i+2] = horizontalSeq[i].toString()
        for (i in 1..vericalSeq.length+1)
            for (j in 1..horizontalSeq.length+1)
                stringArray[i][j] = resultTable[i-1][j-1].toString()
        return stringArray
    }

}