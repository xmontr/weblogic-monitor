package ec.europa.eu.testcentre.client.gui;

import java.awt.EventQueue;

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;

import ec.europa.eu.testcentre.jmx.ConfigGenerator;
import ec.europa.eu.testcentre.jmx.JmxConnection;
import ec.europa.eu.testcentre.jmx.JmxConnectionManager;
import ec.europa.eu.testcentre.jmx.WeblogicDomainEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import weblogic.server.channels.AdminPortService;

import ec.europa.eu.testcentre.jmx.ConfigGenerator;




import cec.monitor.type.Attribut;
import cec.monitor.type.Counter;
import cec.monitor.type.MonitorConfig;
import cec.monitor.type.MonitorConfigDocument;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ServletDeployer {
	
	 static abstract class GuiCommande extends SwingWorker<Void, String>{
		
		protected JFrame frame ;		
		
		public GuiCommande(JFrame frame) {
			super();
			this.frame = frame;
		}


		
		
		protected abstract void back ()  throws Exception;
		
		protected abstract void front()  throws Exception ;
		
		
		protected void unmask() {
			frame.getGlassPane().setVisible(false);
			
		}
		
		
		protected void mask() {
			frame.getGlassPane().setVisible(true);
			
		}


		protected void showMessage(String message) {
			System.out.println(message);
			JOptionPane.showMessageDialog(frame,
					message );
			
		}
		
		protected void showException(Exception ex) {
			StringBuffer sb  = new StringBuffer();
			sb.append(ex.getMessage());
			if(ex.getCause()!= null){
				sb.append(ex.getCause().getMessage());
				
			}
			showMessage(sb.toString());
				
			}
		
		public void doExecute(){
			execute();
			mask();
			
			
		}
		
		@Override
		protected void process(List<String> chunks) {
			super.process(chunks);
			for (Iterator iterator = chunks.iterator(); iterator.hasNext();) {
				String message = (String) iterator.next();
				BetterGlassPane gp = (BetterGlassPane)frame.getGlassPane();
				gp.setMessage(message);
				
				
			}
		}
				
		
				
				@Override
				protected Void doInBackground() throws Exception {
					try{
						back();
						
					}
					catch( Exception e){
						showException(e);
						
					}
					return null;
				}
				
				@Override
				protected void done() {
					try{
					front();
					}
					catch(Exception e){
						showException(e);
					}
					finally{					
						unmask();
					}
				}
			}

	
		
			
		




	
		
	
	
	
	
	
	
	
	

	private JFrame frame;
	private JTextField hostField;
	private JTextField jmxPortField;
	
	private JTabbedPane counterPanel;
	private boolean connectionValid = false;
	private final Action selectHostAction = new SwingAction_1();
	private final Action deployAction = new SwingAction_2();
	private JTextField adminhostfiled_;
	private JTextField adminprttfield;
	private JTextField adminUserField;
	private JTextField adminpasswordfield;
	private JToolBar toolBar;
	private final Action action = new SwingAction();
	private DefaultValidationResultModel vrm;
	private JScrollPane validationResultsComponent;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		System.setSecurityManager(new MyNoSecurityManager());
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServletDeployer window = new ServletDeployer();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}





	/**
	 * Create the application.
	 */
	public ServletDeployer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 905, 510);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		BetterGlassPane gp = new BetterGlassPane(frame);
		frame.setGlassPane(gp);
		
		JLabel hostlabel = new JLabel("Host");
		GridBagConstraints gbc_hostlabel = new GridBagConstraints();
		gbc_hostlabel.insets = new Insets(0, 0, 5, 5);
		gbc_hostlabel.anchor = GridBagConstraints.EAST;
		gbc_hostlabel.gridx = 0;
		gbc_hostlabel.gridy = 0;
		frame.getContentPane().add(hostlabel, gbc_hostlabel);
		
		hostField = new JTextField();
		ValidationComponentUtils.setMandatory(hostField, true);
		ValidationComponentUtils.setMessageKey(hostField, "Host.host_name");
		
		
		GridBagConstraints gbc_hostField = new GridBagConstraints();
		gbc_hostField.insets = new Insets(0, 0, 5, 0);
		gbc_hostField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostField.gridx = 1;
		gbc_hostField.gridy = 0;
		frame.getContentPane().add(hostField, gbc_hostField);
		hostField.setColumns(10);
		
		JLabel jmxPortlabel = new JLabel("jmx port");
		GridBagConstraints gbc_jmxPortlabel = new GridBagConstraints();
		gbc_jmxPortlabel.anchor = GridBagConstraints.EAST;
		gbc_jmxPortlabel.insets = new Insets(0, 0, 5, 5);
		gbc_jmxPortlabel.gridx = 0;
		gbc_jmxPortlabel.gridy = 1;
		frame.getContentPane().add(jmxPortlabel, gbc_jmxPortlabel);
		
		jmxPortField = new JTextField();
		ValidationComponentUtils.setMandatory(jmxPortField, true);
		ValidationComponentUtils.setMessageKey(jmxPortField, "Host.port");
		
		GridBagConstraints gbc_jmxPortField = new GridBagConstraints();
		gbc_jmxPortField.insets = new Insets(0, 0, 5, 0);
		gbc_jmxPortField.anchor = GridBagConstraints.WEST;
		gbc_jmxPortField.gridx = 1;
		gbc_jmxPortField.gridy = 1;
		frame.getContentPane().add(jmxPortField, gbc_jmxPortField);
		jmxPortField.setColumns(10);
		
		JLabel adminhostlabel = new JLabel("admin host");
		GridBagConstraints gbc_adminhostlabel = new GridBagConstraints();
		gbc_adminhostlabel.anchor = GridBagConstraints.EAST;
		gbc_adminhostlabel.insets = new Insets(0, 0, 5, 5);
		gbc_adminhostlabel.gridx = 0;
		gbc_adminhostlabel.gridy = 2;
		frame.getContentPane().add(adminhostlabel, gbc_adminhostlabel);
		
		adminhostfiled_ = new JTextField();
		adminhostfiled_.setEditable(true);
		GridBagConstraints gbc_adminhostfiled_ = new GridBagConstraints();
		gbc_adminhostfiled_.insets = new Insets(0, 0, 5, 0);
		gbc_adminhostfiled_.fill = GridBagConstraints.HORIZONTAL;
		gbc_adminhostfiled_.gridx = 1;
		gbc_adminhostfiled_.gridy = 2;
		frame.getContentPane().add(adminhostfiled_, gbc_adminhostfiled_);
		adminhostfiled_.setColumns(10);
		
		JLabel lblAdminPort = new JLabel("admin port");
		GridBagConstraints gbc_lblAdminPort = new GridBagConstraints();
		gbc_lblAdminPort.anchor = GridBagConstraints.EAST;
		gbc_lblAdminPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdminPort.gridx = 0;
		gbc_lblAdminPort.gridy = 3;
		frame.getContentPane().add(lblAdminPort, gbc_lblAdminPort);
		
		adminprttfield = new JTextField();
		adminprttfield.setEditable(true);
		GridBagConstraints gbc_adminprttfield = new GridBagConstraints();
		gbc_adminprttfield.insets = new Insets(0, 0, 5, 0);
		gbc_adminprttfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_adminprttfield.gridx = 1;
		gbc_adminprttfield.gridy = 3;
		frame.getContentPane().add(adminprttfield, gbc_adminprttfield);
		adminprttfield.setColumns(10);
		
		JLabel lblAdminUser = new JLabel("Admin user");
		GridBagConstraints gbc_lblAdminUser = new GridBagConstraints();
		gbc_lblAdminUser.anchor = GridBagConstraints.EAST;
		gbc_lblAdminUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdminUser.gridx = 0;
		gbc_lblAdminUser.gridy = 4;
		frame.getContentPane().add(lblAdminUser, gbc_lblAdminUser);
		
		adminUserField = new JTextField();
		GridBagConstraints gbc_adminUserField = new GridBagConstraints();
		gbc_adminUserField.fill = GridBagConstraints.HORIZONTAL;
		gbc_adminUserField.anchor = GridBagConstraints.WEST;
		gbc_adminUserField.insets = new Insets(0, 0, 5, 0);
		gbc_adminUserField.gridx = 1;
		gbc_adminUserField.gridy = 4;
		frame.getContentPane().add(adminUserField, gbc_adminUserField);
		adminUserField.setColumns(10);
		
		JLabel lblAdminPassword = new JLabel("admin password");
		GridBagConstraints gbc_lblAdminPassword = new GridBagConstraints();
		gbc_lblAdminPassword.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gbc_lblAdminPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblAdminPassword.gridx = 0;
		gbc_lblAdminPassword.gridy = 5;
		frame.getContentPane().add(lblAdminPassword, gbc_lblAdminPassword);
		
		adminpasswordfield = new JTextField();
		GridBagConstraints gbc_adminpasswordfield = new GridBagConstraints();
		gbc_adminpasswordfield.insets = new Insets(0, 0, 5, 0);
		gbc_adminpasswordfield.fill = GridBagConstraints.HORIZONTAL;
		gbc_adminpasswordfield.gridx = 1;
		gbc_adminpasswordfield.gridy = 5;
		frame.getContentPane().add(adminpasswordfield, gbc_adminpasswordfield);
		adminpasswordfield.setColumns(10);
		
		JButton btnSelect = new JButton("select");
		btnSelect.setAction(selectHostAction);
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 8;
		frame.getContentPane().add(btnSelect, gbc_btnSelect);
		
		toolBar = new JToolBar();
		toolBar.setEnabled(false);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.gridwidth = 2;
		gbc_toolBar.anchor = GridBagConstraints.WEST;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 9;
		frame.getContentPane().add(toolBar, gbc_toolBar);
		
		JButton btnAddCounter = new JButton("add counter");
		btnAddCounter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addCounter();
			}
		});
		toolBar.add(btnAddCounter);
		
		JButton btnDeleteCounter = new JButton("delete counter");
		btnDeleteCounter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deleteCurrentCounter();
			}
		});
		toolBar.add(btnDeleteCounter);
		
		JButton btnDeploy = new JButton("deploy");
		btnDeploy.setAction(deployAction);
		toolBar.add(btnDeploy);
		
		JButton btnActivateCombea = new JButton("activate com.bea");
		btnActivateCombea.setAction(action);
		toolBar.add(btnActivateCombea);
		
		counterPanel = new JTabbedPane();
		counterPanel.setBorder(new TitledBorder(null, "counter selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_counterPanel = new GridBagConstraints();
		gbc_counterPanel.fill = GridBagConstraints.BOTH;
		gbc_counterPanel.gridx = 1;
		gbc_counterPanel.gridy = 10;
		frame.getContentPane().add(counterPanel, gbc_counterPanel);
		
		vrm = new DefaultValidationResultModel();		
		validationResultsComponent = (JScrollPane)
                ValidationResultViewFactory.createReportList(vrm);

		
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 1;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		frame.getContentPane().add(validationResultsComponent, gbc_scrollPane);
		
	}
	
	
	private void deleteCurrentCounter() {
		counterPanel.removeTabAt(counterPanel.getSelectedIndex());
		
	}

	private void addCounter(){
		
		SinglePanelCounter counter = new SinglePanelCounter(frame, hostField.getText(),jmxPortField.getText());
		counterPanel.add(counter);
		counterPanel.setSelectedIndex(counterPanel.getTabCount()-1);
	//	counterPanel.addTab(counter.getTitle(), null, counter, null);
		
	}
	
				
			
			
		
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "selectTheHost");
			putValue(SHORT_DESCRIPTION, "Select the host");
		}
		public void actionPerformed(ActionEvent e) {
			
			ValidationResult vr = validateHostSelectionForm();				
			vrm.setResult(vr);
			ValidationComponentUtils.updateComponentTreeSeverity(frame.getRootPane(), vr);
			ValidationComponentUtils.updateComponentTreeSeverityBackground(frame.getRootPane(), vr);		
			frame.getRootPane().validate();
			if (!vr.hasErrors()) {
			
			GuiCommande guicomm = new GuiCommande(frame) {			
				
				JmxConnection connection;
				
				@Override
				protected void front() throws Exception {
					adminhostfiled_.setText(connection.getAdminServerHost());
					adminprttfield.setText(connection.getAdminServerPort());
					
				}
				
				@Override
				protected void back() throws Exception {
					String port=jmxPortField.getText();
					String host= hostField.getText();
					publish("connecting to jmx port " + host + ":" + port);
					JmxConnection.TestConnection(host,port);					
					connection = new JmxConnection(host, port);
						
					
					
				}
			};
			adminprttfield.setText(null);
			adminprttfield.setText(null);
			guicomm.doExecute();
			
			}

			
		} 	
	}
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "Deploy");
			putValue(SHORT_DESCRIPTION, "launch the remote deployer");
		}
		public void actionPerformed(ActionEvent e) {
			deployServlet();
			
			
			
			
		}
	}
	
	
	
	private MonitorConfigDocument getConfigDoc(){
		Component[] listcompo = counterPanel.getComponents();
		MonitorConfigDocument doc = MonitorConfigDocument.Factory.newInstance();
		MonitorConfig conf = doc.addNewMonitorConfig();
		Vector<cec.monitor.type.Counter> finallist=new Vector<cec.monitor.type.Counter>();
		for (int i = 0; i < listcompo.length; i++) {			
		cec.monitor.type.Counter[] listcounter = ((SinglePanelCounter)listcompo[i]).getSelectedCounter();
		for (int j = 0; j < listcounter.length; j++) {
			finallist.add(listcounter[j]);
		}
		
			
		}
		cec.monitor.type.Counter[] lc = finallist.toArray(new cec.monitor.type.Counter[finallist.size()]);
		conf.setCounterArray(lc);
		return doc;
	}

	public void deployServlet() {
		
		GuiCommande deploycomm = new GuiCommande(frame) {
			
			
			ProgressObject po;
			@Override
			protected void front() throws Exception {
				JOptionPane.showMessageDialog(frame,
					    "deploy finished with status : " + po.getDeploymentStatus().getState() + " message " + po.getDeploymentStatus().getMessage() );
						
				
			}
			
			@Override
			protected void back() throws Exception {
				String hostName = hostField.getText();
				String portNum=jmxPortField.getText();
				String nam = adminUserField.getText();
				System.out.println("building config generator " + hostName+":"+portNum + "for weblogic admin" + nam );	
				publish("building config generator " + hostName+":"+portNum + "for weblogic admin" + nam );
			
				boolean valid = WeblogicDomainEditor.testConnection(adminhostfiled_.getText(), adminprttfield.getText(), adminUserField.getText(), adminpasswordfield.getText());	
				if(!valid){
					JOptionPane.showMessageDialog(frame,
						    "wrong adminuser or password  "  );
					
					
				}
				else{
					ConfigGenerator cfg;
					cfg = new ConfigGenerator(hostName, portNum , getConfigDoc(), nam , adminpasswordfield.getText());
					TargetModuleID pre = cfg.getPreExistingTargetId() ;
					if( pre!= null){
						int n = JOptionPane.showConfirmDialog(
							    frame,
							    "A previous version of j2eemonitor is deployed,Do you want to undeploy ?",
							    "Do you want to undeploy ?",
							    JOptionPane.YES_NO_OPTION);
						
						if(! (n == JOptionPane.NO_OPTION) ){
						
						ProgressObject poun = cfg.undeployPreviousVersion(pre);
						
						
						while (poun.getDeploymentStatus().isRunning() ) {
							System.out.println(" undeploy running");
							publish("undeploy previous version, please wait ...");
							Thread.currentThread().sleep(1000);
						}
						
						}
						
					}
						
						po  = cfg.deploy2server();
						while (po.getDeploymentStatus().isRunning() ) {
							
							System.out.println(" deploy running");
							publish(" deploy new version, please wait ...");
							Thread.currentThread().sleep(1000);
						}

						
						
						
				
			}
		}
		
		
		
		}	;
		
		deploycomm.doExecute();
		
		
				
				
	}
			

			
	
		

		
	
	
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "enable com.bea domain");
			putValue(SHORT_DESCRIPTION, "set plattformmbeanenable and  plattformmbeanused to true");
		}
		public void actionPerformed(ActionEvent ev) {
			
			try {
				WeblogicDomainEditor wle = new WeblogicDomainEditor(adminhostfiled_.getText(), adminprttfield.getText(), adminUserField.getText(), adminpasswordfield.getText());
				wle.setPlatformMbeanServerEnable();
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	try {
				WeblogicDomainEditor wle = new WeblogicDomainEditor(adminhostfiled_.getText(), adminprttfield.getText(), adminUserField.getText(), adminpasswordfield.getText());
				wle.setPlatformMbeanServerEnable();
				JOptionPane.showMessageDialog(frame,
					    "setPlatformMbeanUsed and setPlatformMbeanEnabled where set to true. please restart the server" );
				
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame,
					    "unable to perform modification",
					    " error",
					    JOptionPane.ERROR_MESSAGE);
				e.getMessage();
			}
			
			
			
			
		}
	}
	
	
	
	public ValidationResult validateHostSelectionForm(){
		ValidationResult vr = new ValidationResult();
		
		if (ValidationUtils.isEmpty(hostField.getText())) {			
			vr.addError("Hostname cannot be empty."); 		
		
	}
		if (ValidationUtils.isEmpty(jmxPortField.getText())) {			
			vr.addError("Jmx port cannot be empty."); 		
		
	}
		
		
		

		return vr;
	}
	
	
}
	
	
	
		
	
		
		
	
	
	
	
	
	
	
	
	

