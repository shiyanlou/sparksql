package com.shiyanlou.shinelon.Dao;

import com.shiyanlou.shinelon.Domain.HourTimes;
import com.shiyanlou.shinelon.Utils.MysqlUtil;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class HourTimesDao {

    private static MysqlUtil mysqlUtil;

    public List<HourTimes> selectCityTimes() {
        Connection connection=null;
        PreparedStatement psmt=null;
        List<HourTimes> list = new ArrayList<HourTimes>();
        try {
            connection= MysqlUtil.getConnection();
            psmt= connection.prepareStatement("select * from time_topn");
            ResultSet resultSet = psmt.executeQuery();
            while (resultSet.next()) {
                HourTimes hourTimes = new HourTimes();
                hourTimes.setHour(resultSet.getInt(1));
                hourTimes.setTimes(resultSet.getLong(2));
                list.add(hourTimes);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            MysqlUtil.release();
        }
        return list;
    }

}
