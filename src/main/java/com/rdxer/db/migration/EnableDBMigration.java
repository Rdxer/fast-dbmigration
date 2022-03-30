package com.rdxer.db.migration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        MigrationManager.class,
//        MigrationRunner.class
})
public @interface EnableDBMigration {

}