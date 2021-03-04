
--
-- USER_INFO_EXT  (Table) 
--
CREATE TABLE USER_INFO_EXT
(
  APP_ID    VARCHAR2(30 CHAR)                   NOT NULL,
  IDEN_ID   VARCHAR2(20 CHAR)                   NOT NULL,
  USER_ID   VARCHAR2(30 CHAR)                   NOT NULL,
  BGN_DATE  DATE                                NOT NULL,
  END_DATE  DATE,
  CRE_USER  VARCHAR2(50 CHAR)                   NOT NULL,
  CRE_DATE  DATE                                NOT NULL,
  UPD_USER  VARCHAR2(50 CHAR),
  UPD_DATE  DATE,
  CONSTRAINT USER_INFO_EXT_PK
 PRIMARY KEY
 (APP_ID, IDEN_ID, USER_ID)
);

COMMENT ON TABLE USER_INFO_EXT IS '使用者資料擴充檔';

COMMENT ON COLUMN USER_INFO_EXT.APP_ID IS '系統代號';

COMMENT ON COLUMN USER_INFO_EXT.IDEN_ID IS '識別碼';

COMMENT ON COLUMN USER_INFO_EXT.USER_ID IS '使用者帳號';

COMMENT ON COLUMN USER_INFO_EXT.BGN_DATE IS '起始日期';

COMMENT ON COLUMN USER_INFO_EXT.END_DATE IS '截止日期';

COMMENT ON COLUMN USER_INFO_EXT.CRE_USER IS '建立人員';

COMMENT ON COLUMN USER_INFO_EXT.CRE_DATE IS '建立日期';

COMMENT ON COLUMN USER_INFO_EXT.UPD_USER IS '異動人員';

COMMENT ON COLUMN USER_INFO_EXT.UPD_DATE IS '異動日期';


--
-- USER_INFO_EXT_IDX01  (Index) 
--
CREATE UNIQUE INDEX USER_INFO_EXT_IDX01 ON USER_INFO_EXT
(APP_ID, USER_ID);


--
-- DEPT_INFO_EXT  (Table) 
--
CREATE TABLE DEPT_INFO_EXT
(
  APP_ID     VARCHAR2(30 CHAR)                  NOT NULL,
  IDEN_ID    VARCHAR2(20 CHAR)                  NOT NULL,
  IDEN_TYPE  VARCHAR2(2 CHAR)                   DEFAULT '00'                  NOT NULL,
  CRE_USER   VARCHAR2(50 CHAR)                  NOT NULL,
  CRE_DATE   DATE                               NOT NULL,
  UPD_USER   VARCHAR2(50 CHAR),
  UPD_DATE   DATE,
  CONSTRAINT DEPT_INFO_EXT_PK
 PRIMARY KEY
 (APP_ID, IDEN_ID)
);

COMMENT ON TABLE DEPT_INFO_EXT IS '部門資料擴充檔';

COMMENT ON COLUMN DEPT_INFO_EXT.APP_ID IS '系統代號';

COMMENT ON COLUMN DEPT_INFO_EXT.IDEN_ID IS '識別碼';

COMMENT ON COLUMN DEPT_INFO_EXT.IDEN_TYPE IS '身份別 00:系統 01:通路 02:供應商';

COMMENT ON COLUMN DEPT_INFO_EXT.CRE_USER IS '建立人員';

COMMENT ON COLUMN DEPT_INFO_EXT.CRE_DATE IS '建立日期';

COMMENT ON COLUMN DEPT_INFO_EXT.UPD_USER IS '異動人員';

COMMENT ON COLUMN DEPT_INFO_EXT.UPD_DATE IS '異動日期';



-- XAUTH_APPLICATION
CREATE TABLE PCRCMGR.XAUTH_APPLICATION (
    APP_ID            VARCHAR2(30 CHAR)        NOT NULL,
    APP_NAME        VARCHAR2(150 CHAR)        NOT NULL,
    ENABLED            VARCHAR2(1 CHAR)        DEFAULT '1' NOT NULL,
    PUBLIC_KEY        VARCHAR2(1500 CHAR)        NOT NULL,
    PRIVATE_KEY        VARCHAR2(1500 CHAR)        NOT NULL,
    CRE_USER        VARCHAR2(50 CHAR)       NOT NULL,
    CRE_DATE        DATE                       NOT NULL,
    UPD_USER        VARCHAR2(50 CHAR),
    UPD_DATE        DATE
);

