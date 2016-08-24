package week8

object test_pouring {
  
  val prob = new Pouring(Vector(4, 9))            //> prob  : week8.Pouring = week8.Pouring@1b52d12
  // prob.moves
  // prob.pathSets.take(2).toList
  
  prob.solutions(6)                               //> res0: Stream[week8.test_pouring.prob.Paths] = Stream(Fill(1) Pour(1,0) Empty
                                                  //| (0) Pour(1,0) Empty(0) Pour(1,0) Fill(1) Pour(1,0)-->Vector(4, 6), ?)
  
  
}