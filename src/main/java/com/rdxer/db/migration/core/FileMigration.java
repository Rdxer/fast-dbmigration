package com.rdxer.db.migration.core;

import com.rdxer.db.migration.MigrationManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.TransactionStatus;

import java.io.IOException;


public class FileMigration extends ScriptMigration {

    private String filePath;

    protected FileMigration(String versionName) {
        super(versionName);
    }

    public static FileMigration of(String versionName, String filePath) {
        FileMigration migration = new FileMigration(versionName);
        migration.setFilePath(filePath);
        return migration;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    // 初始化 迁移
    @Override
    public boolean init(MigrationManager migrationManager, TransactionStatus status) {
        ClassPathResource resource = (ClassPathResource) migrationManager.getResourceLoader().getResource("classpath:" + getFilePath());

        try {
            byte[] bytes = resource.getInputStream().readAllBytes();
            String script = new String(bytes);
            setScript(script);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return super.init(migrationManager, status);
    }
}
