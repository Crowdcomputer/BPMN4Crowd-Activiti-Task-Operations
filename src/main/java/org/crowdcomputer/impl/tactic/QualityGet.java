package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class QualityGet extends BaseTask {


    private Logger log = LogManager.getLogger(this.getClass());


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(DelegateExecution execution) throws Exception {
        init(execution);
        long task_id = Integer.parseInt("" + execution.getVariable("taskId"));
        long task_instance_id = getTaskInstanceId(execution);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskId",task_id);
        jsonObject.put("taskInstanceId",task_instance_id);
        jsonObject.put("quality", croco.getQuality(task_id,task_instance_id).get("value"));
        log.debug(jsonObject.toJSONString());
        setResult(execution,jsonObject);

    }
}
