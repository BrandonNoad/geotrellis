/*
 * Copyright (c) 2014 DigitalGlobe.
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

package geotrellis.spark.testfiles
import geotrellis.spark.metadata.PyramidMetadata
import geotrellis.spark.metadata.Context
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import geotrellis.spark.rdd.TileIdPartitioner

class TestFiles(pyramid: Path, conf: Configuration) {

  val (meta, opCtx) = setup

  val raster = new Path(pyramid, meta.maxZoomLevel.toString)
  
  def path = raster
  
  def rasterDefinition = opCtx.rasterDefinition
  def rasterExtent = rasterDefinition.rasterExtent
  def tileLayout = rasterDefinition.tileLayout
  def tileCount = opCtx.rasterDefinition.tileLayout.tileCols * opCtx.rasterDefinition.tileLayout.tileRows

  private def setup: (PyramidMetadata, Context) = {
    val meta = PyramidMetadata(pyramid, conf)
    val partitioner = TileIdPartitioner(new Path(pyramid, meta.maxZoomLevel.toString), conf)
    (meta, Context(meta.maxZoomLevel, meta, partitioner))
  }
}

object AllOnes {
  def apply(prefix: Path, conf: Configuration) = new TestFiles(new Path(prefix, "all-ones"), conf)
}

object AllTwos {
  def apply(prefix: Path, conf: Configuration) = new TestFiles(new Path(prefix, "all-twos"), conf)
}

object AllHundreds {
  def apply(prefix: Path, conf: Configuration) = new TestFiles(new Path(prefix, "all-hundreds"), conf)
}