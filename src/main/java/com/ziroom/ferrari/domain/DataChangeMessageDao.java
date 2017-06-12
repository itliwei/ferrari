package com.ziroom.ferrari.domain;

import com.ziroom.rent.common.orm.OrmFrameEnum;
import com.ziroom.rent.common.orm.dao.BaseDao;
import com.ziroom.rent.common.orm.dao.annotation.DaoDescription;
import org.springframework.stereotype.Repository;

/**
 * 资产、楼盘各系统需要配置msgSendSettingBean
 *
 * @Author zhoutao
 * @Date 2017/6/7
 */
@Repository
@DaoDescription(ormFrame = OrmFrameEnum.JDBC_TEMPLATE,
        settingBean = "masterOnlyJdbcSettings")
public class DataChangeMessageDao extends BaseDao<DataChangeMessageEntity> {
}
