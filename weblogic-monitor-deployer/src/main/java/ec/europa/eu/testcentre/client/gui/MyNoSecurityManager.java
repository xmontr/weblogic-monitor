package ec.europa.eu.testcentre.client.gui;

public class MyNoSecurityManager extends SecurityManager {
	
	/** Voids any Permission */
    public void checkPermission(java.security.Permission permission) {    }
    /** Voids any Permission */
    public void checkPermission(java.security.Permission permission, Object obj) {    }
    
    

}
