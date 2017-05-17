package ec.europa.eu.testcentre.jmx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

import org.apache.xmlbeans.XmlOptions;

import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.SessionHelper;
import weblogic.management.runtime.TaskRuntimeMBean;
import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import cec.monitor.type.MonitorConfig;
import cec.monitor.type.MonitorConfigDocument;

public class ConfigGenerator {
	
	
	
	private static final String DIRTEMP= "/configGenerator/";
	
	
	private static final String APPNAME= "j2eemonitor";
	
	private static final String TASKPREFFIX="REMOTE_J2EE_Monitor_";
	protected static Logger theLogger = Logger.getLogger(ConfigGenerator.class
			.getName());
	
	private String hostName;
	String portNum;
	
	private MBeanServerConnection connection;
	
	private MonitorConfigDocument doc;
	
	private MonitorConfig m;
	
	
	private String adminHost;
	
	private String adminPort;
	
	private String serverName;
			
	private String admName;
	
	
	private String admPass;
	
	private WebLogicDeploymentManager dm = null;
	
	
	
	
	public ConfigGenerator(String hostName, String portNum, MonitorConfigDocument doc, String nam,String pass) throws Exception {
		super();
		if(doc == null){
			doc = MonitorConfigDocument.Factory.newInstance();
			 m = doc.addNewMonitorConfig();
			
		}
		else{
			if(doc.getMonitorConfig() == null){
				m = doc.addNewMonitorConfig();
			}
			else{  m = doc.getMonitorConfig();}
			
		}
		this.hostName = hostName;
		this.portNum = portNum;
		this.doc=doc;
		
		 this.admName = nam;
		 this.admPass = pass;
		
		connect();
	}
	
	
	public void connect() throws Exception{
		/*StringBuffer sb = new StringBuffer();
		sb.append("service:jmx:rmi:///jndi/rmi://");
		sb.append(hostName);
		sb.append(":");
		sb.append(portNum);
		sb.append("/jmxrmi");
		
		JMXServiceURL u = new JMXServiceURL(				
				  sb.toString());
		  JMXConnector c = JMXConnectorFactory.connect(u); 
		  connection = c.getMBeanServerConnection();
		  theLogger.info("jmx connection to " + sb.toString() );
		 
		  
			ObjectName obj = new ObjectName("com.bea:Type=ServerRuntime,*");
			Set<ObjectName> li = connection.queryNames(obj, null);		
			Iterator<ObjectName> it = li.iterator();
			while (it.hasNext()) {
				ObjectName objectName = (ObjectName) it.next();
			
				 adminHost=(String)connection.getAttribute(objectName, "AdminServerHost");
				 adminPort=((Integer)connection.getAttribute(objectName, "AdminServerListenPort")).toString();
				 serverName=(String)connection.getAttribute(objectName, "Name");
				 theLogger.info("connection configmanager on adminhost=" + adminHost + " adminPort=" + adminPort  + "for server name=" +serverName );
			
				 
				 
		
			} */
		  
		  JmxConnection jcon = new JmxConnection(hostName, portNum);
		  adminHost= jcon.getAdminServerHost();
		  adminPort= jcon.getAdminServerPort();
		  serverName=jcon.getServerName();
		  
	}
	
	public String generatePlanFile() throws IOException{
		InputStream is = getClass().getResourceAsStream("app.xml");
		final File sysTempDir = new File(System.getProperty("java.io.tmpdir") + DIRTEMP);
		
		if(! sysTempDir.exists()){
		if( sysTempDir.mkdir() == true ){
			
			theLogger.info("creation directory" + sysTempDir.getAbsolutePath());
		}
		else{
			throw new IOException("enable to create temporary directory for servlet generation :" + sysTempDir.getAbsolutePath( ));
			
		}
		}
			
		
		File tempFile = File.createTempFile("plan", ".xml",sysTempDir);
        byte[] buf = new byte[774];
        DataInputStream din = new DataInputStream(is);
        din.readFully(buf);
        String planString =  new String(buf);
        String newFile = planString.replaceFirst("<module-name>j2eemonitor.war</module-name>", 
        		
        		"<module-name>j2eemonitor" + serverName +".war</module-name>");
        OutputStream out = new DataOutputStream(new FileOutputStream(tempFile));
        out.write(newFile.getBytes());    
        out.close();
        is.close();
      
        return(tempFile.getAbsolutePath());
        
        
        
		
	}
	
	
	
