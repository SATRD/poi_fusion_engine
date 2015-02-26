package org.upv.satrd.fic2.fe.connectors.citySDK;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.upv.satrd.fic2.fe.db.LabelType;
import org.upv.satrd.fic2.fe.db.POILabel;
import org.upv.satrd.fic2.fe.db.Source;

public class Collector {

	private Source source;
	private static org.apache.log4j.Logger log;
	
	
	public Collector(Source source){
		this.source = source;
		log = Logger.getLogger(org.upv.satrd.fic2.fe.connectors.citySDK.Collector.class);	
	}
	
	
	public JSONArray getPOIArrayByCategory(Connection con, String category, String city,String bbox,Integer limit){
		
		
		JSONArray returnedPois = new JSONArray();
						
		try {
						
			// get the corresponding category in the source
			String projectedCategory = getProjectedCategory(con,category,city);			
			
			
			if ((projectedCategory == null) || (projectedCategory.isEmpty()) ){
				log.error("No matching category in source "+source.getName()+" for category "+category);
				projectedCategory=" ";
			}else{
				
				//Check if there is one or more categories mapping (e.g, category shop maps in OSM several categories:mall, book_shop, supermarket,etc.)
				String[] projectedCategoryArray = projectedCategory.split(",");
				int cat_length = projectedCategoryArray.length;				
				
				JSONArray selectedPois = new JSONArray();
				
				if (cat_length > 1){
					
					JSONObject listPois = new JSONObject();					
					
					//There are more than one category to map
					log. debug("Category '"+category+"' maps to "+cat_length+" different categories. Building an aggregated JSON file");
					
					for (int h=0;h<cat_length;h++){
						
						String current_projectedCategory =  projectedCategoryArray[h];
						
						log.debug("ProjectedCategory "+h+": "+current_projectedCategory);
						
						JSONObject listPois_aux = CommonUtils.getPOIListBySourceAndCategory(source.getUrlaccess(),
								source.getName(),current_projectedCategory,bbox,limit.toString(), city);
						
						if (h==0){
							//Take the first result and store it
							listPois = listPois_aux;
							selectedPois = listPois.getJSONArray("poi");
						}else{
							//Append/Merge the new found POIs in listPois
							JSONArray array_aux = listPois_aux.getJSONArray("poi");							
							for (int g=0;g<array_aux.length();g++){
								Object obj = array_aux.get(g);
								selectedPois.put(obj);  
							}								
						}								
					}	
					
				}else{				
				
					log. debug("Corresponding category for '"+category+"' in source "+source.getName()+" : "+projectedCategory);					
					
					JSONObject listPois = CommonUtils.getPOIListBySourceAndCategory(source.getUrlaccess(),
							source.getName(),projectedCategory,bbox,limit.toString(),city);
	        						
					//Get the array of the retrieved object
					selectedPois = listPois.getJSONArray("poi");	
				}				
				
				//TODO :this information may be stored for statistics after the fusion has taken place
				log.debug("length "+source.getName()+" : "+selectedPois.length());
				
				returnedPois = cleanPOIJSONArray(con,selectedPois,projectedCategory);							
			}								
	 
		} catch (Exception ex) {
			System.out.println("Error Collector.getPOIArrayByCategory() :"+ex.getMessage());
			log.error("Error Collector.getPOIArrayByCategory() :"+ex.getMessage());
		}
		
		return returnedPois;
	}
	
	
	
	
	public ArrayList<JSONObject> getZonePOIArrayByCriteria(Connection con, String category, String city, String lat, String lon, String maxDistance, String name, String similarityPercentage){
						
		ArrayList<JSONObject> returnedPois = new ArrayList<JSONObject>();
						
		//We need to map the category in FE (e.g museum) to the category in the source looking at the categorymapping file		
		try {
			
			String projectedCategory = getProjectedCategory(con,category,city);			
			
			if ((projectedCategory == null) || (projectedCategory.isEmpty()) ){
				log.error("No matching category in source "+source.getName()+" for category "+category);
				
			}else{				
				//Check if there is one or more categories mapping (e.g, category shop maps to mall, book_shop, supermarket,etc. in OSM)
				String[] projectedCategoryArray = projectedCategory.split(",");
				int cat_length = projectedCategoryArray.length;								
				
				
				ArrayList<JSONObject> selectedPois = new ArrayList<JSONObject>();
				
				if (cat_length > 1){
					
					//There are more than one category to map
					log. debug("Category '"+category+"' maps to "+cat_length+" different categories. Building an aggregated JSON file");
					
					for (int h=0;h<cat_length;h++){
						String current_projectedCategory =  projectedCategoryArray[h];
						log.debug("ProjectedCategory "+h+": "+current_projectedCategory);
						
						ArrayList<JSONObject> selectedPois_aux = CommonUtils.getZonePOIs(source.getUrlaccess(),source.getName(), current_projectedCategory, lat, lon, 
		        				maxDistance, name, similarityPercentage,city);
						if (h==0){
							selectedPois = selectedPois_aux;
							
						}else{
							//Append/Merge the new found POIs in selectedPois
							for (int g=0;g<selectedPois_aux.size();g++){
								JSONObject obj = selectedPois_aux.get(g);
								selectedPois.add(obj);
								//TODO :this information may be stored for statistics after the fusion has taken place
							}
						}
					}
					
				
				}else{					
					//There is only one category to search for
					log. debug("Corresponding category for '"+category+"' in source "+source.getName()+" : "+projectedCategory);					
				
					selectedPois = CommonUtils.getZonePOIs(source.getUrlaccess(),source.getName(), projectedCategory, lat, lon, 
        				maxDistance, name, similarityPercentage,city);
				}       		
        						
				//TODO :this information may be stored for statistics after the fusion has taken place
				log.debug("Length selectedPois fulfilling the criteria  "+source.getName()+" : "+selectedPois.size());
						
				
				
				//We should delete those POIs that have been already inserted in the database
				returnedPois = cleanPOIArrayList(con,selectedPois,projectedCategory);									
			}					
	 
		} catch (Exception ex) {
			System.out.println("Error Collector.getZonePOIArrayByCriteria() :"+ex.getMessage());
			log.error("Error Collector.getZonePOIArrayByCriteria() :"+ex.getMessage());
		}
		
		return returnedPois;
	}
	
	

    
	public JSONArray getImgPOIArrayByCategory(Connection con, String category, String city,String point_radius, Integer limit){	
		
		JSONArray returnedPois = new JSONArray();			
		try {		
				
				JSONObject listPois = CommonUtils.getImgPOIList(source.getUrlaccess(),category,point_radius,limit.toString(),city);				
	    						
				//Get the array of the retrieved object
				returnedPois = listPois.getJSONArray("poi");	
				
				
				//We don't need to check for POIs already inserted	
	 
		} catch (Exception ex) {
			System.out.println("Error Collector.getImgPOIArrayByCategory() :"+ex.getMessage());
			log.error("Error Collector.getImgPOIArrayByCategory() :"+ex.getMessage());
		}
		
		return returnedPois;
		
	}
	
	
	
	
	//This method creates a POILabel object from the info stored in a JSON file (JSONArray) in a location given by 'index'
	//The POI has already been inserted in the DDBB and therefore the poi_id is known 
	public POILabel getImgPOILabel(Connection con, Integer poi_id,JSONArray img_array, int index){
		
		POILabel label = new POILabel();		
		
		try{
			JSONObject img_poi = img_array.getJSONObject(index);
			
			
			//set POIid
			label.setPOIId(poi_id);
						
			//set TypeId 'image'. If it does not exist, create this LabelType
			LabelType image_type = LabelType.getLabelTypeClassByName(con, "image");	
			
			if (image_type == null){					
				Integer typeid = LabelType.saveLabelType(con, new LabelType("image"));
				label.setLabeltypeId(typeid);
				
			}else{
				label.setLabeltypeId(image_type.getId());
				
			}			
			
			//Set value. We check for term 'image' and its corresponding 'value'			
			JSONArray object = new JSONArray();
			object = img_poi.getJSONArray("link");
			
			if (object.length() >0 ){	
				for(int i=0; i<object.length(); i++){
					
					JSONObject aux =  object.getJSONObject(i);	
										
					String term = (String)aux.get("term");
					if (term.equalsIgnoreCase("image")){
						String value = (String)aux.get("value");
						label.setValue(value);
						
					}
				}
			}			
			
			//set source
			Source source = Source.getSourceClassByName(con, "poiproxy");			
			label.setSourceId(source.getId());
			
			
			//set language. For the moment null
			label.setLanguage(null);
			
			//set license. For the moment null
			label.setLicenseId(null);
			
			//set updated		
			Calendar cal = Calendar.getInstance();				
			SimpleDateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");				
			String date = outputformat.format(cal.getTime());				
			java.util.Date date1 = outputformat.parse(date);						
			java.sql.Date date2 = new java.sql.Date(date1.getTime());			
			label.setUpdated(date2);
			
			
			
		}catch(Exception ex){
			//System.out.println("Collector.getImgPOILabel(): "+ex.getMessage());
			log.warn("Collector.getImgPOILabel(): "+ex.getMessage());
			return null;
			
		}		
		
		return label;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////Utility methods
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public String getProjectedCategory(Connection con, String category,String city){			
		
				
		//We need to map the category in FE (e.g museum) to the category in the source looking at the categorymapping file
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			
			String mappingfile = source.getCategorymapping();
			
			//In the case of POIProxy, as special source, we should map to local content depending on the city
			String sourceName = source.getName();
			if (sourceName.toLowerCase().contains("poiproxy")){
				
				//we need to change the properties file name to match the local data
				mappingfile = mappingfile.substring(0,mappingfile.lastIndexOf("/"));
				mappingfile = mappingfile+"/poiproxy_local_"+city+".properties";
				
				log.debug("Using POIProxy as source. Finding its local properties file given by the cityname "+city +" : "+mappingfile);
			} else
				log.debug("Using "+sourceName+" as source. Finding its properties file given by the cityname "+city +" : "+mappingfile);
			
			
			input = new FileInputStream(mappingfile);
	 
			// load properties file
			prop.load(input);
			
			input.close();
			
			// get the corresponding category in the source
			return prop.getProperty(category);	
			
		}catch(Exception ex){
			System.out.println("Collector.getProjectedCategory(): "+ex.getMessage());
			log.error("Collector.getProjectedCategory(): "+ex.getMessage());
		}
		return null;
	}
	
	
	//This method removes all POIs that have already inserted in the database
	public JSONArray cleanPOIJSONArray(Connection con, JSONArray selectedPois,String projectedCategory){
		
		JSONArray returnedPois = new JSONArray();
		
		
		try{
			//We should delete those POIs that have been already inserted in the database
			for(int k = 0; k < selectedPois.length(); k++) {   				
				
				JSONObject poi;
				
				//Get the POIs one by one
				poi = selectedPois.getJSONObject(k);	
				
				//We need to be sure that POIS have an id, otherwise it will return an empty array of POIs
				int originalRefPos = CommonUtils.getPropertyPos(poi,"id");
				
				// POI has id
				if(originalRefPos != -1){
					String originalRef = poi.getJSONArray("label").getJSONObject(originalRefPos).getString("value");  
					
					log.debug("Checking if POI "+originalRef+" has already been inserted in the database");
					
					boolean alreadyInserted = CommonUtils.CheckPOIAlreadyInserted(con, source.getId(),originalRef, projectedCategory);
					if (!alreadyInserted){
						log.debug("POI not inserted in DB. Adding to the JSONArray");
						returnedPois.put(poi);						
					}        				
				}
			}			
			
		}catch(Exception ex){
				System.out.println("Collector.cleanPOIArray(): "+ex.getMessage());
				log.error("Collector.cleanPOIArray(): "+ex.getMessage());
		}
			
		return returnedPois;	
		
		
	}
	
	
	//This method removes all POIs that have already inserted in the database
	public ArrayList<JSONObject> cleanPOIArrayList(Connection con, ArrayList<JSONObject> selectedPois,String projectedCategory){
		
		ArrayList<JSONObject> returnedPois = new ArrayList<JSONObject>();
		
		
		try{
			for(int k = 0; k < selectedPois.size(); k++) { 			        			
    			
    			
    			JSONObject poi;
				
				//Get the POIs one by one
				poi = (JSONObject) selectedPois.get(k);	
				
				
				int originalRefPos = CommonUtils.getPropertyPos(poi,"id");
				
				// POI has id
    			if(originalRefPos != -1){
    				String originalRef = poi.getJSONArray("label").getJSONObject(originalRefPos).getString("value");  
    				
    				//log.debug("Checking if POI has already been inserted in the database");
    				
    				boolean alreadyInserted = CommonUtils.CheckPOIAlreadyInserted(con, source.getId(),originalRef, projectedCategory);
    				if (!alreadyInserted){
    					//log.debug("POI not inserted. Adding to the JSONArray");
    					returnedPois.add(poi);
    				}        				
    			}				
			}
				
			
		}catch(Exception ex){
				System.out.println("Collector.cleanPOIArray(): "+ex.getMessage());
				log.error("Collector.cleanPOIArray(): "+ex.getMessage());
		}
			
		return returnedPois;	
		
		
	}
	
	

}

