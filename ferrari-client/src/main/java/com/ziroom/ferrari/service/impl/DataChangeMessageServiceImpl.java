package com.ziroom.ferrari.service.impl;

import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.ferrari.service.DataChangeMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DataChangeMessage Service实现类
 * Created by homelink on 2017/6/2 0002.
 */

@Slf4j
@Service
public class DataChangeMessageServiceImpl implements DataChangeMessageService {
    @Autowired
    private DataChangeMessageDao dataChangeMessageDao;

    @Override
    public int insert(DataChangeMessage dataChangeMessage) {
        return dataChangeMessageDao.insert(dataChangeMessage);
    }
}
