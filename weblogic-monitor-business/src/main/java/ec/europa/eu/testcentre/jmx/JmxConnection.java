package ec.europa.eu.testcentre.jmx;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;


import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import cec.monitor.type.MonitorConfig;

public class JmxConnection {

	protected static Logger theLogger = Logger.getLogger(JmxConnection.class
			.getName());

	private String host;

	private String port;

	public JmxConnection(String host, String port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public static void TestConnection(String host,String port) throws TestcentreJmxException{
	
			MBeanServerConnection con = JmxConnectionManager.getInstance().getConnection(host, port);
			
			JmxConnectionManager.getInstance().releaseConnection(host, port);
	

		
		
	
		
	}
	
	
	
	
	public String getAdminServerPort(){
		String ret = null;
		MBeanServerConnection con = null;
		try {
			con = JmxConnectionManager.getInstance().getConnection(host, port);
			ObjectName obj = new ObjectName("com.bea:Type=ServerRuntime,*");
			Set<ObjectName> li = con.queryNames(obj, null);		
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName objectName = (ObjectName) it.next();
			
				 ret=((Integer)con.getAttribute(objectName, "AdminServerListenPort")).toString();
				// serverName=(String)connection.getAttribute(objectName, "Name");			 
		
			} 		
			
			
		} catch (Exception e) {
			theLogger.severe(e.getMessage());
		}
		finally{
			if(con != null)
				JmxConnectionManager.getInstance().releaseConnection(host, port);
			
		}	
		return ret;
		
		
	}
	
	
	
	
	
	
	
	
	public String getAdminServerHost(){
		String ret = null;
		MBeanServerConnection con = null;
		try {
			con = JmxConnectionManager.getInstance().getConnection(host, port);
			ObjectName obj = new ObjectName("com.bea:Type=ServerRuntime,*");
			Set<ObjectName> li = con.queryNames(obj, null);		
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName objectName = (ObjectName) it.next();
			
				 ret=(String)con.getAttribute(objectName, "AdminServerHost");
				 
				// adminPort=((Integer)connection.getAttribute(objectName, "AdminServerListenPort")).toString();
				// serverName=(String)connection.getAttribute(objectName, "Name");			 
		
			} 	
			
		} catch (Exception e) {
			theLogger.severe(e.getMessage());
		}
		finally{
			if(con != null)
				JmxConnectionManager.getInstance().releaseConnection(host, port);
			
		}		
		
