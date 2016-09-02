package quickcheck

object test1 {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  class subHeap extends quickcheck.test.BinomialHeap with IntHeap {
  }
  
  val heap = new subHeap                          //> heap  : quickcheck.test1.subHeap = quickcheck.test1$$anonfun$main$1$subHeap$
                                                  //| 1@30c7da1e
  val h = heap.empty                              //> h  : scala.collection.immutable.Nil.type = List()
  
  val a = heap.insert(1, h)                       //> a  : quickcheck.test1.heap.H = List(Node(1,0,List()))
  val b = heap.insert(2, h)                       //> b  : quickcheck.test1.heap.H = List(Node(2,0,List()))
  val c = heap.insert(1, h)                       //> c  : quickcheck.test1.heap.H = List(Node(1,0,List()))
  
  val min1 = heap.findMin(c)                      //> min1  : quickcheck.test1.heap.A = 1
  val d1 = heap.deleteMin(c)                      //> d1  : quickcheck.test1.heap.H = List()

  val min2 = heap.findMin(d1)                     //> java.util.NoSuchElementException: min of empty heap
                                                  //| 	at quickcheck.test.BinomialHeap$class.findMin(Heap.scala:34)
                                                  //| 	at quickcheck.test1$$anonfun$main$1$subHeap$1.findMin(quickcheck.test1.s
                                                  //| cala:6)
                                                  //| 	at quickcheck.test1$$anonfun$main$1.apply$mcV$sp(quickcheck.test1.scala:
                                                  //| 19)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at quickcheck.test1$.main(quickcheck.test1.scala:3)
                                                  //| 	at quickcheck.test1.main(quickcheck.test1.scala)
  val d2 = heap.deleteMin(d1)
  
  val min3 = heap.findMin(d2)
  val d3 = heap.deleteMin(d2)
}





  
  