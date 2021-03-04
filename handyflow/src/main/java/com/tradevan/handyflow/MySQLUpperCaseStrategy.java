package com.tradevan.handyflow;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;



public class MySQLUpperCaseStrategy extends PhysicalNamingStrategyStandardImpl {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
 
        String tableName = name.getText().toUpperCase();
        return name.toIdentifier(tableName);
    }
 
}