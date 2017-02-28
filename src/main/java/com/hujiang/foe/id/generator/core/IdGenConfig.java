package com.hujiang.foe.id.generator.core;

import org.springframework.beans.factory.InitializingBean;

/**
 * ID生成器配置
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public class IdGenConfig implements InitializingBean{
    private static final int DEFAULT_ID_LENGTH = 32;//最大长度
    private static final int MIN_ID_LENGHT = 5;//ID的最短长度

    private static final int DEFAULT_CACHE_SIZE = 1000;//缓存大小
    private static final int MIN_CACHE_SIZE = 10;//最小缓存大小
    private static final String DEFAULT_DATE_PATTERN = "yyyyMMddHHmmss";//日期格式
    private static final String DEFAULT_PREFIX = "";//默认前缀
    private static final String DEFAULT_SUFFIX = "";//默认前缀


    private String prefix = DEFAULT_PREFIX;//前缀
    private String suffix = DEFAULT_SUFFIX;//后缀
    private String datePattern = DEFAULT_DATE_PATTERN;//日期格式
    private int length = DEFAULT_ID_LENGTH;//id长度
    private boolean cache = true;//是否缓存
    private int cacheSize = 1000;//缓存数


    /**
     * Gets length.
     *
     * @return Value of length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets new datePattern.
     *
     * @param datePattern New value of datePattern.
     */
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    /**
     * Gets datePattern.
     *
     * @return Value of datePattern.
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * Sets new cacheSize.
     *
     * @param cacheSize New value of cacheSize.
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    /**
     * Gets prefix.
     *
     * @return Value of prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets suffix.
     *
     * @return Value of suffix.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets new prefix.
     *
     * @param prefix New value of prefix.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gets cache.
     *
     * @return Value of cache.
     */
    public boolean isCache() {
        return cache;
    }

    /**
     * Sets new length.
     *
     * @param length New value of length.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Sets new cache.
     *
     * @param cache New value of cache.
     */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
     * Gets cacheSize.
     *
     * @return Value of cacheSize.
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Sets new suffix.
     *
     * @param suffix New value of suffix.
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(cacheSize < MIN_CACHE_SIZE){
            throw new Exception("the minimum of cache size is " + MIN_CACHE_SIZE);
        }

        if ((prefix.length() + suffix.length() + datePattern.length()) > length - MIN_ID_LENGHT){
            throw new Exception("length is too short");
        }
    }
}
