package org.crowdcomputer.impl.data;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class MergeData extends BaseTask {

	
//	@SuppressWarnings("unused")
//	private Expression field;
	private Logger log = LogManager.getLogger(this.getClass());

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String data = getData(execution);
//		JSONObject result = croco.mergeData(data, field.getExpressionText());
		JSONObject result = croco.mergeData(data);

		log.debug("Merge data result " + result.get("results"));

		setResult(execution, result);
	}

}
