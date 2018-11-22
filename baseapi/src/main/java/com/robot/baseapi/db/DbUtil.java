package com.robot.baseapi.db;

import android.content.ContentValues;
import android.text.TextUtils;

import org.litepal.FluentQuery;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;
import java.util.Map;

/**
 * Created by lny on 2018/3/21.
 */

public class DbUtil {

    public static <T extends LitePalSupport> int clear(Class<T> clazz) {
        return LitePal.deleteAll(clazz);
    }


    /**
     * 批量保存
     *
     * @param list
     */
    public static void save( List<? extends LitePalSupport> list) {
        LitePal.saveAll(list);
    }

    /**
     * 单个保存
     *
     * @param ds
     * @return
     */
    public static boolean save( LitePalSupport ds) {
        return ds.save();
    }

    /**
     * 计算数据库中有多少条
     *
     * @param clazz
     * @return
     */
    public static <T extends LitePalSupport> int count(Class<T> clazz) {
        return count(clazz,null);
    }

    public static <T extends LitePalSupport> int count(Class<T> clazz, ConditionBuilder conditions) {
        FluentQuery cq = null;
        if (conditions!=null && conditions.size()>0){
            cq = LitePal.where(conditions.condition());
        }
        return  cq==null? LitePal.count(clazz) : cq.count(clazz);
    }


    /**
     * 查询所有,默认激进查询
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends LitePalSupport> List<T> findAll(Class<T> clazz) {
        return LitePal.findAll(clazz, true);
    }
    public static <T extends LitePalSupport> T findFirst(Class<T> clazz){
        return findFirst(clazz,null,null);
    }

    public static <T extends LitePalSupport> T findFirst(Class<T> clazz, Map<String, Order> orders){
        return findFirst(clazz,orders,null);
    }

    public static <T extends LitePalSupport> T findFirst(Class<T> clazz, ConditionBuilder conditions){
        return findFirst(clazz,null,conditions);
    }

    public static <T extends LitePalSupport> T findFirst(Class<T> clazz, Map<String, Order> orders, ConditionBuilder conditions){
        List<T> ts = find(clazz, conditions, orders, 1, 0);
        if (ts.isEmpty()){
            return null;
        }else{
            return find(clazz, conditions, orders, 1, 0).get(0);
        }
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, ConditionBuilder conditions) {
        return find(clazz, conditions, null, -1, -1);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, ConditionBuilder conditions, int limit) {
        return find(clazz, conditions, null, limit, -1);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, ConditionBuilder conditions, Map<String, Order> orders, int limit) {
        return find(clazz, conditions, orders, limit, -1);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz) {
        return find(clazz, null, null, -1, -1);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, int limit) {
        return find(clazz, null, null, limit, -1);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, Map<String, Order> orders, int limit, int offset) {
        return find(clazz, null, orders, limit, offset);
    }

    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, int limit, int offset) {
        return find(clazz, null, null, limit, offset);
    }


    /**
     * @param conditions 满足条件的已经存在,则不保存
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends LitePalSupport> boolean save(ConditionBuilder conditions, T t) {
        List<? extends LitePalSupport> list = find(t.getClass(), conditions);
        if (list.isEmpty()) {
            return save(t);
        } else {
            return false;
        }
    }

    /**
     * 根据条件查询,采用激进查询
     *
     * @param clazz
     * @param conditions
     * @param orders
     * @param limit
     * @param offset
     * @param <T>
     * @return
     */
    public static <T extends LitePalSupport> List<T> find(Class<T> clazz, ConditionBuilder conditions, Map<String, Order> orders, int limit, int offset) {
        FluentQuery cq =null;
        if (conditions != null && conditions.size() != 0) {
            String[] cs = conditions.condition();
//            KLog.d("查询条件 : "+ JSON.toJSONString(cs));
            cq = (cq == null ? LitePal.where(cs): cq.where(cs));
        }

        if (orders != null && !orders.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Order> entry : orders.entrySet()) {
                sb.append(entry.getKey() + " " + entry.getValue() + ",");
            }
            if (!TextUtils.isEmpty(sb.toString())) {
                cq = cq == null ? LitePal.order(sb.substring(0, sb.length() - 1)) : cq.order(sb.substring(0, sb.length() - 1));
            }
        }
        if (limit > 0) {
            cq = cq == null ? LitePal.limit(limit) : cq.limit(limit);
        }

        if (offset > 0) {
            cq = cq == null ? LitePal.offset(offset) : cq.offset(offset);
        }

        if (cq == null) {
            return LitePal.findAll(clazz, true);
        } else {
            return cq.find(clazz, true);
        }
    }

    /**
     * 更新
     *
     * @param clazz
     * @param values
     * @param conditions
     * @param <T>
     * @return
     */
    public static <T extends LitePalSupport> int update(Class<T> clazz, ContentValues values, ConditionBuilder conditions) {
        return LitePal.updateAll(clazz, values, conditions.condition());
    }

    public static <T extends LitePalSupport> int update(Class<T> clazz, ContentValues values) {
        return LitePal.updateAll(clazz, values);
    }

    public static <T extends LitePalSupport> int update(T t, long id) {
        if (t.isSaved()) {
            return t.update(id);
        } else {
            throw new RuntimeException("t is not saved before update");
        }
    }

    /**
     * 删除
     * @param clazz
     * @param conditions
     * @param <T>
     * @return
     */
    public static <T extends LitePalSupport> int delete(Class<T> clazz, ConditionBuilder conditions){
        return LitePal.deleteAll(clazz,conditions.condition());
    }

    public static <T extends LitePalSupport> int delete(Class<T> clazz){
        return LitePal.deleteAll(clazz);
    }

    public static <T extends LitePalSupport> int delete(T t){
        if (t!=null && t.isSaved()){
            return t.delete();
        }
        return 0;
    }

}
