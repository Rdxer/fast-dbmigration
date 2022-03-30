package com.rdxer.db.migration;

public class Constant {

//    public static String find_table = Constant.find_table_pgsql;
//    public static String DDL = Constant.DDL_PGSQL;
//    public static String table_name = "m_migration_history" ;

    public static final String DDL_PGSQL = """
            create table %s
            (
                id         integer not null
                    constraint table_name_pk
                        primary key,
                version    text,
                execdate bigint
            );
                        
            comment on table m_migration_history is '迁移记录表';
            comment on column m_migration_history.id is '序号';
                        """; // .formatted(table_name)
    public static final String DDL_MYSQL = """
            create table %s
            (
                id       int      null,
                version  text     null,
                execdate long null
            )engine=InnoDB
            comment '迁移执行记录表';
            """;
    public static String find_table_mysql = "show tables like '%s';" ;
    public static String find_table_pgsql = """
                  select * from pg_tables where schemaname = 'public' and tablename = '%s';  
            """;


}
