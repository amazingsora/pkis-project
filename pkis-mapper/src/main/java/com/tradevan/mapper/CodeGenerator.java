package com.tradevan.mapper;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {
	
	static String[] XAUTH_TABLES = new String[] {
			"XAUTH_APPLICATION", "XAUTH_DEPT", "XAUTH_IDEN_MENU",
			"XAUTH_IP_GRANT", "XAUTH_MENU", "XAUTH_ROLE", "XAUTH_ROLE_MENU", "XAUTH_ROLE_USER", "XAUTH_ROLE_AGENT_USER",
			"XAUTH_SYS_CODE", "XAUTH_USERS", "XAUTH_USERS_PWD_HISTORY", "XAUTH_USERS_SECRET", "XAUTH_USERS_TOKEN"};
	
	public static void main(String[] args) {
		AutoGenerator mpg = new AutoGenerator();
		
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("system");
        gc.setOpen(false);
        
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setDateType(DateType.ONLY_DATE);

        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:oracle:thin:@//172.31.70.50:1527/T07B");
        dsc.setSchemaName("PCRCMGR");
        dsc.setDriverName("oracle.jdbc.OracleDriver");
        dsc.setUsername("PCRCMGR");
        dsc.setPassword("bJeGZPUQrE");
        dsc.setTypeConvert(new DbConvert());
        mpg.setDataSource(dsc);
        
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("pkis");
        pc.setParent("com.tradevan.mapper");
        pc.setEntity("model");
        pc.setMapper("dao");
        mpg.setPackageInfo(pc);
        
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        String templatePath = "/templates/mapper.xml.vm";
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        
        TemplateConfig templateConfig = new TemplateConfig();        
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setController(null);
        templateConfig.setServiceImpl(null);
        mpg.setTemplate(templateConfig);

        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(false);        
//        strategy.setExclude(XAUTH_TABLES);
        strategy.setInclude("DOCSTATE");
        strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);
        mpg.execute();
	}
	
}

class DbConvert implements ITypeConvert {

	@Override
	public IColumnType processTypeConvert(GlobalConfig globalConfig, String s) {
		String t = s.toUpperCase();
        if (t.contains("CHAR")) {
            return DbColumnType.STRING;
        } 
        else if (t.contains("DATE")) {
            return DbColumnType.DATE;            
        } 
        else if (t.contains("TIMESTAMP")) {
            return DbColumnType.TIMESTAMP;            
        } 
        else if (t.contains("NUMBER")) {
            if (t.matches("NUMBER\\(+\\d\\)")) {
                return DbColumnType.INTEGER;
            } else if (t.matches("NUMBER\\(+\\d{2}+\\)")) {
                return DbColumnType.LONG;
            }
            return DbColumnType.INTEGER;
        } 
        else if (t.contains("FLOAT")) {
            return DbColumnType.FLOAT;
        } 
        else if (t.contains("clob")) {
            return DbColumnType.STRING;
        } 
        else if (t.contains("BLOB")) {
            return DbColumnType.OBJECT;
        } 
        else if (t.contains("binary")) {
            return DbColumnType.BYTE_ARRAY;
        } 
        else if (t.contains("RAW")) {
            return DbColumnType.BYTE_ARRAY;
        }
        else if (t.contains("DECIMAL")) {
            return DbColumnType.LONG;
        }
        return DbColumnType.STRING;
	}
	
}