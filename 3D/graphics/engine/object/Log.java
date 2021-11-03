package graphics.engine.object;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Log {
	private static ArrayList log = new ArrayList();
	public static void log( Object o ) {
		log.add(o);
	}
	
	public static void print() {
		Iterator objs = log.iterator();
		while(objs.hasNext()) {
			System.out.println(objs.next());
		}
	}
}
