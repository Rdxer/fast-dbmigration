# fast-dbmigration [![Maven Central](https://img.shields.io/maven-central/v/com.rdxer/fast-dbmigration.svg)](https://search.maven.org/search?q=g:com.rdxer%20a:fast-dbmigration)

fast - dbmigration by springboot
数据库迁移工具
目前支持 MySql，PGSql。

[demo](https://github.com/Rdxer/demo-fast-dbauto-dbmigration.git)

# 集成
## 1. 添加依赖
最新版：[![Maven Central](https://img.shields.io/maven-central/v/com.rdxer/fast-dbmigration.svg)](https://search.maven.org/search?q=g:com.rdxer%20a:fast-dbmigration)

    <dependency>
        <groupId>com.rdxer</groupId>
        <artifactId>fast-dbmigration</artifactId>
        <version>请替换为最新版</version>
    </dependency>

## 2. 配置`application.properties`
在 `application.properties` 中配置数据库类型
    
    # MySql配置如下
    com.rdxer.db.migration.dbtype=MySql
    
    # or

    # PGSql配置如下
    com.rdxer.db.migration.dbtype=PGSql

    # 其他配置 
    # 禁用
    com.rdxer.db.migration.enable=false

在 Spring Boot 启动类中添加 `@EnableDBMigration` 注解，启用此功能，如下：
    
    @SpringBootApplication
    @EnableDBMigration
    public class ServerApplication {
        public static void main(String[] args) {
            SpringApplication.run(ServerApplication.class, args);
        }
    }

## 3. 配置数据库模型类
创建迁移配置类，继承自 `DBMigrationConfig`，请务必存放于启动类所在的包内，否则扫描不到。
    
    @Component
    public class MigrationConfig extends DBMigrationConfig {
    
        @Override
        public void config(MigrationManager migrationManager) {
            migrationManager.registerMigration(
            // sql脚本迁移，
            // FileMigration.of("v1","sql/v1.sql"),
            // 代码迁移
            ActionMigration.of("添加管理员", (v, status) -> {
    
                       // TODO something
    
                        return true;
                    })
            );
        }
    }
  

## 4. 启动项目，稍等片刻.... 即可大功告成~ 


# 参考配置1如下： 
    
    无
    

# 文档

    源代码没几个类，可以直接看代码 /笑哭

# 注意
1. 数据库迁移工具能计划执行`迁移`操作，按照顺序执行，请勿删除旧迁移的代码。
