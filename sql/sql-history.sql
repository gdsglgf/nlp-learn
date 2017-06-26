
SELECT * FROM `t_html` WHERE title like '%儿童%'

SELECT * FROM `t_link` WHERE link_text = '体育运动';

SELECT * FROM `t_html` where title like '%公司%' limit 0, 10;


-- rela_type_id 可以简化两个join的问题

select * from t_relation t1
join (select entity_type_id, name from t_entity where entity_type_id = 1) as t2 on t1.entity_id1 = t2.entity_type_id
join (select entity_type_id, name from t_entity where entity_type_id = 7) as t3 on t1.entity_id2 = t3.entity_type_id
limit 10;

select * from t_relation t1
join (select entity_type_id, name from t_entity where entity_type_id = 1) as t2 on t1.entity_id1 = t2.entity_type_id
limit 10;

