package com.filestorage.health;

import com.codahale.metrics.health.HealthCheck;
import org.jdbi.v3.core.Jdbi;

public class DatabaseHealthCheck extends HealthCheck {
    private final Jdbi jdbi;

    public DatabaseHealthCheck(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    protected Result check() throws Exception {
        try {
            jdbi.withHandle(handle -> handle.select("SELECT 1").mapTo(Integer.class).one());
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy("Database connection failed: " + e.getMessage());
        }
    }
}