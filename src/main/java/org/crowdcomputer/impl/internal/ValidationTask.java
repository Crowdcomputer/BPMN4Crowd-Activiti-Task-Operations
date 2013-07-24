package org.crowdcomputer.impl.internal;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ValidationTask extends BaseTask {
	private Expression validation;

	private Logger log = LogManager.getLogger(this.getClass());

	private Object convertString(String field_value) {
		Object v = null;
		try {
			v = Integer.parseInt(field_value);
		} catch (Exception e) {
			log.debug("not an int");
			try {
				v = Double.parseDouble(field_value);
			} catch (Exception e2) {
				log.debug("not a double");
				try {
					v = Float.parseFloat(field_value);
				} catch (Exception e3) {
					log.debug("not a float");
					try {
						v = Boolean.parseBoolean(field_value);
					} catch (Exception e4) {
						log.debug("not a bool");

					}
				}
			}
		}
		return v;
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String task_instance = "" + execution.getVariable("task_instance");

		try {
			

			Object o_validation = null;
			log.debug("validation {}", validation);
			if (validation != null) {
				String s_validation = validation.getExpressionText();
				log.debug("here it comes {} {} --> {}",
						Long.parseLong(task_instance),
						validation.getExpressionText(),
						convertString(s_validation).getClass());
			} else {
				String data = getData(execution);
				JSONArray arr_data = (JSONArray) JSONValue.parse(data);
				JSONObject v = (JSONObject) arr_data.get(0);
				log.debug("validation : {}", v.get("validation"));
				o_validation = convertString("" + v.get("validation"));
			}
			croco.validate(Long.parseLong(task_instance), o_validation);
		} catch (Exception e) {
			log.error("There's an error {}",e.getCause().getMessage());
			log.debug("then reward is false by deafult");
//			e.printStackTrace();
			croco.validate(Long.parseLong(task_instance), false);
		}
	}
}
