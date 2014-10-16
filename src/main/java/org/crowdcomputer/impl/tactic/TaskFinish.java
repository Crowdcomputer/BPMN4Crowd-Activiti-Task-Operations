package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class TaskFinish extends BaseTask {


	private Logger log = LogManager.getLogger(this.getClass());


    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		long task_id = Long.valueOf(""+execution.getVariable("taskId")).longValue();
		JSONObject ret_ctask = croco.finishTaskTactic(task_id);
		log.debug("task instance finish: " + ret_ctask);
	}
}
