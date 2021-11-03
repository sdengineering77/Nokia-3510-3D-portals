package graphics.engine.graphics;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DisplayFactory {
	private DisplayFactory() {
	}
	
	public static Display	getDisplay( String className ) throws ClassNotFoundException, InstantiationException,
                          IllegalAccessException {
		Class displayClass = Class.forName(className);
		return (Display) displayClass.newInstance();
	}
}
