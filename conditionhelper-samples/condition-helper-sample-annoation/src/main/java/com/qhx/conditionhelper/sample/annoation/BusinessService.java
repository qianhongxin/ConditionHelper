package com.qhx.conditionhelper.sample.annoation;

import com.qhx.conditionhelper.core.ConditionType;
import com.qhx.conditionhelper.core.start.annoations.SqlCondition;
import com.qhx.conditionhelper.core.start.annoations.SqlSignature;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @SqlCondition(@SqlSignature(field = "field", tableName = "table", conditionType = ConditionType.IN))
    public void search() {
        //xxx
    }

}
