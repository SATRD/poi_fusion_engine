Fusion Engine
==============

This documentation provides information on how to setup one instance of the Fusion Engine (FE) service. Once setup and executed an OCD (Open City Database) will be available in a database. The service focuses more on the fusion (merging of POIS) than the access, however there is a complementary service able to expose a REST JSON API to access (read access) the OCD. For the moment the FE does not provide means to input user generated content (UCG), though it may be possible. 
The FE provides a selected list of POIs (Points of Interests) from a certain city.
Currently the FE only supports CitySDK (http://www.citysdk.eu/developers/) interfaces for accessing data sources and providing data access.
However some integration has been done to import FE POI data from the OCDB SE (https://github.com/fraunhoferfokus/OCDB), and some work is expected to be done to integrate with enablers in the FIWARE project. 

The development carried out in this project has been carried out within the FICONTENT2 project (http://mediafi.org/)


Installation
------------

Required software components for a successful installation and operation:  
* JDK v.1.7.x or higher (the FE is developed in Java)
* POSTGIS 2.1.2 or higher (there is also Oracle support since FE v1.0, but has not been entirely tested yet)
* Tomcat v6.0 or higher (if you plan to deploy the complementary service to expose the data in the OCD). Recommended for testing purposes
* Apache Ant 1.9.3 or higher (to execute the FE). Make sure ANT is in your PATH


Optional compoments (for developers)
* pgAdminIII (this will help you accessing the POSTGIS database)
* Eclipse Luna (with this Java IDE developers may change the code to adapt it you their application needs) 
* git (if you want to download easily the code)


Execute the following steps to install a OCDB instance on your machine / server.

1) Download the code from this site. You can click on 'Download ZIP' or just open a console in your desired folder and type

```
$ git clone https://github.com/fraunhoferfokus/ocdb
```
Developers can also download the code with Eclipse (see http://wiki.eclipse.org/EGit/User_Guide#Starting_from_existing_Git_Repositories for more info)


Configuration
-------------

This is probably the most difficult part, as there are various things you need to pay attention.

TBC


Running
-------

Running is as easy as executing ant:

```
$ cd ocdb
$ ./startdb.sh
```

Secondly, start the service by executing the follwing command:

```
$ cd poi_fusion_engine2
$ ant .
```

Depending on the number of data sources, this can take a little while. As you can imagine, this depends on the number of inputs inserted in the FusionRules.xml file.
For the Tenerife OCD it took around 5 min in an i3-4GB RAM computer, whereas for the Valencia OCD it took around 11 min. Note also that sometimes (many times) is not the fusion algorithm but it may be a network issue accessing DBPedia or OSM data.


Testing
-------

TBC



Problems
--------

Please use the issue tracker to report any problems you might encounter.


License
-------
Copyright 2014 SATRD-Universitat Politecnica de Valencia

```
/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright 2014 Fraunhofer FOKUS
 *******************************************************************************/
```
