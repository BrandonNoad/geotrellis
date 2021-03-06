/*
 * Copyright (c) 2014 Azavea.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.raster.op.local

import geotrellis._
import geotrellis.source._
import geotrellis.process._

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import geotrellis.testkit._

class LessSpec extends FunSpec 
                   with ShouldMatchers 
                   with TestServer 
                   with RasterBuilders {
  describe("Less") {
    it("checks int valued raster against int constant") {
      val r = positiveIntegerRaster
      val result = get(Less(r,5))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.get(col,row)
          val rz = result.get(col,row)
          if(z < 5) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks int valued raster against double constant") {
      val r = probabilityRaster.map(_*100).convert(TypeInt)
      val result = get(Less(r,69.0))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.get(col,row)
          val rz = result.get(col,row)
          if(z < 69) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks double valued raster against int constant") {
      val r = positiveIntegerRaster.convert(TypeDouble).mapDouble(_.toDouble)
      val result = get(Less(r,5))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.getDouble(col,row)
          val rz = result.get(col,row)
          if(z < 5.0) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks double valued raster against double constant") {
      val r = probabilityRaster
      val result = get(Less(r,0.69))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.getDouble(col,row)
          val rz = result.getDouble(col,row)
          if(z < 0.69) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks an integer raster against itself") {
      val r = positiveIntegerRaster
      val result = get(Less(r,r))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (0)
        }
      }
    }

    it("checks an integer raster against a different raster") {
      val r = positiveIntegerRaster
      val r2 = positiveIntegerRaster.map(_*2)
      val result = get(Less(r,r2))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (1)
        }
      }
    }

    it("checks a double raster against itself") {
      val r = probabilityRaster
      val result = get(Less(r,r))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (0)
        }
      }
    }

    it("checks a double raster against a different raster") {
      val r = probabilityRaster
      val r2 = positiveIntegerRaster.mapDouble(_*2.3)
      val result = get(Less(r,r2))
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (1)
        }
      }
    }

    it("compares Less on 2 RasterSources correctly") {
      val rs1 = RasterSource("quad_tiled")
      val rs2 = RasterSource("quad_tiled2")

      run(rs1 < rs2) match {
        case Complete(result,success) =>
          for(row <- 0 until result.rasterExtent.rows) {
            for(col <- 0 until result.rasterExtent.cols) {
              result.get(col,row) should be (0)
            }
          }
        case Error(msg,failure) =>
          println(msg)
          println(failure)
          assert(false)
      }
    }

    it("compares Less on a RasterSource and an int correctly") {
      val rs1 = RasterSource("quad_tiled")

      run(rs1 < 5) match {
        case Complete(result,success) =>
          for(row <- 0 until result.rasterExtent.rows) {
            for(col <- 0 until result.rasterExtent.cols) {
              val cellResult = result.get(col,row)
              if (result.get(col,row) < 5) cellResult should be (1)
              else cellResult should be (0)
            }
          }
        case Error(msg,failure) =>
          println(msg)
          println(failure)
          assert(false)
      }
    }

    it("compares Less on an int correctly and a RasterSource") {
      val rs1 = RasterSource("quad_tiled")

      run(5 <<: rs1) match {
        case Complete(result,success) =>
          for(row <- 0 until result.rasterExtent.rows) {
            for(col <- 0 until result.rasterExtent.cols) {
              val cellResult = result.get(col,row)
              if (5 < result.get(col,row)) cellResult should be (1)
              else cellResult should be (0)
            }
          }
        case Error(msg,failure) =>
          println(msg)
          println(failure)
          assert(false)
      }
    }

    it("adds three tiled RasterSources correctly") {
      val rs1 = createRasterSource(
        Array( NODATA,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1,

               1,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1),
        3,2,3,2)

      val rs2 = createRasterSource(
        Array( 2,2,2, 2,2,2, 2,2,2,
               2,2,2, 2,2,2, 2,2,2,

               2,2,2, 2,2,2, 2,2,2,
               2,2,2, 2,2,2, 2,2,2),
        3,2,3,2)

      val rs3 = createRasterSource(
        Array( -1,3,3, 3,3,3, 3,3,3,
               3,3,3, 3,3,3, 3,3,3,

               3,3,3, 3,3,3, 3,3,3,
               3,3,3, 3,3,3, 3,3,3),
        3,2,3,2)

      run(rs1 < rs2 < rs3) match {
        case Complete(result,success) =>
//          println(success)
          for(row <- 0 until 4) {
            for(col <- 0 until 9) {
              if(col == 0 && row == 0)
                result.get(col,row) should be (0)
              else
                result.get(col,row) should be (1)
            }
          }
        case Error(msg,failure) =>
          println(msg)
          println(failure)
          assert(false)
      }
    }
  }
  describe("Less on Raster") {
    it("checks int valued raster against int constant") {
      val r = positiveIntegerRaster
      val result = (r < 5)
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.get(col,row)
          val rz = result.get(col,row)
          if(z < 5) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks int valued raster against int constant (right associative)") {
      val r = positiveIntegerRaster
      val result = (5 <<: r)
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.get(col,row)
          val rz = result.get(col,row)
          if(5 < z) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks int valued raster against double constant") {
      val r = probabilityRaster.map(_*100).convert(TypeInt)
      val result = r < 69.0
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.get(col,row)
          val rz = result.get(col,row)
          if(z < 69) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks double valued raster against int constant") {
      val r = positiveIntegerRaster.convert(TypeDouble).mapDouble(_.toDouble)
      val result = r < 5
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.getDouble(col,row)
          val rz = result.get(col,row)
          if(z < 5.0) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks double valued raster against int constant (right associative)") {
      val r = positiveIntegerRaster.convert(TypeDouble).mapDouble(_.toDouble)
      val result = (5 <<: r)
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.getDouble(col,row)
          val rz = result.get(col,row)
          if(5.0 < z) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks double valued raster against double constant") {
      val r = probabilityRaster
      val result = r < 0.69
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          val z = r.getDouble(col,row)
          val rz = result.getDouble(col,row)
          if(z < 0.69) rz should be (1)
          else rz should be (0)
        }
      }
    }

    it("checks an integer raster against itself") {
      val r = positiveIntegerRaster
      val result = r < r
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (0)
        }
      }
    }

    it("checks an integer raster against a different raster") {
      val r = positiveIntegerRaster
      val r2 = positiveIntegerRaster.map(_*2)
      val result = r < r2
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (1)
        }
      }
    }

    it("checks a double raster against itself") {
      val r = probabilityRaster
      val result = r < r
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (0)
        }
      }
    }

    it("checks a double raster against a different raster") {
      val r = probabilityRaster
      val r2 = positiveIntegerRaster.mapDouble(_*2.3)
      val result = r < r2
      for(col <- 0 until r.cols) {
        for(row <- 0 until r.rows) {
          result.get(col,row) should be (1)
        }
      }
    }
  }
}
