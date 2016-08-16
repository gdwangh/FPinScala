package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
   // def pascal(c: Int, r: Int): Int = ???
  def pascal(c: Int, r: Int): Int = {
    if ((c == 0) || (c == r)) 1
    else pascal(c-1, r-1)+pascal(c,r-1)
  }
  
  /**
   * Exercise 2
   */
   // def balance(chars: List[Char]): Boolean = ???
  def balance(chars: List[Char]): Boolean = {
    
    def balanceInner(chars: List[Char], braceCnt: Int): Boolean = {
      
      if (braceCnt < 0) false
      else if (chars.isEmpty) (braceCnt == 0)   // braceCnt == 0 true, otherwise false
      else if (chars.head == '(' )  balanceInner(chars.tail, braceCnt+1)
      else if (chars.head == ')') balanceInner(chars.tail, braceCnt-1)
      else  balanceInner(chars.tail, braceCnt)
    }
    
    balanceInner(chars, 0)
  }
  
  /**
   * Exercise 3
   */
  //   def countChange(money: Int, coins: List[Int]): Int = ???
    def countChange(money: Int, coins: List[Int]): Int = {
      def countChangeSorted(money: Int, coins: List[Int]): Int = {
        if (money<0 || coins.isEmpty) 0
        else 
        if (money == coins.head) 1
        else
          countChange(money, coins.tail)+                // without first coins
          countChange(money-coins.head, coins)           // with first coin
      }
      
      countChangeSorted(money, coins.sorted)
    }
  }
