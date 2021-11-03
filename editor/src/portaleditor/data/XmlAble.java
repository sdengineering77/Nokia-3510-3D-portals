package portaleditor.data;

import org.w3c.dom.Document;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface XmlAble {
	public StringBuffer 	getXml();
	public StringBuffer 	getXml(int numTabs);
	public void			setXml(Document in);
}