		return ret;
		
		
	}
	
	
	public String getServerName(){
		String ret = null;
		MBeanServerConnection con = null;
		try {
			con = JmxConnectionManager.getInstance().getConnection(host, port);
			ObjectName obj = new ObjectName("com.bea:Type=ServerRuntime,*");
			Set<ObjectName> li = con.queryNames(obj, null);		
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName objectName = (ObjectName) it.next();
			
				
				 ret=(String)con.getAttribute(objectName, "Name");			 
		
			} 	
			
		} catch (Exception e) {
			theLogger.severe(e.getMessage());
		}
		finally{
			if(con != null)
				JmxConnectionManager.getInstance().releaseConnection(host, port);
			
		}		
		
		return ret;
		
		
	}
	
	
	
	

	public String[] getAllDomains() {
		String[] ret = new String[]{"No data Avalaible"};
		MBeanServerConnection con = null;
		try {
			con = JmxConnectionManager.getInstance().getConnection(host, port);
			ret = con.getDomains();
		} catch (TestcentreJmxException e1) {
			theLogger.severe(e1.getMessage());

			return null;
		} catch (IOException e) {
			theLogger.severe(e.getMessage());
			}
		finally {
			if(con != null)
			JmxConnectionManager.getInstance().releaseConnection(host, port);
		}
		return (ret);
	}
	
	
	
	
	

	/**
	 * return all type of mbean exposed in the jvm
	 */
	public String[] getAllTypesForDomain(String domain) {
		MBeanServerConnection con = null;
		try {
			con = JmxConnectionManager.getInstance().getConnection(host, port);
			Vector<String> ret = new Vector<String>();
			ObjectName obj;
			obj = new ObjectName(domain + ":*");
			Set<ObjectName> set;
			set = con.queryNames(obj, null);
			Iterator<ObjectName> it = set.iterator();
			while (it.hasNext()) {
				ObjectName object = (ObjectName) it.next();
				
				String type = object.getKeyProperty("Type");
				if(type== null)
					type=object.getKeyProperty("type");
				
				if (!ret.contains(type)) {
					ret.add(type);
				}
			}
			return (ret.toArray(new String[ret.size()]));

		} catch (Exception e1) {
			theLogger.severe(e1.getMessage());
			return null;
		}
		
		finally{
			if(con != null)
			JmxConnectionManager.getInstance().releaseConnection(host, port);}

	}

	public Counter[] getMBeanByType(String type, String dom) {
		Counter[] ress = null;
		MBeanServerConnection connection = null;
		try {
			connection = JmxConnectionManager.getInstance().getConnection(host,
					port);
			Vector<Counter> ret = new Vector<Counter>();
			
			
			StringBuffer sb = new StringBuffer(dom);
			sb.append(":Type=");
			sb.append(type);
			sb.append(",*");
			ObjectName name;			
			name = new ObjectName(sb.toString());
			Set<ObjectName> li = connection.queryNames(name, null);
			
			StringBuffer sb2 = new StringBuffer(dom);
			sb2.append(":type=");
			sb2.append(type);
			sb2.append(",*");
			ObjectName name2;			
			name2 = new ObjectName(sb2.toString());
			
			li.addAll(connection.queryNames(name2, null));
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName typ = (ObjectName) it.next();
Counter counter = Counter.Factory.newInstance();				
				ret.add(counter);
				counter.setJmxType(type);
				counter.setJmxObjectName(typ.toString());
				
				
				
				MBeanInfo bi = connection.getMBeanInfo(typ);
				MBeanAttributeInfo[] attInfo = bi.getAttributes();
				for (int i = 0; i < attInfo.length; i++) {

					if (attInfo[i].getType().equals("java.lang.Integer")
							|| attInfo[i].getType().equals("java.lang.Long")  ) {
						Attribut att = counter.addNewAttribut();
						att.setDescription(cleanString(attInfo[i]
								.getDescription()));

						att.setIsComposite(false);
						att.setJmxAttribut(attInfo[i].getName());
						att.setName(attInfo[i].getName() + " "
								+ getPrettyName(typ));
					}
				}
			}
			ress= ret.toArray(new Counter[ret.size()]);


			return (ress);			
		} catch (Exception e1) {
			theLogger.severe(e1.getMessage());
			return null;
		}
		finally{
			if(connection != null)
			JmxConnectionManager.getInstance().releaseConnection(host, port);}

	

	}

	/**
	 * remove html tag <p> <code> and " from description
	 * 
	 * @param input
	 * @return
	 */
	private String cleanString(String input) {
		String ret = null;
		ret = input.replaceAll("<p>", "").replaceAll("</p>", "")
				.replaceAll("<code>", "").replaceAll("</code>", "").replaceAll("\"", "'");
		return ret;

	}

	public static  String getPrettyName(ObjectName ob) {
		StringBuffer sb = new StringBuffer();
		Map<String, String> props = ob.getKeyPropertyList();
		for (Iterator iterator = props.keySet().iterator(); iterator.hasNext();) {
			String type = (String) iterator.next();
			sb.append(type);
			sb.append("=");
			sb.append(props.get(type));
			sb.append(",");
		}
		
		
	
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
		JmxConnection co = new JmxConnection("wlsload4.cc.cec.eu.int", "2102");
		co.getAllTypesForDomain("java.lang");
		
		//Counter[] c = co.getMBeanByType("JVMRuntime", "com.bea");
		//System.out.println(c[0].xmlText());
		
	}
	

}
