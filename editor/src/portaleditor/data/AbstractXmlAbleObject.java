package portaleditor.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AbstractXmlAbleObject implements XmlAble {
	private final static Category log = Category.getInstance(AbstractXmlAbleObject.class);
	
	public StringBuffer getXml() {
		return getXml(0);
	}	

	private void appendTabs(StringBuffer sb, int num_tabs) {
		while(num_tabs > 0) { 	
			sb.append("  ");
			num_tabs--;
		}
		
	}
	
	public StringBuffer getXml(int numTabs) {
		String xml_name = this.getClass().getName().replace('.','_');
		StringBuffer	sb = new StringBuffer();
		
		appendTabs(sb,numTabs++);
		sb.append('<');
		sb.append(xml_name);
		sb.append(">\r\n");
		
		Class portalObject_class = this.getClass();
		
		Field[] fields = portalObject_class.getDeclaredFields();
		for( int cnt=0; cnt<fields.length; cnt++ ) {
			Field field = fields[cnt];
			
			log.debug("field.getModifiers(): " + field.getModifiers() + field.getName());
			if( !Modifier.isTransient(field.getModifiers()) ) {
			log.debug("Modifier.TRANSIENT: " + Modifier.TRANSIENT);
				String name = field.getName();
				Class  type = field.getType();

			
				if( type != null ) {
					String typeName = type.getName();

					
					if( typeName != null ) {
						appendTabs(sb,numTabs);
						sb.append('<');
						sb.append(name);
						sb.append('>');

						try {
							if( typeName.equals("int") ) {
								sb.append(field.getInt(this));	
							}	else 
							if( typeName.equals("long") ) {
								sb.append(field.getLong(this));	
							}	else 
							if( typeName.equals("boolean") ) {
								sb.append(field.getBoolean(this));
							}	else 
							if( typeName.equals("short") ) {
								sb.append(field.getShort(this));
							}	else 
							if( typeName.equals("java.lang.String") ) {
								String value = (String) field.get(this);
								if( value != null ) {
									sb.append(value);	
								}
									
							}	else
							if( typeName.equals("java.util.ArrayList") ) {
								Collection value = (Collection) field.get(this);
								if( value != null && value.size() > 0 ) {
									sb.append("\r\n");	
									
									Iterator it = value.iterator();
									while( it.hasNext() ) {
										Object obj = it.next();
										
										if( obj instanceof XmlAble ) {
											sb.append(((XmlAble)obj).getXml(numTabs+1));
										}
									}
									appendTabs(sb,numTabs);
								}
									
							}
							
						}	catch( IllegalAccessException iae ) {
							iae.printStackTrace();
							
						}
						 
						sb.append("</");
						sb.append(name);
						sb.append(">\r\n");
							
					}
					
				}
			}
		}	

		appendTabs(sb,--numTabs);
		sb.append("</");
		sb.append(xml_name);
		sb.append(">\r\n");
			
		return sb;
	}
	
	public void setXml( Document in ) {
		Class portalObject_class = this.getClass();
		
		Field[] fields = portalObject_class.getDeclaredFields();
		for( int cnt=0; cnt<fields.length; cnt++ ) {
			Field field = fields[cnt];
			
			if( !Modifier.isTransient(field.getModifiers()) ) {
				String name = field.getName();
				Class  type = field.getType();

			
				if( type != null ) {
					String typeName = type.getName();

					
					if( typeName != null ) {
						
						NodeList list = in.getElementsByTagName(name);
log.debug( "name: " + name );						
log.debug( "length: " + list.getLength());						
						if( list.getLength() > 0 ) {
							setObjectField(name, list.item(0));
						}
					}					
				}
			}
		}	
	}
	
	
	private void setObjectField( String name, Node node ) {
		Object portalObject = this;
		if( portalObject != null && node != null ) {
			Class portalObject_class = portalObject.getClass();
			
			try {
				Field field = portalObject_class.getField(name);	
				if( field != null ) {
					Class  type = field.getType();
	
	log.debug("name: " + name);		
	log.debug("type: " + type);		
					
					if( type != null ) {
						String typeName = type.getName();
	
	log.debug("typeName: " + typeName);		
						
						if( typeName != null ) {
							if( node.hasChildNodes() ) {
								String value = node.getFirstChild().getNodeValue();
		log.debug("value: " + value);		
								if( typeName.equals("int") ) {
									field.setInt(portalObject, Integer.parseInt((String)value));
								}	else 
								if( typeName.equals("long") ) {
									field.setLong(portalObject, Long.parseLong((String)value));	
								}	else 
								if( typeName.equals("boolean") ) {
									field.setBoolean(portalObject, Boolean.getBoolean(value));	
								}	else 
								if( typeName.equals("short") ) {
									field.setShort(portalObject, Short.parseShort((String)value));	
								}	else 
								if( typeName.equals("java.lang.String") ) {
									field.set(portalObject, ((String)value));	
								}	else
								if( typeName.equals("java.util.ArrayList") ) {
									// todo...
									// run thru each child tag
									// get name
									// replace _ by .
									// instantiate class by name
									// check interface type
									// run setXml()
									ArrayList list = new ArrayList();
									field.set(portalObject, list );
									NodeList nodelist = node.getChildNodes();
									int length = nodelist.getLength();
									for( int cnt = length-1; cnt>=0; cnt-- ) {
										Node currNode = nodelist.item(cnt);
										if( currNode.getNodeType() == Node.ELEMENT_NODE ) {
											String className = currNode.getNodeName();
											if( className != null ) {
												try {
													className = className.replace('_', '.');
													Class objectClass = Class.forName(className);
													Object objectClassInst = objectClass.newInstance();
													log.debug( "Object of type: " + className + " at " + objectClassInst );
													if( objectClassInst instanceof XmlAble ) {
				
														DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
														DocumentBuilder builder = dbf.newDocumentBuilder();
										
														Document childDocument = builder.newDocument();
														Node newNode = childDocument.importNode(currNode, true);
														
														childDocument.appendChild(newNode);
														
														((XmlAble)objectClassInst).setXml(childDocument);
														list.add(objectClassInst);
														
													}
												}	catch( Exception e ) {
													log.debug( e );
													e.printStackTrace();
												}
											}
										
										}	
									}
										
								}
							}
						}
					}
				}
			}	catch( IllegalAccessException iae ) {
				iae.printStackTrace();
				
			}	catch( NoSuchFieldException nsfe ) {
				nsfe.printStackTrace();
				
			}	catch( NumberFormatException nfe ) {
				nfe.printStackTrace();
			}
				
		}	
	}
	

}
