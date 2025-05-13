-- 병원별 부서 삽입
INSERT INTO department (`hospital-id`, name, nurse_count, created_by, created_at, updated_by, updated_at)
VALUES
-- 강릉아산병원 (id = 1)
(1, '응급의학과', 12, 'ADMIN', NOW(), 'ADMIN', NOW()),
(1, '외과', 8, 'ADMIN', NOW(), 'ADMIN', NOW()),
-- 연세대학교원주세브란스기독병원 (id = 2)
(2, '신경과', 15, 'ADMIN', NOW(), 'ADMIN', NOW()),
(2, '정형외과', 10, 'ADMIN', NOW(), 'ADMIN', NOW()),
-- 한림대학교성심병원 (id = 3)
(3, '소아청소년과', 7, 'ADMIN', NOW(), 'ADMIN', NOW()),
-- 서울아산병원 (id = 4)
(4, '심장내과', 18, 'ADMIN', NOW(), 'ADMIN', NOW()),
(4, '흉부외과', 11, 'ADMIN', NOW(), 'ADMIN', NOW()),
-- 삼성서울병원 (id = 5)
(5, '내과', 22, 'ADMIN', NOW(), 'ADMIN', NOW()),
(5, '정신건강의학과', 5, 'ADMIN', NOW(), 'ADMIN', NOW());