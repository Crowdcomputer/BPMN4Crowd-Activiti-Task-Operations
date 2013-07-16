package org.crowdcomputer.impl.data;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crowdcomputer.impl.utils.BaseTask;
import org.json.simple.JSONObject;

public class SplitObject extends BaseTask {

	private Logger log = LogManager.getLogger(this.getClass());
	private Expression shared;
	private Expression field;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		init(execution);
		String data = getData(execution);
		List<String> shared_list = getList(shared.getExpressionText());
		List<String> field_list = getList(field.getExpressionText());
		JSONObject results = croco.splitObject(data, shared_list, field_list);
		setResults(execution, results);
		log.debug("Split Object " + results);
	}

	private List<String> getList(String str_f) {
	
		ArrayList<String> ret = new ArrayList<String>();
		String[] s_list = str_f.split(",");
		for(String item : s_list){
			ret.add(item);
		}
		return ret;
	}

}
