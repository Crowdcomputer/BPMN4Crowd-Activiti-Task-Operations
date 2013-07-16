package org.crowdcomputer.impl.data;

import javax.ws.rs.core.UriBuilder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.utils.RestCaller;
import org.json.simple.JSONObject;

public class LoadData implements JavaDelegate {


	private Expression url;

	private Logger log = LogManager.getLogger(this.getClass());


	public void execute(DelegateExecution execution) throws Exception {
		RestCaller caller = new RestCaller("", "");
		JSONObject results = (JSONObject) caller.getCall(UriBuilder.fromUri(url.getExpressionText()).build());
		log.debug("result variable: "+results.get("results"));
		execution.setVariable("data", results.get("results"));
	}
}
