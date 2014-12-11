package org.upv.satrd.fic2.fe.config;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

	private String dbConnString;
	private String dbDriverName;
	private String dbResetScript;
	private String dbScriptSeparator = ";";
	
	private String dbHost;
	private String dbPort;
	private String dbName;

	private String dbUser;		
	private String dbPwd;
	
	private String installDir;
	private String mappingDir;
	private String fusionDir;
	private String apiRulesDir;
	
	private String geometryFromLonLatSrid;
	private String lonFromPoint;
	private String latFromPoint;
	
	private static org.apache.log4j.Logger log;
	
	public Configuration(String path) {    
	    Element fstElmnt, fstElmnt2 ; 
	    NodeList fstNmElmntLst, fstNmElmntLst2; 	   
	    
	    log = Logger.getLogger(org.upv.satrd.fic2.fe.config.Configuration.class);
		
		//Read the XML configuration file
		try{
			File file = new File(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("configuration");
		
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
							    
			    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			    	fstElmnt = (Element) fstNode;
			    	 
			    	NodeList nodeLst2 = fstElmnt.getElementsByTagName("DB"); 
				    for (int j = 0; j < nodeLst2.getLength(); j++) {
				    	Node fstNode2 = nodeLst2.item(j);				    
				    	if (fstNode2.getNodeType() == Node.ELEMENT_NODE) {
				    		fstElmnt2 = (Element) fstNode2;	      
				    		
			    			
			    			fstNmElmntLst2 = fstElmnt2.getElementsByTagName("connectionString");
			    			dbConnString = (fstNmElmntLst2.item(0)).getTextContent();

			    			fstNmElmntLst2 = fstElmnt2.getElementsByTagName("driverName");
			    			dbDriverName = (fstNmElmntLst2.item(0)).getTextContent();

			    			
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("host");
						    dbHost = (fstNmElmntLst2.item(0)).getTextContent();
			    			
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("port");
						    dbPort = (fstNmElmntLst2.item(0)).getTextContent();

		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("user");
						    dbUser = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("pwd");
						    dbPwd = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("name");
						    dbName = (fstNmElmntLst2.item(0)).getTextContent();

		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("resetScript");
		    				dbResetScript = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("scriptSeparator");
		    				dbScriptSeparator = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("geometryFromLonLatSrid");
		    				geometryFromLonLatSrid = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("lonFromPoint");
		    				lonFromPoint = (fstNmElmntLst2.item(0)).getTextContent();
		    				
		    				fstNmElmntLst2 = fstElmnt2.getElementsByTagName("latFromPoint");
		    				latFromPoint = (fstNmElmntLst2.item(0)).getTextContent();

				    	} //if
				    	  
				    }//for   				    	
			    	
					
					fstNmElmntLst = fstElmnt.getElementsByTagName("installDir");
					installDir = (fstNmElmntLst.item(0)).getTextContent();
					
					fstNmElmntLst = fstElmnt.getElementsByTagName("mappingDir");
					mappingDir = (fstNmElmntLst.item(0)).getTextContent();
					
					fstNmElmntLst = fstElmnt.getElementsByTagName("fusionDir");
					fusionDir = (fstNmElmntLst.item(0)).getTextContent();
					
					fstNmElmntLst = fstElmnt.getElementsByTagName("apiRulesDir");
					apiRulesDir = (fstNmElmntLst.item(0)).getTextContent();
					
			    } //if
			    
			}	//for	
			
		}catch (Exception  e){
			log.error(e.getMessage());
			e.printStackTrace();			
		}
	}	//constructor	
	
	
	public String getConnectionString() {return  dbConnString;}		
	public String getDriverName() {return  dbDriverName;}
	public String getResetScript() {return  dbResetScript;}
	public String getScriptSeparator() {return  dbScriptSeparator;}
	
	public String getGeometryFromLonLatSrid() {return  geometryFromLonLatSrid;}
	public String getLonFromPoint() {return  lonFromPoint;}
	public String getLatFromPoint() {return  latFromPoint;}

	public String getUser() {return dbUser;}		
	public String getPwd() {return dbPwd;}
	public String getName() {return dbName;}
	public String getHost() {return dbHost;}
	public String getPort() {return dbPort;}
	public String getInstallDir() {return installDir;}
	public String getMappingDir() {return mappingDir;}
	public String getFusionDir() {return fusionDir;}
	public String getApiRulesDir() {return apiRulesDir;}
	
	public String toString () {
		String response = null;
		
		response = "configuration\n";
		response = response + "\tDB\n";
		response = response + "\t\tconnectionString: "+dbConnString+"\n";
		response = response + "\t\tdriverName: "+dbDriverName+"\n";
		response = response + "\t\tresetScript: "+dbResetScript+"\n";
		response = response + "\t\tscriptSeparator: "+dbScriptSeparator+"\n";
		response = response + "\t\thost: "+dbHost+"\n";
		response = response + "\t\tname: "+dbName+"\n";
		response = response + "\t\tport: "+dbPort+"\n";
		response = response + "\t\tuser: "+dbUser+"\n";
		response = response + "\t\tpwd: "+dbPwd+"\n";
		response = response + "\tinstallDir: "+installDir+"\n";
		response = response + "\tmappingDir: "+mappingDir+"\n";
		response = response + "\tfusionDir: "+fusionDir+"\n";
		response = response + "\tapiRulesDir: "+apiRulesDir+"\n";

		return response;
	}


	
	
	
}	//class
