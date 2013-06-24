package org.crowdcomputer.impl;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class TestActiviti implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String processExecutionId = execution.getProcessInstanceId();
		List<String> activity = execution.getEngineServices().getRuntimeService().getActiveActivityIds(processExecutionId);
		for (String active : activity) {
			System.out.println("-" + active);
		}
	}

}
