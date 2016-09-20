package scalashop

object test {
  val numTasks = 2                                //> numTasks  : Int = 2
  val width = 8                                   //> width  : Int = 8
  
  val a = (width+1)/numTasks                      //> a  : Int = 4
  val x = 0 until width by a                      //> x  : scala.collection.immutable.Range = Range(0, 4)
  
  val splitPoint = 0 until width by (width+1)/numTasks
                                                  //> splitPoint  : scala.collection.immutable.Range = Range(0, 4)
  
  splitPoint.zipAll(splitPoint.tail,width, width) //> res0: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((0,4), (4,8
                                                  //| ))
 VerticalBoxBlurRunner                            //> res1: scalashop.VerticalBoxBlurRunner.type = scalashop.VerticalBoxBlurRunner
                                                  //| $@183967b
                                                  
}