COMMENT ON TABLE PCRCMGR.XAUTH_APPLICATION IS '系統資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.APP_NAME IS '系統名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.ENABLED IS '是否啟用 0:停用 1:啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.PUBLIC_KEY IS '公鑰';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.PRIVATE_KEY IS '私鑰';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_APPLICATION.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_APPLICATION_PK ON PCRCMGR.XAUTH_APPLICATION
(APP_ID);

ALTER TABLE PCRCMGR.XAUTH_APPLICATION ADD (
  CONSTRAINT XAUTH_APPLICATION_PK
  PRIMARY KEY
  (APP_ID)
  USING INDEX PCRCMGR.XAUTH_APPLICATION_PK
  ENABLE VALIDATE);
  
-- XAUTH_DEPT
/*CREATE TABLE PCRCMGR.XAUTH_DEPT (
    APP_ID            VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID         VARCHAR2(20 CHAR)           NOT NULL,  
    BAN                VARCHAR2(10 CHAR),
    CNAME            VARCHAR2(150 CHAR)            NOT NULL,
    PARENT_ID        VARCHAR2(20 CHAR)            NOT NULL,
    SEQ_NO            NUMBER                        NOT NULL,
    PARENT_SEQ        NUMBER                        NOT NULL,
    ENABLED            VARCHAR2(1 CHAR)            DEFAULT '1' NOT NULL,
    CRE_USER        VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE        DATE                        NOT NULL,
    UPD_USER        VARCHAR2(50 CHAR),
    UPD_DATE        DATE
);*/
CREATE TABLE XAUTH_DEPT
(
  APP_ID         VARCHAR2(30 CHAR)                NOT NULL,
  IDEN_ID        VARCHAR2(255 CHAR)             NOT NULL,
  BAN            VARCHAR2(255 CHAR),
  CREATEAGENTID  VARCHAR2(30 CHAR),
  CRE_DATE       DATE                   NOT NULL,
  CRE_USER       VARCHAR2(30 CHAR),
  DEPARTMENTNO   NUMBER(10),
  DEPTCHIEF      VARCHAR2(30 CHAR),
  --SERNO          NUMBER(19)                     NOT NULL,
  JOBTYPESERNO   NUMBER(19),
  "level"        NUMBER(10)      DEFAULT 0               NOT NULL,
  CNAME          VARCHAR2(50 CHAR)              NOT NULL,
  PARENTNO       NUMBER(10),
  SEQ_NO          NUMBER(10)                        NOT NULL,
  SHORTNAME      VARCHAR2(30 CHAR),
  PARENT_SEQ  NUMBER(10)                        NOT NULL,
  ENABLED        VARCHAR2(1 CHAR)               NOT NULL,
  PARENT_ID      VARCHAR2(30 CHAR),
  UPDATEAGENTID  VARCHAR2(30 CHAR),
  UPD_DATE       DATE                   ,
  UPD_USER       VARCHAR2(30 CHAR),
  PRIMARY KEY
 (APP_ID, IDEN_ID)
--  CONSTRAINT UK_5IUEONUQ0FIE16UM499XPAIRK
-- UNIQUE (SERNO),
-- CONSTRAINT FKO0NMEXC40USFYJSGTOFCRFS2Q 
 --FOREIGN KEY (APP_ID, PARENT_ID) 
-- REFERENCES XAUTH_DEPT (APP_ID,IDEN_ID)
);

COMMENT ON TABLE PCRCMGR.XAUTH_DEPT IS '部門資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.BAN IS '統一編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.CNAME IS '部門名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.PARENT_ID IS '上層選單編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.SEQ_NO IS '顯示順序';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.PARENT_SEQ IS '上層順序';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.ENABLED IS '是否啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_DEPT.UPD_DATE IS '異動日期';
--CREATE UNIQUE INDEX PCRCMGR.XAUTH_DEPT_PK ON PCRCMGR.XAUTH_DEPT
--(APP_ID, IDEN_ID);  

--ALTER TABLE PCRCMGR.XAUTH_DEPT ADD (
--  CONSTRAINT XAUTH_DEPT_PK
--  PRIMARY KEY
 -- (APP_ID, IDEN_ID)
--  USING INDEX PCRCMGR.XAUTH_DEPT_PK
 -- ENABLE VALIDATE);

  
