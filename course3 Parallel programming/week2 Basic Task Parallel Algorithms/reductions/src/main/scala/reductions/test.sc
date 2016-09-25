package reductions

object test {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  def max(a: Float, b: Float): Float = if (a > b) a else b
                                                  //> max: (a: Float, b: Float)Float
  
  val a = Array[Float](0f, 1f, 8f, 9f)            //> a  : Array[Float] = Array(0.0, 1.0, 8.0, 9.0)
  val b = a.zipWithIndex.drop(1).map{case (y,x)=> y/x}
                                                  //> b  : Array[Float] = Array(1.0, 4.0, 3.0)
  b.scan(0f)(max(_,_))                            //> res0: Array[Float] = Array(0.0, 1.0, 4.0, 4.0)
  
  val from =1                                     //> from  : Int = 1
  val until = 4                                   //> until  : Int = 4
  val startingAngle = 0f                          //> startingAngle  : Float = 0.0
  a.slice(from, until).zipWithIndex.
        map{ case (y, x)=> y/(x+from)}.
        scan(startingAngle)(max(_,_))             //> res1: Array[Float] = Array(0.0, 1.0, 4.0, 4.0)
        //copyToArray(output, from, until-from)
        
}