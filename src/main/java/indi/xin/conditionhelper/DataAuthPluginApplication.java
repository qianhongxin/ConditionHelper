package indi.xin.conditionhelper;

import indi.xin.conditionhelper.core.DataAuthenticationPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class DataAuthPluginApplication {

    public static void main(String[] args) {
        Properties properties = new Properties();
        DataAuthenticationPlugin dataAuthenticationPlugin = new DataAuthenticationPlugin();
        dataAuthenticationPlugin.setProperties(properties);
        // 将插件加入mybatis中
        SpringApplication.run(DataAuthPluginApplication.class, args);
    }

}
