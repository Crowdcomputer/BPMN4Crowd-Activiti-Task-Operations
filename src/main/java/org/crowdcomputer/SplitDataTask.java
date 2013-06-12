package org.crowdcomputer;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class SplitDataTask implements JavaDelegate {

	private Expression operation;
	private Expression n;
	private Expression m;

	private Logger log = LogManager.getLogger(this.getClass());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String app_token = "" + execution.getVariable("app_token");
		String user_token = "" + execution.getVariable("user_token");
		log.debug(app_token + " " + user_token);
		CroCoClient croco = new CroCoClient(app_token, user_token);
		String operation_value = "" + operation.getExpressionText();
		String n_value =  n.getExpressionText();
		String m_value = "0";
		if (m != null)
			m_value = m.getExpressionText();
		Object d = execution.getVariable("data");
		String data = (d == null) ? "[]" : ("" + d);
		log.debug("data " + data);
		JSONObject results = croco.splitData(data, operation_value, n_value,
				m_value);
		log.debug("results " + results.get("results"));
		execution.setVariable("data", results.get("results"));
	}
}
