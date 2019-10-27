package com.shiyanlou.shinelon.job

import java.util.Properties

import org.apache.spark.sql.{Row, SQLContext, SaveMode}
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

object MovieGrade {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("MovieGrade")
      .setMaster("local[4]")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val gradeRDD=sc.textFile("/home/hadoop/MovieGrade/src/main/scala/com/shiyanlou/shinelon/data/film_grade.txt")

    val rowRDD=gradeRDD.map(line=>{
      val tmp=line.split(" ")
      (tmp(0),tmp(1).toInt)
    }).reduceByKey(_+_)
      .map(attr => Row(attr._1.toString,attr._2.toLong))
    //      .foreach(println)

    val schema=StructType{
      Array(
        StructField("grade",StringType),
        StructField("cnt",LongType)
      )
    }

    val gradeDF=sqlContext.createDataFrame(rowRDD,schema)

    gradeDF.registerTempTable("moviegrade")

    val grade=sqlContext.sql("select * from moviegrade")


    val pro=new Properties
    pro.setProperty("driver","com.mysql.jdbc.Driver")
    grade.write.mode(SaveMode.Overwrite)
      .jdbc("jdbc:mysql://127.0.0.1:3306/hive?user=hive&password=hive","moviegrade",pro)

  }


}
