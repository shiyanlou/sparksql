package com.shiyanlou.shinelon.Dao

import java.sql.{Connection, PreparedStatement}

import com.shiyanlou.shinelon.domain.{MovieRate, WeekCnt}
import com.shiyanlou.shinelon.utils.MysqlUtil

object MovieDao {
  var connection:Connection=null
  var pstm:PreparedStatement=null

  def insertWeekCnt(weekCnt: WeekCnt): Unit ={
    try {
      connection = MysqlUtil.getConnection()
      pstm = connection.prepareStatement("insert into week_cnt values(?,?)")
      pstm.setInt(1,weekCnt.week)
      pstm.setInt(2,weekCnt.cnt)
      pstm.execute()
    }catch {
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtil.release(connection,pstm)
    }
  }

  def insertMovieRate(movieRate: MovieRate): Unit ={
    try {
      connection = MysqlUtil.getConnection()
      pstm = connection.prepareStatement("insert into movie_rate values(?,?)")
      pstm.setInt(1,movieRate.movieId)
      pstm.setInt(2,movieRate.rate)
      pstm.execute()
    }catch {
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtil.release(connection,pstm)
    }
  }

}

