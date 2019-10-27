package com.shiyanlou.shinelon.job

import java.io.{File, FileInputStream, InputStreamReader}
import java.util

import com.huaban.analysis.jieba.JiebaSegmenter
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object SegmentText {

  def main(args: Array[String]): Unit = {
    val conf=new SparkConf()
      .setMaster("local[4]")
      .setAppName("SegmentText")

    val sc = new SparkContext(conf)

    val stopWordMapRDD=sc.textFile("/home/hadoop/MovieGrade/src/main/scala/com/shiyanlou/shinelon/data/StopWord.txt").map(word => (word,1))

    val wordMapRDD=segmentWord(sc,"/home/hadoop/MovieGrade/src/main/scala/com/shiyanlou/shinelon/data/film_text.txt").map(word => (word,1))

    val joinRDD=wordMapRDD.leftOuterJoin(stopWordMapRDD)

    val filterRDD=joinRDD.filter(_._2._2.isEmpty)

    val wordCount=filterRDD.map(_._1).filter(!_.equals(" "))
      .saveAsTextFile("hdfs://localhost:9000/user/movie/data/wordGrad.txt")
  }


  def segmentWord(sc:SparkContext,path:String):RDD[String]={
    var array = new ArrayBuffer[String]
    val bufferList = new util.ArrayList[String]()
    var reader:InputStreamReader = new InputStreamReader(new FileInputStream(new File(path)))
    try{
      val text=FileUtils.readFileToString(new File(path))

      val jiebaSegmenter = new JiebaSegmenter()

      val list = jiebaSegmenter.process(text, SegMode.INDEX)
      val it = list.iterator()
      while(it.hasNext){

        var str=it.next()
        var arr=str.toString.split(",")
        var word=arr(0).substring(1)
        //        println(word)
        array += word
      }
    }catch {
      case e:Exception => e.printStackTrace()
    }finally {
      reader.close()
    }

    val rdd:RDD[String]=sc.parallelize(array)

    rdd
  }

}
