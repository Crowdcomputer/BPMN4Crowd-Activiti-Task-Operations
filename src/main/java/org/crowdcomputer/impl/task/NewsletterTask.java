package org.crowdcomputer.impl.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.crowdcomputer.utils.staticvalues.Status;
import org.json.simple.JSONObject;

public class NewsletterTask extends BaseTask {

	private Expression deadline;
	private Expression description;
	private Expression emails;
	private Expression page_url;
	private Expression reward;
	private Expression platform;
	private Expression merge;
	private Expression reward_strategy;
	private Expression validation_process;
	private Logger log = LogManager.getLogger(this.getClass());

	private Date convertPeriod(String s) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s);
		List<Integer> matches = new ArrayList<Integer>();
		while (m.find()) {
			// System.out.println(m.group());
			matches.add(Integer.parseInt(m.group()));
		}
		Calendar cal = GregorianCalendar.getInstance();
		System.out.println(cal.getTime());
		cal.add(Calendar.YEAR, matches.get(0));
		cal.add(Calendar.MONTH, matches.get(1));
		cal.add(Calendar.WEEK_OF_YEAR, matches.get(2));
		cal.add(Calendar.DATE, matches.get(3));
		cal.add(Calendar.HOUR_OF_DAY, matches.get(4));
		cal.add(Calendar.MINUTE, matches.get(5));
		cal.add(Calendar.SECOND, matches.get(6));
		return cal.getTime();
	}

	public boolean hasFinished(JSONObject ret) {
		String status = "" + ret.get("status");
		if (status.equals(Status.FINISHED))
			return true;
		else
			return false;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String process_s = "" + execution.getVariable("processId");
		log.debug(process_s);
		Long process = Long.valueOf(process_s).longValue();
		log.debug("process " + process);
		String title = execution.getCurrentActivityName();
		log.debug("title " + title);
		String desc = description.getExpressionText();
		log.debug("desc  " + desc);
		Date deadline_date = convertPeriod(deadline.getExpressionText());
		log.debug("deadline " + deadline_date);
		String emails_string = emails.getExpressionText();
		log.debug("emails " + emails_string);
		String url = page_url.getExpressionText();
		log.debug("url " + url);
		Double rew = Double.parseDouble(reward.getExpressionText());
		log.debug("reward " + rew);
		String reward_p = platform.getExpressionText();
		log.debug("platfrom " + reward_p);
		String s_reward_strategy = reward_strategy.getExpressionText();

		String s_validation_process = "VALID";
		if (validation_process != null)
			s_validation_process = validation_process.getExpressionText();
		//
		HashMap parameters = getBaseParameters(execution);
		parameters.put("data_name", output.getExpressionText());
		parameters.put("emails", createEmails(emails_string));
		parameters.put("type", "newsletter");
		parameters
				.put("merge", Boolean.parseBoolean(merge.getExpressionText()));

		JSONObject ret_ctask = croco.createCCTask(process, title, desc,
				deadline_date, 0, url, rew, reward_p, s_reward_strategy,
				s_validation_process, parameters);
		log.debug("task cration: " + ret_ctask);
		Long task_id = Long.valueOf("" + ret_ctask.get("id")).longValue();
		String data = getData(execution);
		JSONObject ret_startTask = croco.startTask(task_id, data, ""
				+ execution.getCurrentActivityId());
		log.debug("start Task " + ret_startTask);

	}

	private ArrayList<String> createEmails(String emails_string) {
		String[] emails = emails_string.split(",");
		ArrayList<String> ret = new ArrayList<String>();
		for (String email : emails) {
			ret.add(email);
		}
		return ret;
	}
}
