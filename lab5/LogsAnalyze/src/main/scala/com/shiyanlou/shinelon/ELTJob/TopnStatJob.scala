package com.shiyanlou.shinelon.ELTJob

import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

object TopnStatJob {

  def main(args: Array[String]): Unit = {
    val conf =new SparkConf()
      .setAppName("TopNStatJob")
      .setMaster("local[10]")

    val sc=new SparkContext(conf)

    val sqlContext=new SQLContext(sc)

    val tableDF=sqlContext.parquetFile("hdfs://localhost:9000/user/data/page_view_data.parquet")

    tableDF.registerTempTable("log")

    //    tableDF.printSchema()

    //    tableDF.show(100)
      //需求一功能实现 
       TopNCity(sqlContext,tableDF)
      //需求二功能实现
       TimeTopByCity(sqlContext,tableDF)

  }


  /**
    * 统计访问网站的城市的访问数量
    * @param sqlContext
    * @param tableDF
    */
  def TopNCity(sqlContext: SQLContext,tableDF:DataFrame): Unit ={


    //使用SQL的方式进行统计
    val cityTopN=sqlContext.sql("select day,city,count(1) as times from log " +
      "where day='20150831' " +
      "group by city,day " +
      "order by times desc")


    //    cityTopN.show()

    /**
      * 将统计结果存入Mysql数据库中
      */
    val pro=new Properties
    pro.setProperty("driver","com.mysql.jdbc.Driver")
    pro.setProperty("user","root")
    pro.setProperty("password","")
    pro.setProperty("SaveMode","Overwrite")
    cityTopN.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://127.0.0.1:3306/hive?user=hive&password=hive","city_topn",pro)

  }

  /**
    * 按照城市分组统计访问网站的最频繁的时间段
    * @param sqlContext
    * @param tableDF
    */
  def TimeTopByCity(sqlContext: SQLContext,tableDF:DataFrame): Unit ={
    val timeTop=sqlContext.sql("select t.hour,count(*) cnt from " +
      "(select substring(time,12,2) hour from log) t " +
      "group by t.hour " +
      "order by cnt desc")

    //    timeTop.take(24).foreach(println)

    val pro=new Properties
    pro.setProperty("driver","com.mysql.jdbc.Driver")
    pro.setProperty("user","root")
    pro.setProperty("password","")
    pro.setProperty("SaveMode","Overwrite")
    timeTop.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://127.0.0.1:3306/hive?user=hive&password=hive","time_topn",pro)

  }

}

