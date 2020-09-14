package com.store.common.repository;

import com.store.common.data.base.PageList;
import com.store.common.data.base.PageUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jay on 2016/1/1.
 */
public interface BaseRepository {

    /**
     * 功能：查询，支持单个入参 或 多个以数组形式传入入参
     * 备注：1、select from 之间的具体属性，(1)左侧","，要用"#"代替，(2)右侧，要写具体的对象属性名称
     * @param sql
     * @param clazz = null，表示返回 List<Map<K,V>>，但不建议使用传 null
     * @param params != null，无入参情形，传 new Object[]{}
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> List<T> queryList(String sql, Class<T> clazz, Object... params) throws Exception;

    /**
     * 功能：查询
     * 备注：1、select from 之间的具体属性，(1)左侧","，要用"#"代替，(2)右侧，要写具体的对象属性名称
     * @param sql
     * @param clazz = null，表示返回 List<Map<K,V>>，但不建议使用传 null
     * @param params != null，无入参情形，传 new ArrayList()
     * @param <T>
     * @return
     * @throws Exception
     */
    //<T> List<T> queryList(String sql, Class<T> clazz, List params) throws Exception;

    /**
     * 功能：查询，支持单个入参 或 多个以数组形式传入入参
     * 备注：1、select from 之间的具体属性，(1)左侧","，要用"#"代替，(2)右侧，要写具体的对象属性名称
     * @param sql
     * @param params != null，无入参情形，传 new Object[]{}
     * @return 返回 List<Map<K,V>>
     * @throws Exception
     */
    List queryListForMap(String sql, Object... params) throws Exception;

    /**
     * 功能：查询一个
     * 备注：1、select from 之间的具体属性，(1)左侧","，要用"#"代替，(2)右侧，要写具体的对象属性名称
     * @param clazz = null，表示返回 Map<K,V>
     * @param params != null，无入参情形，传 new Object[]{}
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T queryOneObj(String sql, Class<T> clazz, Object... params) throws Exception;

    /**
     * 功能：查询出某一具体属性
     * 备注：1、select from 之间的具体属性，(1)左侧","，要用"#"代替，(2)右侧，要写具体的对象属性名称
     * @param params != null，无入参情形，传 new Object[]{}
     * @return null：没查找到，否则，根据具体类型转换
     * @throws Exception
     */
    String queryOneObjAttr(String sql, Object... params) throws Exception;

    /**
     * 功能：计数，支持单个入参 或 多个以数组形式传入入参
     * 备注：1、格式：select count(1)
     * @param sql
     * @param params != null，无入参情形，传 new Object[]{}
     * @return
     */
    long getCount(String sql, Object... params) throws Exception;

    /**
     * 功能：计数
     * 备注：1、格式：select count(1)
     * @param sql
     * @param params != null，无入参情形，传 new ArrayList()
     * @return
     */
    //long getCount(String sql, List params) throws Exception;

    /**
     * 功能：条件分页查询
     * 备注：1、select from 之间的具体属性的左侧","，要用"#"代替，而右侧，要写具体的对象属性名称
     * @param sql
     * @param clazz = null，表示返回 PageList<Map<K,V>>，但不建议使用传 null
     * @param params 内应含有分页信息
     * @param pageUtil != null
     * @param sortMap = null，表示无需排序，也就是默认排序，size > 0，表示按照顺序排序， key：表属性名，value：desc 或 asc
     * @return
     * @throws Exception
     */
    <T> PageList<T> queryPage(String sql, Class<T> clazz, PageUtil pageUtil, LinkedHashMap<String, String> sortMap, Object... params) throws Exception;
}