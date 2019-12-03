package indi.xin.conditionhelper.core.intergration.mybatis;

import indi.xin.conditionhelper.core.ConditionContext;
import indi.xin.conditionhelper.core.ConditionContextHolder;
import indi.xin.conditionhelper.core.util.ConditionContextUtil;
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
public class SqlConditionPlugin implements Interceptor {

    private final SqlUtil sqlUtil = new SqlUtil();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return sqlUtil.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        if(ConditionContextUtil.isEmpty()) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        sqlUtil.setProperties(properties);
    }

}
