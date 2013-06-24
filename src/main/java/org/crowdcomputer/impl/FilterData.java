package org.crowdcomputer.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class FilterData extends BaseTask {

	private Expression field;
	private Expression operator;
	private Expression value;

	private Logger log = LogManager.getLogger(this.getClass());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String data = getData(execution);
		JSONObject result = croco.filterData(data, field.getExpressionText(),
				operator.getExpressionText(), value.getExpressionText());
		log.debug("Filter object result " + result.get("results"));

		setResult(execution, result);
	}

}
