package com.ziroom.ferrari.dao;

import com.ziroom.ferrari.domain.DataChangeMessage;
import com.ziroom.rent.common.orm.DialectEnum;
import com.ziroom.rent.common.orm.OrmFrameEnum;
import com.ziroom.rent.common.orm.dao.BaseDao;
import com.ziroom.rent.common.orm.dao.annotation.*;
import org.springframework.stereotype.Repository;

@Repository
@DaoDescription(ormFrame = OrmFrameEnum.JDBC_TEMPLATE,
				dialect = DialectEnum.MYSQL,
				masterDBAccessorBeanName = "masterJdbcTemplate",
				slaveDBAccessorBeanNames = {"slaveJdbcTemplate"})
public class DataChangeMessageDao extends BaseDao<DataChangeMessage> {
}