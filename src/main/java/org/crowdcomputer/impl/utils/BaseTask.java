package org.crowdcomputer.impl.utils;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.CroCoClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class BaseTask implements JavaDelegate {

    protected Expression input;
    protected Expression output;
    protected Expression input_execution;
    protected Expression output_execution;
    protected CroCoClient croco = null;

    protected Logger log = LogManager.getLogger(this.getClass());


    protected void init(DelegateExecution execution) {
        String app_token = "" + execution.getVariable("app_token");
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
        JSONArray data_j = null;
        if (d != null) {
            try {
                ArrayList al_d = (ArrayList) d;
                StringWriter out = new StringWriter();

                JSONValue.writeJSONString(al_d, out);
                log.debug("data j {}", out.toString());
                return out.toString();
            } catch (Exception e) {
//                e.printStackTrace();
                log.debug("direct casting does not work");
            }

            log.debug("D {} {}", d.getClass(), d);
            String data = (d == null) ? "[]" : "" + d;
            log.debug("data now {}", data);
//            check why data is null now!
            data = data.replace("'","\"");
            log.debug("data now {}", data);

            try {
                data_j = (JSONArray) JSONValue.parseWithException(data);
                log.debug("data_j {}", data_j);
                log.debug("getData in json " + data_j.toJSONString());

                return data_j.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("ok, problem with data");
                if (data.startsWith("["))
                    return data;
                else
                    return "["+data+"]";
            }
        } else
            return "[]";
    }


    @SuppressWarnings("unchecked")
    protected String getDatas(DelegateExecution execution) {

        String name = input.getExpressionText();
        String[] datas = name.split(",");
        JSONArray ret = new JSONArray();
        log.debug("datas {}", datas.toString());
        for (String data : datas) {
            Object d = execution.getVariable(data);
            log.debug("{} is {}", data, d);
            String data_t = (d == null) ? "[]" : ("" + d);
            JSONArray data_j = (JSONArray) JSONValue.parse(data_t);
            for (int i = 0; i < data_j.size(); i++) {
                ret.add(data_j.get(i));
            }
        }
        log.debug("getDatas in json " + ret.toJSONString());

        return ret.toJSONString();
    }

    protected HashMap<Object, Object> getBaseParameters(DelegateExecution execution) {
        HashMap<Object, Object> pars = new HashMap<Object, Object>();
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
        log.debug("setResult  " + name + " " + data.get("result"));
        log.debug("{} vs {} vs {}", ((JSONArray) data.get("result")), ((JSONArray) data.get("result")).toJSONString(), data.get("result"));
        String res = "" + data.get("result");
        execution.setVariable(name, ((JSONArray) JSONValue.parse(res)));
    }

    protected void setResults(DelegateExecution execution, JSONObject data) {
        String[] data_names = output.getExpressionText().split(",");
        JSONArray datas = (JSONArray) data.get("result");
        for (int i = 0; i < 2; i++) {
            log.debug("setResults  " + i + " " + datas.get(i));
            execution.setVariable(data_names[i], ((JSONArray) data.get(i)));
        }
    }

    protected JSONObject getExecutionVariables(DelegateExecution execution) {
        //read execution variables
        String name = input_execution.getExpressionText();
        if ((name == null) || (name.length() == 0))
            name = "data";
        Object d = execution.getVariable(name);
        String data = (d == null) ? "{}" : ("" + d);
        return (JSONObject) JSONValue.parse(data);
    }

    protected JSONObject setVarToExecutionVariables(DelegateExecution execution, String v_name, String v_value) {
        //add the value to execution variables
        String name = output_execution.getExpressionText();
        if ((name == null) || (name.length() == 0))
            name = "data";
        Object d = execution.getVariable(name);
        String data = (d == null) ? "{}" : ("" + d);
        JSONObject j_output = (JSONObject) JSONValue.parse(data);
        j_output.put(v_name, v_value);
        execution.setVariable(name, j_output.toJSONString());
        return j_output;
    }

    protected long getTaskInstanceId(DelegateExecution execution) {
        String instanceId = "" + execution.getVariable("taskInstanceId");
        log.debug("instance id first {} ", instanceId);

//        if is not passed in any of the task before (which should be the case), it's taken form the execution.
//        start process should send task_id and task_instance_id
        if (instanceId == null) {
            JSONObject execution_data = getExecutionVariables(execution);
            instanceId = "" + execution_data.get("taskInstanceId");
        }
        log.debug("instance id after {} ", instanceId);

        return Long.valueOf(instanceId).longValue();
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.error("someone just called me");
        throw new Exception("You must extend this class, not use it");

    }

}
