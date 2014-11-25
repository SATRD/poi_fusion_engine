package org.upv.satrd.fic2.fe.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.upv.satrd.fic2.fe.db.APIType;
import org.upv.satrd.fic2.fe.db.Category;
import org.upv.satrd.fic2.fe.db.City;
import org.upv.satrd.fic2.fe.db.License;
import org.upv.satrd.fic2.fe.db.OCD;
import org.upv.satrd.fic2.fe.db.Source;
import org.upv.satrd.fic2.fe.fusionrules.FusionRule;
import org.upv.satrd.fic2.fe.fusionrules.FusionRules;

public class PostgreSQL {
	
	private static org.apache.log4j.Logger log;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	
	
	public static Connection conectDB (String host, String port, String user, String pwd, String name){
		log = Logger.getLogger(org.upv.satrd.fic2.fe.main.PostgreSQL.class);
	
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+name,user, pwd);

			con.setAutoCommit(true);
		} catch ( Exception e ) {
			//System.out.println("Error: "+e.getMessage());
			log.error(e.getMessage());
		}
	
		return con;
	}
	
	public static void disconectDB (Connection con){
		try {
			con.close();
		} catch ( Exception e ) {
			log.error(e.getMessage());
		}
	}
	
	public static ArrayList<HashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

	    while (rs.next()) {
	        HashMap<String, Object> row = new HashMap<String, Object>();
	        
	        for(int i=1; i<=columns; i++){
	          row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        
	        results.add(row);
	    }
	    
	    return results;
	}

	
	
	
	
		
		//Executes a SQL script given the path to that file
		public static int execScript (Connection con, String dbScriptFile){	
			
			
			int ret = -1;			 
			
			try {
				  stmt = con.createStatement();	    	      
    	      
	    	      BufferedReader in = new BufferedReader(new FileReader(dbScriptFile));
	    	      String str;
	    	      StringBuffer sb = new StringBuffer();
	    	      while ((str = in.readLine()) != null) {
	    	    	  sb.append(str + "\n ");
	    	      }
	    	      in.close();
	    	      stmt.executeUpdate(sb.toString());
    	          stmt.close();
    	          ret =1;
				
			} catch ( Exception e ) {
				System.out.println("Error: "+e.getMessage());
				log.error(e.getMessage());
				//e.printStackTrace();
				return -1;
			}
			
			return ret;		
		}
		
		//Executes a SQL command given as a String
		public static int execSQL (Connection con, String sqlCommand){	
			
			
			int ret = -1;			 
			
			try {
				  stmt = con.createStatement();		    	      
	    	      stmt.executeUpdate(sqlCommand);
    	          stmt.close();
    	          ret =1;
				
			} catch ( Exception e ) {
				System.out.println("Error: "+e.getMessage());
				log.error(e.getMessage());
				return -1;
			}
			
			return ret;		
		}
		
		
		
		//FIXME. Now this method makes nothing. It should take data from each table of 'orig' and copy it in the corresponding table in 'dest'
		//Copies BASE DP part from one database to another
		public static int initDBCorePart(Connection con_orig, Connection con_dest){
			
			String sql;
			
			try {
				stmt = con_orig.createStatement();		
				
							
				sql="SELECT * FROM source";
				rs = stmt.executeQuery(sql);
				
				while (rs.next()){  }
				
				
				stmt.close();
				return 1;
				
			} catch ( Exception e ) {
				System.out.println("Error: "+e.getMessage());
				log.error(e.getMessage());
				return -1;
			}
			
					
		}
		
		
		//This method takes a FusionRules class and initializes the core part with the information: city, bbox, categories, ocd,sources		
		public static int initDBFromXML(Connection con, FusionRules fusionRules){
			
			int ret=0;
						
			//insert the city
			City city = new City(fusionRules.getCity(),fusionRules.getBbox());
			Integer cityId = City.saveCity(con, city);			
			if (cityId ==null) return -1;
			
			
			//Insert the OCD (only one). The 'FusionRules' filename does not matter for now
			OCD ocd = new OCD(fusionRules.getCity(),cityId,null,"fusionrules.xml","ocd_tenerife",null);
			Integer ocdId = OCD.saveOCD(con, ocd);			
			if (ocdId ==null) return -1;
			
			//Go though the FusionRules
			ArrayList<FusionRule> frules = fusionRules.GetFusionrules(); 
			for (int i=0;i<frules.size(); i++){
				FusionRule frule = frules.get(i);			
				
				//Insert the category. It is supposed that they are not duplicated in the XML. The icon file does not matter for now
				Category category = new Category(frule.getCategory(),null,1,"category_icon.png");
				Integer categoryId = Category.saveCategory(con, category);
				
				if (categoryId == null) return -1;			
				
				//TODO We may introduce here the sources, but it is much simpler to insert all of them outside this for-statement, without requiring to check
				//whether the source has already been inserted
			}
			
			//insert the APITypes. For the moment, only citySDK
			APIType apitype = new APIType("citySDK",null,null);
			Integer apitypeId = APIType.saveAPIType(con, apitype);			
			if (apitypeId ==null) return -1;
			
			//insert the sources. OSM
			Source source = new Source("osm",null,"http://app.prodevelop.es/ficontent/api/osm/","conf/osm.properties",apitypeId);
			Integer sourceId = Source.saveSource(con, source);			
			if (sourceId == null) return -1;
			
			//insert the sources. DBPedia
			source = new Source("dbpedia",null,"http://app.prodevelop.es/ficontent/api/dbpedia/","conf/dbpedia.properties",apitypeId);
			sourceId = Source.saveSource(con, source);	
			if (sourceId == null) return -1;
			
			//insert the sources. POIProxy
			source = new Source("poiproxy",null,"http://app.prodevelop.es/ficontent/api/poiproxy/","conf/poiproxy.properties",apitypeId);
			sourceId = Source.saveSource(con, source);	
			if (sourceId == null) return -1;			
			
			
			//Tables sourcecity, ocdsource,ocdcategory do not make sense for standalone ocds
			
			//Insert table license 
			License license =new License("ODBl",null,null);
			Integer licenseId = License.saveLicense(con, license);			
			if (licenseId ==null) return -1;
			
			license =new License("Commercial_issues",null,null);
			licenseId = License.saveLicense(con, license);			
			if (licenseId ==null) return -1;
			
			license =new License("CCAS",null,null);
			licenseId = License.saveLicense(con, license);			
			if (licenseId ==null) return -1;
			
			license =new License("GNU_Free_Doc",null,null);
			licenseId = License.saveLicense(con, license);			
			if (licenseId ==null) return -1;		
			
			
			return ret;
					
		}
		
		
		
		
	
	
}
