package com.shiyanlou.shinelon.Dao;


import com.shiyanlou.shinelon.Domain.CityTimes;
import com.shiyanlou.shinelon.Utils.MysqlUtil;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class CityTimesDao {

    private static MysqlUtil mysqlUtil;

    public List<CityTimes> selectCityTimes() throws Exception{
        List<CityTimes> list = new ArrayList<CityTimes>();
        Connection connection=null;
        PreparedStatement psmt=null;
        try {
            connection = MysqlUtil.getConnection();
            psmt = connection.prepareStatement("select city,times from city_topn");
            ResultSet resultSet = psmt.executeQuery();
            while (resultSet.next()) {
                CityTimes cityTimes = new CityTimes();
                cityTimes.setCity(resultSet.getString(1));
                cityTimes.setTimes(resultSet.getLong(2));
                list.add(cityTimes);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            MysqlUtil.release();
        }
        return list;
    }

}
