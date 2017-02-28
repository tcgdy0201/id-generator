package com.hujiang.foe.id.generator.core;

import com.brevy.cache.codis.JodisClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 通过codis获得缓存
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public class CodisUniqueNumberProducer implements UniqueNumberProducer, InitializingBean {

    private Logger log = LoggerFactory.getLogger(UniqueNumberProducer.class);

    private JodisClient jodisClient;//codisClient

    private String cacheKey;//cache key in redis

    private IdGenConfig idGenConfig;
    /**
     * 缓存keys
     */
    private Queue<Long> queue;

    public CodisUniqueNumberProducer(JodisClient jodisClient, String cacheKey, IdGenConfig idGenConfig) {
        this.jodisClient = jodisClient;
        this.cacheKey = cacheKey;
        this.idGenConfig = idGenConfig;

    }


    /**
     * 获取下一唯一数字
     *
     * @return
     */
    @Override
    public long next() {
        long resp;
        if(!idGenConfig.isCache()){
            resp = incrAndCompare(1);
        }else{
            Long val = queue.poll();
            if(val==null){
                fullfillQueue(idGenConfig.getCacheSize());
                resp = next();
            }else {
                resp = val;
            }
        }
        return resp;
    }
    private void fullfillQueue(int size){
        log.info("缓存耗尽,重新加载...");
        synchronized (queue){
            CacheRange range = fetch(size);
            for(long i=range.getMin();i<range.getMax();i++){
                queue.add(i);
            }
        }
        log.info("缓存耗尽,重新加载完毕...");

    }

    /**
     * 按偏移量增加,并视情况reset
     * @param size
     * @return
     */
    private long incrAndCompare(int size){
        long val = jodisClient.incrBy(cacheKey, size);
        if (val > 9000000000000000000L) {//如果即将接近Long.MaxValue,重置cache
            resetCache();
        }

        return val;
    }

    /**
     * 获取缓存
     *
     * @param size
     * @return
     */
    private CacheRange fetch(int size) {
        Long val = incrAndCompare(size);

        return new CacheRange(val - size, val);
    }

    /**
     * 重置缓存
     */
    private void resetCache() {

        String lockKey = cacheKey + ".lock";

        //可以增加个临时锁,避免集群环境下的并发问题(因为会涉及del)
        String val = jodisClient.get(lockKey, String.class);
        //if (val != null && !"".equals(val)) {
        if(StringUtils.isNotBlank(val)){
            log.warn("其他进程/线程正在重置缓存中...本机不做处理...");
            return;
        }
        jodisClient.set(lockKey, "true", 60);//60秒超时
        jodisClient.del(cacheKey, lockKey);//reset key value
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //if (idGenConfig == null || jodisClient == null || cacheKey == null || "".equals(cacheKey)) {
        if (idGenConfig == null || jodisClient == null ||  StringUtils.isBlank(cacheKey)) {
            throw new Exception("idGenConfig/jodisClient/cacheKey can not be null");
        }
        if(idGenConfig.isCache()){
            queue = new ConcurrentLinkedQueue<>();
        }
    }


}
