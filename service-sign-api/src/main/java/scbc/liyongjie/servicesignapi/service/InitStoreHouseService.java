package scbc.liyongjie.servicesignapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import scbc.liyongjie.servicesignapi.dao.StoreHousePoMapper;
import scbc.liyongjie.servicesignapi.exception.StoreHouseBuildException;
import scbc.liyongjie.servicesignapi.po.StoreHousePo;
import scbc.liyongjie.servicesignapi.util.UUIDUtils;

import javax.annotation.Resource;
import java.io.File;

/**
 * @Author:SCBC_LiYongJie
 * @time:2022/3/13
 */

@Service
@PropertySource(value = {"classpath:config.properties"},encoding="utf-8")
public class InitStoreHouseService {

    private static final Logger log = LoggerFactory.getLogger(InitStoreHouseService.class);

    @Resource
    private StoreHousePoMapper storeHousePoMapper;

    @Value("${upload.storePath}")
    private String rootPath;

    @Async
    public void initStoreHouse(String number){
        String storeHouseUUID = UUIDUtils.getUUID();

        //朴实无华的创建一个专属文件夹作为user的视频仓库
        String storeHousePath = rootPath+storeHouseUUID;
        File storeHouseFolder= new File(storeHousePath);

        if (!storeHouseFolder.mkdir())
            throw new StoreHouseBuildException();
        log.info("初始化仓库创建成功");

        //db
        StoreHousePo storeHousePo = new StoreHousePo();
        storeHousePo.setStorehouse(storeHouseUUID);
        storeHousePo.setNumber(number);
        storeHousePoMapper.insert(storeHousePo);

        log.info("仓库信息已存储至数据库-------{}",storeHousePo);

    }

}
