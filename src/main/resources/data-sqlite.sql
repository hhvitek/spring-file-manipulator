INSERT INTO file_regex_predefined_category (id, unique_name_id, description, path_matcher_syntax_and_pattern)
VALUES (1, 'audio', 'Audio files only.', 'regex:audio');
INSERT INTO file_regex_predefined_category (id, unique_name_id, path_matcher_syntax_and_pattern)
VALUES (2, 'video', 'regex:videos');

INSERT INTO settings (source_folder, destination_folder, fk_selected_file_regex_predefined_category_id, selected_string_operation_unique_name_id)
VALUES ('/source/to/folder', '/destination/to/folder', 1, 'NICE_LOOKING_FILENAME');

------------------------

INSERT INTO task (file_operation_unique_name_id, string_operation_unique_name_id)
VALUES ('COPY', 'NICE_LOOKING_FILENAME');

INSERT INTO task (file_operation_unique_name_id, string_operation_unique_name_id)
VALUES ('COPY', 'NICE_LOOKING_FILENAME');

INSERT INTO task (file_operation_unique_name_id, string_operation_unique_name_id, has_error, error_msg)
VALUES ('MOVE', 'REPLACE_WHAT_WITH', true, 'The error encountered');

-------------------------
