package pl.edu.ziele3920.needelmanwunsh

/**
 * Created by Zioło on 2017-11-13.
 */
class NeedelmanWunsh(private val firstSeq: String, private val secondSeq: String) {

    private val reward: Int = 1
    private val indelPunish: Int = -1
    private val mismatchDiagonalPunish: Int = 0
    private lateinit var resultTable: Array<IntArray>
    private lateinit var resultSequences: Array<String>
    private var score: Int = 0

    fun getResultStringArray(): Array<Array<String>> {
        resultTable = Array(secondSeq.length+1, {IntArray(firstSeq.length+1)})


        for (i in 0..secondSeq.length)
            resultTable[i][0] = -i

        for (i in 0..firstSeq.length)
            resultTable[0][i] = -i
        for (i in 1..secondSeq.length) {
            for (j in 1..firstSeq.length) {
                if (secondSeq[i-1] == firstSeq[j-1])
                    resultTable[i][j] = resultTable[i - 1][j - 1] + reward
                else
                    resultTable[i][j] = resultTable[i - 1][j - 1] + mismatchDiagonalPunish
                    resultTable[i][j] = Math.max(Math.max(resultTable[i - 1][j], resultTable[i][j - 1]) + indelPunish, resultTable[i][j])
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
        while(maxX > 0 || maxY > 0) {
            if (maxX > 0 && maxY > 0 && resultTable[maxX][maxY] == resultTable[maxX - 1][maxY - 1] + reward) {
                resultSequences[0] = firstSeq[maxY-1] + resultSequences[0]
                resultSequences[1] = secondSeq[maxX-1] + resultSequences[1]
                --maxX
                --maxY
            }
            else if (maxX > 0 && maxY > 0 && resultTable[maxX][maxY] == resultTable[maxX - 1][maxY - 1] + indelPunish) {
                resultSequences[0] = firstSeq[maxY-1] + resultSequences[0]
                resultSequences[1] = secondSeq[maxX-1] + resultSequences[1]
                --maxX
                --maxY
            }

            else if(maxX > 0 && resultTable[maxX][maxY] == resultTable[maxX-1][maxY] + indelPunish) {
                resultSequences[0] = "- " + resultSequences[0]
                resultSequences[1] = secondSeq[maxX-1] + resultSequences[1]
                --maxX
            }
            else {
                resultSequences[0] = firstSeq[maxY - 1] + resultSequences[0]
                resultSequences[1] = "- " + resultSequences[1]
                --maxY
            }
        }
        score = max
    }

    private fun generateStringArray(resultTable: Array<IntArray>): Array<Array<String>> {
        val stringArray: Array<Array<String>> = Array(secondSeq.length+2, { Array(firstSeq.length+2, {""})})
        for(i in 0.until(secondSeq.length))
            stringArray[i+2][0] = secondSeq[i].toString()
        for(i in 0.until(firstSeq.length))
            stringArray[0][i+2] = firstSeq[i].toString()
        for (i in 1..secondSeq.length+1)
            for (j in 1..firstSeq.length+1)
                stringArray[i][j] = resultTable[i-1][j-1].toString()
        return stringArray
    }

}