package calculator

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest._

import TweetLength.MaxTweetLength

@RunWith(classOf[JUnitRunner])
class CalculatorSuite extends FunSuite with ShouldMatchers {

  /******************
   ** TWEET LENGTH **
   ******************/

  def tweetLength(text: String): Int =
    text.codePointCount(0, text.length)

  test("tweetRemainingCharsCount with a constant signal") {
    val result = TweetLength.tweetRemainingCharsCount(Var("hello world"))
    assert(result() == MaxTweetLength - tweetLength("hello world"))

    val tooLong = "foo" * 200
    val result2 = TweetLength.tweetRemainingCharsCount(Var(tooLong))
    assert(result2() == MaxTweetLength - tweetLength(tooLong))
  }

  test("tweetRemainingCharsCount with a supplementary char") {
    val result = TweetLength.tweetRemainingCharsCount(Var("foo blabla \uD83D\uDCA9 bar"))
    assert(result() == MaxTweetLength - tweetLength("foo blabla \uD83D\uDCA9 bar"))
  }


  test("colorForRemainingCharsCount with a constant signal") {
    val resultGreen1 = TweetLength.colorForRemainingCharsCount(Var(52))
    assert(resultGreen1() == "green")
    val resultGreen2 = TweetLength.colorForRemainingCharsCount(Var(15))
    assert(resultGreen2() == "green")

    val resultOrange1 = TweetLength.colorForRemainingCharsCount(Var(12))
    assert(resultOrange1() == "orange")
    val resultOrange2 = TweetLength.colorForRemainingCharsCount(Var(0))
    assert(resultOrange2() == "orange")

    val resultRed1 = TweetLength.colorForRemainingCharsCount(Var(-1))
    assert(resultRed1() == "red")
    val resultRed2 = TweetLength.colorForRemainingCharsCount(Var(-5))
    assert(resultRed2() == "red")
  }

  test("eval expr Ref") {
    val references : Map[String, Signal[Expr]]= Map("a"->Signal(Literal(2.5)), 
                                                    "b"->Signal(Ref("a")),
                                                    "c"->Signal(Ref("c")),
                                                    "d"->Signal(Ref("e")),
                                                    "e"->Signal(Ref("d")))
                                                    
    val resulta = Calculator.eval(Literal(2.5), references, Set("a"))
    assert(resulta === 2.5)
    
    val resultb = Calculator.eval(Ref("a"), references, Set("b"))
    assert(resultb === 2.5)
    
    val resultc = Calculator.eval(Ref("c"), references, Set("c"))
    assert(resultc.isNaN() === true)
    
    val resultd = Calculator.eval(Ref("d"), references, Set("d"))
    assert(resultd.isNaN() === true)
  }
  
  test("eval expr Plus") {
    val references : Map[String, Signal[Expr]]= Map("a"->Signal(Literal(2.5)), 
                                                    "b"->Signal(Plus(Ref("a"), Literal(1))),
                                                    "c"->Signal(Plus(Ref("c"), Literal(1))),
                                                    "d"->Signal(Plus(Ref("e"), Literal(1))),
                                                    "e"->Signal(Plus(Ref("f"), Literal(1))),
                                                    "g"->Signal(Plus(Ref("d"), Literal(1))))
    val resultb = Calculator.eval(Plus(Ref("a"), Literal(1)), references, Set("b"))
    assert(resultb === 3.5)
    
    val resultc = Calculator.eval(Plus(Ref("c"), Literal(1)), references, Set("c"))
    assert(resultc.isNaN() === true)
    
    val resultd = Calculator.eval(Plus(Ref("e"), Literal(1)), references, Set("d"))
    assert(resultd.isNaN() === true)
  }
}
