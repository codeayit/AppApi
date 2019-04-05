package com.robot.baseapi.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lny on 2018/4/13.
 * 创建查询符号条件
 */

public class ConditionBuilder {
    private ConnectSymbol connectSymbol = new ConnectSymbol();
    private ConnectCondition connectCondition = new ConnectCondition();


    public static ConditionBuilder getInstance() {
        return new ConditionBuilder();
    }


    private List<Condition> conditions;
    private List<String> symbols;
    private String[] values;
    private int size;

    public ConnectCondition start() {
        clear();
        return connectCondition;
    }

    public ConditionBuilder() {
        conditions = new ArrayList<>();
        symbols = new ArrayList<>();
    }

    public int size() {
        return size;
    }


    /**
     * 生成条件 str
     *
     * @return
     */
    public String[] condition() {
        StringBuilder sb = new StringBuilder();
        Condition condition = null;
        values = new String[conditions.size() + 1];
        for (int i = 0; i < conditions.size(); i++) {
            condition = conditions.get(i);
            values[i + 1] = String.valueOf(condition.getValue());
            sb.append(condition.getKey() + condition.getSymbol() + "?");
            if (i != conditions.size() - 1 && i < symbols.size()) {
                //除去最后一个
                sb.append(" " + symbols.get(i) + " ");
            }
        }
        values[0] = sb.toString();
//        KLog.json(JSON.toJSONString(values));
        return values;
    }


    /**
     * 清空
     */
    public void clear(){
        symbols.clear();
        conditions.clear();
        size=0;
    }


    /**
     * 条件并联符号
     */
    public class ConnectSymbol {

        public ConnectCondition and() {
            symbols.add("and");
            return connectCondition;
        }

        public ConnectCondition or() {
            symbols.add("or");
            return connectCondition;
        }

        public ConditionBuilder end() {
            return ConditionBuilder.this;
        }
    }

    /**
     * 链接条件
     */
    public class ConnectCondition {
        public ConnectSymbol addCondition(Condition qc) {
            size++;
            conditions.add(qc);
            return connectSymbol;
        }

        public ConnectSymbol addCondition(String key, Object value) {
            addCondition(Condition.newInstance(key, String.valueOf(value)));
            return connectSymbol;
        }



        public ConnectSymbol addCondition(String key, String symbol, Object value) {
            addCondition(Condition.newInstance(key, symbol, String.valueOf(value)));
            return connectSymbol;
        }


        public ConnectCondition resetCondition(Condition qc) {
            if (conditions.contains(qc)) {

                for (Condition condition : conditions){
                    if (condition.getKey().equals(qc.getKey())){
                        condition.reset(qc.getSymbol(), qc.getValue());
                    }
                }
            }
            return connectCondition;
        }

        public ConnectCondition resetCondition(String key, String symbol, Object value) {
            return resetCondition(Condition.newInstance(key, symbol, String.valueOf(value)));
        }

        public ConnectCondition resetCondition(String key, Object value) {
            return resetCondition(Condition.newInstance(key, String.valueOf(value)));
        }
    }


}
