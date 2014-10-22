package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ValidationTask extends BaseTask {
	private Expression validation_process;

	private Logger log = LogManager.getLogger(this.getClass());


	@Override
	public void execute(DelegateExecution execution) throws Exception {
        init(execution);
        long task_id = Long.valueOf(""+execution.getVariable("taskId")).longValue();
        long task_instance_id = getTaskInstanceId(execution);
        String s_validation_process = validation_process.getExpressionText();

        croco.process_validation(task_id, task_instance_id, s_validation_process);
	}
}
