package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.DataAuthentication;
import indi.xin.conditionhelper.core.SqlSignature;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @DataAuthentication(@SqlSignature(field = "field", tableName = "table"))
    public void search() {
        //xxx
    }

}
