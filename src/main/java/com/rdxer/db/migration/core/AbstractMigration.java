package com.rdxer.db.migration.core;

import com.rdxer.db.migration.MigrationManager;
import org.springframework.transaction.TransactionStatus;

public abstract class AbstractMigration {

    protected String versionName;

    public AbstractMigration(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 初始化 迁移
     * @param migrationManager 迁移mng
     * @param status 事务对象
     */
    public abstract boolean init(MigrationManager migrationManager, TransactionStatus status);

    /**
     * 返回 false 表示执行失败
     * @param migrationManager 迁移mng
     * @param status 事务对象
     */
    public abstract boolean exec(MigrationManager migrationManager, TransactionStatus status);
}


