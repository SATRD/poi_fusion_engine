package org.upv.satrd.fic2.fe.test;


import java.sql.Connection;

import org.apache.log4j.Logger;
import org.upv.satrd.fic2.fe.config.Configuration;
import org.upv.satrd.fic2.fe.db.APIType;
import org.upv.satrd.fic2.fe.db.Category;
import org.upv.satrd.fic2.fe.db.City;
import org.upv.satrd.fic2.fe.db.Component;
import org.upv.satrd.fic2.fe.db.LabelType;
import org.upv.satrd.fic2.fe.db.License;
import org.upv.satrd.fic2.fe.db.OCD;
import org.upv.satrd.fic2.fe.db.POI;
import org.upv.satrd.fic2.fe.db.POICategory;
import org.upv.satrd.fic2.fe.db.POILabel;
import org.upv.satrd.fic2.fe.db.POISource;
import org.upv.satrd.fic2.fe.db.Source;
import org.upv.satrd.fic2.fe.main.PostgreSQL;


public class TestDBClasses {

	private static org.apache.log4j.Logger log;

	
	
	private static String configurationFile = "conf/config.xml";
	
	//@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		log = Logger.getLogger(org.upv.satrd.fic2.fe.test.TestDBClasses.class);	
		
		
		//Load configuration parameters of the OCD in order to access the PostGRESQL database
		Configuration config = new Configuration(configurationFile);
		Connection con = PostgreSQL.conectDB(config.getHost(), config.getPort(), config.getUser(), config.getPwd(), config.getName());	
		
		
		//Reset and Init database. NOTE!!! It is supposed that psql is installed and in the path, otherwise we cannot execute any script
		log.info("Reseting database "+config.getName());
		System.out.println("Reseting database "+config.getName());
		String sqlscript = "db/fe_ocd_reset.sql";
		
	     try {
	         Runtime rt = Runtime.getRuntime();
	         String executeSqlCommand = "psql"
	         		+ " -U " + config.getUser()
	         		+ " -p " + config.getPort()
	         		+ " -h " + config.getHost()
	         		+ " -f " + sqlscript
	         		+ " " + config.getName();
	         Process pr = rt.exec(executeSqlCommand);
	         pr.waitFor();	         
	      } catch (Exception e) {	        
	        log.error(e.getMessage()+".Exiting...");
	        System.exit(-1);
	        
	      }		
	     System.out.println("Database has been reset");
	     log.info("Database has been reset");
		
	     
	     
		 // Init test
	     try{
	    	 
	    	 APIType.testAPIType(con);
	    	 Category.tesCategory(con);
	    	 City.testCity(con);
	    	 License.testLicense(con);
	    	 OCD.testOCD(con);
	    	 POI.testPOI(con);
	    	 POILabel.testPOILabel(con);
	    	 POISource.testPOISource(con);   	 	    	 
	    	 Source.testSource(con);
	    	 Component.testComponent(con);
	    	 LabelType.testLabelType(con);
	    	 POICategory.testPOICategory(con);
	    	 
	    	 
	    	 
	     }catch (Exception ex){
	    	 ex.printStackTrace();
	     }
	     
		
	    		
	    
	    
	}
	
}
