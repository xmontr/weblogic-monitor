package ec.europa.eu.testcentre.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;



public class JmxConnectionManager {
	
	private static class MyJmxConnector{
		
		private JMXConnector connector;
		
		
		public MyJmxConnector(String url) throws IOException{
			
			JMXServiceURL u = new JMXServiceURL(				
					  url);
		
			connector = JMXConnectorFactory.connect(u); 
		}
		
		private int inUse = 0;
		
		public void increaseUse(){
			synchronized (this) {
				inUse++;
			}
			
		}
		
		public void decreaseUse(){
			synchronized (this) {
				inUse--;
			}
			
		}
		
		
		public JMXConnector getJMXConnector(){
			return connector;
		}
		
		
		
	}
	
	
	
	
	private static JmxConnectionManager instance = null;
	
	public static  JmxConnectionManager getInstance(){
		if(instance == null){
			instance = new JmxConnectionManager();
			
		}
	return instance;
		
	}
	
	private Map<String,MyJmxConnector> cache;
	
	protected JmxConnectionManager(){
		Hashtable<String, MyJmxConnector> table =new Hashtable<String, MyJmxConnector>();
		cache = Collections.synchronizedMap(table);
		
		
	}
	
	public synchronized void releaseConnection(String host, String port) {
		StringBuffer sb = new StringBuffer();
		sb.append("service:jmx:rmi:///jndi/rmi://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/jmxrmi");
		cache.get(sb.toString()).decreaseUse();
		
		
	}
	
	
	
	
	public synchronized MBeanServerConnection getConnection (String host, String port) throws TestcentreJmxException{
		MBeanServerConnection ret = null;
		StringBuffer sb = new StringBuffer();
		sb.append("service:jmx:rmi:///jndi/rmi://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/jmxrmi");	
		if(!cache.containsKey(sb.toString())){
			MyJmxConnector c;
			try {
				c = new MyJmxConnector(sb.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new TestcentreJmxException(e);
			}
			
			cache.put(sb.toString(), c);
		}
		
		
		  JMXConnector co = cache.get(sb.toString()).getJMXConnector(); 
		  try {
			ret = co.getMBeanServerConnection();
			cache.get(sb.toString()).increaseUse();
		} catch (IOException e) {
			throw new TestcentreJmxException(e);
			
		}
		  
		  
		  

		
		
		
		return(ret);
	
	

}
	
}
