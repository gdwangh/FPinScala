package streams

object testpart {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  class level1 extends GameDef with Solver with StringParserTerrain {
    val level =
    """ooo-------
      |oSoooo----
      |ooooooooo-
      |-ooooooooo
      |-----ooToo
      |------ooo-""".stripMargin

    val optsolution = List(Right, Right, Down, Right, Right, Right, Down)
  }
  
  val prob = new level1                           //> prob  : streams.testpart.level1 = streams.testpart$$anonfun$main$1$level1$1@
                                                  //| 3d8c7aca
  prob.goalBlock                                  //> res0: streams.testpart.prob.Block = Block(Pos(4,7),Pos(4,7))
  
  val initial = Stream((prob.startBlock, List())) //> initial  : scala.collection.immutable.Stream[(streams.testpart.prob.Block, L
                                                  //| ist[Nothing])] = Stream((Block(Pos(1,1),Pos(1,1)),List()), ?)
  val explored: Set[prob.Block] = Set(prob.startBlock)
                                                  //> explored  : Set[streams.testpart.prob.Block] = Set(Block(Pos(1,1),Pos(1,1)))
                                                  //| 
  
  
  // prob.from(initial, explored).take(10).toList
  /* val a = prob.pathsFromStart
	val c = for ( b <- a )
	   				yield b
	   				
  c.filter(x => prob.done(x._1)) */
   
  val b = prob.pathsToGoal                        //> b  : Stream[(streams.testpart.prob.Block, List[streams.testpart.prob.Move])]
                                                  //|  = Stream((Block(Pos(4,7),Pos(4,7)),List(Down, Right, Right, Right, Down, Ri
                                                  //| ght, Right)), ?)
  
  prob.solution                                   //> res1: List[streams.testpart.prob.Move] = List(Right, Right, Down, Right, Rig
                                                  //| ht, Right, Down)
  
                                                  
                               
}