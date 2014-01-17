package geotrellis

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import scala.util.Random

class GeoTrellisAssignment extends FunSuite with ShouldMatchers {

  test("isArraySorted") {
    
    // unsorted array
    val unsortedArray = Array.fill(100)(Random.nextInt)
        
    // sort array
    val sortedArray = unsortedArray.sorted

    for (i <- 0 until 99) {
      sortedArray(i) should be <= (sortedArray(i + 1))
    }
  }
  
}