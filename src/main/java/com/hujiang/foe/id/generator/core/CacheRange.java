package com.hujiang.foe.id.generator.core;

/**
 * 缓存区间
 * Created by 三石她爸
 * Date : 2016-08-26
 */
public class CacheRange {
    private long min;
    private long max;

    public CacheRange(long min, long max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Gets max.
     *
     * @return Value of max.
     */
    public long getMax() {
        return max;
    }

    /**
     * Sets new min.
     *
     * @param min New value of min.
     */
    public void setMin(long min) {
        this.min = min;
    }

    /**
     * Gets min.
     *
     * @return Value of min.
     */
    public long getMin() {
        return min;
    }

    /**
     * Sets new max.
     *
     * @param max New value of max.
     */
    public void setMax(long max) {
        this.max = max;
    }
}
