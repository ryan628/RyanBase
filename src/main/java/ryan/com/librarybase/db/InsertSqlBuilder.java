package ryan.com.librarybase.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 18:01.
 */

import org.apache.http.NameValuePair;

import java.lang.reflect.Field;

public class InsertSqlBuilder extends SqlBuilder {
    @Override
    public void onPreGetStatement() throws DBException,
            IllegalArgumentException, IllegalAccessException {
        // TODO Auto-generated method stub
        if (getUpdateFields() == null) {
            setUpdateFields(getFieldsAndValue(entity));
        }
        super.onPreGetStatement();
    }

    @Override
    public String buildSql() throws DBException, IllegalArgumentException,
            IllegalAccessException {
        // TODO Auto-generated method stub
        StringBuilder columns = new StringBuilder(256);
        StringBuilder values = new StringBuilder(256);
        columns.append("INSERT INTO ");
        columns.append(tableName).append(" (");
        values.append("(");
        ArrayList updateFields = getUpdateFields();
        if (updateFields != null) {
            for (int i = 0; i < updateFields.size(); i++) {
                NameValuePair nameValuePair = updateFields.get(i);
                columns.append(nameValuePair.getName());
                String value = nameValuePair.getValue().toString();
                value = value != "" ? value.replace("'", "''") : "";
                values.append(StringUtils
                        .isNumeric(value != null ? value : "") ? value : "'" + value + "'");
                if (i + 1 < updateFields.size()) {
                    columns.append(", ");
                    values.append(", ");
                }
            }
        } else {
            throw new DBException("插入数据有误！");
        }
        columns.append(") values ");
        values.append(")");
        columns.append(values);
        return columns.toString();
    }

    /**
     * 从实体加载,更新的数据
     *
     * @return
     * @throws DBException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static ArrayList getFieldsAndValue(Object entity)
            throws DBException, IllegalArgumentException,
            IllegalAccessException {
        // TODO Auto-generated method stub
        ArrayList arrayList = new ArrayList();
        if (entity == null) {
            throw new DBException("没有加载实体类！");
        }
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (!DBUtils.isTransient(field)) {
                if (DBUtils.isBaseDateType(field)) {
                    PrimaryKey annotation = field
                            .getAnnotation(PrimaryKey.class);
                    if (annotation != null && annotation.autoIncrement()) {

                    } else {
                        String columnName = DBUtils.getColumnByField(field);
                        field.setAccessible(true);
                        arrayList
                                .add((columnName != null && !columnName
                                                .equals("")) ? columnName : field
                                                .getName(),
                                        field.get(entity) == null ? null
                                                : field.get(entity).toString());
                    }

                }
            }
        }
        return arrayList;
    }
}
