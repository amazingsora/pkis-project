﻿ALTER TABLE AUTHPROF
 DROP PRIMARY KEY CASCADE;

DROP TABLE AUTHPROF CASCADE CONSTRAINTS;


ALTER TABLE DEPT_INFO_EXT
 DROP PRIMARY KEY CASCADE;

DROP TABLE DEPT_INFO_EXT CASCADE CONSTRAINTS;

ALTER TABLE DOCNOTICELOG
 DROP PRIMARY KEY CASCADE;

DROP TABLE DOCNOTICELOG CASCADE CONSTRAINTS;

ALTER TABLE DOCSTATE
 DROP PRIMARY KEY CASCADE;

DROP TABLE DOCSTATE CASCADE CONSTRAINTS;

ALTER TABLE DOCSTATELOG
 DROP PRIMARY KEY CASCADE;

DROP TABLE DOCSTATELOG CASCADE CONSTRAINTS;

ALTER TABLE DOCTODO
 DROP PRIMARY KEY CASCADE;

DROP TABLE DOCTODO CASCADE CONSTRAINTS;

ALTER TABLE DUTYTYPE
 DROP PRIMARY KEY CASCADE;

DROP TABLE DUTYTYPE CASCADE CONSTRAINTS;

ALTER TABLE EMAILQUEUE
 DROP PRIMARY KEY CASCADE;

DROP TABLE EMAILQUEUE CASCADE CONSTRAINTS;

ALTER TABLE FLOWCONF
 DROP PRIMARY KEY CASCADE;

DROP TABLE FLOWCONF CASCADE CONSTRAINTS;

ALTER TABLE FLOWSTEP
 DROP PRIMARY KEY CASCADE;

DROP TABLE FLOWSTEP CASCADE CONSTRAINTS;

ALTER TABLE FLOWSTEPLINK
 DROP PRIMARY KEY CASCADE;

DROP TABLE FLOWSTEPLINK CASCADE CONSTRAINTS;

ALTER TABLE FLOWXML
 DROP PRIMARY KEY CASCADE;

DROP TABLE FLOWXML CASCADE CONSTRAINTS;

ALTER TABLE FORMCONF
 DROP PRIMARY KEY CASCADE;

DROP TABLE FORMCONF CASCADE CONSTRAINTS;

ALTER TABLE HIBERNATE_SEQUENCES
 DROP PRIMARY KEY CASCADE;

DROP TABLE HIBERNATE_SEQUENCES CASCADE CONSTRAINTS;

ALTER TABLE JOBRANK
 DROP PRIMARY KEY CASCADE;

DROP TABLE JOBRANK CASCADE CONSTRAINTS;

ALTER TABLE JOBTITLE
 DROP PRIMARY KEY CASCADE;

DROP TABLE JOBTITLE CASCADE CONSTRAINTS;

ALTER TABLE JOBTYPE
 DROP PRIMARY KEY CASCADE;

DROP TABLE JOBTYPE CASCADE CONSTRAINTS;

ALTER TABLE PROJAUTH
 DROP PRIMARY KEY CASCADE;

DROP TABLE PROJAUTH CASCADE CONSTRAINTS;

ALTER TABLE PROJPROF
 DROP PRIMARY KEY CASCADE;

DROP TABLE PROJPROF CASCADE CONSTRAINTS;

ALTER TABLE SUBFLOWCONF
 DROP PRIMARY KEY CASCADE;

DROP TABLE SUBFLOWCONF CASCADE CONSTRAINTS;

ALTER TABLE SUBJOBTYPE
 DROP PRIMARY KEY CASCADE;

DROP TABLE SUBJOBTYPE CASCADE CONSTRAINTS;

ALTER TABLE SYSPROF
 DROP PRIMARY KEY CASCADE;

DROP TABLE SYSPROF CASCADE CONSTRAINTS;

ALTER TABLE TRAININGORG
 DROP PRIMARY KEY CASCADE;

DROP TABLE TRAININGORG CASCADE CONSTRAINTS;
