package reductions

import scala.annotation._
import org.scalameter._
import common._

object ParallelParenthesesBalancingRunner {

  @volatile var seqResult = false

  @volatile var parResult = false

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer(new Warmer.Default)

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def balance(chars: Array[Char]): Boolean = {
    // ???
    var pos = 0
    var bal_cnt = 0
    
    while ((bal_cnt>=0) && (pos < chars.length)) {
        if (chars(pos)=='(') bal_cnt += 1
        else 
          if (chars(pos)==')') bal_cnt -= 1
        pos += 1
    }
    bal_cnt == 0
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
   */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    // def traverse(idx: Int, until: Int, arg1: Int, arg2: Int) /*: ???*/ = {
    // arg1: 多余的左括号，arg2：多余的右括号, 如果两者都不为0,模式只能是 一个或多个)在一个或多个(之前
    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int):(Int, Int) = {
      // ???
      var pos = idx
      var l_cnt = arg1
      var r_cnt = arg2
      
      while (pos < until) {
        if (chars(pos) == '(') l_cnt+=1   // 增加一个未成对的左括号
        else if (chars(pos)==')') 
                if (l_cnt > 0) l_cnt -= 1   // 抵消一个未成对的左括号
                else r_cnt += 1  // 增加一个未成对的右括号
             
        pos+=1
      }
      
      (l_cnt, r_cnt)
    }

    // def reduce(from: Int, until: Int) /*: ???*/ = {
    def reduce(from: Int, until: Int) : (Int,Int) = {  // 返回：(没成对的左括号，没成对的右括号)
      //???
      if (until - from <= threshold) {
        traverse(from, until, 0, 0)
      }  else {
        val mid = from + (until - from) / 2
        val ((ll, lr),(rl, rr)) = parallel(reduce(from, mid), reduce(mid, until))
        if (ll>rr)  // 左边多余的左括号 比 右边多余的右括号多
            (ll-rr+rl, lr)
        else
          (rl, rr-ll+lr)
       }
    }

    // reduce(0, chars.length) == ???
    reduce(0, chars.length) == (0,0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
