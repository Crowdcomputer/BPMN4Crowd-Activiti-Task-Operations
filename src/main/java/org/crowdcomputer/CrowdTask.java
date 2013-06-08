package org.crowdcomputer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.utils.staticvalues.Status;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CrowdTask implements JavaDelegate {

	private Expression deadline;
	private Expression description;
	private Expression number_of_instances;
	private Expression page_url;
	private Expression reward;
	private Expression platform;
	private Logger log = LogManager.getLogger(this.getClass());

	private Date convertPeriod(String s){
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s);
		List<Integer> matches = new ArrayList<Integer>();
		while(m.find()){
//			System.out.println(m.group());
		    matches.add(Integer.parseInt(m.group()));
		}
		Calendar cal = GregorianCalendar.getInstance();
		System.out.println(cal.getTime());
		cal.add(Calendar.YEAR, matches.get(0));
		cal.add(Calendar.MONTH,matches.get(1));
		cal.add(Calendar.WEEK_OF_YEAR,matches.get(2));
		cal.add(Calendar.DATE,matches.get(3));
		cal.add(Calendar.HOUR_OF_DAY,matches.get(4));
		cal.add(Calendar.MINUTE,matches.get(5));
		cal.add(Calendar.SECOND,matches.get(6));
		return cal.getTime();
	}
	
	public boolean hasFinished(JSONObject ret){
		String status = ""+ret.get("status");
		log.debug("status " + status);
		if (status.equals(Status.FINISHED))
			return true;
		else
			return false;
		
	}

	public void execute(DelegateExecution execution) throws Exception {

		System.out.println("CROWDCOMPUTER STUFF");
		String app_token = ""+execution.getVariable("app_token");
		String user_token = ""+execution.getVariable("user_token");
		log.debug(app_token+" "+user_token);
		CroCoClient croco = new CroCoClient(app_token, user_token);
		String process_s = ""+execution.getVariable("processId");
		log.debug(process_s);
		Long process = Long.valueOf(process_s).longValue();
		log.debug("process "+process);
		String title=execution.getCurrentActivityName();
		log.debug("title " + title);
		String desc = description.getExpressionText();
		log.debug("desc  " +desc);
		Date deadline_date = convertPeriod(deadline.getExpressionText());
		log.debug("deadline " + deadline_date);
		Integer n_o_i=Integer.parseInt(number_of_instances.getExpressionText());
		log.debug("noi " + n_o_i);
		String url = page_url.getExpressionText();
		log.debug("url " + url);
		Double rew = Double.parseDouble(reward.getExpressionText());
		log.debug("reward " + rew);
		String reward_p = platform.getExpressionText();
		log.debug("platfrom " + reward_p);
		JSONObject ret_ctask = croco.createCCTask(process, title, desc,  deadline_date, n_o_i, url, rew, reward_p);
		log.debug("Create Task " + ret_ctask);
		Long task_id = Long.valueOf(""+ret_ctask.get("id")).longValue();
		Object d = execution.getVariable("data");
		String data = (d==null) ? "[]" : (""+d) ;
		log.debug("data "+data);
		JSONObject ret_startTask = croco.startTask(task_id,data );
		log.debug("start task "+ ret_startTask);
		int i =0;
		while (!hasFinished(croco.getStatus(task_id))){
			log.debug("nothing has done for " + task_id);
			Thread.sleep(30000);
		}
		log.debug("and it's done "+task_id);
		JSONObject results = croco.getResult(task_id);
		log.debug("result variable: "+results.get("results"));
		execution.setVariable("data", results.get("results"));
		log.debug("Data variable: "+execution.getVariable("data"));
		

	}
}
