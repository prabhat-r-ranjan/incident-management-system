-- setup-azure-postgres.sql
-- Run this entire script at once

-- Create database if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'incidentdb') THEN
        CREATE DATABASE incidentdb;
    END IF;
END
$$;

-- Connect to incidentdb
\c incidentdb;

-- Create user if not exists
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename = 'incidentuser') THEN
        CREATE USER incidentuser WITH PASSWORD 'incidentpass';
    END IF;
END
$$;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE incidentdb TO incidentuser;
GRANT ALL ON SCHEMA public TO incidentuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO incidentuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO incidentuser;

-- Create tables
CREATE TABLE IF NOT EXISTS problems (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    severity VARCHAR(2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    root_cause TEXT,
    workaround TEXT,
    resolution TEXT,
    assigned_to VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS problem_incidents (
    problem_id BIGINT NOT NULL REFERENCES problems(id) ON DELETE CASCADE,
    incident_id BIGINT NOT NULL REFERENCES incidents(id) ON DELETE CASCADE,
    PRIMARY KEY (problem_id, incident_id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_problems_status ON problems(status);
CREATE INDEX IF NOT EXISTS idx_problems_severity ON problems(severity);
CREATE INDEX IF NOT EXISTS idx_problems_assigned_to ON problems(assigned_to);
CREATE INDEX IF NOT EXISTS idx_problems_created_at ON problems(created_at);
CREATE INDEX IF NOT EXISTS idx_problem_incidents_problem ON problem_incidents(problem_id);
CREATE INDEX IF NOT EXISTS idx_problem_incidents_incident ON problem_incidents(incident_id);

-- Add constraints
ALTER TABLE problems 
ADD CONSTRAINT chk_problem_status 
CHECK (status IN ('OPEN', 'INVESTIGATING', 'KNOWN_ERROR', 'RESOLVED', 'CLOSED'));

ALTER TABLE problems 
ADD CONSTRAINT chk_problem_severity 
CHECK (severity IN ('P0', 'P1', 'P2', 'P3', 'P4'));