package ryan.com.librarybase.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 17:59.
 */


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class TableInfofactory implements Serializable {
    /**
     * 表名为键，表信息为值的HashMap
     */
    private static final java.util.HashMap<String, TableInfoEntity> tableInfoEntityMap = new java.util.HashMap<String, TableInfoEntity>();

    private TableInfofactory() {

    }

    private static TableInfofactory instance;

    /**
     * 获得数据库表工厂
     *
     * @return 数据库表工厂
     */
    public static TableInfofactory getInstance() {
        if (instance == null) {
            instance = new TableInfofactory();
        }
        return instance;
    }

    /**
     * 获得表信息
     *
     * @param clazz 实体类型
     * @return 表信息
     * @throws DBException
     */
    public TableInfoEntity getTableInfoEntity(Class<?> clazz)
            throws DBException {
        if (clazz == null)
            throw new DBException("表信息获取失败，应为class为null");
        TableInfoEntity tableInfoEntity = tableInfoEntityMap.get(clazz
                .getName());
        if (tableInfoEntity == null) {
            tableInfoEntity = new TableInfoEntity();
            tableInfoEntity.setTableName(DBUtils.getTableName(clazz));
            tableInfoEntity.setClassName(clazz.getName());
            Field idField = DBUtils.getPrimaryKeyField(clazz);
            if (idField != null) {
                PKProperyEntity pkProperyEntity = new PKProperyEntity();
                pkProperyEntity.setColumnName(DBUtils
                        .getColumnByField(idField));
                pkProperyEntity.setName(idField.getName());
                pkProperyEntity.setType(idField.getType());
                pkProperyEntity.setAutoIncrement(DBUtils
                        .isAutoIncrement(idField));
                tableInfoEntity.setPkProperyEntity(pkProperyEntity);
            } else {
                tableInfoEntity.setPkProperyEntity(null);
            }
            List<PropertyEntity> propertyList = DBUtils
                    .getPropertyList(clazz);
            if (propertyList != null) {
                tableInfoEntity.setPropertieArrayList(propertyList);
            }

            tableInfoEntityMap.put(clazz.getName(), tableInfoEntity);
        }
        if (tableInfoEntity == null
                || tableInfoEntity.getPropertieArrayList() == null
                || tableInfoEntity.getPropertieArrayList().size() == 0) {
            throw new DBException("不能创建+" + clazz + "的表信息");
        }
        return tableInfoEntity;
    }
}
