package com.rdxer.db.migration.core;

import com.rdxer.db.migration.MigrationManager;
import org.springframework.transaction.TransactionStatus;


public class ScriptMigration extends AbstractMigration {

    private String script;

    public ScriptMigration(String versionName) {
        super(versionName);
    }

    public static ScriptMigration of(String versionName, String script) {
        ScriptMigration migration = new ScriptMigration(versionName);
        migration.setScript(script);
        return migration;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    // 初始化 迁移
    @Override
    public boolean init(MigrationManager migrationManager, TransactionStatus status) {
        return true;
    }

    // 执行 迁移
    @Override
    public boolean exec(MigrationManager migrationManager, TransactionStatus status) {
        System.out.println(String.format("versionName:%s \nexec sql >>\n%s \n<< %n", versionName, getScript()));

        migrationManager.getJdbcTemplate().execute(this.script);

        System.out.println("执行 SQL 迁移 >> " + script);

        return true;
    }


}
