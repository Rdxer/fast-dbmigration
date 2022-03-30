package com.rdxer.db.migration;

public class Config {
    public enum DBType {
        mysql, pgsql, other
    }


    public static Config of(DBType dbType) {
        Config config = new Config(dbType);
        return config;
    }


    public Config(DBType dbType) {
        this.dbType = dbType;
    }

    public Config(DBType dbType, String find_table, String DDL) {
        this.dbType = dbType;
        this.find_table = find_table;
        this.DDL = DDL;
    }

    public Config(DBType dbType, String table_name, String find_table, String DDL) {
        this.dbType = dbType;
        this.table_name = table_name;
        this.find_table = find_table;
        this.DDL = DDL;
    }

    /**
     * pgsql mysql , default mysql
     */
//    @Value("${db.migration.dbType}")
    public DBType dbType;

    //    @Value("${db.migration.table_name}")
    public String table_name = "m_migration_history" ;


    //    @Value("${db.migration.find_table}")
    public String find_table;
    //    @Value("${db.migration.DDL}")
    public String DDL;

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFind_table() {
        return find_table;
    }

    public void setFind_table(String find_table) {
        this.find_table = find_table;
    }

    public String getDDL() {
        return DDL;
    }

    public void setDDL(String DDL) {
        this.DDL = DDL;
    }
}
