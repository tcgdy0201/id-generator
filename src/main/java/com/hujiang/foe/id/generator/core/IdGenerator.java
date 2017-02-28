package com.hujiang.foe.id.generator.core;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;

/**
 * 通用id生成器
 * 线程安全
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public class IdGenerator implements InitializingBean{

    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    private UniqueNumberProducer uniqueNumberProducer;//缓存获取器
    private IdGenConfig idGenConfig;//配置信息

    public IdGenerator(UniqueNumberProducer uniqueNumberProducer, IdGenConfig idGenConfig) {
        this.uniqueNumberProducer = uniqueNumberProducer;
        this.idGenConfig = idGenConfig;
    }


    /**
     * 下一个单号
     * @return
     */
    public String next(){
        StringBuilder sb = new StringBuilder();
        int idLength = idGenConfig.getLength() - (idGenConfig.getPrefix().length() + idGenConfig.getSuffix().length() + idGenConfig.getDatePattern().length());
        String uniqueId = Long.toString(uniqueNumberProducer.next());
        String strUniqueId = uniqueId.length()>idLength
                ? uniqueId.substring(uniqueId.length() - idLength)
                : StringUtils.leftPad(uniqueId, idLength, "0");
        return sb.append(idGenConfig.getPrefix()).append(DateTime.now().toString(DATE_PATTERN)).append(strUniqueId).append(idGenConfig.getSuffix()).toString();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if(uniqueNumberProducer ==null || idGenConfig==null){
            throw new Exception("uniqueNumberProducer && idGenConfig can not be null!");
        }

    }
}
