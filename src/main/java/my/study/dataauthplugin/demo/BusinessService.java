package my.study.dataauthplugin.demo;

import my.study.dataauthplugin.core.DataAuthentication;
import my.study.dataauthplugin.core.SqlSignature;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @DataAuthentication(@SqlSignature(field = "field", tableName = "table"))
    public void search() {
        //xxx
    }

}
