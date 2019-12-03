package indi.xin.conditionhelper;

import indi.xin.conditionhelper.core.intergration.mybatis.SqlConditionPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class ConditionHelperPluginApplication {

    public static void main(String[] args) {
        Properties properties = new Properties();
        SqlConditionPlugin dataAuthenticationPlugin = new SqlConditionPlugin();
        dataAuthenticationPlugin.setProperties(properties);
        // 将插件加入mybatis中
        SpringApplication.run(ConditionHelperPluginApplication.class, args);
    }

}
