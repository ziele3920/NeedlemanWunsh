package pl.edu.ziele3920.needelmanwunsh

/**
 * Created by Zioło on 2017-11-13.
 */
class NeedelmanWunsh(private val firstSeq: String, private val secondSeq: String) {

    fun getResultStringArray(): Array<Array<String>> {
        val workingArray: Array<IntArray> = Array(secondSeq.length+1, {IntArray(firstSeq.length+1)})

        for (i in 0..secondSeq.length)
            workingArray[i][0] = i

        for (i in 0..firstSeq.length)
            workingArray[0][i] = i

        for (i in 1..secondSeq.length) {
            for (j in 1..firstSeq.length) {
                if (secondSeq[i - 1] == firstSeq[j - 1])
                    workingArray[i][j] = workingArray[i - 1][j - 1]
                else
                    workingArray[i][j] = Math.min(workingArray[i - 1][j], workingArray[i][j - 1]) + 1
            }
        }
        return generateStringArray(workingArray)
    }

    private fun generateStringArray(workingArray: Array<IntArray>): Array<Array<String>> {
        val stringArray: Array<Array<String>> = Array(secondSeq.length+2, { Array(firstSeq.length+2, {""})})
        for(i in 0..secondSeq.length)
            stringArray[i][0] = secondSeq[i].toString()
        for(i in 0..firstSeq.length)
            stringArray[i][0] = firstSeq[i].toString()
        for (i in 1..secondSeq.length)
            for (j in 1..firstSeq.length)
                stringArray[i][j] = workingArray[i][j].toString()
        return stringArray
    }

}