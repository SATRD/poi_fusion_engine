package org.upv.satrd.fic2.fe.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;


public class OCD {
	
	private Integer id;
	private String name;
	private Integer city_id;
	private String description;
	private String fusionRules;
	private String accesskey;
	private Date fusionDate;	
	
	
	
	private static org.apache.log4j.Logger log =Logger.getLogger(org.upv.satrd.fic2.fe.db.OCD.class);
	
	//The OCD object has been inserted in the DDB, and the id is known
	public OCD(Integer id, String name, Integer city_id, String description, String fusionRules, String accesskey, Date fusionDate ) {    
		this.id = id;
		this.name = name;
		this.city_id = city_id;
		this.description = description;
		this.fusionRules = fusionRules;
		this. accesskey = accesskey;
		this.fusionDate = fusionDate;		
	}
	
	//The OCD object has not been inserted in the DDB, and the id is not known
	public OCD( String name, Integer city_id, String description, String fusionRules, String accesskey, Date fusionDate ) {    
		this.id = null;
		this.name = name;
		this.city_id = city_id;
		this.description = description;
		this.fusionRules = fusionRules;
		this. accesskey = accesskey;
		this.fusionDate = fusionDate;		
	}
	
	
	
	//GET METHODS
	public Integer getId() {return  id;}		
	public String getName() {return name;}
	public Integer getCityId() {return city_id;}		
	public String getDescription() {return description;}
	public String getFusionRules() {return fusionRules;}
	public String getAccessKey() {return accesskey;}
	public Date getFusionDate() {return fusionDate;}
	
	
	//SET METHODS
	public void setId(Integer id){this.id = id;}
	public void setName(String name){this.name =name;}
	public void setCity(Integer city_id){this.city_id = city_id;}
	public void setDescription(String description){this.description=description;}
	public void setFusionRules(String fusionRules){this.fusionRules=fusionRules;}
	public void setAccessKey(String accessKey){this.accesskey = accessKey;}
	public void setFusionDate(Date fusionDate){this.fusionDate = fusionDate;}
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////DB methods/////////////////////////////////////////
	
	public static OCD getOCDClassById(Connection con, Integer id){
		OCD ocd = null;
		Statement stmt;
		ResultSet rs;
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM ocd WHERE id="+id+";";
			rs = stmt.executeQuery(sql);
			Object aux = null;
			
			
			list = resultSetToArrayList(rs);        		
			
			if (!list.isEmpty()){				
					
				//name field mandatory
				String name = (list.get(0)).get("name").toString();
				
				//city_id field mandatory
				Integer city_id = new Integer ((list.get(0)).get("city").toString());	
				
				//description field optional
				String description = null;
				aux = (list.get(0)).get("description");				
				if (aux!=null) description = aux.toString();
				
				//fusionRules field mandatory
				String fusionRules = (list.get(0)).get("fusionrules").toString();
								
				//accesskey field mandatory
				String accesskey = (list.get(0)).get("accesskey").toString();
				
				//date field might null
				Date fusiondate = null;
				aux = (list.get(0)).get("fusiondate");
				if (aux!=null){
					String date = aux.toString(); 					
					//convert to Date
					fusiondate = Date.valueOf(date);
				}						
										 
				ocd = new OCD(id, name,city_id,description,fusionRules,accesskey,fusiondate);
				
			}			
			rs.close();
	        stmt.close();  
			
		} catch ( SQLException e ) {
			System.out.println("Error OCD.getOCDClassById(): "+e.getMessage());
			log.error("Error OCD.getOCDClassById(): "+e.getMessage());
		}
		