-- XAUTH_IDEN_MENU
CREATE TABLE PCRCMGR.XAUTH_IDEN_MENU
(  
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    MENU_ID                    VARCHAR2(40 CHAR)            NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_IDEN_MENU IS '識別選單資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.MENU_ID IS '選單編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_IDEN_MENU.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_IDEN_MENU_PK ON PCRCMGR.XAUTH_IDEN_MENU
(APP_ID, IDEN_ID, MENU_ID);


ALTER TABLE PCRCMGR.XAUTH_IDEN_MENU ADD (
  CONSTRAINT XAUTH_IDEN_MENU_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, MENU_ID)
  USING INDEX PCRCMGR.XAUTH_IDEN_MENU_PK
  ENABLE VALIDATE);
  
  
-- XAUTH_MENU
CREATE TABLE PCRCMGR.XAUTH_MENU
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    MENU_ID                   VARCHAR2(40 CHAR)            NOT NULL,
    MENU_URL            VARCHAR2(150 CHAR),
    MENU_CNAME               VARCHAR2(150 CHAR)            NOT NULL,
    PARENT_ID            VARCHAR2(20 CHAR)            NOT NULL,
    SEQ_NO                   NUMBER                        NOT NULL,
    PARENT_SEQ               NUMBER                        NOT NULL,
    MEMO                   VARCHAR2(150 CHAR),
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_MENU IS '選單資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.MENU_ID IS '選單編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.MENU_URL IS '選單網址';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.MENU_CNAME IS '選單名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.PARENT_ID IS '上層選單編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.SEQ_NO IS '顯示順序';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.PARENT_SEQ IS '上層順序';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.MEMO IS '備註';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_MENU.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_MENU_PK ON PCRCMGR.XAUTH_MENU
(APP_ID, MENU_ID);

CREATE INDEX PCRCMGR.XAUTH_MENU_IDX01 ON PCRCMGR.XAUTH_MENU
(APP_ID, MENU_URL);


ALTER TABLE PCRCMGR.XAUTH_MENU ADD (
  CONSTRAINT XAUTH_MENU_PK
  PRIMARY KEY
  (APP_ID, MENU_ID)
  USING INDEX PCRCMGR.XAUTH_MENU_PK
  ENABLE VALIDATE);

 
-- XAUTH_ROLE
/*CREATE TABLE PCRCMGR.XAUTH_ROLE
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    ROLE_ID              VARCHAR2(20 CHAR)           NOT NULL,
    ROLE_CNAME           VARCHAR2(150 CHAR)          NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);*/
CREATE TABLE XAUTH_ROLE
(
  APP_ID         VARCHAR2(30 CHAR)                     NOT NULL,
  IDEN_ID        VARCHAR2(255 CHAR)             NOT NULL,
  ROLE_ID        VARCHAR2(255 CHAR)             NOT NULL,
  CATEGORY       VARCHAR2(30 CHAR),
  CREATEAGENTID  VARCHAR2(30 CHAR),
  CRE_DATE       DATE                   NOT NULL,
  CRE_USER       VARCHAR2(30 CHAR),
  --SERNO          NUMBER(19)                     NOT NULL,
  ISDEPTROLE     NUMBER(1)    DEFAULT 0         NOT NULL,
  "level"        NUMBER(10)   DEFAULT 0         NOT NULL,
  ROLE_CNAME     VARCHAR2(255 CHAR)             NOT NULL,
  STATUS         VARCHAR2(1 CHAR)   DEFAULT 1             NOT NULL,
  SYSID          VARCHAR2(30 CHAR),
  UPDATEAGENTID  VARCHAR2(30 CHAR),
  UPD_DATE       DATE                   ,
  UPD_USER       VARCHAR2(30 CHAR),
  PRIMARY KEY
 (APP_ID, IDEN_ID, ROLE_ID)
 -- CONSTRAINT UK_8HS39KTVSUGSTUF3VYOYPYF9O
--UNIQUE (SERNO)
);

COMMENT ON TABLE PCRCMGR.XAUTH_ROLE IS '角色資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.ROLE_ID IS '角色代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.ROLE_CNAME IS '角色名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE.UPD_DATE IS '異動日期';

--CREATE UNIQUE INDEX PCRCMGR.XAUTH_ROLE_PK ON PCRCMGR.XAUTH_ROLE
--(APP_ID, IDEN_ID, ROLE_ID);


--ALTER TABLE PCRCMGR.XAUTH_ROLE ADD (
--  CONSTRAINT XAUTH_ROLE_PK
--  PRIMARY KEY
--  (APP_ID, IDEN_ID, ROLE_ID)
--  USING INDEX PCRCMGR.XAUTH_ROLE_PK
--  ENABLE VALIDATE);


