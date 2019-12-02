package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.SqlCondition;
import indi.xin.conditionhelper.core.SqlSignature;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @SqlCondition(@SqlSignature(field = "field", tableName = "table"))
    public void search() {
        //xxx
    }

}
