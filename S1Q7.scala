// Q7 of problem sheet 1
// code to calculate the product of two matrices concurrently using bag of tasks approach

import io.threadcso._

class matrixMultiplier(A: Array[Array[Int]], B: Array[Array[Int]]) {

    private val m = A.length
    private val n = B.length
    private val p = B(0).length

    private val nWorkers = 16

    // output matrix
    private var C = Array.fill(m)(Array.fill(p)(0))

    // entry to calculate
    private type Task = (Int, Int)
    // entry and result
    private type Result = (Int, Int, Int)

    private val toWorkers = OneMany[Task]
    private val toController = OneMany[Result]

    // calculate one entry of C
    private def calculateEntry(i: Int, j: Int): Int = {
        var count = 0
        var total = 0
        while (count < n) {
            total = total + A(i)(count) * B(count)(j)
            count = count + 1
        }
        total
    }

    // individual worker - each task is a single entry of C
    private def worker = proc("worker") {
        repeat {
            val (i, j) = toWorkers?()
            val result = calculateEntry(i, j)
            toController!(i, j, result)
        }

    }

    // distributor - distributes tasks
    private def distributor = proc("distributor") {
        for (i <- 0 until m) {
            for (j <- 0 until p) {
                toWorkers!(i, j)
            }
        }
        toWorkers.close
    }

    // collector - collects worker results
    private def collector = proc("collector") {
        for (count <- 0 until m * p) {
            val (i, j, result) = toController?()
            C(i)(j) = result
        }
    }

    // the main system
    private def system: PROC = {
        val workers = || (for (i <- 0 until nWorkers) yield worker)
        workers || distributor || collector
    }

    // multiply function which returns result
    def multiply: Array[Array[Int]] = { run(system); C }
}
object matrixMultiplierTest {
    def main(args : Array[String]) = {
        // [1, 2, 3]
        // [2, 3, 4]
        // [3, 4, 5]
        val matrixA = Array (Array(3, 4, 2))
        val matrixB = Array (Array(13, 9, 7, 15), Array(8, 7, 4, 6), Array(6, 4, 0, 3))

        val matrixC = Array (Array(83, 63, 37, 75))

        val MM = new matrixMultiplier(matrixA, matrixB)
        val result = MM.multiply
        println(matrixC.map(_.mkString(" ")).mkString("\n"))
        println(result.map(_.mkString(" ")).mkString("\n"))
    }
}