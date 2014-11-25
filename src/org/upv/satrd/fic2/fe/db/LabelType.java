package org.upv.satrd.fic2.fe.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class LabelType {

	
	private Integer id;
	private String name;	
	
	
	private static org.apache.log4j.Logger log =Logger.getLogger(org.upv.satrd.fic2.fe.db.LabelType.class);
	
	
	
	//The LabelType object has been inserted in the DDB, and the id is known
	public LabelType(Integer id, String name){
		this.id = id;
		this.name = name;
	}
	
	//The LabelType object has been inserted in the DDB, and the id is known
	public LabelType(String name){
		this.id = null;
		this.name = name;
	}
	
	//GET METHODS
	public Integer getId(){ return this.id;}
	public String getName(){ return this.name;}
	
	
	//SET METHODS
	public void setID(Integer id){ this.id = id;}
	public void setName(String name) {this.name = name;}
	

	
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////DB methods/////////////////////////////////////////
	
	public static LabelType getLabelTypeClassById(Connection con, Integer id){
		LabelType labeltype = null;
		Statement stmt ;
		ResultSet rs;
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM labeltype WHERE id="+id+";";
			rs = stmt.executeQuery(sql);			
			
			list = resultSetToArrayList(rs);        		
			
			if (!list.isEmpty()){				
					
				//name field mandatory
				String name = (list.get(0)).get("name").toString();				
										 
				labeltype = new LabelType(id, name);
				
			}			
			rs.close();
	        stmt.close();  
			
		} catch ( SQLException e ) {
			System.out.println("Error LabelType.getLabelTypeClassById(): "+e.getMessage());
			log.error("Error LabelType.getLabelTypeClassById(): "+e.getMessage());
		}
		
		return labeltype;
		
	}
	
	
	
	public static LabelType getLabelTypeClassByName(Connection con, String name){
		LabelType labeltype = null;
		Statement stmt ;
		ResultSet rs;
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			String sql = "SELECT * FROM labeltype WHERE name='"+name+"';";
			rs = stmt.executeQuery(sql);
			
			
			
			list = resultSetToArrayList(rs);        		
			
			if (!list.isEmpty()){				
					
				//id field mandatory
				Integer id = new Integer((list.get(0)).get("id").toString());				
										 
				labeltype = new LabelType(id, name);
				
			}			
			rs.close();
	        stmt.close();  
			
		} catch ( SQLException e ) {
			System.out.println("Error LabelType.getLabelTypeClassByName() : "+e.getMessage());
			log.error("Error LabelType.getLabelTypeClassByName() : "+e.getMessage());
		}
		
		return labeltype;
		
	}
	
	
	public static Integer saveLabelType(Connection con, LabelType LabelType){
	
	
		Integer id=null;		
		String sql;
		PreparedStatement ps;		
		
			try{   	
				
				sql = "INSERT INTO labeltype (name) VALUES (?)";	
	
				ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);	
				ps.setString(1,LabelType.getName());							
				
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) { id = rs.getInt(1); }	
								
				rs.close();				
		        ps.close();
				
			} catch ( SQLException e) {
				System.out.println("Error LabelType.saveLabelType(): "+e.getMessage());
				log.error("Error LabelType.saveLabelType(): "+e.getMessage());
			}	
	
		return id;
	}
	
	
	
	public static boolean deleteLabelTypeById(Connection con, Integer id){			
			
		String sql;	
		Statement stmt ;
		
		try {
			
			//First we need to delete all POILable that relate to this labeltype
			if (POILabel.deletePOILabelByLabelTypeId(con, id)){ 
			
				stmt = con.createStatement();
								
				sql = "DELETE FROM labeltype WHERE id="+id+";";
				stmt.executeUpdate(sql);
				
				stmt.close();
			}else return false;
				
		} catch ( SQLException e) {
			System.out.println("Error LabelType.deleteLabelTypeById(): "+e.getMessage());
			log.error("Error LabelType.deleteLabelTypeById(): "+e.getMessage());
			return false;
		}	
	
		return true;
	}
	
	
	
	
	public static boolean deleteLabelTypeByName(Connection con, String name){			
		
		LabelType labeltype = LabelType.getLabelTypeClassByName(con, name);
		if (labeltype !=null)
			return LabelType.deleteLabelTypeById(con, labeltype.getId());
		else
		return false;		
		
	}
	
	
	
	
	
	public static boolean updateLabelType(Connection con, LabelType labeltype){
				
		String sql;
		PreparedStatement ps;		
		
			try{   	
				
				sql = "UPDATE labeltype SET name=? WHERE id="+labeltype.getId();
	
				ps = con.prepareStatement(sql);	
				ps.setString(1,labeltype.getName());	
							
				ps.executeUpdate();
				
				ps.close();								
				
				
			} catch ( SQLException e) {
				System.out.println("Error LabelType.updateLabelType(): "+e.getMessage());
				log.error("Error LabelType.updateLabelType(): "+e.getMessage());
				return false;
			}	
	
		return true;
	}
	
	
	
	
	public static ArrayList<LabelType> getLabelTypeList (Connection con){
		
		ArrayList<LabelType> labeltype_array = new ArrayList<LabelType>();
		String sql;
		Statement stmt ;
		ResultSet rs;
		
		
		ArrayList<HashMap<String, Object>> list;
		
		try {
			stmt = con.createStatement();
			sql = "SELECT * FROM labeltype ORDER BY name";
			rs = stmt.executeQuery(sql);
			
			
			
			list = resultSetToArrayList(rs);
			
			if (!list.isEmpty()){	
				
				for (int k=0;k<list.size();k++){
				
					//id field mandatory
					Integer id = new Integer((list.get(k)).get("id").toString());
					
					//name field mandatory
					String name = list.get(k).get("name").toString();
											 
					LabelType labeltype = new LabelType(id, name);
					labeltype_array.add(labeltype);
				}
				
			}			
		
			rs.close();
	        stmt.close();
			
		} catch ( Exception e ) {
			System.out.println("Error LabelType.getLabelTypeList(): "+e.getMessage());
			log.error("Error LabelType.getLabelTypeList(): "+e.getMessage());
		}
		
		return labeltype_array;
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
	
	public static void testLabelType(Connection con) {
		System.out.println("****************************************************************************************");
		System.out.println("LabelType test. Creating, inserting,selecting and deleting an LabelType object from DB");	
		log.info("****************************************************************************************");
		log.info("LabelType test. Creating, inserting,selecting and deleting an LabelType object from DB");	
		
		String name = "some_name";		
		LabelType labeltype = new LabelType(name);
		System.out.println("Creating LabelType class...... OK");
		log.info("Creating LabelType class......OK");
		
		
		Integer id = LabelType.saveLabelType(con, labeltype);
		if (id==null){
			System.out.println("Saving LabelType class...... Error ");
			log.info("Saving LabelType class...... Error ");
		}else{
			System.out.println("Saving LabelType class...... OK ");
			log.info("Saving LabelType class...... OK ");
			
			LabelType labeltype2 = LabelType.getLabelTypeClassById(con, id);
			if (labeltype2==null){
				System.out.println("Getting LabelType class by ID...... Error ");
				log.info("Getting LabelType class by ID...... Error ");
			}else{
				System.out.println("Getting LabelType class by ID...... OK ");
				log.info("Getting LabelType class by ID...... OK ");
				
				labeltype2 = LabelType.getLabelTypeClassByName(con, name);
				if (labeltype2 == null){
					System.out.println("Getting LabelType class by Name...... Error ");
					log.info("Getting LabelType class by Name...... Error ");
				}else{
					System.out.println("Getting LabelType class by Name...... OK ");
					log.info("Getting LabelType class by Name...... OK ");
					
					if (!LabelType.deleteLabelTypeById(con, id)){
						System.out.println("Deleting LabelType class by ID ...... Error ");
						log.info("Deleting LabelType class by ID ...... Error  ");
					}else{
						System.out.println("Deleting LabelType class by ID ...... OK ");
						log.info("Deleting LabelType class by ID ...... OK  ");
						
						id = LabelType.saveLabelType(con, labeltype);
						if (id!=null){
							if (!LabelType.deleteLabelTypeByName(con, name)){
								System.out.println("Deleting LabelType class by Name ...... Error ");
								log.info("Deleting LabelType class by Name ...... Error  ");
							}else{
								System.out.println("Deleting LabelType class by Name ...... OK ");
								log.info("Deleting LabelType class by Name ...... OK  ");
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