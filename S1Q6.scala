// Q6 of problem sheet 1
// code to sort a list of n integers using a pipeline of n components
// Each component receives a stream of n integers from its left and shifts the largest to the right

import io.threadcso._

object sortChannels {
    def component(in: ?[Int], out: ![Int]) : PROC = proc("component") {
        attempt {
            var largest = in?()
            repeat {
                var current = in?()
                if (current > largest) {
                    out!(largest)
                    largest = current
                }
                else out!(current)

            }
            in.closeIn
            out!(largest)
            out.closeOut
        }
        { in.closeIn; out.closeOut }
    }

    def sort(in: ?[Int], out: ![Int], n: Int): PROC = proc("sort") {
        var tempIn = in
        var count = 0
        var sorter = proc {}
        while (count < n - 1) {
            var tempOut = OneOne[Int]
            sorter = sorter || component(tempIn, tempOut)
            tempIn = tempOut;
            count = count + 1
        }
        sorter = sorter || component(tempIn, out)
        run(sorter)
    }
}
import scala.util.Random

object sortChannelsTest {
    // Number of elements to sort; range of input values.
    val N = 100; val Max = 100

    def doTest = {
        val xs = Array.fill(N)(Random.nextInt(Max))
        val ys = new Array[Int](N)
        val in, out = OneOne[Int]
        def sender = proc{ for(x <- xs) in!x; in.close }
        def receiver = proc{ var i = 0; repeat{ ys(i) = out?(); i += 1 } }
        run(sender || sortChannels.sort(in, out, N) || receiver)
        assert(xs.sorted.sameElements(ys))
    }

    def main(args : Array[String]) = {
        for(i <- 0 until 1000){ doTest; if(i%10 == 0) print(".") }
        println; exit
    }
}

// sends n^2 messages in total
// the longest number of sequentially ordered messages is n