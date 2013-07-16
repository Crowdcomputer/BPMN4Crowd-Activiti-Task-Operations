package org.crowdcomputer.impl.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.UriBuilder;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.crowdcomputer.utils.RestCaller;
import org.crowdcomputer.utils.staticvalues.Endpoints;
import org.crowdcomputer.utils.staticvalues.Status;
import org.json.simple.JSONObject;

public class MachineTask extends BaseTask {

	private Expression service_url;
	private Expression method;
	private Logger log = LogManager.getLogger(this.getClass());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(DelegateExecution execution) throws Exception {
		String in_data = getData(execution);
		JSONObject out_data = null;
		String s_method = method.getExpressionText();
		String s_url = service_url.getExpressionText();
		log.debug("calling {} {}", s_method, s_url);
		HashMap pars = new HashMap();
		pars.put("data", in_data);
		log.debug("data {}", in_data);
		RestCaller caller = new RestCaller("", "");
		if (s_method.equals("GET")) {
			log.debug("GET");
			out_data = (JSONObject) caller.getCall(UriBuilder.fromUri(s_url)
					.build());
		} else if (s_method.equals("POST")) {
			log.debug("POST");
			out_data = (JSONObject) caller.postCall(UriBuilder.fromUri(s_url)
					.build(),pars);
		} else if (s_method.equals("PUT")) {
			log.debug("PUT");
			out_data = (JSONObject) caller.putCall(UriBuilder.fromUri(s_url)
					.build(),pars);

//		} else if (s_method.equals("DELETE")) {
//			out_data = (JSONObject) caller.deleteCall(UriBuilder.fromUri(s_url)
//					.build(),pars);
//
		} else {
			throw new Exception("No method: this should not happens");
		}
		log.debug("OUT: {}",out_data.toJSONString());
		setResult(execution, out_data);
	}
}
