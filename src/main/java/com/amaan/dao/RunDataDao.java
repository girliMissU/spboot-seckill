package com.amaan.dao;

import com.amaan.domain.RunData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 佛祖保佑，永无BUG
 *
 * @author AMAAN
 * springboot-mybatis-redis
 * 2020-11-30 19:09
 */
//@Mapper
public interface RunDataDao {
    @Insert("insert into run_data(rotate,current,garage_id,month) values(${rotate},${current},${garageId},${month})")
    void addData(RunData data);
}
