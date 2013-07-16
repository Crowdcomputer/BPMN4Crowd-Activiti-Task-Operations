package org.crowdcomputer.impl.internal;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;

public class ValidationTask extends BaseTask {
	private Expression validation;
	private Logger log = LogManager.getLogger(this.getClass());

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		log.warn("execute of the task");
		String s_validation = "" + validation.getExpressionText();
		String task_instance = "" + execution.getVariable("task_instance");
		log.debug("here it comes {} {} --> {}",Long.parseLong(task_instance),s_validation, Boolean.parseBoolean(s_validation));
		croco.validate(Long.parseLong(task_instance), Boolean.parseBoolean(s_validation));
	}
}
