package com.ziroom.ferrari.dao;


import com.ziroom.ferrari.DataChangeMessage;
import org.springframework.stereotype.Repository;


@Repository
public interface DataChangeMessageDao {

    int deleteByPrimaryKey(Long id);

    int insert(DataChangeMessage record);

    int insertSelective(DataChangeMessage record);

    DataChangeMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataChangeMessage record);

    int updateByPrimaryKey(DataChangeMessage record);
}