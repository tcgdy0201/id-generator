package com.hujiang.foe.id.generator.core;

/**
 * 集群内
 * 全局唯一数字的获取器
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public interface UniqueNumberProducer {
    /**
     * 获取下一唯一数字
     * @return
     */
    long next();
}
