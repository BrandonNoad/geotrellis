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
import geotrellis.raster._
import geotrellis.source._

/**
 * Or's cell values of rasters or Int values.
 *
 * @note        NoData values will cause the results of this operation
 *              to be NODATA.
 * @note        If used with Double typed rasters, the values
 *              will be rounded to Ints.
 */
object Or extends LocalRasterBinaryOp {
  def combine(z1:Int,z2:Int) =
    if (isNoData(z1) || isNoData(z2)) NODATA
    else z1 | z2

  def combine(z1:Double,z2:Double) =
    if (isNoData(z1) || isNoData(z2)) Double.NaN
    else i2d(d2i(z1) | d2i(z2))
}

trait OrOpMethods[+Repr <: RasterSource] { self: Repr =>
  /** Or a constant Int value to each cell. */
  def localOr(i: Int): RasterSource = self.mapOp(Or(_, i))
  /** Or a constant Int value to each cell. */
  def |(i:Int): RasterSource = localOr(i)
  /** Or a constant Int value to each cell. */
  def |:(i:Int): RasterSource = localOr(i)
  /** Or the values of each cell in each raster.  */
  def localOr(rs:RasterSource): RasterSource = self.combineOp(rs)(Or(_,_))
  /** Or the values of each cell in each raster. */
  def |(rs:RasterSource): RasterSource = localOr(rs)
  /** Or the values of each cell in each raster.  */
  def localOr(rss:Seq[RasterSource]): RasterSource = self.combineOp(rss)(Or(_))
  /** Or the values of each cell in each raster. */
  def |(rss:Seq[RasterSource]): RasterSource = localOr(rss)
}

trait OrMethods { self: Raster =>
  /** Or a constant Int value to each cell. */
  def localOr(i: Int): Raster = Or(self, i)
  /** Or a constant Int value to each cell. */
  def |(i:Int): Raster = localOr(i)
  /** Or a constant Int value to each cell. */
  def |:(i:Int): Raster = localOr(i)
  /** Or the values of each cell in each raster.  */
  def localOr(r:Raster): Raster = Or(self,r)
  /** Or the values of each cell in each raster. */
  def |(r:Raster): Raster = localOr(r)
  /** Or the values of each cell in each raster.  */
  def localOr(rs:Seq[Raster]): Raster = Or(self +: rs)
  /** Or the values of each cell in each raster. */
  def |(rs:Seq[Raster]): Raster = localOr(rs)
}
