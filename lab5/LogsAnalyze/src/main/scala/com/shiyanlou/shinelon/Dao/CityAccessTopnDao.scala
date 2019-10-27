package com.shiyanlou.shinelon.Dao

import java.sql.{Connection, PreparedStatement}

import com.shiyanlou.shinelon.Domain.{CityAccessTopn, TimeAccessTopn, TrafficAccessTopn}
import com.shiyanlou.shinelon.Utils.MysqlUtil

import scala.collection.mutable.ListBuffer

object CityAccessTopnDao {
  var connection:Connection=null
  var pstm:PreparedStatement=null

  def insertCityTopn(list:ListBuffer[CityAccessTopn]): Unit ={
    try{
      connection=MysqlUtil.getConnection()
      connection.setAutoCommit(false)
      val  sql="insert into city_topn(day,city,time) values(?,?,?)"
      pstm=connection.prepareStatement(sql)

      for(city<-list){
        pstm.setString(1,city.day)
        pstm.setString(2,city.city)
        pstm.setLong(3,city.times)
        pstm.addBatch()          
      }
      pstm.executeBatch()
      connection.commit()

    }catch {
      case e:Exception=>e.printStackTrace()
    }finally {
      MysqlUtil.release(connection,pstm)
    }
  }

  /**
    *
    * @param list
    */
  def insertTimeTopn(list:ListBuffer[TimeAccessTopn]): Unit ={
    try{
      connection=MysqlUtil.getConnection()
      connection.setAutoCommit(false)
      val  sql="insert into time_topn(hour,cnt) values(?,?)"
      pstm=connection.prepareStatement(sql)

      for(time<-list){
        if(time.cnt!=null&&time.hour!=null){
          pstm.setString(1,time.hour)
          pstm.setLong(2,time.cnt)

          pstm.addBatch()
        }

      }
      pstm.executeBatch()
      connection.commit()

    }catch {
      case e:Exception=>e.printStackTrace()
    }finally {
      MysqlUtil.release(connection,pstm)
    }
  }


}