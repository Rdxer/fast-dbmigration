package com.rdxer.db.migration.core;

import com.rdxer.db.migration.MigrationManager;
import org.springframework.transaction.TransactionStatus;


public class ActionMigration extends AbstractMigration {

    public interface IMigrationExec {
        /**
         * 执行迁移
         * @param migrationManager 迁移mng
         * @param status 事务对象
         * @return  返回 false 表示执行失败
         */
        boolean exec(MigrationManager migrationManager, TransactionStatus status);
    }

    private IMigrationExec migrationExec;

    public static ActionMigration of(String versionName, IMigrationExec migrationExec) {
        ActionMigration migration = new ActionMigration(versionName);
        migration.migrationExec = migrationExec;
        return migration;
    }

    public ActionMigration(String versionName) {
        super(versionName);
    }

    @Override
    public boolean init(MigrationManager migrationManager, TransactionStatus status) {
        return true;
    }


    @Override
    public boolean exec(MigrationManager migrationManager, TransactionStatus status) {
        return migrationExec.exec(migrationManager, status);
    }

}
