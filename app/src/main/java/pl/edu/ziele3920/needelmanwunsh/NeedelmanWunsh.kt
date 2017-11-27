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
    private lateinit var resultSequences: Array<String>
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
                var d =7
                val c = 4
                if(max == 3)
                    d +=c
                if(diagonalPathScore == max)
                    paths[i][j].add(Pair(Pair(i-1, j-1), Pair(vericalSeq[i-1].toString(), horizontalSeq[j-1].toString())))
                if(verticalPathScore == max)
                    paths[i][j].add(Pair(Pair(i-1, j), Pair(vericalSeq[i-1].toString(), "_")))
                if(horizontalPathScore == max)
                    paths[i][j].add(Pair(Pair(i, j-1), Pair("_", horizontalSeq[j-1].toString())))
            }
        }
        calculateResults()
        return generateStringArray(resultTable)
    }

    fun getAlignment(): Array<String>{
        return resultSequences
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
        horizontalStringAlignment.append(horizontalSeq[destinationCoords.second-1])
        verticalStringAlignment.append(vericalSeq[destinationCoords.first-1])
        if(paths[destinationCoords.first][destinationCoords.second].size == 0) {
            alignments.add(Pair(horizontalStringAlignment.toString(), verticalStringAlignment.toString()))
            return
        }
        //for(stepCoord in paths[destinationCoords.first][destinationCoords.second]) {
        //    if(horizontalSeq[sourceCoords.second-1] == )
        //}
    }

    private fun calculateResults() {
        resultSequences = Array(2, {""})
        var max = Int.MIN_VALUE
        var maxX = 0
        var maxY = 0
        for(i in 0..resultTable[0].size-1) {
            if (resultTable[resultTable.size - 1][i] >= max) {
                max = resultTable[resultTable.size - 1][i]
                maxX = resultTable.size - 1
                maxY = i
            }
        }
        for(i in 0..resultTable.size-1) {
            if (resultTable[i][resultTable[0].size-1] >= max) {
                max = resultTable[i][resultTable[0].size-1]
                maxX = i
                maxY = resultTable[0].size-1
            }
        }
        score = max
        while(maxX > 0 || maxY > 0) {
            if (maxX > 0 && maxY > 0 && resultTable[maxX][maxY] == resultTable[maxX - 1][maxY - 1] + reward) {
                resultSequences[0] = horizontalSeq[maxY-1] + resultSequences[0]
                resultSequences[1] = vericalSeq[maxX-1] + resultSequences[1]
                --maxX
                --maxY
            }
            else if (maxX > 0 && maxY > 0 && resultTable[maxX][maxY] == resultTable[maxX - 1][maxY - 1] + mismatchDiagonalPunish && resultTable[maxX-1][maxY-1] >= Math.max(resultTable[maxX-1][maxY], resultTable[maxX][maxY-1]) ) {
                resultSequences[0] = horizontalSeq[maxY-1] + resultSequences[0]
                resultSequences[1] = vericalSeq[maxX-1] + resultSequences[1]
                --maxX
                --maxY
            }

            else if(maxX > 0 && resultTable[maxX][maxY] == resultTable[maxX-1][maxY] + indelPunish) {
                resultSequences[0] = "_" + resultSequences[0]
                resultSequences[1] = vericalSeq[maxX-1] + resultSequences[1]
                --maxX
            }
            else {
                resultSequences[0] = horizontalSeq[maxY - 1] + resultSequences[0]
                resultSequences[1] = "_" + resultSequences[1]
                --maxY
            }
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