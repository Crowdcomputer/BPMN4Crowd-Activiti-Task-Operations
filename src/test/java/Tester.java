import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class Tester {

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
				log.debug("not a float");
				try {
					v = Boolean.parseBoolean(field_value);
				} catch (Exception e3) {
					log.debug("not a bool");

				}
			}

		}
		System.out.println(""+v.getClass());
		return v;
	}

	@Test
	public void test() {
		assertTrue(((Boolean) convertString("true")));
		assertFalse(((Boolean) convertString("false")));
		assertTrue(((Integer) convertString("123")) == 123);
		assertTrue(((Double) convertString("123.1")) == 123.1);
		assertTrue(((Double) convertString("123.1f")) == 123.1);

	}	
}
