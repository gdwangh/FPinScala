package barneshut

import java.util.concurrent._
import scala.collection._
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import common._
import scala.math._
import scala.collection.parallel._
import barneshut.conctrees.ConcBuffer

@RunWith(classOf[JUnitRunner])
class BarnesHutSuite extends FunSuite {

  // test cases for quad tree

import FloatOps._
  test("Empty: center of mass should be the center of the cell") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.massX == 51f, s"${quad.massX} should be 51f")
    assert(quad.massY == 46.3f, s"${quad.massY} should be 46.3f")
  }

  test("Empty: mass should be 0") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.mass == 0f, s"${quad.mass} should be 0f")
  }

  test("Empty: total should be 0") {
    val quad = Empty(51f, 46.3f, 5f)
    assert(quad.total == 0, s"${quad.total} should be 0")
  }

  test("Leaf with 1 body") {
    val b = new Body(123f, 18f, 26f, 0f, 0f)
    val quad = Leaf(17.5f, 27.5f, 5f, Seq(b))

    assert(quad.mass ~= 123f, s"${quad.mass} should be 123f")
    assert(quad.massX ~= 18f, s"${quad.massX} should be 18f")
    assert(quad.massY ~= 26f, s"${quad.massY} should be 26f")
    assert(quad.total == 1, s"${quad.total} should be 1")
  }


  test("Fork with 3 empty quadrants and 1 leaf (nw)") {
    val b = new Body(123f, 18f, 26f, 0f, 0f)
    val nw = Leaf(17.5f, 27.5f, 5f, Seq(b))
    val ne = Empty(22.5f, 27.5f, 5f)
    val sw = Empty(17.5f, 32.5f, 5f)
    val se = Empty(22.5f, 32.5f, 5f)
    val quad = Fork(nw, ne, sw, se)

    assert(quad.centerX == 20f, s"${quad.centerX} should be 20f")
    assert(quad.centerY == 30f, s"${quad.centerY} should be 30f")
    assert(quad.mass ~= 123f, s"${quad.mass} should be 123f")
    assert(quad.massX ~= 18f, s"${quad.massX} should be 18f")
    assert(quad.massY ~= 26f, s"${quad.massY} should be 26f")
    assert(quad.total == 1, s"${quad.total} should be 1")
  }

  test("Empty.insert(b) should return a Leaf with only that body") {
    val quad = Empty(51f, 46.3f, 5f)
    val b = new Body(3f, 54f, 46f, 0f, 0f)
    val inserted = quad.insert(b)
    inserted match {
      case Leaf(centerX, centerY, size, bodies) =>
        assert(centerX == 51f, s"$centerX should be 51f")
        assert(centerY == 46.3f, s"$centerY should be 46.3f")
        assert(size == 5f, s"$size should be 5f")
        assert(bodies == Seq(b), s"$bodies should contain only the inserted body")
      case _ =>
        fail("Empty.insert() should have returned a Leaf, was $inserted")
    }
  }
  
   test("Fork.insert(b) should return a new Fork with all the existing bodies and the new one") {
          val a = new Body(123f, 18f, 26f, 0f, 0f)

          val nw = Leaf(17.5f, 27.5f, 5f, Seq(a))
          val ne = Empty(22.5f, 27.5f, 5f)
          val sw = Empty(17.5f, 32.5f, 5f)
          val se = Empty(22.5f, 32.5f, 5f)
          val quad = Fork(nw, ne, sw, se)

           val b = new Body(3f, 23f, 33f, 0f, 0f)
    
          val inserted = quad.insert(b)
          
          inserted match {
            case Fork(newnw, newne, newsw, newse) =>
              assert(inserted.total === 2, s"${inserted.total} should be 2")
              assert(inserted.mass === 126f, s"${inserted.mass} should be 126f")
              assert(inserted.size === 10f, s"${inserted.size} should be 10f")
              assert(newnw.contain(a)===true, s"nw should contain only the existing body")
              assert(newse.contain(b)===true, s"se should contain only the inserted body")
            case _ =>
              fail("Empty.insert() should have returned a Leaf, was $inserted")
          }
  }
  
   
  test("Leaf.insert(b) should return the Leaf with all the existing bodies and the new one  if size <= minimumSize") {
      val a = new Body(123f, 18f, 26f, 0f, 0f)
      val quad = Leaf(17.5f, 27.5f, minimumSize/2, Seq(a))
      
      val b = new Body(3f, 54f, 46f, 0f, 0f)
      val inserted = quad.insert(b)
      
      inserted match {
        case Leaf(centerX, centerY, size, bodies) =>
          assert(centerX == 17.5f, s"$centerX should be 17.5f")
          assert(centerY == 27.5f, s"$centerY should be 27.5f")
          assert(size == minimumSize/2, s"$size should be $minimumSize/2")
          assert(bodies == Seq(a,b), s"$bodies should contain only the inserted body")
        case _ =>
          fail("when size <= minimumSize, Leaf.insert() should have returned a  Leaf with all the existing bodies and the new one, was $inserted")
      }
    }

  test("Leaf.insert(b) should return should return a new Fork if size > minimumSize") {
    val a = new Body(123f, 18f, 26f, 0f, 0f)
    val quad = Leaf(17.5f, 27.5f, 5f, Seq(a))
      
    val b = new Body(3f, 54f, 46f, 0f, 0f)
    val inserted = quad.insert(b)
    
    inserted match {
      case Fork(nw, ne, sw, se) =>
        assert(inserted.total == 2, s"${inserted.total} should be 2")
        assert(inserted.mass == 126f, s"${inserted.total} should be 126f")
        assert((ne.total==1)&&(ne.contain(a) == true), s"ne should contain only the existing body")
        assert((se.total==1)&&(se.contain(b) == true), s"se should contain only the inserted body")        
      case _ =>
        fail("when size > minimumSize, Leaf.insert() should have returned a new Fork, was $inserted")
    }
  }
  
  // test cases for Body

  test("Body.updated should do nothing for Empty quad trees") {
    val b1 = new Body(123f, 18f, 26f, 0f, 0f)
    val body = b1.updated(Empty(50f, 60f, 5f))

    assert(body.xspeed == 0f)
    assert(body.yspeed == 0f)
  }

  test("Body.updated should take bodies in a Leaf into account") {
    val b1 = new Body(123f, 18f, 26f, 0f, 0f)
    val b2 = new Body(524.5f, 24.5f, 25.5f, 0f, 0f)
    val b3 = new Body(245f, 22.4f, 41f, 0f, 0f)

    val quad = Leaf(15f, 30f, 20f, Seq(b2, b3))

    val body = b1.updated(quad)

    assert(body.xspeed ~= 12.587037f)
    assert(body.yspeed ~= 0.015557117f)
  }

  // test cases for sector matrix

  test("'SectorMatrix.+=' should add a body at (25,47) to the correct bucket of a sector matrix of size 96") {
    val body = new Body(5, 25, 47, 0.1f, 0.1f)
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97
    val sm = new SectorMatrix(boundaries, SECTOR_PRECISION)
    sm += body
    val res = sm(2, 3).size == 1 && sm(2, 3).find(_ == body).isDefined
    assert(res, s"Body not found in the right sector")
  }

  // test case for simulator computeBoundaries
  test("'Simulator.updateBoundaries' should updates the minX, minY, maxX and maxY values so that the boundaries include the body") {
    val body = new Body(5, 25, 47, 0.1f, 0.1f)

    val boundaries = new Boundaries()
    boundaries.minX = 50
    boundaries.minY = 50
    boundaries.maxX = 97
    boundaries.maxY = 97
    
    val simulator = new Simulator(defaultTaskSupport,new TimeStatistics)
    simulator.updateBoundaries(boundaries, body)
    assert(boundaries.minX===25, s"${boundaries.minX} should be 25")
    assert(boundaries.maxX===97, s"${boundaries.maxX} should be 97")
    assert(boundaries.minY===47, s"${boundaries.minY} should be 47")
    assert(boundaries.maxY===97, s"${boundaries.maxY} should be 97")
  }
  
  test("'Simulator.mergeBoundaries' should  creates a new Boundaries object, which represents the smallest rectangle that contains both the input boundaries") {
    val a = new Boundaries()
    a.minX = 50
    a.minY = 50
    a.maxX = 97
    a.maxY = 97

    val b = new Boundaries()
    b.minX = 10
    b.minY = 10
    b.maxX = 80
    b.maxY = 80
    
    val simulator = new Simulator(defaultTaskSupport,new TimeStatistics)
    val c = simulator.mergeBoundaries(a, b)
    assert(c.minX===10, s"${c.minX} should be 10")
    assert(c.maxX===97, s"${c.maxX} should be 97")
    assert(c.minY===10, s"${c.minY} should be 10")
    assert(c.maxY===97, s"${c.maxY} should be 97")
  }
  
  test("'Simulator.computeSectorMatrix' should  creates a new sector matrix") {
    val boundaries = new Boundaries()
    boundaries.minX = 1
    boundaries.minY = 1
    boundaries.maxX = 97
    boundaries.maxY = 97
    
    val body = new Body(5, 25, 47, 0.1f, 0.1f)
    val simulator = new Simulator(defaultTaskSupport,new TimeStatistics)
    val sm = simulator.computeSectorMatrix(Seq(body), boundaries)
    
    val res = sm(2, 3).size == 1 && sm(2, 3).find(_ == body).isDefined
    assert(res, s"Body not found in the right sector")
  }
    
}

object FloatOps {
  private val precisionThreshold = 1e-4

  /** Floating comparison: assert(float ~= 1.7f). */
  implicit class FloatOps(val self: Float) extends AnyVal {
    def ~=(that: Float): Boolean =
      abs(self - that) < precisionThreshold
  }

  /** Long floating comparison: assert(double ~= 1.7). */
  implicit class DoubleOps(val self: Double) extends AnyVal {
    def ~=(that: Double): Boolean =
      abs(self - that) < precisionThreshold
  }

  /** Floating sequences comparison: assert(floatSeq ~= Seq(0.5f, 1.7f). */
  implicit class FloatSequenceOps(val self: Seq[Float]) extends AnyVal {
    def ~=(that: Seq[Float]): Boolean =
      self.size == that.size &&
        self.zip(that).forall { case (a, b) =>
          abs(a - b) < precisionThreshold
        }
  }
}

