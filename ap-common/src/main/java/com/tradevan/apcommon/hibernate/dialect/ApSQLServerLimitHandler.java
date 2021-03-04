package com.tradevan.apcommon.hibernate.dialect;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.dialect.pagination.SQLServer2005LimitHandler;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.internal.util.StringHelper;

public class ApSQLServerLimitHandler extends SQLServer2005LimitHandler {
	
	private static final String SELECT = "select";
	private static final String SELECT_WITH_SPACE = SELECT + ' ';
	private static final String FROM = "from";
	private static final String DOT = ".";
	private static final String SPACE = " ";
	
	private static final Pattern ALIAS_PATTERN = Pattern.compile( "(?i)\\sas\\s(.)+$" );
	private static final String NON_COLUMN_NAME = "(.*)\\((.*)|(.*)\\)(.*)";
	
	public ApSQLServerLimitHandler(String sql, RowSelection selection) {
		//super(sql, selection);
	}
	
	protected String fillAliasInSelectClause(StringBuilder sb) {
		final List<String> aliases = new LinkedList<String>();
		final int startPos = shallowIndexOf( sb, SELECT_WITH_SPACE, 0 );
		int endPos = shallowIndexOfWord( sb, FROM, startPos );
		int nextComa = startPos;
		int prevComa = startPos;
		int unique = 0;
		boolean selectsMultipleColumns = false;

		while ( nextComa != -1 ) {
			prevComa = nextComa;
			nextComa = shallowIndexOf( sb, ",", nextComa );
			if ( nextComa > endPos ) {
				break;
			}
			if ( nextComa != -1 ) {
				final String expression = sb.substring( prevComa, nextComa );
				if ( selectsMultipleColumns( expression ) ) {
					selectsMultipleColumns = true;
				}
				else {
					String alias = getAlias( expression );
					if ( alias == null ) {
						// Inserting alias. It is unlikely that we would have to add alias, but just in case.
						alias = getColumnName(expression);
						if (alias.matches(NON_COLUMN_NAME)) {
							alias = StringHelper.generateAlias( "page", unique );
							++unique;
						}
					}
					aliases.add( alias );
				}
				++nextComa;
			}
		}
		// Processing last column.
		// Refreshing end position, because we might have inserted new alias.
		endPos = shallowIndexOfWord( sb, FROM, startPos );
		final String expression = sb.substring( prevComa, endPos );
		if ( selectsMultipleColumns( expression ) ) {
			selectsMultipleColumns = true;
		}
		else {
			String alias = getAlias( expression );
			if ( alias == null ) {
				// Inserting alias. It is unlikely that we would have to add alias, but just in case.
				alias = getColumnName(expression);
				if (alias.matches(NON_COLUMN_NAME)) {
					alias = StringHelper.generateAlias( "page", unique );
					++unique;
				}
			}
			aliases.add( alias );
		}

		// In case of '*' or '{table}.*' expressions adding an alias breaks SQL syntax, returning '*'.
		return selectsMultipleColumns ? "*" : StringHelper.join( ", ", aliases.iterator() );
	}
	
	private boolean selectsMultipleColumns(String expression) {
		final String lastExpr = expression.trim().replaceFirst( "(?i)(.)*\\s", "" );
		return "*".equals( lastExpr ) || lastExpr.endsWith( ".*" );
	}
	
	private String getAlias(String expression) {
		final Matcher matcher = ALIAS_PATTERN.matcher( expression );
		if ( matcher.find() ) {
			// Taking advantage of Java regular expressions greedy behavior while extracting the last AS keyword.
			// Note that AS keyword can appear in CAST operator, e.g. 'cast(tab1.col1 as varchar(255)) as col1'.
			return matcher.group( 0 ).replaceFirst( "(?i)(.)*\\sas\\s", "" ).trim();
		}
		return null;
	}
	
	private String getColumnName(String expression) {
		String expr = expression.trim();
		int idxSpace = expr.lastIndexOf(SPACE);
		int idxDot = expr.indexOf(DOT);
		if (idxSpace != -1){
			expr=  expr.substring(idxSpace + 1);
			idxDot = expr.indexOf(DOT);
			if (idxDot != -1) {
				return expr.substring(idxDot + 1);
			}
			else {
				return expr;
			}
		}
		else if (idxDot != -1) {
			return expr.substring(idxDot + 1);
		}
		else {
			return expr;
		}
	}
	
	private static int shallowIndexOfWord(final StringBuilder sb, final String search, int fromIndex) {
		final int index = shallowIndexOf( sb, ' ' + search + ' ', fromIndex );
		// In case of match adding one because of space placed in front of search term.
		return index != -1 ? ( index + 1 ) : -1;
	}
	
	private static int shallowIndexOf(StringBuilder sb, String search, int fromIndex) {
		// case-insensitive match
		final String lowercase = sb.toString().toLowerCase(Locale.ROOT);
		final int len = lowercase.length();
		final int searchlen = search.length();
		int pos = -1;
		int depth = 0;
		int cur = fromIndex;
		do {
			pos = lowercase.indexOf( search, cur );
			if ( pos != -1 ) {
				for ( int iter = cur; iter < pos; iter++ ) {
					final char c = sb.charAt( iter );
					if ( c == '(' ) {
						depth = depth + 1;
					}
					else if ( c == ')' ) {
						depth = depth - 1;
					}
				}
				cur = pos + searchlen;
			}
		} while ( cur < len && depth != 0 && pos != -1 );
		return depth == 0 ? pos : -1;
	}

}
