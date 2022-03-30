package com.rdxer.db.migration.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MigrationHistory implements RowMapper<MigrationHistory> {

    private int id;
    private String version;
    private long execdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getExecdate() {
        return execdate;
    }

    public void setExecdate(long execdate) {
        this.execdate = execdate;
    }

    @Override
    public MigrationHistory mapRow(ResultSet resultSet, int i) throws SQLException {
        var user = new MigrationHistory();
        user.setId(resultSet.getInt("id"));
        user.setVersion(resultSet.getString("version"));
        user.setExecdate(resultSet.getLong("execdate"));
        return user;
    }
}