-- XAUTH_ROLE_MENU 
CREATE TABLE PCRCMGR.XAUTH_ROLE_MENU
(  
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    ROLE_ID              VARCHAR2(20 CHAR)           NOT NULL,  
    MENU_ID                   VARCHAR2(40 CHAR)            NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_ROLE_MENU IS '角色選單資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.MENU_ID IS '選單編號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.ROLE_ID IS '角色代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_MENU.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_ROLE_MENU_PK ON PCRCMGR.XAUTH_ROLE_MENU
(APP_ID, IDEN_ID, ROLE_ID, MENU_ID);


ALTER TABLE PCRCMGR.XAUTH_ROLE_MENU ADD (
  CONSTRAINT XAUTH_ROLE_MENU_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, ROLE_ID, MENU_ID)
  USING INDEX PCRCMGR.XAUTH_ROLE_MENU_PK
  ENABLE VALIDATE);


  

--CREATE UNIQUE INDEX PCRCMGR.XAUTH_ROLE_USER_PK ON PCRCMGR.XAUTH_ROLE_USER
--(APP_ID, IDEN_ID, ROLE_ID, USER_ID);


--ALTER TABLE PCRCMGR.XAUTH_ROLE_USER ADD (
-- CONSTRAINT XAUTH_ROLE_USER_PK
--  PRIMARY KEY
--  (APP_ID, IDEN_ID, ROLE_ID, USER_ID)
--  USING INDEX PCRCMGR.XAUTH_ROLE_USER_PK
--  ENABLE VALIDATE);

  
-- XAUTH_USERS  
/*CREATE TABLE PCRCMGR.XAUTH_USERS
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    USER_ID              VARCHAR2(30 CHAR)           NOT NULL,
    USER_PW              VARCHAR2(60 CHAR)           NOT NULL,
    USER_CNAME           VARCHAR2(60 CHAR)           NOT NULL,  
    USER_TYPE            VARCHAR2(2 CHAR)            DEFAULT '02' NOT NULL,
    ENABLED                   VARCHAR2(1 CHAR)            DEFAULT '1' NOT NULL,
    IS_FIRST            VARCHAR2(1 CHAR)            DEFAULT '1' NOT NULL,
    EMAIL                 VARCHAR2(50 CHAR),
    EMAIL_TOKEN           VARCHAR2(100 CHAR),
    EMAIL_EXPIRE          DATE,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);*/
CREATE TABLE PCRCMGR.XAUTH_USERS
(
  APP_ID            VARCHAR2(30 CHAR)            NOT NULL,
  IDEN_ID           VARCHAR2(255 CHAR)          NOT NULL,
  USER_ID           VARCHAR2(255 CHAR)          NOT NULL,
  BIRTHDAY          VARCHAR2(8 CHAR),
  CARDNO            VARCHAR2(50 CHAR),
  CREATEAGENTID     VARCHAR2(30 CHAR),
  CRE_DATE          TIMESTAMP(6)                NOT NULL,
  CRE_USER          VARCHAR2(30 CHAR),
  DUTYPLACE         VARCHAR2(50 CHAR),
  DUTYTYPESERNO     NUMBER(19),
  EMAIL             VARCHAR2(100 CHAR),
  EMAIL_EXPIRE      TIMESTAMP(6),
  EMAIL_TOKEN       VARCHAR2(255 CHAR),
  ENGNAME           VARCHAR2(50 CHAR),
  EXT               VARCHAR2(50 CHAR),
  GENDER            VARCHAR2(255 CHAR),
  IC2HRDATE         TIMESTAMP(6),
  IC6HRDATE         TIMESTAMP(6),
  ICHALFREALDATE    TIMESTAMP(6),
 -- SERNO             NUMBER(19)                  NOT NULL,
  IDCARDNO          VARCHAR2(20 CHAR),
  IS_FIRST          VARCHAR2(255 CHAR),
  JOBRANKSERNO      NUMBER(19),
  JOBTITLESERNO     NUMBER(19),
  JOBTYPESERNO      NUMBER(19),
  LEAVEDATE         VARCHAR2(8 CHAR),
  MOBILENO          VARCHAR2(20 CHAR),
  USER_CNAME        VARCHAR2(50 CHAR)           NOT NULL,
  ONBOARDDATE       VARCHAR2(8 CHAR),
  PHONE             VARCHAR2(50 CHAR),
  PREEMPCODE        VARCHAR2(255 CHAR),
  PREEMPDATE        TIMESTAMP(6),
  PWDHASH           VARCHAR2(200 CHAR)          NOT NULL,
  SENDEMAIL         VARCHAR2(5 CHAR),
  ENABLED            VARCHAR2(1 CHAR)            NOT NULL,
  SUBJOBTYPESERNO   NUMBER(19),
  SYSID             VARCHAR2(30 CHAR),
  SYSMEMO           VARCHAR2(50 CHAR),
  TRAININGORGSERNO  NUMBER(19),
  UPDATEAGENTID     VARCHAR2(30 CHAR),
  UPD_DATE          TIMESTAMP(6)              ,
  UPD_USER          VARCHAR2(30 CHAR),
  USER_PW           VARCHAR2(255 CHAR),
  USERSTATE         VARCHAR2(5 CHAR),
  USER_TYPE         VARCHAR2(20 CHAR)           NOT NULL,
  PRIMARY KEY
 (APP_ID, IDEN_ID, USER_ID)
 -- CONSTRAINT UK_PY04U1EFTJ7OPS2XULX9Y0BUX
 --UNIQUE (SERNO),
 -- CONSTRAINT FK212OY5G25TDY61DI9R95HDRJX 
-- FOREIGN KEY (APP_ID, IDEN_ID) 
 --REFERENCES XAUTH_DEPT (APP_ID,IDEN_ID)
);