		return ocd;
		
	}
	
	
	
	public static OCD getOCDClassByName(Connection con, String name){
		OCD ocd = null;
		Statement stmt;
		ResultSet rs;
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM ocd WHERE name='"+name+"';";
			rs = stmt.executeQuery(sql);
			Object aux = null;
			
			
			list = resultSetToArrayList(rs);        		
			
			if (!list.isEmpty()){				
					
				//id field mandatory
				Integer id = new Integer((list.get(0)).get("id").toString());
				
				//city_id field mandatory
				Integer city_id = new Integer ((list.get(0)).get("city").toString());	
				
				//description field optional
				String description = null;
				aux = (list.get(0)).get("description");				
				if (aux!=null) description = aux.toString();
				
				//fusionRules field mandatory
				String fusionRules = (list.get(0)).get("fusionrules").toString();
								
				//accesskey field mandatory
				String accesskey = (list.get(0)).get("accesskey").toString();
				
				//date field might null
				Date fusiondate = null;
				aux = (list.get(0)).get("fusiondate");
				if (aux!=null){
					String date = aux.toString(); 					
					//convert to Date
					fusiondate = Date.valueOf(date);
				}				
										 
				ocd = new OCD(id, name,city_id,description,fusionRules,accesskey,fusiondate);
				
			}			
			rs.close();
	        stmt.close();  
			
		} catch ( SQLException e ) {
			System.out.println("Error OCD.getOCDClassByName(): "+e.getMessage());
			log.error("Error OCD.getOCDClassByName(): "+e.getMessage());
		}		
		return ocd;
		
	}
	
	
	public static Integer saveOCD(Connection con, OCD ocd){
	
	
		Integer id=null;		
		String sql;
		PreparedStatement ps;		
		
			try{   	
				
				sql = "INSERT INTO ocd (name,city,description,fusionrules,accesskey,fusiondate) VALUES (?,?,?,?,?,?)";	
	
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
				ps.setString(1,ocd.getName());	
				ps.setInt(2,ocd.getCityId());
				if (ocd.getDescription() == null) ps.setNull(3, java.sql.Types.VARCHAR); else ps.setString(3,ocd.getDescription());
				ps.setString(4,ocd.getFusionRules());
				ps.setString(5,ocd.getAccessKey());
				if (ocd.getFusionDate() == null) ps.setNull(6, java.sql.Types.DATE); else ps.setDate(6,ocd.getFusionDate());	
								
				
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) { id = rs.getInt(1); }	
								
				rs.close();				
		        ps.close();
				
			} catch ( SQLException e) {
				System.out.println("Error OCD.saveOCD(): "+e.getMessage());
				log.error("Error OCD.saveOCD(): "+e.getMessage());
			}	
	
		return id;
	}
	
	
	
	public static boolean deleteOCDById(Connection con, Integer id){			
			
		String sql;		
		Statement stmt;
		
		
		try {
			stmt = con.createStatement();
			
			//Delete all dependencies with other tables (ocdcategory,ocdsource)
			sql = "DELETE FROM ocdcategory WHERE ocdid="+id+";";
			stmt.executeUpdate(sql);
			
			
			sql = "DELETE FROM ocdsource WHERE ocdid="+id+";";
			stmt.executeUpdate(sql);
			
			//Finally delete the city
			sql = "DELETE FROM ocd WHERE id="+id+";";
			stmt.executeUpdate(sql);
			
			stmt.close();
				
		} catch ( SQLException e) {
			System.out.println("Error OCD.deleteOCDById(): "+e.getMessage());
			log.error("Error OCD.deleteOCDById(): "+e.getMessage());
			return false;
		}	
	
		return true;
	}
	
	
	
	
	public static boolean deleteOCDByName(Connection con, String name){			
		
		OCD ocd = OCD.getOCDClassByName(con, name);
		if (ocd !=null)
			return OCD.deleteOCDById(con, ocd.getId());
		else
		return false;		
		
	}
	
	
	
	
	
	public static boolean updateOCD(Connection con, OCD ocd){
				
		String sql;
		PreparedStatement ps;		
		
			try{   	
				
				sql = "UPDATE ocd SET name=?, city=?, description=?, fusionRules=?, accesskey=?, fusiondate=? WHERE id="+ocd.getId();
	
				ps = con.prepareStatement(sql);	
				ps.setString(1,ocd.getName());	
				ps.setInt(2,ocd.getCityId());
				if (ocd.getDescription() == null) ps.setNull(3, java.sql.Types.VARCHAR); else ps.setString(3,ocd.getDescription());
				ps.setString(4,ocd.getFusionRules());
				ps.setString(5,ocd.getAccessKey());
				if (ocd.getFusionDate() == null) ps.setNull(6, java.sql.Types.DATE); else ps.setDate(6,ocd.getFusionDate());											
				
				ps.executeUpdate();
				
				ps.close();								
				
				
			} catch ( SQLException e) {
				System.out.println("Error OCD.updateOCD(): "+e.getMessage());
				log.error("Error OCD.updateOCD(): "+e.getMessage());
				return false;
			}	
	
		return true;
	}
	
	
	
	
	public static ArrayList<OCD> getOCDList (Connection con){
		
		ArrayList<OCD> ocd_array = new ArrayList<OCD>();
		String sql;
		Statement stmt;
		ResultSet rs;		
		
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			sql = "SELECT * FROM ocd ORDER BY name";
			rs = stmt.executeQuery(sql);
			Object aux = null;
			
			
			list = resultSetToArrayList(rs);
			
			if (!list.isEmpty()){	
				
				for (int k=0;k<list.size();k++){
				
					//id field mandatory
					Integer id = new Integer((list.get(k)).get("id").toString());
					
					//name field mandatory
					String name = list.get(k).get("name").toString();
					
					//city_id field mandatory
					Integer city_id = new Integer ((list.get(k)).get("city").toString());	
					
					//description field optional
					String description = null;
					aux = (list.get(k)).get("description");				
					if (aux!=null) description = aux.toString();
					
					//fusionRules field mandatory
					String fusionRules = (list.get(k)).get("fusionRules").toString();
									
					//accesskey field mandatory
					String accesskey = (list.get(k)).get("accesskey").toString();
					
					//date field might null
					Date fusiondate = null;
					aux = (list.get(k)).get("fusiondate");
					if (aux!=null){
						String date = aux.toString(); 					
						//convert to Date
						fusiondate = Date.valueOf(date);
					}				
											 
					OCD ocd = new OCD(id, name,city_id,description,fusionRules,accesskey,fusiondate);
					ocd_array.add(ocd);
				}
				
			}			
		
			rs.close();
	        stmt.close();
			
		} catch ( Exception e ) {
			System.out.println("Error City.getCityList(): "+e.getMessage());
			log.error("Error City.getCityList(): "+e.getMessage());
		}
		
		return ocd_array;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////Utility method///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	/////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////testing method///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public static void testOCD(Connection con) throws ParseException {
		System.out.println("****************************************************************************************");
		System.out.println("OCD test. Creating, inserting,selecting and deleting an OCD object from DB");	
		log.info("****************************************************************************************");
		log.info("OCD test. Creating, inserting,selecting and deleting an OCD object from DB");	
		
		String name = "some_name";
		
		//Before inserting a city_id, it must exist
		Integer city_id = City.saveCity(con, new City("some_name","some_bbox"));
		
		
		String description = "some_description";
		String fusionRules = "some_fusionRules";
		String accesskey = "some accesskey";
		
		//Set the current time
		Calendar cal = Calendar.getInstance();				
		SimpleDateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");				
		String date1 = outputformat.format(cal.getTime());		
		java.util.Date date2 = outputformat.parse(date1);						
		java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
		
		
		OCD ocd = new OCD(name,city_id,description,fusionRules,accesskey,sqlDate);
		System.out.println("Creating City class...... OK");
		log.info("Creating City class......OK");
		
		
		Integer id = OCD.saveOCD(con, ocd);
		
		if (id==null){
			System.out.println("Saving OCD class...... Error ");
			log.info("Saving OCD class...... Error ");
		}else{
			System.out.println("Saving OCD class...... OK ");
			log.info("Saving OCD class...... OK ");
			
			
			OCD ocd2 = OCD.getOCDClassById(con, id);
			
			if (ocd2==null){
				System.out.println("Getting OCD class by ID...... Error ");
				log.info("Getting OCD class by ID...... Error ");
			}else{
				System.out.println("Getting OCD class by ID...... OK ");
				log.info("Getting OCD class by ID...... OK ");
				
				ocd2 = OCD.getOCDClassByName(con, name);
				if (ocd2 == null){
					System.out.println("Getting OCD class by Name...... Error ");
					log.info("Getting OCD class by Name...... Error ");
				}else{
					System.out.println("Getting OCD class by Name...... OK ");
					log.info("Getting OCD class by Name...... OK ");
					
					if (!OCD.deleteOCDById(con, id)){
						System.out.println("Deleting OCD class by ID ...... Error ");
						log.info("Deleting OCD class by ID ...... Error  ");
					}else{
						System.out.println("Deleting OCD class by ID ...... OK ");
						log.info("Deleting OCD class by ID ...... OK  ");
						
						id = OCD.saveOCD(con, ocd);
						if (id!=null){
							if (!OCD.deleteOCDByName(con, name)){
								System.out.println("Deleting OCD class by Name ...... Error ");
								log.info("Deleting OCD class by Name ...... Error  ");
							}else{
								System.out.println("Deleting OCD class by Name ...... OK ");
								log.info("Deleting OCD class by Name ...... OK  ");
								System.out.println("All tests passed OK");
								log.info("All tests passed OK");
							}
						}
					}
					
				}
				
			}
		}
		
		System.out.println("****************************************************************************************");
		log.info("****************************************************************************************");
		
		
	}
	
	
	
	
}	
