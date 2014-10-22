package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class QualitySet extends BaseTask {


    private Logger log = LogManager.getLogger(this.getClass());
    private Expression quality;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(DelegateExecution execution) throws Exception {
        init(execution);
        long task_id = Long.valueOf(""+execution.getVariable("taskId")).longValue();
        long task_instance_id = getTaskInstanceId(execution);
        String s_quality = quality.getExpressionText();
        log.debug("quality is {} ",s_quality);
        int i_quality = Integer.parseInt(s_quality);
        JSONObject ret = croco.setQuality(task_id,task_instance_id,i_quality);
        log.debug("set quality " + ret.toJSONString());
    }
}
