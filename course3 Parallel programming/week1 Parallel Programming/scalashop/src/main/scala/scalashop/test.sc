package scalashop

object test {
  val numTasks = 2
  val width = 8
  
  val a = (width+1)/numTasks
  val x = 0 until width by a
  
  val splitPoint = 0 until width by (width+1)/numTasks
  
  splitPoint.zipAll(splitPoint.tail,width, width)
 VerticalBoxBlurRunner
  val l = List(1,2,3,4)
  sum(l)
}