COMMENT ON TABLE PCRCMGR.XAUTH_USERS IS '使用者資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.USER_ID IS '使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.USER_PW IS '使用者密碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.USER_CNAME IS '使用者姓名';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.USER_TYPE IS '使用者類型 00:系統管理員 01:管理員 02:使用者';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.ENABLED IS '是否啟用 0:停用 1:啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.IS_FIRST IS '是否第一次登入 0:否 1:是';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.EMAIL IS '電子郵件';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.EMAIL_TOKEN IS '電子郵件重新認証識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.EMAIL_EXPIRE IS '電子郵件重新認証到期日';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS.UPD_DATE IS '異動日期';

--CREATE UNIQUE INDEX PCRCMGR.XAUTH_USERS_PK ON PCRCMGR.XAUTH_USERS
--(APP_ID, IDEN_ID, USER_ID);


--ALTER TABLE PCRCMGR.XAUTH_USERS ADD (
--  CONSTRAINT XAUTH_USERS_PK
--  PRIMARY KEY
--  (APP_ID, IDEN_ID, USER_ID)
--  USING INDEX PCRCMGR.XAUTH_USERS_PK
 -- ENABLE VALIDATE);
  
-- XAUTH_USERS_PWD_HISTORY
CREATE TABLE PCRCMGR.XAUTH_USERS_PWD_HISTORY
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    USER_ID              VARCHAR2(30 CHAR)           NOT NULL,
    USER_PW              VARCHAR2(60 CHAR)           NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_USERS_PWD_HISTORY IS '使用者密碼歷史檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.USER_ID IS '使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.USER_PW IS '使用者密碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_PWD_HISTORY.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_USERS_PWD_HISTORY_PK ON PCRCMGR.XAUTH_USERS_PWD_HISTORY
(APP_ID, IDEN_ID, USER_ID, CRE_DATE);


ALTER TABLE PCRCMGR.XAUTH_USERS_PWD_HISTORY ADD (
  CONSTRAINT XAUTH_USERS_PWD_HISTORY_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, USER_ID, CRE_DATE)
  USING INDEX PCRCMGR.XAUTH_USERS_PWD_HISTORY_PK
  ENABLE VALIDATE);
  
-- XAUTH_USERS_TOKEN
CREATE TABLE PCRCMGR.XAUTH_USERS_TOKEN 
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID               VARCHAR2(20 CHAR)           NOT NULL,  
    USER_ID               VARCHAR2(30 CHAR)           NOT NULL,
    TOKEN                VARCHAR2(100 CHAR)            NOT NULL,
    ACTIVE_DATE            DATE                        NOT NULL,
    CRE_USER              VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE              DATE                        NOT NULL,
    UPD_USER              VARCHAR2(50 CHAR),
    UPD_DATE              DATE
);

COMMENT ON TABLE PCRCMGR.XAUTH_USERS_TOKEN IS '使用者登入識別檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.USER_ID IS '使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.TOKEN IS 'TOKEN';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.ACTIVE_DATE IS '啟用日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_TOKEN.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_USERS_TOKEN_PK ON PCRCMGR.XAUTH_USERS_TOKEN
(APP_ID, IDEN_ID, USER_ID);


