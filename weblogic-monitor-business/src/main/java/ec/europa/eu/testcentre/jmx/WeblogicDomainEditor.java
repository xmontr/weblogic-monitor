package ec.europa.eu.testcentre.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.management.Attribute;

public class WeblogicDomainEditor {
	
	protected static Logger theLogger = Logger.getLogger(WeblogicDomainEditor.class
			.getName());
	
	
	private String hostname;
	
	
	private String port;
	
	
	private String weblogicAdminName;
	
	
	private String weblogicAdminPassword;
	
	
	private JMXConnector connector;
	
	private MBeanServerConnection connection;
	 
	private static final  ObjectName service;
	
	   static {
		      try {
		        service = new ObjectName(
		           "com.bea:Name=EditService,Type=weblogic.management.mbeanservers.edit.EditServiceMBean");
		      } catch (MalformedObjectNameException e) {
		         throw new AssertionError(e.getMessage());
		      }
		   }

public static boolean testConnection(String hostname, String port,String weblogicAdminName, String weblogicAdminPassword){
	
	boolean ret =false;
	WeblogicDomainEditor wled=null;
	try {
		 wled = new WeblogicDomainEditor(hostname, port, weblogicAdminName, weblogicAdminPassword);
		 ret=true;
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally{
		if(wled != null)wled.close();
			
			
		
	}
	return ret;
	
	
	
	
	
	

	
	
}
	   
	   
	   
	   
	public WeblogicDomainEditor(String hostname, String port,
			String weblogicAdminName, String weblogicAdminPassword) throws IOException, MalformedObjectNameException, NullPointerException {
		super();
		this.hostname = hostname;
		this.port = port;
		this.weblogicAdminName = weblogicAdminName;
		this.weblogicAdminPassword = weblogicAdminPassword;


			connect();
		
		
	}
	
	
	private void connect() throws IOException{
		
	      String protocol = "t3";
	      Integer portInteger = Integer.valueOf(port);
	      int port = portInteger.intValue();
	      String jndiroot = "/jndi/";
	      String mserver = "weblogic.management.mbeanservers.edit";

	      JMXServiceURL serviceURL = new JMXServiceURL(protocol, hostname, port,
	      jndiroot + mserver);

	      Hashtable<String,Object> h = new Hashtable<String,Object>();
	      h.put(Context.SECURITY_PRINCIPAL, weblogicAdminName);
	      h.put(Context.SECURITY_CREDENTIALS, weblogicAdminPassword);
	      h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
	         "weblogic.management.remote");
	      h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
	      connector = JMXConnectorFactory.connect(serviceURL, h);
	      connection = connector.getMBeanServerConnection();
		theLogger.info("connecting to edit mbean server for host" + hostname);
		
		
	
		
		
	}
	
	public void close(){
		try {
			connector.close();
		} catch (IOException e) {
			theLogger.severe(e.getMessage());
			
		}
		
		
	}
	
	
	
	
	public void setPlatformMbeanServerEnable(){
		
	
		
		StringBuffer sb = new StringBuffer("com.bea:*,Type=JMX");
		ObjectName name;
		ObjectName typ;
		try {
			startEditSession();
			
		
			name = new ObjectName(sb.toString());
			Set<ObjectName> li;
			li = connection.queryNames(name, null);
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				 typ = (ObjectName) it.next();
				setPlatformMbeanServerEnable(typ);
			
			}
			SaveAndActivate();
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			theLogger.severe(e.getMessage());
		} 

		
	}
	private void setPlatformMbeanServerEnable(ObjectName typ) throws InstanceNotFoundException, ReflectionException, IOException {
		AttributeList list = new AttributeList();
	
		Attribute attenable = new Attribute("PlatformMBeanServerEnabled", new Boolean(true));
		Attribute attused = new Attribute("PlatformMBeanServerUsed", new Boolean(true));
		list.add(attused);
		list.add(attenable);
		connection.setAttributes(typ, list);
		theLogger.info("setting PlatformMBeanServerEnabled and PlatformMBeanServerUsed for "  + typ.toString());
		
	}


	private ObjectName SaveAndActivate() throws Exception {
	      // Get the object name for ConfigurationManagerMBean.
	      ObjectName cfgMgr = (ObjectName) connection.getAttribute(service,
	         "ConfigurationManager");
	      // Instruct MBeanServerConnection to invoke
	      // ConfigurationManager.activate(long timeout).
	      // The activate operation returns an ActivationTaskMBean.
	      // You can use the ActivationTaskMBean to track the progress
	      // of activating changes in the domain.
	      connection.invoke(cfgMgr, "save", null, null);
	      
	      ObjectName task = (ObjectName) connection.invoke(cfgMgr, "activate",
	         new Object[] { new Long(120000) }, new String[] { "java.lang.Long" });
	      
	      theLogger.info("save and activate the session");
	      return task;
	   }
	
	
	private ObjectName startEditSession() throws Exception {	
	      // Get the object name for ConfigurationManagerMBean.
	      ObjectName cfgMgr = (ObjectName) connection.getAttribute(service,   "ConfigurationManager");

	      // Instruct MBeanServerConnection to invoke
	      // ConfigurationManager.startEdit(int waitTime int timeout).
	      // The startEdit operation returns a handle to DomainMBean, which is
	      // the root of the edit hierarchy.
	      ObjectName domainConfigRoot = (ObjectName) 	         connection.invoke(
	        		 cfgMgr,
	        		 "startEdit", 
	        		 	new Object[] { new Integer(60000),	 new Integer(120000) }, 
	        				 new String[] { "java.lang.Integer",	         "java.lang.Integer" }
	        		 );
	      
	      
	      if (domainConfigRoot == null) {
	         // Couldn't get the lock
	         throw new Exception("Somebody else is editing already");
	      }
	      theLogger.info("starting edit session");
	      return domainConfigRoot;
}
	
	}


