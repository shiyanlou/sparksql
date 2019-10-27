package com.shiyanlou.shinelon.ETLJob

import com.shiyanlou.shinelon.Dao.MovieDao
import com.shiyanlou.shinelon.domain.{MovieRate, WeekCnt}
import com.shiyanlou.shinelon.utils.DateUtil
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object AnalyzeMovie {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("AnalyzeMovie").master("local[2]").getOrCreate()
    val path =args(0)
    select(path,spark)
    top10Movie(path,spark)

  }

  def creatTable(path:String,spark:SparkSession): DataFrame ={
    val text=spark.sparkContext.textFile(path)

    val schemaString = "userId movieId rate week"
    
    val fields = schemaString.split(" ").map(fieldName => StructField(fieldName,IntegerType, nullable = true))
    val schema = StructType(fields)
    
    val rowRDD = text.map(_.split("\t")).map(attributes => Row(attributes(0).toInt, attributes(1).toInt,attributes(2).toInt,DateUtil.dateToWeek(attributes(3).toLong)))

    val df=spark.createDataFrame(rowRDD,schema)

    df.createOrReplaceTempView("movie")
      df
  }

  def select(path:String,spark:SparkSession): Unit ={
    creatTable(path,spark)
    spark.sql("select week,count(1) cnt from movie group by week order by cnt desc").rdd.foreach(line => {
      val week=line(0).toString.toInt
      val cnt=line(1).toString.toInt
      MovieDao.insertWeekCnt(WeekCnt(week,cnt))
    })



  }

  def top10Movie(path:String,spark:SparkSession): Unit ={
    creatTable(path,spark)
    spark.sql("select movieId,rate from movie order by rate desc limit 10").rdd.foreach(line => {
      val movieId=line(0).toString.toInt
      val rate=line(1).toString.toInt
      MovieDao.insertMovieRate(MovieRate(movieId,rate))
    })
  }

}