ALTER TABLE PCRCMGR.XAUTH_USERS_TOKEN ADD (
  CONSTRAINT XAUTH_USERS_TOKEN_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, USER_ID)
  USING INDEX PCRCMGR.XAUTH_USERS_TOKEN_PK
  ENABLE VALIDATE);  
  
-- XAUTH_IP_GRANT  
CREATE TABLE PCRCMGR.XAUTH_IP_GRANT
(
    APP_ID                VARCHAR2(30 CHAR)                    NOT NULL,
    IDEN_ID                VARCHAR2(20 CHAR)                   NOT NULL,
    IP_ADDR                VARCHAR2(50 CHAR)                     NOT NULL,
    SYS_TYPE            VARCHAR2(20 CHAR)                    NOT NULL,
    ENABLED                VARCHAR2(1 CHAR)                    DEFAULT '1' NOT NULL,    
    MEMO                   VARCHAR2(150 CHAR),
    CRE_USER             VARCHAR2(50 CHAR)                   NOT NULL,
    CRE_DATE             DATE                                NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_IP_GRANT IS 'IP授權資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.IP_ADDR IS '網路IP位址';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.SYS_TYPE IS '系統別 REF:XAUTH_SYS_CODE.GP';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.ENABLED IS '是否啟用 0:停用 1:啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.MEMO IS '備註';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_IP_GRANT.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_IP_GRANT_PK ON PCRCMGR.XAUTH_IP_GRANT
(APP_ID, IDEN_ID, IP_ADDR, SYS_TYPE);


ALTER TABLE PCRCMGR.XAUTH_IP_GRANT ADD (
  CONSTRAINT XAUTH_IP_GRANT_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, IP_ADDR, SYS_TYPE)
  USING INDEX PCRCMGR.XAUTH_IP_GRANT_PK
  ENABLE VALIDATE);
  
-- XAUTH_SYS_CODE 
CREATE TABLE PCRCMGR.XAUTH_SYS_CODE
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID                VARCHAR2(20 CHAR)           NOT NULL,
    GP                     VARCHAR2(20 CHAR)           NOT NULL,
    CODE                   VARCHAR2(15 CHAR)           NOT NULL,
    CNAME                  VARCHAR2(150 CHAR)          NOT NULL,
    ENAME                  VARCHAR2(150 CHAR),
    ORDER_SEQ              NUMBER,
    ENABLED                VARCHAR2(1 CHAR)            DEFAULT '1' NOT NULL,
    BGN_DATE               DATE                        NOT NULL,
    END_DATE               DATE,
    MEMO                   VARCHAR2(150 CHAR),
    C01                    VARCHAR2(150 CHAR),
    C02                    VARCHAR2(150 CHAR),
    C03                    VARCHAR2(150 CHAR),
    C04                    VARCHAR2(150 CHAR),
    C05                    VARCHAR2(150 CHAR),
    CRE_USER               VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE               DATE                        NOT NULL,
    UPD_USER               VARCHAR2(50 CHAR),
    UPD_DATE               DATE
);

COMMENT ON TABLE PCRCMGR.XAUTH_SYS_CODE IS '系統代碼檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.GP IS '群組';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.CODE IS '代碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.CNAME IS '中文名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.ENAME IS '英文名稱';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.ORDER_SEQ IS '顯示順序';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.ENABLED IS '是否啟用 0:停用 1:啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.BGN_DATE IS '起始日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.END_DATE IS '截止日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.MEMO IS '備註';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.C01 IS '擴充欄位1';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.C02 IS '擴充欄位2';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.C03 IS '擴充欄位3';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.C04 IS '擴充欄位4';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.C05 IS '擴充欄位5';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_SYS_CODE.UPD_DATE IS '異動日期';


CREATE UNIQUE INDEX PCRCMGR.XAUTH_SYS_CODE_PK ON PCRCMGR.XAUTH_SYS_CODE
(APP_ID, IDEN_ID, GP, CODE);


ALTER TABLE PCRCMGR.XAUTH_SYS_CODE ADD (
  CONSTRAINT XAUTH_SYS_CODE_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, GP, CODE)
  USING INDEX PCRCMGR.XAUTH_SYS_CODE_PK
  ENABLE VALIDATE);
  
