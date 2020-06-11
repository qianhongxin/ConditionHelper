package com.qhx.conditionhelper.sample.code;

import com.qhx.conditionhelper.core.ConditionType;
import com.qhx.conditionhelper.core.start.ConditionHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessService {

    /**
     * @Description: 使用例子1
     *
     * @author: hongxin.qian
     * @time: 2019/12/3 17:48
     */
    public void search() {
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ConditionHelper.startCondition("field", "tableName", ConditionType.IN, ids);
    }

    /**
     * @Description: 使用例子2
     *
     * @author: hongxin.qian
     * @time: 2019/12/3 17:48
     */
    public void search2() {
        List<Integer> ids1 = new ArrayList<>();
        ids1.add(1);
        ids1.add(2);

        List<Integer> ids2 = new ArrayList<>();
        ids2.add(3);

        ConditionHelper.addCondition("field1", "tableName1", ConditionType.IN, ids1);
        ConditionHelper.addCondition("field2", "tableName2", ConditionType.EQ, ids2);
        ConditionHelper.startCondition();
    }

}
