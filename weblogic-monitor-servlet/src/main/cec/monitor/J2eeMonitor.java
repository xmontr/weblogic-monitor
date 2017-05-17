package cec.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlException;

import cec.monitor.type.Attribut;
import cec.monitor.type.CompositeValue;
import cec.monitor.type.Counter;
import cec.monitor.type.MonitorConfigDocument;

public class J2eeMonitor extends HttpServlet {

	private MonitorConfigDocument config;
	private MBeanServer mbeanserver;
	private Counter[] counterlist;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
        try
        {
            PrintWriter xml = response.getWriter();
            xml.append("<PerformanceMonitor>");
            xml.append("\n");
            xml.append("\t<object class=\"InstanceName\" name=\"Testcentre j2ee Monitor\">").append("\n");
            for (int i = 0; i < counterlist.length; i++) {
            	try{
            	 addCounterMetric(xml,counterlist[i]);
            	}
            	catch (Exception e) {
					System.out.println("Testcentre monitor : could not add counter" + counterlist[i].xmlText());
					e.printStackTrace();
				}
            	
			}
            xml.append("\t</object>\n");
            xml.append("</PerformanceMonitor>\n");
            xml.flush();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

	

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);

	}

	public void init() throws ServletException {
		
		  System.out.println("beginning initialisation of testcentre j2ee monitor");

		try {
			config = MonitorConfigDocument.Factory.parse(getServletContext()
					.getResourceAsStream("/WEB-INF/configFile.xml"));
			counterlist = config.getMonitorConfig().getCounterArray();
			mbeanserver = ManagementFactory.getPlatformMBeanServer();
		     System.out.println("------------------Initialisation of testcentre j2ee monitor successfull");
		     System.err.println("------------------Initialisation of testcentre j2ee monitor successfull");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("--config file :" + getServletContext()
					.getResourceAsStream("/WEB-INF/configFile.xml"));
			System.out.println("--counterlist :" + counterlist );
			System.out.println("--mbeanserver :" + mbeanserver );
			System.err.println("--config file :" + getServletContext()
					.getResourceAsStream("/WEB-INF/configFile.xml"));
			System.err.println("--counterlist :" + counterlist );
			System.err.println("--mbeanserver :" + mbeanserver );
			throw (new ServletException("Could not initialize testcentre monitor"));
		} 

	}
	
	
    private void addCounterMetric(PrintWriter xml, Counter counter)
    throws Exception
{
    	 Attribut[]  listattrib = counter.getAttributArray();
    	   for (int i = 0; i < listattrib.length; i++) {	
    		   // composite attribut
    		   if(listattrib[i].getIsComposite() ){
    			   CompositeData  valObj = (CompositeData ) mbeanserver.getAttribute( 
    					   new ObjectName(counter.getJmxObjectName()), 
    					   listattrib[i].getJmxAttribut()
    				); 
    			   CompositeValue[] listkeys =  listattrib[i].getKeysArray();
    			  
    			   for (int j = 0; j < listkeys.length; j++) {
    				   StringBuffer sb1 = new StringBuffer();
    				   StringBuffer sb2 = new StringBuffer();
    				   sb1.append(listattrib[i].getName());sb1.append(":");sb1.append(listkeys[j].getKey());
    				   sb2.append(listattrib[i].getDescription());sb2.append(":");sb2.append(listkeys[j].getDescription());
    				   printCounter(xml
    						   ,sb1.toString(),
    						   valObj.get( listkeys[j].getKey() ).toString()  ,
    						   sb2.toString()
    				);
    				
    			}
    			   
    			   
    			   
    			   
    		   }
    		   else{
    		   
    			   Object valObj = mbeanserver.getAttribute( 
    					   new ObjectName(counter.getJmxObjectName()), 
    					   listattrib[i].getJmxAttribut()
    				);
    			   printCounter(xml,listattrib[i].getName(),valObj.toString(),listattrib[i].getDescription() );

    		   
    		 
    	    }
    	   }

}
    
	private void printCounter(PrintWriter xml, String name,
			String value, String desc) {
		xml.append("\t\t<counter name=\"");
		xml.append(name);
		xml.append("\"");
		xml.append(" val=\"");
		xml.append(value);
		xml.append("\"");
		xml.append(" desc=\"");
		xml.append(desc);
		xml.append("\"  />\r\n");

	}
	
	
	
	
	
	
	
	
	

}