-- XAUTH_USERS_SECRET
CREATE TABLE PCRCMGR.XAUTH_USERS_SECRET
(  
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    USER_ID                    VARCHAR2(30 CHAR)            NOT NULL,
    TOKEN                    VARCHAR2(300 CHAR)            NOT NULL,
    EXPIRE_DATE               DATE                        NOT NULL,
    PUBLIC_KEY               VARCHAR2(1500 CHAR)            NOT NULL,
    PRIVATE_KEY               VARCHAR2(1500 CHAR)            NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);

COMMENT ON TABLE PCRCMGR.XAUTH_USERS_SECRET IS '使用者密鑰檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.USER_ID IS '使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.TOKEN IS 'TOKEN';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.EXPIRE_DATE IS '到期日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.PUBLIC_KEY IS '公鑰';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.PRIVATE_KEY IS '私鑰';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_USERS_SECRET.UPD_DATE IS '異動日期';

CREATE UNIQUE INDEX PCRCMGR.XAUTH_USERS_SECRET_PK ON PCRCMGR.XAUTH_USERS_SECRET
(APP_ID, IDEN_ID, USER_ID);


ALTER TABLE PCRCMGR.XAUTH_USERS_SECRET ADD (
  CONSTRAINT XAUTH_USERS_SECRET_PK
  PRIMARY KEY
  (APP_ID, IDEN_ID, USER_ID)
  USING INDEX PCRCMGR.XAUTH_USERS_SECRET_PK
  ENABLE VALIDATE);
  
-- XAUTH_ROLE_USER  
/*CREATE TABLE PCRCMGR.XAUTH_ROLE_USER
(
    APP_ID                VARCHAR2(30 CHAR)            NOT NULL,
    IDEN_ID              VARCHAR2(20 CHAR)           NOT NULL,
    ROLE_ID              VARCHAR2(20 CHAR)           NOT NULL,
    USER_ID              VARCHAR2(30 CHAR)           NOT NULL,
    CRE_USER             VARCHAR2(50 CHAR)           NOT NULL,
    CRE_DATE             DATE                        NOT NULL,
    UPD_USER             VARCHAR2(50 CHAR),
    UPD_DATE             DATE                         
);*/
CREATE TABLE PCRCMGR.XAUTH_ROLE_USER
(
  APP_ID         VARCHAR2(30 CHAR)              NOT NULL,
  IDEN_ID        VARCHAR2(255 CHAR)             NOT NULL,
  ROLE_ID        VARCHAR2(255 CHAR)             NOT NULL,
  USER_ID        VARCHAR2(255 CHAR)             NOT NULL,
  CREATEAGENTID  VARCHAR2(30 CHAR),
  CRE_DATE       DATE                    NOT NULL,
  CRE_USER       VARCHAR2(30 CHAR),
  UPD_DATE       DATE                    ,
  UPD_USER       VARCHAR2(30 CHAR),
  --SERNO          NUMBER(19)                     NOT NULL,
  PRIMARY KEY
 (APP_ID, IDEN_ID, USER_ID, ROLE_ID)
 -- CONSTRAINT UK_GDX61NY4DOY2ONOB3IQKNYKY6
 --UNIQUE (SERNO),
  --CONSTRAINT FKDBX7HCQLMHI1NGABHLQGV1CKP 
 --FOREIGN KEY (APP_ID, IDEN_ID) 
-- REFERENCES XAUTH_DEPT (APP_ID,IDEN_ID),
 -- CONSTRAINT FKDVR5RXYW487ON194XEIMW3PJ3 
 --FOREIGN KEY (APP_ID, IDEN_ID, USER_ID) 
 --REFERENCES XAUTH_USERS (APP_ID,IDEN_ID,USER_ID),
  --CONSTRAINT FKH08D7SC4SPLBK59PR2CXAX2N0 
 --FOREIGN KEY (APP_ID, IDEN_ID, ROLE_ID) 
-- REFERENCES XAUTH_ROLE (APP_ID,IDEN_ID,ROLE_ID)
);


