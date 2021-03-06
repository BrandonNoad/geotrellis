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

class CeilSpec extends FunSpec 
                 with ShouldMatchers 
                 with TestServer 
                 with RasterBuilders {
  describe("Ceil") {
    it("takes ceil of int tiled RasterSource") {
      val rs = createRasterSource(
        Array( NODATA,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1,

               1,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1),
        3,2,3,2)

      run(rs.localCeil) match {
        case Complete(result,success) =>
//          println(success)
          for(row <- 0 until 4) {
            for(col <- 0 until 9) {
              if(row == 0 && col == 0)
                result.get(col,row) should be (NODATA)
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

    it("takes ceil of Double tiled RasterSource") {
      val rs = createRasterSource(
        Array( Double.NaN,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,
               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,

               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,
               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3),
        3,2,3,2)

      run(rs.localCeil) match {
        case Complete(result,success) =>
//          println(success)
          for(row <- 0 until 4) {
            for(col <- 0 until 9) {
              if(row == 0 && col == 0)
                isNoData(result.getDouble(col,row)) should be (true)
              else
                result.getDouble(col,row) should be (2.0)
            }
          }
        case Error(msg,failure) =>
          println(msg)
          println(failure)
          assert(false)
      }
    }
  }
  describe("Ceil on Raster") {
    it("takes ceil of int Raster") {
      val r = createRaster(
        Array( NODATA,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1,

               1,1,1, 1,1,1, 1,1,1,
               1,1,1, 1,1,1, 1,1,1),
        9, 4)

      val result = r.localCeil()

      for(row <- 0 until 4) {
        for(col <- 0 until 9) {
          if(row == 0 && col == 0)
            result.get(col,row) should be (NODATA)
          else
            result.get(col,row) should be (1)
        }
      }
    }

    it("takes ceil of Double Raster") {
      val r = createRaster(
        Array( Double.NaN,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,
               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,

               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3,
               1.3,1.3,1.3, 1.3,1.3,1.3, 1.3,1.3,1.3),
        9, 4)

      val result = r.localCeil()

      for(row <- 0 until 4) {
        for(col <- 0 until 9) {
          if(row == 0 && col == 0)
            isNoData(result.getDouble(col,row)) should be (true)
          else
            result.getDouble(col,row) should be (2.0)
        }
      }
    }
  }
}
