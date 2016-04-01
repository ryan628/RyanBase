package ryan.com.librarybase.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 18:02.
 */

import android.text.TextUtils;

import java.util.regex.Pattern;

public class QuerySqlBuilder extends SqlBuilder {
    protected Pattern sLimitPattern = Pattern
            .compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");

    @Override
    public String buildSql() throws DBException, IllegalArgumentException,
            IllegalAccessException {
        // TODO Auto-generated method stub
        return buildQueryString();
    }

    /**
     * 创建查询的字段
     */
    public String buildQueryString() {
        if (TextUtils.isEmpty(groupBy) && !TextUtils.isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!TextUtils.isEmpty(limit)
                && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);
        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        query.append("* ");
        query.append("FROM ");
        query.append(tableName);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }
}
