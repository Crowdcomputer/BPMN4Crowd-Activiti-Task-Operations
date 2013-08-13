package org.crowdcomputer.impl.utils;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.CroCoClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class BaseTask implements JavaDelegate {
	
	protected Expression input;
	protected Expression output;
	protected CroCoClient croco = null;
	
	protected Logger log = LogManager.getLogger(this.getClass());

	
	protected void init(DelegateExecution execution){
		String app_token = "" + execution.getVariable("app_token") ;
		String user_token = "" + execution.getVariable("user_token");
		croco = new CroCoClient(app_token, user_token);
		log.debug("Croco created " + app_token + " - " + user_token);
//		Map<String, Object> allV = execution.getVariables();
//		log.debug("printing allV {}",allV.size());
//		for (Iterator<Map.Entry<String, Object>> it = allV.entrySet().iterator(); it
//				.hasNext();) {
//			Map.Entry<String, Object> entry = it.next();
//			String key = entry.getKey();
//			Object value = entry.getValue();
//			log.debug("{} -> {}",key,value);
//			// do something with the key and the value  
//		}
	}
	protected String getData(DelegateExecution execution) {
	
		String name = input.getExpressionText();
		if ((name == null) || (name.length() == 0))
			name = "data";
		Object d = execution.getVariable(name);
		String data = (d == null) ? "[]" : ("" + d);
		log.debug("data now {}",data);
		JSONArray data_j = (JSONArray) JSONValue.parse(data);
		log.debug("getData in json " + data_j.toJSONString());

		return data_j.toJSONString();
	}
	
	
	@SuppressWarnings("unchecked")
	protected String getDatas(DelegateExecution execution) {

		String name = input.getExpressionText();
		String[] datas = name.split(",");
		JSONArray ret = new JSONArray();
		log.debug("datas {}",datas.toString());
		for(String data: datas){
			Object d = execution.getVariable(data);
			log.debug("{} is {}",data,d);
			String data_t = (d == null) ? "[]" : ("" + d);
			JSONArray data_j = (JSONArray) JSONValue.parse(data_t);
			for(int i=0;i<data_j.size();i++){
				ret.add(data_j.get(i));
			}
		}
		log.debug("getDatas in json " +  ret.toJSONString());

		return ret.toJSONString();
	}

	protected HashMap<Object,Object> getBaseParameters(DelegateExecution execution){
		HashMap<Object,Object> pars = new HashMap<Object,Object>();
//		String validation = "" + execution.getVariable("validation");
//		if (validation.length()>0){
//			pars.put("validation", validation);
//		}
//		String task_instance = "" + execution.getVariable("task_instance");
//		if (task_instance.length()>0){
//			pars.put("task_instance", task_instance);
//		}
		return pars;

	}

	protected void setResult(DelegateExecution execution, JSONObject data) {
		String name = output.getExpressionText();
		if ((name == null) || (name.length() == 0))
			name = "data";
		log.debug("setResult  " + name + " "+  data.get("result"));
		log.debug("{} vs {} vs {}",((JSONArray)data.get("result")),((JSONArray)data.get("result")).toJSONString(),data.get("result"));
		String res = ""+data.get("result");
		execution.setVariable(name,((JSONArray) JSONValue.parse(res)));
	}

	protected void setResults(DelegateExecution execution, JSONObject data) {
		String[] data_names = output.getExpressionText().split(",");
		JSONArray datas = (JSONArray) data.get("result");
		for(int i=0; i<2; i++){
			log.debug("setResults  " + i + " " + datas.get(i));
			execution.setVariable(data_names[i], ((JSONArray)data.get(i)));
		}
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.error("someone just called me");
		throw new Exception("You must extend this class, not use it");

	}

}
