package org.crowdcomputer.impl.data;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class SplitData extends BaseTask {

	private Expression operation;
	private Expression n;
	private Expression m;

	private Logger log = LogManager.getLogger(this.getClass());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String m_value = (m != null) ? "0" : m.getExpressionText();
		String data = getData(execution);
		JSONObject result = croco.splitData(data,
				operation.getExpressionText(), n.getExpressionText(), m_value);
		log.debug("Split data result " + result.get("results"));
		setResult(execution, result);
	}
}
