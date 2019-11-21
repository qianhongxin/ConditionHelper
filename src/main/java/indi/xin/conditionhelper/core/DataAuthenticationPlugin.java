package indi.xin.conditionhelper.core;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class DataAuthenticationPlugin implements Interceptor {

    private final SqlUtil sqlUtil = new SqlUtil();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return sqlUtil.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        UserContext uc = UserContextHolder.userContextThreadLocal.get();
        if(uc != null && uc.getConditions() != null && !uc.getConditions().isEmpty() && uc.getIds() != null && !uc.getIds().isEmpty() && uc.getUserId() != null) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        sqlUtil.setProperties(properties);
    }

}
