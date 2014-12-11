--------------------------------------------------------
-- Archivo creado  - martes-diciembre-09-2014   
--------------------------------------------------------

DROP TABLE "OCDSOURCE"
/
DROP TABLE "POICOMPONENT"
/
DROP TABLE "POILABEL"
/
DROP TABLE "POIFICONTENT"
/
DROP TABLE "POICATEGORY"
/
DROP TABLE "OCDCATEGORY"
/
DROP TABLE "LICENSESOURCE"
/
DROP TABLE "POISOURCE"
/
DROP TABLE "SOURCECITY"
/
DROP TABLE "CATEGORYRELATION"
/
DROP TABLE "SOURCE"
/
DROP TABLE "CATEGORY"
/
DROP TABLE "COMPONENT"
/
DROP TABLE "LABELTYPE"
/
DROP TABLE "LICENSE"
/
DROP TABLE "OCD"
/
DROP TABLE "CITY"
/
DELETE FROM USER_SDO_GEOM_METADATA
WHERE TABLE_NAME = 'POI' AND COLUMN_NAME = 'POSITION'
/
DROP TABLE "POI"
/
DROP TABLE "APITYPE"
/

--------------------------------------------------------
--  DDL for Table APITYPE
--------------------------------------------------------

  CREATE TABLE "APITYPE" 
   (	"ID" number(12), 
	"NAME" NVARCHAR2(45), 
	"DESCRIPTION" NVARCHAR2(255), 
	"APIRULES" NVARCHAR2(255)
   ) 
/

--------------------------------------------------------
--  DDL for Table CATEGORY
--------------------------------------------------------

  CREATE TABLE "CATEGORY" 
   (	"ID" number(12), 
	"NAME" NVARCHAR2(45), 
	"DESCRIPTION" NVARCHAR2(255), 
	"level" number(12),
	"ICON" NVARCHAR2(255)
   ) 
/

--------------------------------------------------------
--  DDL for Table CATEGORYRELATION
--------------------------------------------------------

  CREATE TABLE "CATEGORYRELATION" 
   (	"CATEGORYID1" number(12),
	"CATEGORYID2" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table CITY
--------------------------------------------------------

  CREATE TABLE "CITY" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45), 
	"BBOX" NVARCHAR2(127)
   ) 
/

--------------------------------------------------------
--  DDL for Table COMPONENT
--------------------------------------------------------

  CREATE TABLE "COMPONENT" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45)
   ) 
/

--------------------------------------------------------
--  DDL for Table LABELTYPE
--------------------------------------------------------

  CREATE TABLE "LABELTYPE" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45)
   ) 
/

--------------------------------------------------------
--  DDL for Table LICENSE
--------------------------------------------------------

  CREATE TABLE "LICENSE" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45), 
	"DESCRIPTION" NVARCHAR2(255), 
	"INFO" NVARCHAR2(255)
   ) 
/

--------------------------------------------------------
--  DDL for Table LICENSESOURCE
--------------------------------------------------------

  CREATE TABLE "LICENSESOURCE" 
   (	"SOURCEID" number(12),
	"LICENSEID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table OCD
--------------------------------------------------------

  CREATE TABLE "OCD" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45), 
	"CITY" number(12),
	"DESCRIPTION" NVARCHAR2(255), 
	"FUSIONRULES" NVARCHAR2(255), 
	"ACCESSKEY" NVARCHAR2(45), 
	"FUSIONDATE" DATE
   ) 
/

