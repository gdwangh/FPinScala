package kmeans

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import scala.math._

object KM extends KMeans
import KM._

@RunWith(classOf[JUnitRunner])
class KMeansSuite extends FunSuite {

  def checkClassify(points: GenSeq[Point], means: GenSeq[Point], expected: GenMap[Point, GenSeq[Point]]) {
    assert(classify(points, means) == expected,
      s"classify($points, $means) should equal to $expected")
  }

  test("'classify should work for empty 'points' and empty 'means'") {
    val points: GenSeq[Point] = IndexedSeq()
    val means: GenSeq[Point] = IndexedSeq()
    val expected = GenMap[Point, GenSeq[Point]]()
    checkClassify(points, means, expected)
  }

  test("'classify' should work for empty 'points' and 'means' == GenSeq(Point(1,1,1))") {
    val points: GenSeq[Point] = IndexedSeq()
    val mean = new Point(1, 1, 1)
    val means: GenSeq[Point] = IndexedSeq(mean)
    val expected = GenMap[Point, GenSeq[Point]]((mean, GenSeq()))
    checkClassify(points, means, expected)
  }

  test("'classify' should work for 'points' == GenSeq((1, 1, 0), (1, -1, 0), (-1, 1, 0), (-1, -1, 0)) and 'means' == GenSeq((0, 0, 0))") {
    val p1 = new Point(1, 1, 0)
    val p2 = new Point(1, -1, 0)
    val p3 = new Point(-1, 1, 0)
    val p4 = new Point(-1, -1, 0)
    val points: GenSeq[Point] = IndexedSeq(p1, p2, p3, p4)
    val mean = new Point(0, 0, 0)
    val means: GenSeq[Point] = IndexedSeq(mean)
    val expected = GenMap((mean, GenSeq(p1, p2, p3, p4)))
    checkClassify(points, means, expected)
  }

  test("'classify' should work for 'points' == GenSeq((1, 1, 0), (1, -1, 0), (-1, 1, 0), (-1, -1, 0)) and 'means' == GenSeq((1, 0, 0), (-1, 0, 0))") {
    val p1 = new Point(1, 1, 0)
    val p2 = new Point(1, -1, 0)
    val p3 = new Point(-1, 1, 0)
    val p4 = new Point(-1, -1, 0)
    val points: GenSeq[Point] = IndexedSeq(p1, p2, p3, p4)
    val mean1 = new Point(1, 0, 0)
    val mean2 = new Point(-1, 0, 0)
    val means: GenSeq[Point] = IndexedSeq(mean1, mean2)
    val expected = GenMap((mean1, GenSeq(p1, p2)), (mean2, GenSeq(p3, p4)))
    checkClassify(points, means, expected)
  }

  def checkParClassify(points: GenSeq[Point], means: GenSeq[Point], expected: GenMap[Point, GenSeq[Point]]) {
    assert(classify(points.par, means.par) == expected,
      s"classify($points par, $means par) should equal to $expected")
  }

  test("'classify with data parallelism should work for empty 'points' and empty 'means'") {
    val points: GenSeq[Point] = IndexedSeq()
    val means: GenSeq[Point] = IndexedSeq()
    val expected = GenMap[Point,GenSeq[Point]]()
    checkParClassify(points, means, expected)
  }

  test("'converged' should work for 'oldMeans' == GenSeq((1,1,1), ..., (99,99,99)) and 'newMeans' == GenSeq((1,1,1), ..., (99, 99, 99))") {
    var oldMeans: GenSeq[Point] = IndexedSeq()
    var newMeans: GenSeq[Point] = IndexedSeq()
    
    for (x<- 0 to 99) {
      oldMeans = oldMeans :+ new Point(x,x,x)
      newMeans = oldMeans :+ new Point(x,x,x)
    }
   assert(converged(0.1)(oldMeans, newMeans) === true, "GenSeq((1,1,1), ..., (99,99,99)) ")
  }

  test("'converged' should work for 'oldMeans' == GenSeq() and 'newMeans' == GenSeq()") {
    var oldMeans: GenSeq[Point] = IndexedSeq()
    var newMeans: GenSeq[Point] = IndexedSeq()
    assert(converged(0.1)(oldMeans, newMeans) === true, "GenSeq() ")
  }
  
  test("'converged' should work for 'oldMeans' == GenSeq((1,1,1), ..., (99,99,99)) and 'newMeans' == GenSeq((1,1,1), ..., (99, 99, 99.01))") {
    var oldMeans: GenSeq[Point] = IndexedSeq()
    var newMeans: GenSeq[Point] = IndexedSeq()
    
    for (x<- 0 to 99) {
      oldMeans = oldMeans :+ new Point(x,x,x)
      newMeans = oldMeans :+ new Point(x,x,x)
    }
    newMeans.updated(99, 99.01)
    assert(converged(0.1)(oldMeans, newMeans) === true, "GenSeq((1,1,1), ..., (99,99,99.01)) ")
  }
  
  def checkParKMeans(points: GenSeq[Point], means: GenSeq[Point], eta: Double, expected: GenSeq[Point]) {
    assert(kMeans(points.par, means.par, eta) === expected,
      s" --- kMeans($points par, $means par, $eta) should equal to $expected")
  }
  
  test("'kMeans' should work for 'points' == GenSeq((0, 0, 1), (0,0, -1), (0,1,0), (0,10,0)) and 'oldMeans' == GenSeq((0, -1, 0), (0, 2, 0)) and 'eta' == 12.25") {
    val p1 = new Point(0, 0, 1)
    val p2 = new Point(0,0, -1)
    val p3 = new Point(0,1,0)
    val p4 = new Point(0,10,0)
    val points: GenSeq[Point] = IndexedSeq(p1, p2, p3, p4)
    
    val mean1 = new Point(0, -1, 0)
    val mean2 = new Point(0, 2, 0)
    val means: GenSeq[Point] = IndexedSeq(mean1, mean2)
    
    val p5 = new Point(0.0, 0.0, 0.0)
    val p6 = new Point(0.0, 5.5, 0.0) 
    val expected:GenSeq[Point] = IndexedSeq(p5,p6)
    
    val eta = 12.25
    checkParKMeans(points, means, eta, expected)
  }
  
}


  
