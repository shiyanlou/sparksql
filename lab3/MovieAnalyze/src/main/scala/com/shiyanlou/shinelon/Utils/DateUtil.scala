package com.shiyanlou.shinelon.utils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object DateUtil {
  def main(args: Array[String]): Unit = {
    val week=dateToWeek(881250949)
    println(week)

  }

  
  def dateToWeek(timeStamp:Long):Int={
    val simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val format=simple.format(new Date(timeStamp*1000))
    val date=simple.parse(format)
    val calendar=Calendar.getInstance()
    calendar.set(date.getYear,date.getMonth-1,date.getDay)
    val weekDay=calendar.get(Calendar.DAY_OF_WEEK)
    weekDay
  }


}