	public  String generateWarFile( ) throws IOException{
		
		InputStream is = ConfigGenerator.class.getResourceAsStream("J2eeMonitor.war");
		String zipEntryName="WEB-INF/configFile.xml";
		final File sysTempDir = new File(System.getProperty("java.io.tmpdir") + DIRTEMP);
		
		if(! sysTempDir.exists()){
		if( sysTempDir.mkdir() == true ){
			
			theLogger.info("creation directory" + sysTempDir.getAbsolutePath());
		}
		else{
			throw new IOException("enable to create temporary directory for servlet generation :" + sysTempDir.getAbsolutePath( ));
			
		}
		}
			
		
		File tempFile = File.createTempFile("configFile", ".war",sysTempDir);
        byte[] buf = new byte[1024];
         
        ZipInputStream zin = new ZipInputStream(is);
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tempFile));
        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            zout.putNextEntry(new ZipEntry(name));
            // Transfer bytes from the ZIP file to the output file
            int len;
            while ((len = zin.read(buf)) > 0) {
                zout.write(buf, 0, len);
            }
		
            entry = zin.getNextEntry();
        }        
        // add the configfile
		zout.putNextEntry(new ZipEntry(zipEntryName));
		 XmlOptions opts = new XmlOptions();
		 opts.setSavePrettyPrint();
		 opts.setSavePrettyPrintIndent(4);		
		 opts.setDocumentType(doc.type);		 
		
        zout.write(doc.xmlText(opts).getBytes());
        zout.closeEntry();
        zout.close();
        zin.close();
        return(tempFile.getAbsolutePath());
		
		
		
	}
	
	public WebLogicDeploymentManager getRemoteDeployerManager() throws DeploymentManagerCreationException{
		synchronized (this) {
			if(dm == null){
				 dm = SessionHelper.getRemoteDeploymentManager("t3",adminHost, adminPort,admName,admPass);
					theLogger.info("getting remote deploymentmanager from "+ adminHost + ":" + adminPort);
				
			}
			
		return(dm);	
		}
		
	}
	
	
	public ProgressObject undeployPreviousVersion(TargetModuleID pre_tmid) throws Exception {
		dm = getRemoteDeployerManager();
		theLogger.info( " re undeploying previous module " + pre_tmid.getModuleID() );
		ProgressObject po = dm.undeploy(new TargetModuleID[]{pre_tmid});
		
		return po;
	}
	
	
	
	private Target getTarget() throws Exception {
		Target currenttarget = null;
		 dm = getRemoteDeployerManager();				
			Target[] ts = dm.getTargets();
			for (int i = 0; i < ts.length; i++) {
				if(ts[i].getName().equals(serverName))
					currenttarget=ts[i];
			}
		return (currenttarget);
	}
	
	
	public ProgressObject deploy2server( ) throws Exception{
		
		
		try{
		 dm = getRemoteDeployerManager();				
	
		dm.enableFileUploads();
		String appname= getAppName();
		ModuleType moduletyp = ModuleType.WAR;
		Target target = getTarget();
		TargetModuleID[] tmid = new TargetModuleID[]{  dm.createTargetModuleID(appname, moduletyp, target)   } ;
		Target[] argtarget= new Target[]{target};
		File warfile = new File (generateWarFile());		
		File planfile = new File(   generatePlanFile());
		DeploymentOptions dopt = new DeploymentOptions();
		dopt.setNameFromLibrary(false);	
		dopt.setRemote(true);
		dopt.setName(appname);		
		dopt.setGracefulIgnoreSessions(true);	
		String taskid = TASKPREFFIX + System.currentTimeMillis();
		dm.setTaskId(taskid);		
		theLogger.info( " deploying " + warfile.getAbsolutePath()  + " with plan " + planfile.getAbsolutePath() );
			ProgressObject po2=dm.deploy(tmid,  warfile, planfile,dopt);	
		warfile.deleteOnExit();
		planfile.deleteOnExit();
		
	
		return po2;
		}
		finally{
			if(dm != null)
			dm.release();
			
			
		}
		 

	}
	
	
	/**
	 * @return the name of the deployment 
	 */
	private String getAppName() {
		
		return APPNAME + serverName;
	}


	/**
	 * @param tg 
	 * @return the targetmoduleid if servlet already deploy or null
	 */
	public TargetModuleID getPreExistingTargetId(){
		TargetModuleID ret = null;
		try {
			System.out.println("searching " + getAppName() + " in target" + getTarget().getName());
			TargetModuleID[] list =  dm.getAvailableModules(ModuleType.WAR, new Target[]{getTarget()});
			for (int i = 0; i < list.length; i++) {
				System.out.println("found module "+list[i].getModuleID() );
				if(list[i].getModuleID().equals(getAppName())){
					ret = list[i];break;
				}
			
			}		
			
		} catch (Exception e) {
		theLogger.severe(e.getMessage());
		} 
		
		
		
		
		
		return ret;
		
		
	}
	
	
	
	public String getFile(){
		 XmlOptions opts = new XmlOptions();
		 opts.setSavePrettyPrint();
		 opts.setSavePrettyPrintIndent(4);		
		 opts.setDocumentType(doc.type);		 
		return(doc.xmlText(opts));
		
	}
	
	

	
	
	public String geteploymentStateByDeploymentTaskId(String taskid) throws DeploymentManagerCreationException{
		StringBuffer ret = new StringBuffer();
		String id="ADTR-"+taskid;
		 WebLogicDeploymentManager localdm = getRemoteDeployerManager();
		boolean found = false;
		
		TaskRuntimeMBean[] toto= localdm.getHelper().getAllTasks();
		
		for (int i = 0; i < toto.length; i++) {
			if(toto[i].getName().equals(id)){
				ret.append("Task name:").append("=").append(toto[i].getName()).append("\r\n");
				ret.append("Task status:").append("=").append(toto[i].getStatus()).append("\r\n");
				ret.append("Task is Running:").append("=").append(toto[i].isRunning()).append("\r\n");
				found = true;
				break;		
			}
			
			
		}
		if(!found){
			ret.append("no task with id ").append(id);
			
		}
		
		
		return ret.toString();
		
	}
	
	

	
	

	
	
	/**
	 *  remove html tag from description
	 * @param input
	 * @return
	 */
	private String cleanString(String input){
		String ret = null;		
		ret = input.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<code>", "").replaceAll("</code>", "");
		return ret;
		
	}
	
	
	public void addCounterByType(String type) throws MalformedObjectNameException, NullPointerException, IOException, InstanceNotFoundException, IntrospectionException, ReflectionException{
		
		QueryExp query=null;
		StringBuffer sb = new StringBuffer("com.bea:Type=");
		sb.append(type);
		sb.append(",*");
		ObjectName name=new ObjectName(sb.toString()) ;
		Set<ObjectName> li = connection.queryNames(name, null);		
		Iterator<ObjectName> it = li.iterator();
		while (it.hasNext()) {		
			ObjectName typ = (ObjectName) it.next();
			
			Counter counter =m.addNewCounter();
			counter.setJmxType(type);
			counter.setJmxObjectName(typ.toString());
			MBeanInfo bi = connection.getMBeanInfo(typ);
			MBeanAttributeInfo[] attInfo = bi.getAttributes();
			for (int i = 0; i < attInfo.length; i++) {
				
				if(attInfo[i].getType().equals("java.lang.Integer") || attInfo[i].getType().equals("java.lang.Long") ){
				Attribut att = counter.addNewAttribut();
				att.setDescription(cleanString(attInfo[i].getDescription()));
				
				att.setIsComposite(false);
				att.setJmxAttribut(attInfo[i].getName());
				att.setName(attInfo[i].getName() + " " +  getPrettyName(typ) );
				}
				
				
			}
			
		
		}
		
		
		
	}
	
	private String getPrettyName(ObjectName ob){
		StringBuffer sb = new StringBuffer();
		Map<String,String> props= ob.getKeyPropertyList();
		if ( props.containsKey("Name"))
			sb.append(props.get("Name"));
		sb.append(" ");
		if ( props.containsKey("ServerRuntime"))
			sb.append(props.get("ServerRuntime"));	
		return sb.toString();
	}
	
	
	
	
	
	
	
	

}
