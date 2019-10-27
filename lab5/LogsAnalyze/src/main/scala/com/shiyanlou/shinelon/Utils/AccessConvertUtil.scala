package com.shiyanlou.shinelon.Utils

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import com.ggstar.util.ip.IpHelper

object AccessConvertUtil {
  val structType=StructType{
    Array(
      StructField("url",StringType),
      StructField("traffic",StringType),
      StructField("ip",StringType),
      StructField("city",StringType),
      StructField("time",StringType),
      StructField("day",StringType)
    )
  }

  /**
    * 将每一行的输入信息转换为输出信息
    * @param accessLog
    * @return
    */
  def parseLog(accessLog:String) ={
    try{
      val splits=accessLog.split("\t")
      val url=splits(2)
      val ip=splits(0)
      val traffic=splits(3)
      val city=IpHelper.findRegionByIp(ip)              //借助第三方工具解析ip地址
      val time=splits(1)
      val day=time.substring(0,10).replace("-","")      //截取天信息
      Row(url,traffic,ip,city,time,day)
    }catch {
      case e:Exception=>{
        //        throw e
        println("解析异常")
        Row(0)
      }
    }

  }

}