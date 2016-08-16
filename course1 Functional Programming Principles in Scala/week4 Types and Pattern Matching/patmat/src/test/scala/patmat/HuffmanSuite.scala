package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
	trait TestTrees {
		val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
		val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
	}


  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }


  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a','b','d'))
    }
  }


  test("makeCodeTree a larger tree") {
    new TestTrees {
      val t3 = makeCodeTree(
                        makeCodeTree(Leaf('x', 1), Leaf('e', 1)),
                        Leaf('t', 2)
                      )
      assert(chars(t3) === List('x','e','t'))
      assert(weight(t3) === 4)
    }
  }
    
  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("times(List('a', 'b', 'a')") {
    assert(times(List('a', 'b', 'a')) === List(('a', 2), ('b', 1)))
  }
    

  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }

  test("singleton for Nil") {
    assert(singleton(Nil) === false)
  }
  
  test("singleton for LIst(t1,t2)") {
    new TestTrees {
        assert(singleton(List(t1,t2)) === false)
    }
  }
 
  test("singleton for LIst(t1)") {
    new TestTrees {
        assert(singleton(List(t2)) === true)
    }
  }
    
  test("combine of some leaf list") {
     new TestTrees {
        val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
        assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
     }
  }
  test("until(singleton, combine)(trees)") {
    new TestTrees {
      val leaflist = List(Leaf('a', 2), Leaf('b', 3), Leaf('d', 4))
      val t4 = List(Fork(Leaf('d',4), Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), List('d', 'a','b'), 9))
      assert(until(singleton, combine)(leaflist) === t4)
  
    }
  }
  
  test("decode a very short text") {
    new TestTrees {
      assert(decode(t1, List(0,1,0,1,1)) === "ababb".toList)
    }
  }
  
  test("decode a short text") {
    new TestTrees {
      assert(decode(t2, List(0,0,1,0,1,1)) === "adbd".toList)
    }
  }

  test("encode a very short text") {
    new TestTrees {
      assert(encode(t1)("abaab".toList) === List(0,1,0,0,1))
    }
  }  
  
  
  test("decode and encode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
    }
  }

  test("decode and quickencode a very short text should be identity") {
    new TestTrees {
      assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
    }
  }
    
}
