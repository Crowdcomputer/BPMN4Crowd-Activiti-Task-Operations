package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.crowdcomputer.utils.staticvalues.Status;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateInstance extends BaseTask {


	private Logger log = LogManager.getLogger(this.getClass());


    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		long task_id = Long.valueOf(""+execution.getVariable("taskId")).longValue();
        String input = getData(execution);
        //TODO: is execution.getCurrentActivityId() correct?
        String processId = execution.getProcessInstanceId();
        HashMap<String,String> pars = new HashMap<String, String>();
        pars.put("receiver",execution.getCurrentActivityId()+"-receive");
        pars.put("process_tactics_id",""+processId);
		JSONObject ret_ctask = croco.createInstance(task_id,input,pars);
		log.debug("task instance creation: " + ret_ctask);
		long task_instance_id = Long.valueOf("" + ret_ctask.get("id")).longValue();
        setVarToExecutionVariables(execution,"task_instance_id",""+task_instance_id);
//        output.put("task_instance created:",task_instance_id);
//		JSONObject ret_startTask = croco.startTaskInstance(task_id,task_instance_id);
//		log.debug("start task intance" + ret_startTask);

	}
}