COMMENT ON TABLE PCRCMGR.XAUTH_ROLE_USER IS '角色與使用者對應檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.ROLE_ID IS '角色代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.USER_ID IS '使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_USER.UPD_DATE IS '異動日期';
   
  
-- XAUTH_ROLE_AGENT_USER  
/*CREATE TABLE PCRCMGR.XAUTH_ROLE_AGENT_USER (
    APP_ID            VARCHAR2(30 CHAR)        NOT NULL,
    IDEN_ID         VARCHAR2(20 CHAR)       NOT NULL,  
    ROLE_ID         VARCHAR2(20 CHAR)       NOT NULL,
    AGENT_APP_ID    VARCHAR2(30 CHAR)        NOT NULL,
    AGENT_IDEN_ID   VARCHAR2(20 CHAR)       NOT NULL,
    AGENT_USER_ID   VARCHAR2(30 CHAR)       NOT NULL,
    ENABLED               VARCHAR2(1 CHAR)        DEFAULT '1' NOT NULL,
    CRE_USER        VARCHAR2(50 CHAR)       NOT NULL,
    CRE_DATE        DATE                       NOT NULL,
    UPD_USER        VARCHAR2(50 CHAR),
    UPD_DATE        DATE
);*/
CREATE TABLE PCRCMGR.XAUTH_ROLE_AGENT_USER
(
  APP_ID         VARCHAR2(30 CHAR)              NOT NULL,
  IDEN_ID        VARCHAR2(255 CHAR)             NOT NULL,
  USER_ID        VARCHAR2(255 CHAR)             NOT NULL,
  AGENT_USER_ID  VARCHAR2(255 CHAR)             NOT NULL,
  AGENT_IDEN_ID  VARCHAR2(255 CHAR),
  AGENT_APP_ID   VARCHAR2(30 CHAR) ,
  BEGDATE        TIMESTAMP(6)                   ,
  CREATEAGENTID  VARCHAR2(30 CHAR),
  CRE_DATE       TIMESTAMP(6)                   NOT NULL,
  CRE_USER       VARCHAR2(30 CHAR),
  ENDDATE        TIMESTAMP(6)                   ,
 --SERNO          NUMBER(19)                     NOT NULL,
  ROLE_ID        VARCHAR2(255 CHAR),
  ENABLED         VARCHAR2(1 CHAR)               NOT NULL,
  UPDATEAGENTID  VARCHAR2(30 CHAR),
  UPD_DATE       TIMESTAMP(6)                   ,
  UPD_USER       VARCHAR2(30 CHAR),
  PRIMARY KEY
 (APP_ID, IDEN_ID, USER_ID),
--  CONSTRAINT UK_FVFV09QF5HQNFYRENLTPD0QGD
-- UNIQUE (SERNO),
  CONSTRAINT FKBDX2L4XCO53WMDD4T2BWQ0FXS 
 FOREIGN KEY (APP_ID, IDEN_ID, USER_ID) 
 REFERENCES XAUTH_USERS (APP_ID,IDEN_ID,USER_ID),
  CONSTRAINT FKBTKEGLB7V4IK95RKFVFSIOH05 
 FOREIGN KEY (AGENT_APP_ID, AGENT_IDEN_ID, AGENT_USER_ID) 
 REFERENCES XAUTH_USERS (APP_ID,IDEN_ID,USER_ID)
);

COMMENT ON TABLE PCRCMGR.XAUTH_ROLE_AGENT_USER IS '角色代理人資料檔';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.APP_ID IS '系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.IDEN_ID IS '識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.ROLE_ID IS '角色代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.AGENT_APP_ID IS '代理系統代號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.AGENT_IDEN_ID IS '代理識別碼';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.AGENT_USER_ID IS '代理使用者帳號';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.ENABLED IS '是否啟用 0:停用 1:啟用';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.CRE_USER IS '建立人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.CRE_DATE IS '建立日期';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.UPD_USER IS '異動人員';

COMMENT ON COLUMN PCRCMGR.XAUTH_ROLE_AGENT_USER.UPD_DATE IS '異動日期';

--CREATE UNIQUE INDEX PCRCMGR.XAUTH_ROLE_AGENT_USER_PK ON PCRCMGR.XAUTH_ROLE_AGENT_USER
--(APP_ID, IDEN_ID, ROLE_ID, AGENT_APP_ID, AGENT_IDEN_ID, AGENT_USER_ID);


--ALTER TABLE PCRCMGR.XAUTH_ROLE_AGENT_USER ADD (
--  CONSTRAINT XAUTH_ROLE_AGENT_USER_PK
--  PRIMARY KEY
--  (APP_ID, IDEN_ID, ROLE_ID, AGENT_APP_ID, AGENT_IDEN_ID, AGENT_USER_ID)
 -- USING INDEX PCRCMGR.XAUTH_ROLE_AGENT_USER_PK
--  ENABLE VALIDATE);
  
COMMIT;    