package geotrellis

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.util.Random

class GeoTrellisAssignment extends FunSuite with ShouldMatchers {

  test("isArraySorted") {
    
    // random number generator
    val prng = new Random()

    // unsorted array
    val unsortedArray = new Array[Int](100)
        
    for (i <- 0 until 100) {
      unsortedArray(i) = prng.nextInt
    }

    // sort array
    val sortedArray = unsortedArray.sorted

    for (i <- 0 until 99) {
      sortedArray(i) should be <= (sortedArray(i + 1))
    }
  }
  
}