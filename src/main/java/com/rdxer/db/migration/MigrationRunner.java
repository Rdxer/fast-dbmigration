package com.rdxer.db.migration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(20)
public class MigrationRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(MigrationRunner.class);

    @Resource
    MigrationManager migrationManager;

    @Resource
    DBMigrationConfig config;

    @Value(value = "${com.rdxer.db.migration.dbtype}")
    Config.DBType dbtype;

    @Value(value = "${com.rdxer.db.migration.enable:true}")
    Boolean enable;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (dbtype == null) {
            String msg = "com.rdxer.db.migration.dbtype == null ,请配置 com.rdxer.db.migration.dbtype={mysql, pgsql, other}";
            this.logger.error(msg);
            throw new RuntimeException(msg);
        }

        logger.info("dbType: " + dbtype);

        if (!enable) {
            logger.warn("db.migration.enable: " + enable);
            return;
        }

        migrationManager.setConfig(Config.of(dbtype));

        config.config(migrationManager);

        migrationManager.run();
    }
}
