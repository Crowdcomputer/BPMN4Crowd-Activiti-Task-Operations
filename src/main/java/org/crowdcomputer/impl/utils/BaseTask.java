package org.crowdcomputer.impl.utils;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.CroCoClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BaseTask implements JavaDelegate {
	
	protected Expression input;
	protected Expression output;
	protected CroCoClient croco = null;
	
	private Logger log = LogManager.getLogger(this.getClass());

	
	protected void init(DelegateExecution execution){
		String app_token = "" + execution.getVariable("app_token");
		String user_token = "" + execution.getVariable("user_token");
		croco = new CroCoClient(app_token, user_token);
		log.debug("Croco created");
	}
	protected String getData(DelegateExecution execution) {
		String name = input.getExpressionText();
		if ((name == null) || (name.length() == 0))
			name = "data";
		Object d = execution.getVariable(name);
		String data = (d == null) ? "[]" : ("" + d);
		log.debug("getData " + data);
		return data;
	}
	
	
	@SuppressWarnings("unchecked")
	protected String getDatas(DelegateExecution execution) {
		String name = input.getExpressionText();
		String[] datas = name.split(",");
		List<String> ret = new ArrayList<String>();
		for(String data: datas){
			Object d = execution.getVariable(data);
			String data_t = (d == null) ? "[]" : ("" + d);
			ret.add(data_t);
		}
		JSONArray arr= new JSONArray();
		arr.addAll(ret);
		log.debug("getDatas " +  arr.toJSONString());

		return arr.toJSONString();
	}


	protected void setResult(DelegateExecution execution, JSONObject data) {
		String name = output.getExpressionText();
		if ((name == null) || (name.length() == 0))
			name = "data";
		log.debug("setResult  " +  data.get("result"));

		execution.setVariable(name, data.get("result"));
	}

	protected void setResults(DelegateExecution execution, JSONObject data) {
		String[] data_names = output.getExpressionText().split(",");
		JSONArray datas = (JSONArray) data.get("result");
		for(int i=0; i<2; i++){
			log.debug("setResults  " + i + " " + datas.get(i));

			execution.setVariable(data_names[i], datas.get(i));
		}
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.error("someone just called me");
		throw new Exception("You must extend this class, not use it");

	}

}
