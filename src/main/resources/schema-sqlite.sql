-- foreign keys enforce
PRAGMA foreign_keys = OFF;

DROP TABLE IF EXISTS task_file_processed;

DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS task;

DROP TABLE IF EXISTS task_to_scheduled_job_mapping;

DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS file_regex_predefined_category;


CREATE TABLE IF NOT EXISTS file_regex_predefined_category (
    id                                      INTEGER PRIMARY KEY AUTOINCREMENT,
    unique_name_id                          TEXT NOT NULL UNIQUE,
    description                             TEXT,
    path_matcher_syntax_and_pattern         TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS settings (
    id                                              INTEGER PRIMARY KEY AUTOINCREMENT,
    source_folder                                   TEXT,
    destination_folder                              TEXT,

    fk_selected_file_regex_predefined_category_id   INTEGER,
    selected_string_operation_unique_name_id        TEXT,

    FOREIGN KEY(fk_selected_file_regex_predefined_category_id)    REFERENCES file_regex_predefined_category(id)     ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS task (
    id                                          INTEGER PRIMARY KEY AUTOINCREMENT,

    task_status_unique_name_id                  TEXT NOT NULL DEFAULT 'CREATED',

    file_operation_unique_name_id               TEXT NOT NULL,
    file_operation_input_folder                 TEXT,
    file_operation_destination_folder           TEXT,

    string_operation_unique_name_id             TEXT NOT NULL,
    string_operation_regex_what                 TEXT,
    string_operation_replace_to                 TEXT,

    has_error                                   BOOLEAN NOT NULL
                                                    CHECK ( has_error IN (0, 1) )
                                                    DEFAULT 0,
    error_msg                                   TEXT DEFAULT '',

    total_file_count                            INTEGER NOT NULL DEFAULT 0,
    processed_file_count                        INTEGER NOT NULL DEFAULT 0,

    created_date                                DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')),
    last_modified_date                          DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW'))
);

CREATE TABLE IF NOT EXISTS task_file_processed (
    id                              INTEGER PRIMARY KEY AUTOINCREMENT,

    fk_task_id                      INTEGER NOT NULL,

    source_file                     TEXT NOT NULL,
    destination_file                TEXT NOT NULL,

    has_error                       BOOLEAN NOT NULL
                                        CHECK ( has_error IN (0, 1) )
                                        DEFAULT 0,
    error_msg                       TEXT DEFAULT '',

    created_date                    DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')),
    last_modified_date              DATETIME DEFAULT(STRFTIME('%Y-%m-%d %H:%M:%f', 'NOW')),

    FOREIGN KEY(fk_task_id)         REFERENCES task(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS job (
    id                           INTEGER PRIMARY KEY AUTOINCREMENT,
    job_status_unique_name_id    TEXT NOT NULL DEFAULT 'CREATED'
);

CREATE TABLE IF NOT EXISTS task_to_scheduled_job_mapping (
    fk_task_id                INTEGER NOT NULL,
    fk_job_id                 INTEGER NOT NULL,

    PRIMARY KEY (fk_task_id, fk_job_id),
    FOREIGN KEY (fk_task_id)            REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_job_id)             REFERENCES job(id) ON DELETE CASCADE
);

-- foreign keys enforce
PRAGMA foreign_keys = ON;

-- write ahead journaling
PRAGMA journal_mode=WAL;