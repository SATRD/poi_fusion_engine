package org.upv.satrd.fic2.fe.main;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Definer.OnError;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.upv.satrd.fic2.fe.config.Configuration;
import org.upv.satrd.fic2.fe.connectors.citySDK.Fusion;
import org.upv.satrd.fic2.fe.connectors.citySDK.FusionResult;
import org.upv.satrd.fic2.fe.connectors.citySDK.Collector;
import org.upv.satrd.fic2.fe.connectors.citySDK.CommonUtils;
import org.upv.satrd.fic2.fe.db.APIType;
import org.upv.satrd.fic2.fe.db.POI;
import org.upv.satrd.fic2.fe.db.POILabel;
import org.upv.satrd.fic2.fe.db.POISource;
import org.upv.satrd.fic2.fe.db.Source;
import org.upv.satrd.fic2.fe.fusionrules.FusionRule;
import org.upv.satrd.fic2.fe.fusionrules.FusionRules;
import org.upv.satrd.fic2.fe.fusionrules.FusionRulesParser;

public class FE_sample_valencia {

	private static org.apache.log4j.Logger log;
	
	
	
	private static String configurationFile = "conf/config.xml";
	public static Configuration config = new Configuration(configurationFile);
	
	//These methods are required to reload the configuration from a given File
	public static void setConfigurationString(String path){
		configurationFile = path;
		config = new Configuration(configurationFile);
	}
	
	
	
	
	//@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		log = Logger.getLogger(org.upv.satrd.fic2.fe.main.FE_sample_valencia.class);	
		
		
		//Load configuration parameters of the OCD in order to access the PostGRESQL database		
		Connection con = OutputDB.connectDB(
				config.getConnectionString(),
				config.getUser(),
				config.getPwd(),
				config.getDriverName());	
		
		
		OutputDB.setConfiguration(configurationFile);
		
		
		//Reset and Init database. FIXME It is supposed that psql is installed and in the path, otherwise we cannot execute any script
		log.debug("Reseting tables in database "+config.getName()
				+ " (user: " + config.getUser() + ")");
		String sqlscript = config.getResetScript();
		
		File script_file = new File(
				System.getProperty("user.dir") + File.separator + sqlscript);
		try {
			executeSql(
					script_file,
					config.getDriverName(),
					config.getUser(),
					config.getPwd(),
					config.getConnectionString(),
					config.getScriptSeparator());
		} catch (Exception e1) {
			e1.printStackTrace();
	        log.error(e1.getMessage()+".Exiting...");
	        System.exit(-1);
		}
		
		
	     log.debug("Tables in database "+config.getName()
	    		 + " (user " + config.getUser() + ") "
	     		+ "have been reset");
		
		
	    		
	    //Load fusion rules
		String fusion_path = "conf/FusionRules_Valencia.xml";
		log.debug("Loading fusion rules located at: "+fusion_path);
	    FusionRules fusionRules = new FusionRulesParser().getFusionRules(fusion_path);
	    
	    if (fusionRules.GetFusionrules().isEmpty()){
	    	log.error("No fusion rules have been loaded. Exiting...");
	    	System.exit(0);
	    }
	    log.debug("Fusion rules loaded");
	    
	    log.debug("Initializing database "+config.getName()+" with elements extracted from "+fusion_path);
	    int ret = OutputDB.initDBFromXML(con, fusionRules);
	    if (ret<0){
	    	log.error("There was a problem inserting the data. Error in  PostgreSQL.initDBFromXML() method");
	        System.exit(-1);
	    }
	    log.debug("Database "+config.getName()+" initialized");
	    	    
	    	    
	   
	    log.info("Starting fusion of OCD "+fusionRules.getCity());		    
	    log.debug("Detecting "+fusionRules.GetFusionrules().size()+" fusion_rules for city "+fusionRules.getCity());
	    
