package com.shiyanlou.shinelon.ELTJob

import com.shiyanlou.shinelon.Utils.AccessConvertUtil
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

object LogJob {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf()
      .setMaster("local[2]")
      .setAppName("LogJob")

    val sc=new SparkContext(conf)

    val accessRDD=sc.textFile("hdfs://localhost:9000/user/data/page_view_data.log")

    //创建sqlContext
    val sqlContext=new SQLContext(sc)
    //    CityAccessTopnDao$
    //    README.md
    //   RDD=>DF
    val accessDF= sqlContext.createDataFrame(accessRDD.map(x=>{
      AccessConvertUtil.parseLog(x)
    }),AccessConvertUtil.structType)

    SaveMode.Overwrite

    /**
      * saveAsParquetFile源码：
      * def saveAsParquetFile(path: String): Unit = {
        if (sqlContext.conf.parquetUseDataSourceApi) {
          save("org.apache.spark.sql.parquet", SaveMode.ErrorIfExists, Map("path" -> path))
        } else {
          sqlContext.executePlan(WriteToFile(path, logicalPlan)).toRdd
        }
  }
      */
     // accessDF.registerTempTable("page_view_table")
   
      accessDF.write.parquet("hdfs://localhost:9000/user/data/page_view_data.parquet")

    sc.stop()
  }

}