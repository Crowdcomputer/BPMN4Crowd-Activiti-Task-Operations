package org.crowdcomputer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONObject;

public class MergeDataTask implements JavaDelegate {

	
	private Expression field;
	private Logger log = LogManager.getLogger(this.getClass());

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String app_token = ""+execution.getVariable("app_token");
		String user_token = ""+execution.getVariable("user_token");
		log.debug(app_token+" "+user_token);
		CroCoClient croco = new CroCoClient(app_token, user_token);
		String field_value = ""+field.getExpressionText();
		Object d = execution.getVariable("data");
		String data = (d==null) ? "[]" : (""+d) ;
		JSONObject results = croco.mergeData(data, field_value);
		execution.setVariable("data", results.get("results"));
	}

}
