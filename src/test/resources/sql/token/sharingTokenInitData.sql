insert into worker (worker_id, first_name, last_name) values ('d16f795b-3a64-4ce1-9b87-91a8851d5c60', 'Jane', 'Doe'), ('6f1564b1-0cfb-4e13-94a1-6c10f6d9f8e8', 'Carl', 'Smith');
insert into warehouse(warehouse_id, name, unit, rows_number) values ('123e4567-e89b-12d3-a456-426614174000', 'warehouse1', 'METER', 3);
insert into warehouse(warehouse_id, name, unit, rows_number) values ('d17a22d4-f834-41f0-ac55-d8ad82a18964', 'warehouse2', 'METER', 3);
insert into worker_warehouse(ww_id, warehouse_id, worker_id, role) values('fe8ef72e-e3ff-11ee-bd3d-0242ac120002','123e4567-e89b-12d3-a456-426614174000', 'd16f795b-3a64-4ce1-9b87-91a8851d5c60', 'ADMIN');
insert into sharing_token(token_owner_id, owner_type, salt, sharing_token) values ('123e4567-e89b-12d3-a456-426614174000','WAREHOUSE',  'LUR3G68pElA26X3yDw+9mA==', '$2a$10$Hr33QvDgEBzirFfAqfVeTeOj4ycfLUR3G68pElA26X3yDw+9mA==');
insert into sharing_token(token_owner_id, owner_type, salt, sharing_token) values ('d17a22d4-f834-41f0-ac55-d8ad82a18964','WAREHOUSE',  'E8L1K02tIpE6aB7cHa+3qE==', '$2a$10$E8L1K02tIpE6aB7cHa+3qERm6cjhE8L1K02tIpE6aB7cHa+3qE==');
alter table worker_warehouse alter column ww_id  set default gen_random_uuid();
insert into warehouse_group (group_id, name) values ('88e6e8c8-061c-47e9-861d-f56d7b6a9085', 'group1');
insert into sharing_token(token_owner_id, owner_type, salt, sharing_token) values ('88e6e8c8-061c-47e9-861d-f56d7b6a9085','GROUP',  'D7K0J91sHoD59A6bGz+2pD==', '$2a$10$D7K0J91sHoD59A6bGz+2pDQl5bifD7K0J91sHoD59A6bGz+2pD==');