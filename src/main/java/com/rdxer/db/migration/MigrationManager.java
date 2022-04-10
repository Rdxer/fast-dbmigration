package com.rdxer.db.migration;

import com.rdxer.db.migration.core.AbstractMigration;
import com.rdxer.db.migration.model.MigrationHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MigrationManager {


    // 默认jdbc
    private final JdbcTemplate jdbcTemplate;
    // 资源加载器
    private ResourceLoader resourceLoader;
    // 迁移 数组
    private final List<AbstractMigration> migrationList = new ArrayList<>();
    // 需要迁移的数组
    private final List<AbstractMigration> needExecMigrationList = new ArrayList<>();
    // 最后一个ID
    private int lastId;
    // 事务支持的 Template
    private TransactionTemplate transactionTemplate;
    // 配置文件
    private Config config;

    private String findTableSQL;
    private String migrationDDL;

    // 获取当前类路径下的 res
    // this.getClass().getResource("/dbv/sql.sql")

    @Autowired
    public MigrationManager(ResourceLoader resourceLoader,
                            TransactionTemplate transactionTemplate,
                            JdbcTemplate jdbcTemplate
    ) {
        this.resourceLoader = resourceLoader;
        this.transactionTemplate = transactionTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 注册 迁移操作
     */
    public MigrationManager registerMigration(AbstractMigration... migrationList) {
        this.migrationList.addAll(Arrays.stream(migrationList).collect(Collectors.toList()));
        return this;
    }

    public void run() {
        transactionTemplate.execute(status -> {
            if (isNeedMigration(status)) {
                System.out.println("执行迁移数量为: " + needExecMigrationList.size());
                execMigration(needExecMigrationList, status);
            } else {
                System.out.println("已经是最新版本，无需执行迁移操作。");
            }
            return null;
        });
    }


    // 判断是否需不需要执行迁移操作 需要执行迁移操作的将迁移对象挪到 待操作数组中
    private boolean isNeedMigration(TransactionStatus status) {

        // 1. 判断是否存在表
        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(findTableSQL);

        // 1.1 不存在则创建
        if (queryForList.size() == 0) {
            System.out.println("执行创建迁移版本记录表 >>");
            jdbcTemplate.execute(migrationDDL);
            System.out.println("执行创建迁移版本记录表 <<");
        }

        // 2. 查询表数据中版本的记录

        List<MigrationHistory> dbHistoryList = jdbcTemplate.query(String.format("select * from %s order by id", config.table_name), new MigrationHistory());
        this.lastId = dbHistoryList.size();

        // 4. 采集注册的 迁移信息，与数据库的作为比对，迁移的记录数量。
        // 注册进来的 不能少于 数据库存在的，
        if (dbHistoryList.size() > migrationList.size()) {
            throw new RuntimeException("数据库迁移部分出错，已经迁移的版本比未迁移版本多，请检查待迁移的版本");
        }

        // 5. 需要进行执行的迁移操作添加到 需要执行的迁移数组中
        for (int i = 0; i < migrationList.size(); i++) {
            AbstractMigration migration = migrationList.get(i);

            if (!StringUtils.hasText(migration.getVersionName())) {
                throw new RuntimeException("版本名称不能为空");
            }

            if (dbHistoryList.size() <= i) {
                // 已经比对完了 需要执行的 迁移 添加到数组
                needExecMigrationList.add(migration);
            } else {
                // 待比对
                var dbHistory = dbHistoryList.get(i);
                if (!dbHistory.getVersion().equals(migration.getVersionName())) {
                    // 如果不相等
                    throw new RuntimeException("迁移版本不匹配，请检查迁移版本 数据库版本=>" + dbHistory.getVersion());
                }
            }
        }

        return needExecMigrationList.size() > 0;
    }

    /**
     * 执行迁移
     *
     * @param migrationList 待迁移数组
     * @param status        事务
     */
    protected void execMigration(List<AbstractMigration> migrationList, TransactionStatus status) {
        for (AbstractMigration migration : migrationList) {
            if (migration.init(this, status)) {
                System.out.printf("数据库执行迁移开始 >> ID(%d) version(%s)%n", lastId, migration.getVersionName());
                if (migration.exec(this, status)) {
                    // 执行成功 添加记录
                    long time = System.currentTimeMillis();
                    jdbcTemplate.update(String.format("insert into %s (id, version, execdate) values (?,?,?)", config.table_name), lastId, migration.getVersionName(), time);
                    System.out.printf("数据库执行迁移成功 << ID(%d) version(%s)%n", lastId, migration.getVersionName());
                    lastId++;
                } else {
                    // 回滚
                    throw new RuntimeException(String.format("数据库迁移操作错误 >> ID(%d) version(%s)", lastId, migration.getVersionName()));
                }
            } else {
                // 回滚
                throw new RuntimeException(String.format("数据库迁移操作错误 >> ID(%d) version(%s)", lastId, migration.getVersionName()));
            }
        }
        // 执行完毕
        System.out.printf("数据库迁移操作执行完毕。总共（%d）%n", migrationList.size());
    }


    public MigrationManager setConfig(Config config) {
        this.config = config;

        // 初始化配置
        Map<Config.DBType, String[]> confMap = Map.of(
                Config.DBType.mysql, new String[]{
                        Constant.find_table_mysql,
                        Constant.DDL_MYSQL
                },
                Config.DBType.pgsql, new String[]{
                        Constant.find_table_pgsql,
                        Constant.DDL_PGSQL
                },
                Config.DBType.other, new String[]{
                        config.find_table,
                        config.DDL
                }
        );

        String[] dbconf = confMap.get(config.dbType);
        if (StringEx.isEmpty(dbconf[0]) || StringEx.isEmpty(dbconf[1])) {
            System.out.println(dbconf[0]);
            System.out.println(dbconf[1]);
            throw new RuntimeException("请直接配置:迁移数据库表{db.migration.DDL} 以及 查找表{db.migration.find_table}");
        }

        this.findTableSQL = String.format(dbconf[0], config.table_name);
        this.migrationDDL = String.format(dbconf[1], config.table_name);

        return this;
    }
}
