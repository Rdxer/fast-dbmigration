package com.rdxer.db.migration;

public class Constant {

//    public static String find_table = Constant.find_table_pgsql;
//    public static String DDL = Constant.DDL_PGSQL;
//    public static String table_name = "m_migration_history" ;

    public static final String DDL_PGSQL = "create table %s\n" +
            "(\n" +
            "    id         integer not null\n" +
            "        constraint table_name_pk\n" +
            "            primary key,\n" +
            "    version    text,\n" +
            "    execdate bigint\n" +
            ");\n" +
            "\n" +
            "comment on table m_migration_history is '迁移记录表';\n" +
            "comment on column m_migration_history.id is '序号';\n";

    public static final String DDL_MYSQL = "create table %s\n" +
            "(\n" +
            "    id       int      null,\n" +
            "    version  text     null,\n" +
            "    execdate long null\n" +
            ")engine=InnoDB\n" +
            "comment '迁移执行记录表';\n";
    public static String find_table_mysql = "show tables like '%s';";
    public static String find_table_pgsql = "select * from pg_tables where schemaname = 'public' and tablename = '%s';\n";


}
