package org.crowdcomputer.impl.utils;

import org.activiti.engine.delegate.DelegateExecution;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

public class StringToJson extends BaseTask {

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String data = getData(execution);
		JSONArray arra = (JSONArray) JSONValue.parse(data);
		log.debug("{} {}",data,arra);
		execution.setVariable(input.getExpressionText(), arra);
	}
}
