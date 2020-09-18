package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.mapper.CommonMapper;
import org.jeecg.modules.business.service.ICommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class CommonService implements ICommonService {
    private static final String TABLE_SCHEMA = "jeecg-boot-test";
    @Resource
    private CommonMapper commonMapper;
    private final static Logger logger= LoggerFactory.getLogger(CommonService.class);

    /**
     * 检查表是否存在
     * @param tableName
     * @return
     */
    @Override
    public boolean checkTableExists(String tableName) {
        try {
            Integer count = commonMapper.checkTableExistsWithSchema(TABLE_SCHEMA, tableName);
            return count == 1;
        } catch (Exception e) {
            logger.error("使用information_schema检测表失败", e);
            Map<String, String> list = commonMapper.checkTableExistsWithShow(tableName);
            if(!CollectionUtils.isEmpty(list)) {
                return true;
            }
        }

        return false;
    }
}
