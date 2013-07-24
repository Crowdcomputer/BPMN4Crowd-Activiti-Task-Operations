import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class snippets {

	private Logger log = LogManager.getLogger(this.getClass());

	private Object convertString(String field_value) {
		Object v = null;
		try {
			v = Integer.parseInt(field_value);
		} catch (Exception e) {
			log.debug("not an int");
			try {
				v = Float.parseFloat(field_value);
			} catch (Exception e1) {
				log.debug("not a float");
				try {
					v = Double.parseDouble(field_value);
				} catch (Exception e2) {
					log.debug("not a double");
					try {
						v = Boolean.parseBoolean(field_value);
					} catch (Exception e3) {
						log.debug("not a bool");

					}
				}
			}
		}
		return v;
	}

	
}
