package indi.xin.conditionhelper.demo.annoations;

import indi.xin.conditionhelper.core.ConditionType;
import indi.xin.conditionhelper.core.start.annoations.SqlCondition;
import indi.xin.conditionhelper.core.start.annoations.SqlSignature;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @SqlCondition(@SqlSignature(field = "field", tableName = "table", conditionType = ConditionType.IN))
    public void search() {
        //xxx
    }

}
