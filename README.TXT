FI-CONTENT2 Fusion Engine (FE) Specific Enabler (SE) version 0.2.0

The Fusion Engine allows to retrieve Points of Interest (POIs) from several data sources and combine (fusion) them building a single Open City Database (OCD).
Currently only CitySDK interface is supported for:
- Accessing the data sources (input interface)
- Providing an interface to external applications (output interface)

The FE is still under development and has only been tested  with CitySDK interfaces for OSM, DBPedia and PoiProxy for certain categories.
More information about CitySDK and development issues can be found in http://www.citysdk.eu/developers/




Following the list of directories belonging to the FE SE  
src/ 	contains all relates sources. It is divided under several 'org.upv.satrd.fic2.fe' subdirectories
		config/					configuration class that encapsulates the configuration
		connectors.citySDK/		java classes for collecting and fusioning POIs arriving from data sources in CitySDK format
		db/						java classes for representing the tables in the data model, allowing the storage (INSERT) and retrieval (SELECT) from info
		fusionrules/			java classes for representing the fusion rules
		main/		  			main classes. It provides a sample main application for Tenerife
		test/					test java classes. It just test if classes in 'db' packages are able to properly store and retrieve information

conf/ 	contains configuration files
		config.xml is the main configuration file. Currently only database configuration parameters are used
		FusionRules_Tenerife.xml is a sample fusionRules files with various categories. 
		Each data source has its own properties file where the common categories are mapped in those categories established in their respective data source
		Poiproxy is a special data source and has also additional properties files
		Log4j properties file is also located here
		
	
db/		contains a SQL script to reset the OCD database. It is supposed that the database has been created in POSTGIS with the postgis_template as template
		It also contains a PNG file showing the datamodel and the different tables and relationships
		
/javadoc 	Javadoc of all developed classes

lib/	contains all necessary libraries

build.xml The project has been generated under Eclipse Luna as an Ant file. You may import the whole project under Eclipse for simplicity	