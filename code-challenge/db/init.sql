CREATE DATABASE IF NOT EXISTS taskdb;
USE taskdb;

drop table if exists tasks;
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE
);

drop table if exists audit_log;
-- New table for auditing
CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL,
    #action_details VARCHAR(100) NOT NULL, -- The bug is here: the column is too small
    action_details TEXT NOT NULL,
    log_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert enough data to trigger the bugs
INSERT INTO tasks (id, title, description) VALUES
(1, 'Fix login page', 'The login form is broken on Safari.'),
(9, 'Refactor user service', 'The user service is too complex.'),
(10, 'Write API documentation', 'Document all public endpoints.'),
-- THIS IS THE NEW, LONGER TITLE THAT WILL TRIGGER THE BUG
(11, 'Investigate and resolve the critical performance degradation issue on the main dashboard for enterprise-level clients', 'The main dashboard is loading slowly.'),
(12, 'Deploy to staging', 'Final deployment for Q3 release.'),
(130, 'Update third-party libraries for security compliance', 'Several libraries have known vulnerabilities.');

select * from tasks;
select * from audit_log;

#Triggers when insert rows into  the audit_log – AFTER INSERT
DROP TRIGGER IF EXISTS tasks_after_insert;

DELIMITER //

CREATE TRIGGER tasks_after_insert
AFTER INSERT ON tasks
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (action_type, action_details)
    VALUES (
        'INSERT',
        CONCAT(
            'New task added - ID: ', NEW.id,
            ', Title: ', NEW.title,
            ', Description: ', NEW.description,
            ', Completed: ', IF(NEW.completed, 'Yes', 'No')
        )
    );
END;
//

DELIMITER ;

INSERT INTO tasks (title, description, completed)
VALUES ('Finish milestone 1', 'Finalize and review deliverables.', TRUE);


