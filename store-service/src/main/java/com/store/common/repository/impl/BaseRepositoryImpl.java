package com.store.common.repository.impl;

import com.store.common.data.base.PageList;
import com.store.common.data.base.PageUtil;
import com.store.common.repository.BaseRepository;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by jay on 2016/1/1.
 */
public class BaseRepositoryImpl implements BaseRepository {

    private static final Logger LOG = LoggerFactory.getLogger(BaseRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public <T> List<T> queryList(String sql, Class<T> clazz, Object... params) {
        return returnList(sql, clazz, params);
    }

    /*
    @Override
    public <T> List<T> queryList(String sql, Class<T> clazz, List params) throws Exception {
        return queryList(sql.toString(), clazz, params.toArray());
    }
    */

    @Override
    public List queryListForMap(String sql, Object... params) throws Exception {
        return returnList(sql, null, params);
    }

    @Override
    public <T> T queryOneObj(String sql, Class<T> clazz, Object... params) throws Exception {
        List<T> list = returnList(sql, clazz, params);
        if (list == null || list.size() == 0) {
            return null;
        } else if (list.size() > 1) {
            throw new Exception("More than one result!");
        } else {
            return list.get(0);
        }
    }

    @Override
    public String queryOneObjAttr(String sql, Object... params) throws Exception {
        Map<String, Object> res = queryOneObj(sql, null, params);
        if (res == null) {
            return null;
        } else {
            for (Object v : res.values()) {
                return String.valueOf(v);
            }
            return null;
        }
    }

    @Override
    public long getCount(String sql, Object... params) throws Exception {
        Map<String, Object> res = queryOneObj(sql, null, params);
        if (res == null) {
            return 0;
        } else {
            for (Object v : res.values()) {
                return ((BigInteger) v).longValue();
            }
            return 0;
        }
    }

    /*
    @Override
    public long getCount(String sql, List params) throws Exception {
        return getCount(sql, params.toArray());
    }
    */

    @Override
    public <T> PageList<T> queryPage(String sql, Class<T> clazz, PageUtil pageUtil, LinkedHashMap<String, String> sortMap, Object... params) throws Exception {
        if (pageUtil == null || pageUtil.getPageNo() <= 0 || pageUtil.getPageSize() <= 0) {
            throw new Exception("PageUtil Error!");
        }
        String countSql = sql.toLowerCase();
        countSql = "select count(1) " + sql.substring(countSql.indexOf("from"));
        long c = getCount(countSql, params);
        if (c <= 0) {
            return new PageList(0, new ArrayList<>());
        }
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append(sql);
        if (sortMap != null && sortMap.size() > 0) {
            sqlBuild.append(" order by ");
            sortMap.forEach((key, value) -> {
                sqlBuild.append(key).append(" ").append(value).append(",");
            });
            sqlBuild.deleteCharAt(sqlBuild.length() - 1);
        }
        sqlBuild.append(" limit ").append(pageUtil.getStart()).append(",").append(pageUtil.getPageSize());
        List<T> list = queryList(sqlBuild.toString(), clazz, params);
        return new PageList(c, list != null ? list : new ArrayList());
    }

    private <T> List<T> returnList(String sql, Class<T> clazz, Object[] params) {
        List<T> list = null;
        String filedSql = sql;
        if (sql.contains("#")) {
            sql = sql.replaceAll("#", ",");
        }
        Session session = em.getEntityManagerFactory().createEntityManager().unwrap(Session.class);
        try {
            LOG.info("执行SQL：{}", sql);
            SQLQuery query = session.createSQLQuery(sql);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            if (clazz != null) {
                List<String> fields = getFields(filedSql);
                for (String field : fields) {
                    query = query.addScalar(field, getType(field, clazz));
                }
                list = query.setResultTransformer(Transformers.aliasToBean(clazz)).list();
            } else {
                list = query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).list();
            }
        } catch (Exception e) {
            LOG.error("查询错误！{}", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return list;
    }

    //判断类型
    private <T> Type getType(String field, Class<T> clazz) throws Exception {
        Field declaredField;
        try {
            declaredField = clazz.getDeclaredField(field);
        } catch (Exception e) {
            //如果类中没有字段，找父类的字段，再没有就抛异常
            Class<? super T> superclass = clazz.getSuperclass();
            declaredField = superclass.getDeclaredField(field);
        }
        Class<?> type = declaredField.getType();
        if (Date.class.equals(type)) {
            //return DateType.INSTANCE;
            return TimestampType.INSTANCE;
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return DoubleType.INSTANCE;
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            return IntegerType.INSTANCE;
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return LongType.INSTANCE;
        } else if (Timestamp.class.equals(type)) {
            return TimestampType.INSTANCE;
        } else if (BigDecimal.class.equals(type)) {
            return BigDecimalType.INSTANCE;
        } else {
            return StringType.INSTANCE;
        }
    }

    private List<String> getFields(String sql) {
        String lcSql = sql.toLowerCase();
        int start = lcSql.indexOf("select ");
        int end = lcSql.indexOf(" from");
        String strField = sql.substring(start, end);
        String[] fieldArray = strField.split(",");
        List<String> fieldList = new ArrayList<>();
        for (String field : fieldArray) {
            String realField = "";
            //根据空格找到字段名称
            while (realField.length() == 0) {
                int index = field.lastIndexOf(" ");
                if (index > 0) {
                    realField = field.substring(index).trim();
                    field = field.substring(0, index);
                } else {
                    realField = field;
                }
            }
            //去掉表名
            if (realField.indexOf(".") > 0) {
                fieldList.add(realField.substring(realField.indexOf(".") + 1).trim());
            } else {
                fieldList.add(realField.trim());
            }
        }
        return fieldList;
    }
}