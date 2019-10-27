package com.shiyanlou.shinelon.job

import java.util.Properties

import org.apache.spark.sql.{Row, SaveMode, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[4]")
      .appName("WordCount")
      .getOrCreate()

    val schema = StructType{
      Array(
        StructField("word",StringType),
        StructField("count",IntegerType)
      )
    }

    val rowRDD=spark.sparkContext.textFile("hdfs://localhost:9000/user/movie/data/wordGrad.txt")
      .map((_,1)).reduceByKey(_+_).sortBy(_._2,false)
      .map(attributes => Row(attributes._1, attributes._2))

    val peopleDF = spark.sqlContext.createDataFrame(rowRDD, schema)

    peopleDF.registerTempTable("wordcount")

    val wordGrade=spark.sql("select * from wordcount limit 200")

    val pro=new Properties
    pro.setProperty("driver","com.mysql.jdbc.Driver")
    wordGrade.write.mode(SaveMode.Overwrite)
      .jdbc("jdbc:mysql://127.0.0.1:3306/hive?user=hive&password=hive","wordcount",pro)

  }

}
