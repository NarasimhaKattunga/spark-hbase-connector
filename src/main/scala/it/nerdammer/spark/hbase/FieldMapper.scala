package it.nerdammer.spark.hbase

import org.apache.hadoop.hbase.util.Bytes


/**
 * Created by Nicola Ferraro on 10/01/15.
 */
trait FieldMapper[T] extends Serializable {
  def map(data: HBaseDataHolder): T
}

trait SingleColumnFieldMapper[T] extends FieldMapper[T] {

  def map(data: HBaseDataHolder): T =
    if(data.columns.size!=1) throw new IllegalArgumentException(s"Unexpected number of columns: expected 1, returned ${data.columns.size}")
    else columnMap(data.columns.head)

  def columnMap(cols: Array[Byte]): T
}

trait FieldMapperConversions extends Serializable {

  implicit def intMapper: FieldMapper[Int] = new SingleColumnFieldMapper[Int] {
    def columnMap(cols: Array[Byte]): Int = Bytes.toInt(cols)
  }

  implicit def stringMapper: FieldMapper[String] = new SingleColumnFieldMapper[String] {
    def columnMap(cols: Array[Byte]): String = Bytes.toString(cols)
  }


  // TEMP
  implicit def testMapper: FieldMapper[(String, String)] = new FieldMapper[(String, String)] {
    def map(data: HBaseDataHolder) = (Bytes.toString(data.columns.head), Bytes.toString(data.columns.tail.head))
  }


}