	    for (int i=0;i<fusionRules.GetFusionrules().size();i++){
	    	FusionRule fusionRule = fusionRules.GetFusionrules().get(i);
	    	
	    	log.info("Fusion rule "+new Integer(i+1).toString()+" with category "+fusionRule.getCategory());
	    	
	    	
	    	
	    	//Get the location element and iterate through all its sources
	    	ArrayList<String> location = fusionRule.getLocation();
	    	log.debug("Fusion rule "+fusionRule.getCategory()+" has "+location.size()+" location elements");
	    	
	    	
	    	for (int j=0;j<location.size();j++){
	    		
	    		String sourceName = location.get(j).toString();	    		
	    		log.debug("location element "+new Integer(j+1).toString()+" has as sourcename: "+sourceName);
	    		
			    
	    		Source source = Source.getSourceClassByName (con,sourceName);			    
			    
			    if (source !=null){			    	
			    	
			    	//Get APIType 
			    	APIType apitype = APIType.getAPITypeClassById(con, source.getAPITypeId());
			    	log.debug(sourceName+" has APIType "+apitype.getName());
			    	if (apitype != null){
			    		
			    		///////////////////////////////
			    		//CitytSDK
			    		//////////////////////////////
			    		if (apitype.getName().equalsIgnoreCase("citySDK")){
			    			
			    			//Instantiate CitySDK Collector class to start retrieving objects
			    			Collector collector = new Collector(source);
			    			String bbox = fusionRules.getBbox(); 
			    			
			    			
			    			//This method already checks that the returned JSONArray does not include any POI that has been already inserted in the database
			    			JSONArray poiArray = collector.getPOIArrayByCategory(
			    					con, fusionRule.getCategory(),
			    					fusionRules.getCity(),
			    					bbox,fusionRule.getLimit());
			    			
			    			//TODO include this info as statistics
			    			log.info("Getting "+poiArray.length()+" POIs of '"+fusionRule.getCategory()+"' category from "+source.getName());
			        		
			    			
			    			
			        		for(int k = 0; k < poiArray.length(); k++) { 			        			
			        			
			        			FusionResult fusionresult = new FusionResult();
			        			ArrayList<POISource> poisourceArray = new ArrayList<POISource>();
			        			JSONObject poi1;
								try {
										//Get the POIs one by one
										poi1 = poiArray.getJSONObject(k);											
										
										
										//Get latitude and longitude of this POI. 
					        			// FIXME: We assume here that all returned POIS  are georeferenced. Otherwise it will throw an Exception and the loop will end
					    				String lat  = CommonUtils.getLatitude(poi1);
					        			String lon  = CommonUtils.getLongitude(poi1);				        			
					        			String name = CommonUtils.getName(poi1);
					        								        			
					        			
					        			// POI has a name
					        			if(name != null){
					        				
					        				log.debug("POI "+k+" has longitude "+lon+" , latitude "+lat+" and name "+name);
					        				
					        				//The POISource structure needs to be initialized with this poi (poi_id, poiproxyattribute initially are null)				        				
					        				POISource poisource = CommonUtils.getPOISource(con,poi1);
					        				
					        				if (source.getName().equalsIgnoreCase("poiproxy")){
					        					//set the poiproxyattribute as the category
					        					String projectedCategory = getProjectedCategory(source,fusionRules.getCity() ,fusionRule.getCategory());
					        					poisource.setPoiproxyAttribute(projectedCategory); 
					        				}
					        				poisourceArray.add(poisource);
					        				
					        				
					        				//Find in the remaining sources if there are matching POIs 
					        				log.debug("Checking in the remaining sources if we can find matching POIs");
					        				
					        				//j indexes the current source. The remaining sources start from j+1  
					        				for (int p=j+1;p<location.size();p++){
					        		    		
					        		    		String remainingSourceName = location.get(p).toString();
					        		    		log.debug("Checking for matches in "+remainingSourceName);
					        		    		
					        		    		Source source2 = Source.getSourceClassByName(con, remainingSourceName);					        		    		
					        		    		Collector collector2 = new Collector(source2);
					        		    		ArrayList<JSONObject> matchingPOIArray = collector2.getZonePOIArrayByCriteria(con, fusionRule.getCategory(), fusionRules.getCity(), lat, lon, 
					        		    				fusionRule.getMaxDistance(), name, fusionRule.getSimilarityPercentage()); 
					        		    		
					        		    		
					        		    		if(matchingPOIArray.size() == 0){ 
					        		    			log.info("The POI '"+name+"' has no matches in '"+source2.getName()); 
					        		    			
					        		    		}else if(matchingPOIArray.size() > 1){ 
					        		    			log.info("The POI '"+name+"' has "+matchingPOIArray.size()+" matches in '"+source2.getName()); 
					        		    		
					        		    		}else{
							        				
							        				//One match only. Take the first element
					        		    			JSONObject poi2 = matchingPOIArray.get(0);
							        				
					        		    			log.info("The POI '"+name+"' has 1 match in '"+source2.getName()+"'. Applying fusion...");
					        		    			
					        		    			//Add this poi in the sources list
					        		    			poisource = CommonUtils.getPOISource(con,poi2);
					        		    			Source source_aux = Source.getSourceClassById(con, poisource.getSourceId());
							        				if (source_aux.getName().equalsIgnoreCase("poiproxy")){
							        					//set the poiproxyattribute as the category
							        					String projectedCategory = getProjectedCategory(source2,fusionRules.getCity() ,fusionRule.getCategory() );
							        					poisource.setPoiproxyAttribute(projectedCategory); 
							        				}
							        				poisourceArray.add(poisource);
							        				
					        		    			
					        		    			
					        		    			//We store the fusioned object in poi1.The fusionresult structure allows us to obtain not only the POI, 
					        		    			// but also the name of the source we have used for the 'name' field in the POI table. We'll use also 
					        		    			// the source of the position, but we can take it from the for-statements in the code just
					        		    			//using source.getName()
					        		    			
					        		    			fusionresult = Fusion.fusion(poi1,poi2,fusionRule);   
					        		    			poi1 = fusionresult.getFusionedPOI(); 
							        				
							        			}
					        		    		
					        				}
					        				
					        				//After all iterations through the remaining sources, save result POI in DB
					        				//We need to check the source of the 'name' field. This is done during fusion. However, if there is no fusion (no matching)
					        				//then we need to set up the source of the 'name' from the source
					        				String sourceNameOfname = source.getName();
					        				if (fusionresult.getSourceNameFromName() != null) sourceNameOfname = fusionresult.getSourceNameFromName();
					        							        		    			
					        				
			        		    			int poi_id = Fusion.save(con, poi1,fusionRule.getCategory(),sourceNameOfname,source.getName(),poisourceArray);
			        		    			
			        		    			if (poi_id!=-1){
			        		    				log.debug("POI inserted with id: "+poi_id+" Inserting additional images from POIProxy...(if configured so)");			        		    				
			        		    				if (fusionRule.getImgActive().equalsIgnoreCase("true")){
			        		    					log.debug("FusionRule indicates to search for "+fusionRule.getImgLimit()+ " images");
			        		    					ArrayList<String> imgSource = fusionRule.getImgSource();
			        		    					if (imgSource.size()== 0) log.error("No sources present to search for images");
			        		    					int img_index = 0;
			        		    					for (int p=0;p<imgSource.size();p++){
			        		    						log.debug("Looking for images in "+imgSource.get(p));
			        		    						//This method already checks that the returned JSONArray does not include any POI that has been already inserted in the database
			        		    						Source poiproxySource = Source.getSourceClassByName(con, "poiproxy");	
			        		    						Collector collector_img = new Collector(poiproxySource);
			        		    						//Here the bbox refers to the location of the point and the radius
			        		    						Double lon_d = POI.getPOIClassById(con, poi_id).getLongitude();
			        		    						Double lat_d = POI.getPOIClassById(con, poi_id).getLatitude();
			        		    						
			        		    						String longitude = lon_d.toString();			        		    						
			        		    						String latitude = lat_d.toString();
			        		    						String bbox_img=latitude+","+longitude+","+fusionRule.getMaxDistance();
			        		    						
														JSONArray poiArrayImages = collector_img.getImgPOIArrayByCategory(con, imgSource.get(p),fusionRules.getCity(),bbox_img,new Integer(fusionRule.getImgLimit()));
														log.debug("Found "+poiArrayImages.length()+" images in "+imgSource.get(p));
														
														for (int q=0;q<poiArrayImages.length();q++){
															//Get poi
															POILabel poilabel = collector_img.getImgPOILabel(con, poi_id,poiArrayImages,q);
															
															//save poi
															if (img_index ==new Integer(fusionRule.getImgLimit()) ) break;
															if (poilabel !=null){
																POILabel.savePOILabel(con, poilabel); 
																img_index++;
															}
															
														}
														
														if (img_index ==new Integer(fusionRule.getImgLimit()) ) break;
			        		    					}
			        		    					
			        		    				}else{
			        		    					log.debug("FusionRule indicates not to include any images");
			        		    				}
			        		    				
			        		    				
			        		    				
			        		    			}else{
			        		    				log.error("There was a problem saving the POI");
			        		    			}
					        				
					        				
					        			
			        		    			
			        		    			
					        				
					        			}
								} catch (JSONException e) {
										log.error(e.getMessage());
								}
			        			
			        		}	  		
			    			
			    		}
			    		if (apitype.getName().equalsIgnoreCase("Common WP3 POI API")){
			    			//TODO
			    		}
			    	}
			    	
			    }else{
			    	log.error("Could not find a source in the database corresponding with "+sourceName);
			    }		    
			   
	    	}	
	    	
	    }	  
	    
