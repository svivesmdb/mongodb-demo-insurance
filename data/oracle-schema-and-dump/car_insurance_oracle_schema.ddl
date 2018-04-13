--------------------------------------------------------
--  File created - Wednesday-April-11-2018   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CAR_CLAIM
--------------------------------------------------------

  CREATE TABLE "CAR_CLAIM" 
   (	"CLAIM_ID" VARCHAR2(12 CHAR), 
	"POLICY_ID" VARCHAR2(12 CHAR), 
	"CLAIM_DATE" DATE, 
	"SETTLED_DATE" DATE, 
	"CLAIM_AMOUNT" NUMBER(30,2), 
	"SETTLED_AMOUNT" NUMBER(30,2), 
	"CLAIM_REASON" VARCHAR2(30 CHAR), 
	"LAST_CHANGE" TIMESTAMP (6)
   )  ;
--------------------------------------------------------
--  DDL for Table CAR_CUSTOMER
--------------------------------------------------------

  CREATE TABLE "CAR_CUSTOMER" 
   (	"CUSTOMER_ID" VARCHAR2(12 CHAR), 
	"FIRST_NAME" VARCHAR2(150 CHAR), 
	"LAST_NAME" VARCHAR2(150 CHAR), 
	"GENDER" VARCHAR2(1 CHAR), 
	"JOB" VARCHAR2(150 CHAR), 
	"EMAIL" VARCHAR2(100 CHAR), 
	"PHONE" VARCHAR2(40 CHAR), 
	"NUMBER_CHILDREN" NUMBER(4,0), 
	"MARITAL_STATUS" VARCHAR2(12 CHAR), 
	"DATE_OF_BIRTH" DATE, 
	"STREET" VARCHAR2(120 CHAR), 
	"ZIP" VARCHAR2(10 CHAR), 
	"CITY" VARCHAR2(100 CHAR), 
	"COUNTRY_CODE" VARCHAR2(2 CHAR), 
	"NATIONALITY" VARCHAR2(20 CHAR), 
	"LAST_CHANGE" TIMESTAMP (6);
--------------------------------------------------------
--  DDL for Table CAR_POLICY
--------------------------------------------------------

  CREATE TABLE "CAR_POLICY" 
   (	"POLICY_ID" VARCHAR2(12 CHAR), 
	"CUSTOMER_ID" VARCHAR2(12 CHAR), 
	"COVER_START" DATE, 
	"CAR_MODEL" VARCHAR2(255 CHAR), 
	"MAX_COVERD" NUMBER(10,2), 
	"LAST_ANN_PREMIUM_GROSS" NUMBER(10,2), 
	"LAST_CHANGE" TIMESTAMP (6) ;
