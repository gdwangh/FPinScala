package week8

class Pouring(capacity: Vector[Int]) {
    // glasses
  val glasses = 0 to capacity.length-1
  
  // states
  type State = Vector[Int]
  val initialStates = capacity.map(x => 0)
  
  // moves
  trait Move {
    def changeState(states:State):State
  }
  
  case class Empty(glass:Int) extends Move {
    def changeState(state:State) = state updated (glass, 0)
  }
  
  case class Fill(glass:Int) extends Move {
    def changeState(state:State) = state updated (glass, capacity(glass)) 
  }
  
  case class Pour(fromGlass:Int, toGlass:Int)  extends Move {
    def changeState(state:State) = {
      val amount = state(fromGlass) min (capacity(toGlass) - state(toGlass))
      state updated (fromGlass, state(fromGlass)-amount) updated (toGlass, state(toGlass)+amount)
    }
    
  }
  
  val moves = (glasses.map(x => Empty(x))) ++ 
             (glasses.map(x => Fill(x))) ++ 
             (for {from<-glasses; 
                   to<-glasses if (from != to)
                  }
                 yield Pour(from, to)
              )
              
   // paths
   class Paths(history:List[Move], val endState:State) {   
    def extend(move:Move) = new Paths(move::history, move changeState endState)
    override def toString = (history.reverse mkString " ") + "-->" + endState
   }
  
  val initPath = new Paths(Nil, initialStates)
  
  def from(pathSet:Set[Paths], explored:Set[State]):Stream[Set[Paths]] = 
    if (pathSet == Nil) 
       Stream.Empty
    else {
      val more = for { aPath<-pathSet
                       next<- moves map aPath.extend if !(explored contains next.endState)
                   }
                     yield next
                     
      pathSet #:: from(more, explored ++ (more map (_.endState)))
    }
  
   val pathSets = from(Set(initPath), Set(initialStates))
   
   def solutions(targeVol:Int):Stream[Paths] = 
     for {
       pathSet <- pathSets
       path <- pathSet if path.endState contains targeVol
     }
       yield path
   
}