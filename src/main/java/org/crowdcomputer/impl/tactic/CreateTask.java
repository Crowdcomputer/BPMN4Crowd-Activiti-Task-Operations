package org.crowdcomputer.impl.tactic;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.crowdcomputer.utils.staticvalues.Status;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTask extends BaseTask {

    private Expression deadline;
    private Expression description;
    private Expression page_url;
    private Expression reward;
    private Expression platform;
    private Expression tactic_process;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(DelegateExecution execution) throws Exception {
        init(execution);
        //this process id is passed by the call of the process
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
        String url = page_url.getExpressionText();
        log.debug("url " + url);
        Double rew = Double.parseDouble(reward.getExpressionText());
        log.debug("reward " + rew);
        String reward_p = platform.getExpressionText();
        log.debug("platfrom " + reward_p);
        String s_tactic_process = tactic_process.getExpressionText();
        log.debug("tactic process " + s_tactic_process);


        HashMap parameters = getBaseParameters(execution);
        parameters.put("data_name", output.getExpressionText());
        parameters.put("type", "custom");
        parameters.put("process_tactic", s_tactic_process);
//        this is for pick and send result
        parameters.put("receivers", execution.getCurrentActivityId());


        JSONObject ret_ctask = croco.createCCTask(process, title, desc,
                deadline_date, 0, url, rew, reward_p, "NONE", null, parameters);
        log.debug("task cration: " + ret_ctask);
        Long task_id = Long.valueOf("" + ret_ctask.get("id")).longValue();
        execution.setVariable("taskId", task_id);
        String data = getData(execution);
        JSONObject ret_startTask = croco.startTaskTactic(task_id, data);
        log.debug("start Task " + ret_startTask);

    }
}
