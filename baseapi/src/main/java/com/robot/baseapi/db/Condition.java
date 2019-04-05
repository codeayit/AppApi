package com.robot.baseapi.db;


import android.support.annotation.NonNull;

/**
 * Created by lny on 2018/4/13.
 * 差选条件
 */

public class Condition {
    /**
     * 键
     */
    private String key;
    /**
     * 符号
     */
    private String symbol;
    /**
     * 值
     */
    private String value;


    public Condition(@NonNull String key, @NonNull String symbol, @NonNull Object value) {
        this.key = key;
        this.symbol = symbol;
        this.value = String.valueOf(value);
    }

    public String getKey() {
        return key;
    }

    public String getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value;
    }

    public interface Field {
        String key = "key";
        String symbol = "symbol";
        String value = "value";
    }

    public interface Symbol {
        String equal = "=";
        String unequal = "!=";
        String little = "<";
        String bigger = ">";
        String like = "like";
    }

    public void reset(String symbol, Object value) {
        this.symbol = symbol;
        this.value = String.valueOf(value);
    }

    /**
     * 生成实例
     *
     * @param key
     * @param symbol
     * @param value
     * @return
     */
    public static Condition newInstance(@NonNull String key, @NonNull String symbol, @NonNull Object value) {
        return new Condition(key, symbol, String.valueOf(value));
    }

    /**
     * 默认等于
     *
     * @param key
     * @param value
     * @return
     */
    public static Condition newInstance(@NonNull String key, @NonNull Object value) {
        return new Condition(key, Symbol.equal, String.valueOf(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Condition condition = (Condition) o;

        return key.equals(condition.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
