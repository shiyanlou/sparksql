package com.shiyanlou.shinelon.ELTJob

import com.shiyanlou.shinelon.Utils.TimeDateFormatUtil
import org.apache.spark.{SparkConf, SparkContext}

/**
  * step 1
  */
object DataFormatJob {def main(args: Array[String]): Unit = {
  val conf=new SparkConf()
    .setMaster("local[2]")
    .setAppName("DataFormatJob")

  val sc=new SparkContext(conf)
  //从HDFS文件系统上读取数据
  val textRDD=sc.textFile("hdfs://localhost:9000/user/data/pre_data/page_view_data.log")

  val dataRDD=textRDD.map(text=>{
    val data=text.split(" ")
    val ip=data(0).replace("\"","")
    val time=data(2)+" "+data(3)
    val traffic=data(8).replace("\"","")      
    var url=data(10).replace("\"","")         //访问的URL地址
    val log_data=ip+"\t"+TimeDateFormatUtil.parseDate(time)+"\t"+url+"\t"+traffic
    log_data
    //将清洗完的数据存入HDFS文件系统中
  }).saveAsTextFile("hdfs://localhost:9000/user/data/page_view_data.log")

}

}