		OutputDB.disconnectDB(con);
		
		//System.out.println("Fin");
		 
		  
		 
	}
	
	private static String getProjectedCategory(Source source, String city, String category){
	
		String projectedCategory = null;
		String mappingfile = source.getCategorymapping();
		//In the case of POIProxy, as special source, we should map to local content depending on the city
		String sourceName = source.getName();
		if (sourceName.toLowerCase().contains("poiproxy")){
			//we need to change the properties file name to match the local data
			mappingfile = mappingfile.substring(0,mappingfile.lastIndexOf("/"));
			mappingfile = mappingfile+"/poiproxy_local_"+city+".properties";
			
		}
		try{
			FileInputStream input = new FileInputStream(mappingfile);
		
			Properties prop =new Properties();
			// load properties file
			prop.load(input);
			
			// get the corresponding category in the source
			projectedCategory = prop.getProperty(category);
		}catch(Exception ex){
			log.error("Error in getProjectedCategory: "+ex.getMessage());
			System.out.println("Error in getProjectedCategory: "+ex.getMessage());
		}
		
		return projectedCategory;
	}
	
	
	
	public static void executeSql(
			File sqlFile,
			String driver,
			String user,
			String pwd,
			String url,
			String scr_sep) throws Exception {
		
	    final class SqlExecuter extends SQLExec {
	        public SqlExecuter() {
	            Project project = new Project();
	            project.init();
	            setProject(project);
	            setTaskType("sql");
	            setTaskName("sql");
	        }
	    }
	    
	    final class OnErrorContinue extends SQLExec.OnError {
	    	
	    	public OnErrorContinue() {
	    		this.setValue("continue");
	    	}
	    	
	    	public String[] getValues() {
	    		return new String[]{"continue"};
	    	}
	    }

	    SqlExecuter executer = new SqlExecuter();
	    executer.setOnerror(new OnErrorContinue());
	    // executer.setOutput(new File("c:/tempo/log_" + System.currentTimeMillis() + ".txt"));
	    executer.setDelimiter(scr_sep);
	    executer.setSrc(sqlFile);
	    executer.setDriver(driver);
	    executer.setPassword(pwd);
	    executer.setUserid(user);
	    executer.setUrl(url);
	    executer.execute();
	    
	}

	
}
