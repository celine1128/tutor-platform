USE tutor_db;

-- 补充历史需求数据的发布时间，避免页面“发布时间”列为空
UPDATE demand
SET create_time = NOW()
WHERE id > 0 AND create_time IS NULL;

-- 补充历史订单数据的创建时间，避免页面“创建时间”列为空
UPDATE tutor_order
SET create_time = NOW()
WHERE id > 0 AND create_time IS NULL;