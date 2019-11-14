package my.study.dataauthplugin.core;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SqlUtil {

    private Field additionalParametersField;

    private Properties properties;

    public Object intercept(Invocation invocation) throws Throwable {
        return doIntercept(invocation);
    }

    // Pagehelper插件中有不分页时，intercept中执行resultList = (List) invocation.proceed();直接执行当前pagehelper代理对象的目标对象的调用。
    // 我这里没有，因为我在DataAuthenticationPlugin的plugin方法中做了是否代理的判断，只要代理，必然执行。但是最好这里还是判断下
    public Object doIntercept(Invocation invocation) throws Throwable {
        //获取拦截方法的参数
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        List resultList;

        ResultHandler resultHandler = (ResultHandler) args[3];
        //当前的目标对象
        Executor executor = (Executor) invocation.getTarget();
        BoundSql boundSql = args.length == 6 ? (BoundSql) args[5] : ms.getBoundSql(parameterObject);
        //反射获取动态参数
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(ms.getBoundSql(parameterObject));

        //生成数据权限查询的缓存 key
        CacheKey authDataKey = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);

        //获取校验 sql
        String authDataSql = getAuthDataSql(ms, boundSql, parameterObject, rowBounds, authDataKey);

        BoundSql authDataBoundSql = new BoundSql(ms.getConfiguration(), authDataSql, boundSql.getParameterMappings(), parameterObject);
        //设置动态参数
        for (String key : additionalParameters.keySet()) {
            authDataBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
        }
        // 执行查询, 当前的executor可能是下一个代理对象，也可能是执行数据库操作的executor对象
        // 如果是代理对象，则执行的是Plugin的invoke方法（Plugin是InvocationHandler）
        //public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //    try {
        //      // 获取 method 所属类或接口的字节码对象
        //      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
        //      // 判断 methods 中是否包括 method，包含则执行 拦截器的 intercept 方法。 代理创建时，不会根据method判断是否创建，只要                   type等格式对就创建
        //      if (methods != null && methods.contains(method)) {
        //        return interceptor.intercept(new Invocation(target, method, args));
        //      }
        //      // 调用 method 的 invoke 方法，进入下一层调用
        //      return method.invoke(target, args);
        //    } catch (Exception e) {
        //      throw ExceptionUtil.unwrapThrowable(e);
        //    }
        //  }
        resultList = executor.query(ms, parameterObject, RowBounds.DEFAULT, resultHandler, authDataKey, authDataBoundSql);

        //返回默认查询
        return resultList;
    }

    private String getAuthDataSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey authDataKey) {
        String sql = boundSql.getSql();
        UserContext uc = UserContextHolder.userContextThreadLocal.get();

        for (int i = 0; i < uc.getConditions().size(); i++) {
            sql = buildDataSql(sql, uc.getIds(), uc.getConditions().get(i));
        }

        return sql;
    }

    private String buildDataSql(String sql, List<Integer> shopIds, UserContext.Condition condition) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        String field = condition.getField();
        String tableName = condition.getTableName();
        // 获取字段的索引
        int tableNameIndex = sql.indexOf("as " + tableName);
        if(tableNameIndex == -1) {
            tableNameIndex = sql.indexOf("AS " + tableName);
            if(tableNameIndex == -1) {
                tableNameIndex = sql.indexOf(tableName);
            }
        }
        if(tableNameIndex == -1) {
            return sql;
        }
        // 获取距离字段最近的第一个where的索引
        int firstWhereIndex = sql.indexOf("where", tableNameIndex);
        if(firstWhereIndex == -1) {
            firstWhereIndex = sql.indexOf("WHERE", tableNameIndex);
        }
        if(firstWhereIndex == -1) {
            return sql;
        }
        // 截取 字段索引 到 第一个where索引 的字符串
        String splitTarget = sql.substring(tableNameIndex, firstWhereIndex + 5);

        String[] sqlArr = sql.split(splitTarget);
        sqlBuilder.append(sqlArr[0]);
        sqlBuilder.append(splitTarget);
        sqlBuilder.append(" ").append(tableName).append(".").append(field).append(" in (").append(formatIn(shopIds)).append(") and ");
        sqlBuilder.append(sqlArr[1]);

        return sqlBuilder.toString();
    }

    private String formatIn(List<Integer> shopIds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shopIds.size(); i++) {
            sb.append(shopIds.get(i));
            if(shopIds.size() - 1 != i) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public void setProperties(Properties properties) {
        try {
            //反射获取 BoundSql 中的 additionalParameters 属性
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