--------------------------------------------------------
--  DDL for Table OCDCATEGORY
--------------------------------------------------------

  CREATE TABLE "OCDCATEGORY" 
   (	"OCDID" number(12),
	"CATEGORYID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table OCDSOURCE
--------------------------------------------------------

  CREATE TABLE "OCDSOURCE" 
   (	"OCDID" number(12),
	"SOURCEID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table POI
--------------------------------------------------------

  CREATE TABLE "POI" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(128), 
	"UPDATED" DATE, 
	"POSITION" "SDO_GEOMETRY"
   ) 
/

--------------------------------------------------------
--  DDL for Table POICATEGORY
--------------------------------------------------------

  CREATE TABLE "POICATEGORY" 
   (	"POIID" number(12),
	"CATEGORYID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table POICOMPONENT
--------------------------------------------------------

  CREATE TABLE "POICOMPONENT" 
   (	"POILABELID" number(12),
	"COMPONENTID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table POIFICONTENT
--------------------------------------------------------

  CREATE TABLE "POIFICONTENT" 
   (	"POIID" number(12),
	"FICONTENTPOIID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table POILABEL
--------------------------------------------------------

  CREATE TABLE "POILABEL" 
   (	"ID" number(12),
	"POIID" number(12),
	"TYPEID" number(12),
	"VALUE" NVARCHAR2(2000), 
	"SOURCEID" number(12),
	"LANGUAGE" NVARCHAR2(10), 
	"LICENSEID" number(12),
	"UPDATED" DATE
   ) 
/

--------------------------------------------------------
--  DDL for Table POISOURCE
--------------------------------------------------------

  CREATE TABLE "POISOURCE" 
   (	"POIID" number(12),
	"SOURCEID" number(12),
	"ORIGINALREF" NVARCHAR2(255), 
	"POIPROXYATTRIBUTE" NVARCHAR2(255)
   ) 
/

--------------------------------------------------------
--  DDL for Table SOURCE
--------------------------------------------------------

  CREATE TABLE "SOURCE" 
   (	"ID" number(12),
	"NAME" NVARCHAR2(45), 
	"DESCRIPTION" NVARCHAR2(255), 
	"URLACCESS" NVARCHAR2(255), 
	"CATEGORYMAPPING" NVARCHAR2(255), 
	"APITYPEID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Table SOURCECITY
--------------------------------------------------------

  CREATE TABLE "SOURCECITY" 
   (	"SOURCEID" number(12),
	"CITYID" number(12)
   ) 
/

--------------------------------------------------------
--  DDL for Index CATEGORYRELATION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "CATEGORYRELATION_PK" ON "CATEGORYRELATION" ("CATEGORYID1", "CATEGORYID2") 
  
/

--------------------------------------------------------
--  DDL for Index OCDCATEGORY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "OCDCATEGORY_PK" ON "OCDCATEGORY" ("OCDID", "CATEGORYID") 
/

--------------------------------------------------------
--  DDL for Index OCDSOURCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "OCDSOURCE_PK" ON "OCDSOURCE" ("OCDID", "SOURCEID") 
/


--------------------------------------------------------
--  DDL for Index POISOURCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "POISOURCE_PK" ON "POISOURCE" ("POIID", "SOURCEID") 
  
/

--------------------------------------------------------
--  DDL for Index COMPONENT_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "COMPONENT_UK1" ON "COMPONENT" ("NAME") 
  
/

--------------------------------------------------------
--  DDL for Index SOURCECITY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SOURCECITY_PK" ON "SOURCECITY" ("SOURCEID", "CITYID") 
  
/

--------------------------------------------------------
--  DDL for Index LICENSESOURCE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LICENSESOURCE_PK" ON "LICENSESOURCE" ("SOURCEID", "LICENSEID") 
  
/

--------------------------------------------------------
--  DDL for Index POICOMPONENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "POICOMPONENT_PK" ON "POICOMPONENT" ("POILABELID", "COMPONENTID") 
  
/

--------------------------------------------------------
--  DDL for Index LABELTYPE_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "LABELTYPE_UK1" ON "LABELTYPE" ("NAME") 
  
/

--------------------------------------------------------
--  DDL for Index POICATEGORY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "POICATEGORY_PK" ON "POICATEGORY" ("POIID", "CATEGORYID") 
  
/

--------------------------------------------------------
--  DDL for Index APITYPE_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "APITYPE_UK1" ON "APITYPE" ("NAME") 
  
/

--------------------------------------------------------
--  DDL for Index POI_SPATIAL_IDX
--------------------------------------------------------

INSERT INTO user_sdo_geom_metadata
    (TABLE_NAME,
     COLUMN_NAME,
     DIMINFO,
     SRID)
  VALUES (
  'POI',
  'POSITION',
  SDO_DIM_ARRAY(   -- 20X20 grid
    SDO_DIM_ELEMENT('Lon', -180, 180, 0.5),
    SDO_DIM_ELEMENT('Lat', -90, 90, 0.5)
     ),
  4326   -- SRID
)
/


  CREATE INDEX "POI_SPATIAL_IDX" ON "POI" ("POSITION") 
   INDEXTYPE IS "MDSYS"."SPATIAL_INDEX"  PARAMETERS ('sdo_indx_dims=2, layer_gtype=point')
/

--------------------------------------------------------
--  Constraints for Table CITY
--------------------------------------------------------

  ALTER TABLE "CITY" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "CITY" MODIFY ("BBOX" NOT NULL ENABLE)
/
 
  ALTER TABLE "CITY" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table APITYPE
--------------------------------------------------------

  ALTER TABLE "APITYPE" ADD CONSTRAINT "APITYPE_UK1" UNIQUE ("NAME") ENABLE
/
 
  ALTER TABLE "APITYPE" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "APITYPE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table POICATEGORY
--------------------------------------------------------

  ALTER TABLE "POICATEGORY" ADD CONSTRAINT "POICATEGORY_PK" PRIMARY KEY ("POIID", "CATEGORYID") ENABLE
/
 
  ALTER TABLE "POICATEGORY" MODIFY ("POIID" NOT NULL ENABLE)
/
 
  ALTER TABLE "POICATEGORY" MODIFY ("CATEGORYID" NOT NULL ENABLE)
/


--------------------------------------------------------
--  Constraints for Table OCD
--------------------------------------------------------

  ALTER TABLE "OCD" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "OCD" MODIFY ("CITY" NOT NULL ENABLE)
/
 
  ALTER TABLE "OCD" MODIFY ("FUSIONRULES" NOT NULL ENABLE)
/
 
  ALTER TABLE "OCD" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table POISOURCE
--------------------------------------------------------

  ALTER TABLE "POISOURCE" ADD CONSTRAINT "POISOURCE_PK" PRIMARY KEY ("POIID", "SOURCEID") ENABLE
/
 
  ALTER TABLE "POISOURCE" MODIFY ("POIID" NOT NULL ENABLE)
/
 
  ALTER TABLE "POISOURCE" MODIFY ("SOURCEID" NOT NULL ENABLE)
/
 
--   ALTER TABLE "POISOURCE" MODIFY ("POIPROXYATTRIBUTE" NOT NULL ENABLE)

--------------------------------------------------------
--  Constraints for Table POILABEL
--------------------------------------------------------

  ALTER TABLE "POILABEL" MODIFY ("POIID" NOT NULL ENABLE)
/
 
  ALTER TABLE "POILABEL" MODIFY ("TYPEID" NOT NULL ENABLE)
/
 
  ALTER TABLE "POILABEL" MODIFY ("VALUE" NOT NULL ENABLE)
/
 
  ALTER TABLE "POILABEL" MODIFY ("SOURCEID" NOT NULL ENABLE)
/
 
--  ALTER TABLE "POILABEL" MODIFY ("LICENSEID" NOT NULL ENABLE)
-- 
 
  ALTER TABLE "POILABEL" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table LICENSESOURCE
--------------------------------------------------------

  ALTER TABLE "LICENSESOURCE" ADD CONSTRAINT "LICENSESOURCE_PK" PRIMARY KEY ("SOURCEID", "LICENSEID") ENABLE
/
 
  ALTER TABLE "LICENSESOURCE" MODIFY ("SOURCEID" NOT NULL ENABLE)
/
 
  ALTER TABLE "LICENSESOURCE" MODIFY ("LICENSEID" NOT NULL ENABLE)
/

--------------------------------------------------------
--  Constraints for Table OCDCATEGORY
--------------------------------------------------------

  ALTER TABLE "OCDCATEGORY" ADD CONSTRAINT "OCDCATEGORY_PK" PRIMARY KEY ("OCDID", "CATEGORYID") ENABLE
/
 
  ALTER TABLE "OCDCATEGORY" MODIFY ("OCDID" NOT NULL ENABLE)
/
 
  ALTER TABLE "OCDCATEGORY" MODIFY ("CATEGORYID" NOT NULL ENABLE)
/

--------------------------------------------------------
--  Constraints for Table COMPONENT
--------------------------------------------------------

  ALTER TABLE "COMPONENT" ADD CONSTRAINT "COMPONENT_UK1" UNIQUE ("NAME") ENABLE
/
 
  ALTER TABLE "COMPONENT" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table POIFICONTENT
--------------------------------------------------------

  ALTER TABLE "POIFICONTENT" ADD PRIMARY KEY ("POIID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table CATEGORYRELATION
--------------------------------------------------------

  ALTER TABLE "CATEGORYRELATION" ADD CONSTRAINT "CATEGORYRELATION_PK" PRIMARY KEY ("CATEGORYID1", "CATEGORYID2") ENABLE
/
 
  ALTER TABLE "CATEGORYRELATION" MODIFY ("CATEGORYID1" NOT NULL ENABLE)
/
 
  ALTER TABLE "CATEGORYRELATION" MODIFY ("CATEGORYID2" NOT NULL ENABLE)
/

--------------------------------------------------------
--  Constraints for Table CATEGORY
--------------------------------------------------------

  ALTER TABLE "CATEGORY" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "CATEGORY" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table SOURCE
--------------------------------------------------------

  ALTER TABLE "SOURCE" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "SOURCE" MODIFY ("URLACCESS" NOT NULL ENABLE)
/
 
  ALTER TABLE "SOURCE" MODIFY ("CATEGORYMAPPING" NOT NULL ENABLE)
/
 
  ALTER TABLE "SOURCE" MODIFY ("APITYPEID" NOT NULL ENABLE)
/
 
  ALTER TABLE "SOURCE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table POI
--------------------------------------------------------

  ALTER TABLE "POI" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "POI" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table LABELTYPE
--------------------------------------------------------

  ALTER TABLE "LABELTYPE" ADD CONSTRAINT "LABELTYPE_UK1" UNIQUE ("NAME") ENABLE
/
 
  ALTER TABLE "LABELTYPE" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "LABELTYPE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table LICENSE
--------------------------------------------------------

  ALTER TABLE "LICENSE" MODIFY ("NAME" NOT NULL ENABLE)
/
 
  ALTER TABLE "LICENSE" ADD PRIMARY KEY ("ID") ENABLE
/

--------------------------------------------------------
--  Constraints for Table OCDSOURCE
--------------------------------------------------------

  ALTER TABLE "OCDSOURCE" ADD CONSTRAINT "OCDSOURCE_PK" PRIMARY KEY ("OCDID", "SOURCEID") ENABLE
/
 
  ALTER TABLE "OCDSOURCE" MODIFY ("OCDID" NOT NULL ENABLE)
/
 
  ALTER TABLE "OCDSOURCE" MODIFY ("SOURCEID" NOT NULL ENABLE)
/

--------------------------------------------------------
--  Constraints for Table SOURCECITY
--------------------------------------------------------

  ALTER TABLE "SOURCECITY" ADD CONSTRAINT "SOURCECITY_PK" PRIMARY KEY ("SOURCEID", "CITYID") ENABLE
/
 
  ALTER TABLE "SOURCECITY" MODIFY ("SOURCEID" NOT NULL ENABLE)
/
 
  ALTER TABLE "SOURCECITY" MODIFY ("CITYID" NOT NULL ENABLE)
/

--------------------------------------------------------
--  Constraints for Table POICOMPONENT
--------------------------------------------------------

  ALTER TABLE "POICOMPONENT" ADD CONSTRAINT "POICOMPONENT_PK" PRIMARY KEY ("POILABELID", "COMPONENTID") ENABLE
/
 
  ALTER TABLE "POICOMPONENT" MODIFY ("POILABELID" NOT NULL ENABLE)
/
 
  ALTER TABLE "POICOMPONENT" MODIFY ("COMPONENTID" NOT NULL ENABLE)
/


--------------------------------------------------------
--  Ref Constraints for Table CATEGORYRELATION
--------------------------------------------------------

  ALTER TABLE "CATEGORYRELATION" ADD CONSTRAINT "FK_CATEGORYRELATION_CAT1" FOREIGN KEY ("CATEGORYID1")
	  REFERENCES "CATEGORY" ("ID") ENABLE
/
 
  ALTER TABLE "CATEGORYRELATION" ADD CONSTRAINT "FK_CATEGORYRELATION_CAT2" FOREIGN KEY ("CATEGORYID2")
	  REFERENCES "CATEGORY" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table LICENSESOURCE
--------------------------------------------------------

  ALTER TABLE "LICENSESOURCE" ADD CONSTRAINT "FK_LICENSESOURCE_LICENSE" FOREIGN KEY ("LICENSEID")
	  REFERENCES "LICENSE" ("ID") ENABLE
/
 
  ALTER TABLE "LICENSESOURCE" ADD CONSTRAINT "FK_LICENSESOURCE_SOURCE" FOREIGN KEY ("SOURCEID")
	  REFERENCES "SOURCE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table OCD
--------------------------------------------------------

  ALTER TABLE "OCD" ADD CONSTRAINT "FK_OCD_CITY" FOREIGN KEY ("CITY")
	  REFERENCES "CITY" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table OCDCATEGORY
--------------------------------------------------------

  ALTER TABLE "OCDCATEGORY" ADD CONSTRAINT "FK_OCDCATEGORY_CATEGORY" FOREIGN KEY ("CATEGORYID")
	  REFERENCES "CATEGORY" ("ID") ENABLE
/
 
  ALTER TABLE "OCDCATEGORY" ADD CONSTRAINT "FK_OCDCATEGORY_OCD" FOREIGN KEY ("OCDID")
	  REFERENCES "OCD" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table OCDSOURCE
--------------------------------------------------------

  ALTER TABLE "OCDSOURCE" ADD CONSTRAINT "FK_OCDSOURCE_OCD" FOREIGN KEY ("OCDID")
	  REFERENCES "OCD" ("ID") ENABLE
/
 
  ALTER TABLE "OCDSOURCE" ADD CONSTRAINT "FK_OCDSOURCE_SOURCE" FOREIGN KEY ("SOURCEID")
	  REFERENCES "SOURCE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table POICATEGORY
--------------------------------------------------------

  ALTER TABLE "POICATEGORY" ADD CONSTRAINT "FK_POICATEGORY_CATEGORY" FOREIGN KEY ("CATEGORYID")
	  REFERENCES "CATEGORY" ("ID") ENABLE
/
 
  ALTER TABLE "POICATEGORY" ADD CONSTRAINT "FK_POICATEGORY_POI" FOREIGN KEY ("POIID")
	  REFERENCES "POI" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table POICOMPONENT
--------------------------------------------------------

  ALTER TABLE "POICOMPONENT" ADD CONSTRAINT "FK_POICOMPONENT_COMPONENT" FOREIGN KEY ("COMPONENTID")
	  REFERENCES "COMPONENT" ("ID") ENABLE
/
 
  ALTER TABLE "POICOMPONENT" ADD CONSTRAINT "FK_POICOMPONENT_POILABEL" FOREIGN KEY ("POILABELID")
	  REFERENCES "POILABEL" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table POIFICONTENT
--------------------------------------------------------

  ALTER TABLE "POIFICONTENT" ADD CONSTRAINT "FK_POIFICONTENT_POI" FOREIGN KEY ("POIID")
	  REFERENCES "POI" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table POILABEL
--------------------------------------------------------

  ALTER TABLE "POILABEL" ADD CONSTRAINT "FK_POILABEL_LICENSE" FOREIGN KEY ("LICENSEID")
	  REFERENCES "LICENSE" ("ID") ENABLE
/
 
  ALTER TABLE "POILABEL" ADD CONSTRAINT "FK_POILABEL_POI" FOREIGN KEY ("POIID")
	  REFERENCES "POI" ("ID") ENABLE
/
 
  ALTER TABLE "POILABEL" ADD CONSTRAINT "FK_POILABEL_SOURCE" FOREIGN KEY ("SOURCEID")
	  REFERENCES "SOURCE" ("ID") ENABLE
/
 
  ALTER TABLE "POILABEL" ADD CONSTRAINT "FK_POILABEL_TYPE" FOREIGN KEY ("TYPEID")
	  REFERENCES "LABELTYPE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table POISOURCE
--------------------------------------------------------

  ALTER TABLE "POISOURCE" ADD CONSTRAINT "FK_POISOURCE_POI" FOREIGN KEY ("POIID")
	  REFERENCES "POI" ("ID") ENABLE
/
 
  ALTER TABLE "POISOURCE" ADD CONSTRAINT "FK_POISOURCE_SOURCE" FOREIGN KEY ("SOURCEID")
	  REFERENCES "SOURCE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table SOURCE
--------------------------------------------------------

  ALTER TABLE "SOURCE" ADD CONSTRAINT "FK_SOURCE_APITYPE" FOREIGN KEY ("APITYPEID")
	  REFERENCES "APITYPE" ("ID") ENABLE
/

--------------------------------------------------------
--  Ref Constraints for Table SOURCECITY
--------------------------------------------------------

  ALTER TABLE "SOURCECITY" ADD CONSTRAINT "FK_SOURCECITY_CITY" FOREIGN KEY ("CITYID")
	  REFERENCES "CITY" ("ID") ENABLE
/
 
  ALTER TABLE "SOURCECITY" ADD CONSTRAINT "FK_SOURCECITY_SOURCE" FOREIGN KEY ("SOURCEID")
	  REFERENCES "SOURCE" ("ID") ENABLE
/

----------------------------------------------------------
-- SEQUENCES AND TRIGGERS for ID primary keys           --
----------------------------------------------------------

CREATE SEQUENCE apitype_id_seq
/
CREATE OR REPLACE TRIGGER apitype_trg
BEFORE INSERT ON apitype FOR EACH ROW
BEGIN
  SELECT apitype_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE category_id_seq
/
CREATE OR REPLACE TRIGGER category_trg
BEFORE INSERT ON category FOR EACH ROW
BEGIN
  SELECT category_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE city_id_seq
/
CREATE OR REPLACE TRIGGER city_trg
BEFORE INSERT ON city FOR EACH ROW
BEGIN
  SELECT city_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE component_id_seq
/
CREATE OR REPLACE TRIGGER component_trg
BEFORE INSERT ON component FOR EACH ROW
BEGIN
  SELECT component_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE license_id_seq
/
CREATE OR REPLACE TRIGGER license_trg
BEFORE INSERT ON license FOR EACH ROW
BEGIN
  SELECT license_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE ocd_id_seq
/
CREATE OR REPLACE TRIGGER ocd_trg
BEFORE INSERT ON ocd FOR EACH ROW
BEGIN
  SELECT ocd_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE source_id_seq
/
CREATE OR REPLACE TRIGGER source_trg
BEFORE INSERT ON source FOR EACH ROW
BEGIN
  SELECT source_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE poi_id_seq
/
CREATE OR REPLACE TRIGGER poi_trg
BEFORE INSERT ON poi FOR EACH ROW
BEGIN
  SELECT poi_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE poilabel_id_seq
/
CREATE OR REPLACE TRIGGER poilabel_trg
BEFORE INSERT ON poilabel FOR EACH ROW
BEGIN
  SELECT poilabel_id_seq.nextval INTO :new.id FROM dual;
END;
/

CREATE SEQUENCE labeltype_id_seq
/
CREATE OR REPLACE TRIGGER labeltype_trg
BEFORE INSERT ON labeltype FOR EACH ROW
BEGIN
  SELECT labeltype_id_seq.nextval INTO :new.id FROM dual;
END;
/

COMMIT
/
