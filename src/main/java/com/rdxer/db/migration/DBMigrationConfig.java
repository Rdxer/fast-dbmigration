package com.rdxer.db.migration;

public abstract class DBMigrationConfig {
    public abstract void config(MigrationManager migrationManager);
}
