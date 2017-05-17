package ec.europa.eu.testcentre.client.gui;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.xmlbeans.XmlOptions;

import cec.monitor.type.Attribut;
import cec.monitor.type.MonitorConfig;
import cec.monitor.type.MonitorConfigDocument;


import ec.europa.eu.testcentre.jmx.JmxConnection;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PanelCounter extends JPanel implements ActionListener {
	


	private String host;
	
	private String port;

	private JComboBox domainComboBox;

	private JComboBox typecomboBox;
	
	private Object olddomain;
	
	
	private Object oldtype;

	private JTree attributTree;

	private DefaultMutableTreeNode root;
	
	

	
	


	
	public PanelCounter(String host,String port){
		init();
		this.host=host;
		this.port=port;
		JmxConnection connection = new JmxConnection(host, port);
		
		String[] ret = connection.getAllDomains();	
		for (int i = 0; i < ret.length; i++) {
			domainComboBox.addItem(ret[i]);
			
		}
		
		
	}
	
	private void init() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblDomain = new JLabel("domain");
		GridBagConstraints gbc_lblDomain = new GridBagConstraints();
		gbc_lblDomain.insets = new Insets(0, 0, 5, 5);
		gbc_lblDomain.anchor = GridBagConstraints.EAST;
		gbc_lblDomain.gridx = 0;
		gbc_lblDomain.gridy = 0;
		add(lblDomain, gbc_lblDomain);
		
		
		
		domainComboBox =new JComboBox() {
		    public void addItem(Object anObject) {
		        int size = ((DefaultComboBoxModel) dataModel).getSize();
		        Object obj;
		        boolean added = false;
		        for (int i=0; i<size; i++) {
		            obj = dataModel.getElementAt(i);
		            int compare = anObject.toString().compareToIgnoreCase(obj.toString());
		            if (compare <= 0) { // if anObject less than or equal obj
		                super.insertItemAt(anObject, i);
		                added = true;
		                break;
		            }
		        }
		 
		        if (!added) {
		            super.addItem(anObject);
		        }
		    }
		};
		
		domainComboBox.addActionListener(this);
	
		
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		add(domainComboBox, gbc_comboBox);
		
		JLabel lblType = new JLabel("type");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.anchor = GridBagConstraints.EAST;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 1;
		add(lblType, gbc_lblType);
		
		typecomboBox = new JComboBox() {
		    public void addItem(Object anObject) {
		        int size = ((DefaultComboBoxModel) dataModel).getSize();
		        Object obj;
		        boolean added = false;
		        for (int i=0; i<size; i++) {
		            obj = dataModel.getElementAt(i);
		            int compare = anObject.toString().compareToIgnoreCase(obj.toString());
		            if (compare <= 0) { // if anObject less than or equal obj
		                super.insertItemAt(anObject, i);
		                added = true;
		                break;
		            }
		        }
		 
		        if (!added) {
		            super.addItem(anObject);
		        }
		    }
		};
		typecomboBox.addActionListener(this);
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 1;
		add(typecomboBox, gbc_comboBox_1);
		root = new DefaultMutableTreeNode();
		
		
		attributTree =new JTree(root) {
		      public boolean isPathEditable(TreePath path) {
		          Object comp = path.getLastPathComponent();
		          if (comp instanceof DefaultMutableTreeNode) {
		            DefaultMutableTreeNode node = (DefaultMutableTreeNode) comp;
		            Object userObject = node.getUserObject();
		            if (userObject instanceof AttributNodeData) {
		              return true;
		            }
		          }
		          return false;
		        }
		      
		      
		      };
		   
		attributTree.setCellRenderer(new myTreeRender());
		attributTree.setCellEditor(new myTreeEditor());
		
		attributTree.setVisible(true);
		attributTree.setRootVisible(true);
		attributTree.setEditable(true);
		GridBagConstraints gbc_tree = new GridBagConstraints();
		gbc_tree.gridwidth = 2;
		gbc_tree.insets = new Insets(0, 0, 0, 5);
		gbc_tree.fill = GridBagConstraints.BOTH;
		gbc_tree.gridx = 0;
		gbc_tree.gridy = 2;
		add(new JScrollPane(attributTree), gbc_tree);
	
		
		
		
	}

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(domainComboBox)      && e.getActionCommand().equals("comboBoxChanged")){
			//update type accordin to domain
			Object newdomain = domainComboBox.getSelectedItem();
			if(newdomain != null && !newdomain.equals(olddomain)){
				olddomain = newdomain;
				populateType(newdomain);
			}
			
			
		}
		
		if(e.getSource().equals(typecomboBox)  && e.getActionCommand().equals("comboBoxChanged") ){
			//update attribut accordin to type
			//update type accordin to domain
			Object newtype = typecomboBox.getSelectedItem();
			if(newtype != null && !newtype.equals(oldtype)){
				oldtype = newtype;
				populateAttribut(newtype);
			}
			
			
			
		}
		
		
		
	}

	private void populateAttribut(Object newtype) {	
			root.removeAllChildren();
			JmxConnection connection = new JmxConnection(host, port);
			cec.monitor.type.Counter[] count= connection.getMBeanByType((String)newtype, (String) domainComboBox.getSelectedItem());		
			for (int i = 0; i < count.length; i++) {				
				 try {
					
					  DefaultMutableTreeNode newtreNode = new DefaultMutableTreeNode(new TypeNodeData(count[i]));
					  
					  
					 					  
					 root.add(newtreNode);
					 Attribut[] at =  count[i].getAttributArray();	
					 
					 
					 Comparator<? super Attribut> c = new Comparator<Attribut>() {
						 public int compare(Attribut o1, Attribut o2) {
							 return o1.getName().compareToIgnoreCase(o2.getName());
							 
							 
						 };
					};
					List<Attribut> listatt = Arrays.asList(at);
					Collections.sort(listatt, c );
					for (Iterator iterator = listatt.iterator(); iterator
							.hasNext();) {
						Attribut attribut = (Attribut) iterator.next();
						 AttributNodeData dt = new AttributNodeData(attribut);
						 DefaultMutableTreeNode son = new DefaultMutableTreeNode(dt); 
						  
							 newtreNode.add(son);
						
						
					}
					
				
					
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();		}
			
					 
				 
			}
		attributTree.repaint();
	}

	private void populateType(Object newdomain) {
		JmxConnection connection = new JmxConnection(host, port);
		String[] ret = connection.getAllTypesForDomain((String)newdomain);	
		typecomboBox.removeAllItems();
		root.removeAllChildren();
		for (int i = 0; i < ret.length; i++) {
			typecomboBox.addItem(ret[i]);
		}
		
	}
	
	
	private static class AttributNodeData {
		private Attribut at;
		private boolean checked;
		
		public AttributNodeData( Attribut att){
			this.at=att;
			checked=false;
		}
		
		public String toString(){
			return (at.getName());		
			
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}	
		
		
	}
	
	private static class TypeNodeData {
		
		private cec.monitor.type.Counter c;
		
		public TypeNodeData ( cec.monitor.type.Counter counter ){
			c=counter;
			
		}
		
		public cec.monitor.type.Counter getC() {//return a copy of the counter
			
		
			/*MonitorConfigDocument doc = MonitorConfigDocument.Factory.newInstance();
			MonitorConfig conf = doc.addNewMonitorConfig();*/
			cec.monitor.type.Counter cpi= cec.monitor.type.Counter.Factory.newInstance();
			
			cpi.setJmxObjectName(c.getJmxObjectName());
			cpi.setJmxType(c.getJmxType());
			Attribut[] cpa = new Attribut[c.getAttributArray().length];
			System.arraycopy(c.getAttributArray(), 0, cpa, 0, c.getAttributArray().length);
			cpi.setAttributArray(cpa);
			
			return cpi;
		}

		public  String toString() {
			ObjectName objname;
			String ret="";
			try {
				objname = new ObjectName(c.getJmxObjectName());
				String typename=objname.getKeyProperty("Name");
				ret =c.getJmxType() + " " + typename;
				
			} catch (MalformedObjectNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			
		return ret;	
		}
		
	}
	
	
	
	
	private static class  myTreeEditor extends DefaultCellEditor {

		private AttributNodeData nodedata;

		public myTreeEditor() {
			super(new JCheckBox());
		
		}
		
		@Override
		public Object getCellEditorValue() {
			   JCheckBox editor = (JCheckBox) (super.getComponent());
			   if(nodedata != null)
				   nodedata.setChecked (editor.isSelected());
			    return nodedata;
		}
		
		
		@Override
		public Component getTreeCellEditorComponent(JTree tree, Object value,
				boolean isSelected, boolean expanded, boolean leaf, int row) {
		
			 JCheckBox editor = null;
			 nodedata = null;
			 if (value != null && value instanceof DefaultMutableTreeNode) {
			      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			      
			      Object userObject = node.getUserObject();
			      if (userObject instanceof AttributNodeData) {
			    	  nodedata= (AttributNodeData) userObject; 			    	  
			    	   if (nodedata != null) {
			    		      editor = (JCheckBox) (super.getComponent());
			    		      editor.setText(nodedata.toString());
			    		      editor.setSelected(nodedata.isChecked());
			    		    }  
			    	   return editor;
			    	  
			      }
			      
			      
			      
			      
			    }
			
			
			
			return super.getTreeCellEditorComponent(tree, value, isSelected, expanded,
					leaf, row);
		}
		
		
		
	}
	
	
	
	
	private static class myTreeRender extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		protected JCheckBox checkBoxRenderer = new JCheckBox();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
	    if (value instanceof DefaultMutableTreeNode) {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	        Object userObject = node.getUserObject();
	        if (userObject instanceof AttributNodeData) {
	        	AttributNodeData dt = (AttributNodeData) userObject;
	        	checkBoxRenderer.setText(dt.toString());
	        	checkBoxRenderer.setSelected(dt.isChecked());
	          return checkBoxRenderer;
	        }
	    
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
	}
	    return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

	}
	

}
	
	public cec.monitor.type.Counter[] getSelectedCounter(){
		
		Enumeration list = root.children();
		int index=0;
		cec.monitor.type.Counter[] ret = new cec.monitor.type.Counter[root.getChildCount()];
		while (list.hasMoreElements()) {
			DefaultMutableTreeNode object = (DefaultMutableTreeNode) list.nextElement();
			TypeNodeData data = (TypeNodeData)object.getUserObject();
			//remove attribut not selected
			ret[index]=removeUnselectedAttribut(data.getC(),object);			
			index++;
			
		
			
		}
		
		
		
		return ret;
		
	}

	private cec.monitor.type.Counter removeUnselectedAttribut(cec.monitor.type.Counter c,
			DefaultMutableTreeNode object) {
		
		Vector<Attribut> listtoadd = new Vector<Attribut>();
		Attribut[] att = c.getAttributArray();
		
		for (int i = 0; i < att.length; i++) {
			Enumeration fils = object.children();
			while (fils.hasMoreElements()) {
				DefaultMutableTreeNode nodefils = (DefaultMutableTreeNode) fils.nextElement();
				AttributNodeData nd = (AttributNodeData) nodefils.getUserObject();
				if( nd.at.getName().equals(att[i].getName()) && nd.isChecked() ){
					listtoadd.add(nd.at);
					
					
				}
			}
			
			
			
		}
		
		Attribut[] newlist = listtoadd.toArray(new Attribut[listtoadd.size()] );
		c.setAttributArray(newlist);
		
		
		return c;
	}
	
	
	
	
	
	
}
