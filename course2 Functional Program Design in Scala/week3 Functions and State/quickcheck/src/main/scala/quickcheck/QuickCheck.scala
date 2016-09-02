package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._


abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {
  
  // lazy val genHeap: Gen[H] = ???
  lazy val genHeap: Gen[H] = for { x <- arbitrary[Int]
                                   h <- oneOf(const(empty), genHeap)
                                 }
                                    yield insert(x, h) 
                                    
  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("gen2") = forAll { (l:List[Int]) =>
    val h = insertList(l, empty)
    val l2 = getMinList(h)
    l2.equals(l.sorted)
  }
  
  def insertList(l:List[Int], h:H):H = l match {
    case Nil => h 
    case head::tail => insertList(tail, insert(head, h))
  }
  
  property("findMin") = forAll{ (x:Int, y:Int) => 
    val h = insert(y, insert(x, empty))
    val m = if (x<y) x else y
    
    findMin(h) == m
  }
  
  property("del1") = forAll{ (x:Int, y:Int) => 
    val h = insert(y, insert(x, empty))
    val m = if (x<y) y else x
    
    findMin(deleteMin(h)) == m 
  }
    
  property("del2") = forAll { (h:H) =>
      checkSort(getMinList(h))
  }
 
  def getMinList(h:H):List[Int] = 
      if (isEmpty(h)) Nil
      else         
        findMin(h)::getMinList(deleteMin(h))
   
  def checkSort(itemList : List[Int]):Boolean = itemList match {
        case Nil => true
        case head::Nil => true
        case head::tail => (head <= tail.head) && checkSort(tail)
      }    
     
  property("meld1") = forAll { (h1:H, h2:H) =>
    val min1 = findMin(h1)
    val min2 = findMin(h2)
    findMin(meld(h1,h2)) == (if (min1 < min2) min1 else min2)
  }
  
  property("meld2") = forAll { (h1:H, h2:H) =>
    val h = meld(h1, h2)
    checkSort(getMinList(h))
  }
  

}
