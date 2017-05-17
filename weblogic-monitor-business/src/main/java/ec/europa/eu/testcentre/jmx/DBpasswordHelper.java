package ec.europa.eu.testcentre.jmx;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import weblogic.management.scripting.utils.WLSTInterpreter;

public class DBpasswordHelper {

	/**
	 * @param args
	 * @throws TestcentreJmxException 
	 * @throws NullPointerException 
	 * @throws MalformedObjectNameException 
	 */
	public static void main(String[] args) throws Exception{
		String hostname = "wlsload5.cc.cec.eu.int";
		String jmxport ="1149";
		String adminPort= null;
		String adminHost=null;
		String adminUser="system";
		String adminPassword="weblogic11g";
		
		
		
		MBeanServerConnection connection = JmxConnectionManager.getInstance().getConnection(hostname, jmxport);
		JmxConnection jc = new JmxConnection(hostname, jmxport);
		adminPort = jc.getAdminServerPort();
		adminHost = jc.getAdminServerHost();
		
		ObjectName domainName = new ObjectName("com.bea:Name=SEP_FO_LOAD,Type=Domain");
		String rootDirectory = (String)connection.getAttribute(domainName, "RootDirectory");
		
		//create local rootdirectory
		
		File localrootdirectory = new File(rootDirectory + "/security");
		System.out.println("creation loacl root directory");
		boolean ret = localrootdirectory.mkdirs();
		if( ret == false){
			
			System.out.println( "------------ERROR CREATION LOCAL DIRECTORY");
		}
		
StringBuffer sbconnect = new StringBuffer("connect('")
.append(adminUser).append("','").append("weblogic11g','t3://")
.append(adminHost)
.append(":")
.append(adminPort)
.append("')");
	
WLSTInterpreter interp = new WLSTInterpreter();
System.out.println(sbconnect.toString());
interp.exec(sbconnect.toString());




StringBuffer command1 = new StringBuffer("    domainHomeAbsolutePath = os.path.abspath('").append(rootDirectory).append("')") ;
StringBuffer command2=new StringBuffer( " encryptionService = weblogic.security.internal.SerializedSystemIni.getEncryptionService(domainHomeAbsolutePath)");

StringBuffer command3=new StringBuffer(  "  ces = weblogic.security.internal.encryption.ClearOrEncryptedService(encryptionService)");


System.out.println(command1.toString());
interp.exec(command1.toString());

System.out.println(command2.toString());
interp.exec(command2.toString());

System.out.println(command3.toString());
interp.exec(command3.toString());






		
		
		
		ObjectName name=new ObjectName("com.bea:Type=weblogic.j2ee.descriptor.wl.JDBCDriverParamsBean,*");
		Set<ObjectName> list  = connection.queryNames(name, null);
		Iterator<ObjectName> it = list.iterator();
		while (it.hasNext()) {
			ObjectName objectName = (ObjectName) it.next();
			String password = (String)connection.getAttribute(objectName, "Password");
			String PasswordEncrypted = new String(  (byte[])connection.getAttribute(objectName, "PasswordEncrypted"));
			
			
			StringBuffer command4=new StringBuffer("   clear = ces.decrypt('").append(PasswordEncrypted).append("')");
			StringBuffer command6 = new StringBuffer("print \"decoded password:\" + clear");
			System.out.println(command4.toString());
			interp.exec(command4.toString());
			
			System.out.println(command6.toString());
			interp.exec(command6.toString());
			
			
			
			String Properties = ((ObjectName)connection.getAttribute(objectName, "Properties")).getKeyProperty("Name");
			String url = ((String)connection.getAttribute(objectName, "Url"));
			System.out.println("found "+ Properties);
			System.out.println("for URL=" + url);
			System.out.println(" password= " + password + "  and passwordencrypted=" + PasswordEncrypted );
			
			
			
			
		}
		

		
		
		
	
		
		
		

	}

}
