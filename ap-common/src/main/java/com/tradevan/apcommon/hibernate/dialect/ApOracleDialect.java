package com.tradevan.apcommon.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;

/**
 * Title: ApOracleDialect<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class ApOracleDialect extends Oracle10gDialect {

	public ApOracleDialect() {
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
		registerColumnType( Types.VARCHAR, "nvarchar2($1)" );
	    registerColumnType( Types.CLOB, "nclob" );
	    registerColumnType( Types.NCLOB, "nclob" );
	}

}
