-- member
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- member 그룹
(1, 'Y', 'member', '수간호사', 'HEAD_NURSE'),
(2, 'Y', 'member', '간호사', 'NURSE'),
(3, 'Y', 'member', '응급요원', 'PARAMEDIC'),
(4, 'Y', 'member', '관리자', 'ADMIN');

-- board
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- board_status 그룹
(1, 'Y', 'board_status', '요청 수리됨', 'Completed'),
(2, 'Y', 'board_status', '요청 대기중', 'Waiting');


-- nurse
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- working_status 그룹
(1, 'Y', 'working_status', '수술중', 'IN_SURGERY'),
(2, 'Y', 'working_status', '오프', 'OFF'),
(3, 'Y', 'working_status', '근무중', 'ON');

-- schedule
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- schedule_category 그룹
(1, 'Y', 'schedule_category', '확정된 오프', 'ACCEPTED_OFF'),
(2, 'Y', 'schedule_category', '근무', 'WORKING'),
(3, 'Y', 'schedule_category', '개인 근무 일정', 'PERSONAL');


-- schedule_temp
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- schedule_temp_category 그룹
(1, 'Y', 'schedule_temp_category', '근무 일정 대기', 'WORKING_TEMP'),
(2, 'Y', 'schedule_temp_category', '대기중인 오프', 'WAITING_OFF'),
(3, 'Y', 'schedule_temp_category', '처리된 오프', 'PROCESSED_OFF');


-- room 
INSERT INTO common_code (sort_order, use_yn, code_group, code_label, code_value) VALUES
-- room_status 그룹
(1, 'Y', 'room_status', '비어있음', 'EMPTY'),
(2, 'Y', 'room_status', '사용중', 'USING');