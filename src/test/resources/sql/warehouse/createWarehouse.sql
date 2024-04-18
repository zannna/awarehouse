alter table worker_warehouse alter column ww_id  set default gen_random_uuid();
insert into worker (worker_id, first_name, last_name) values ('d16f795b-3a64-4ce1-9b87-91a8851d5c60', 'John', 'Smith'), ('6f1564b1-0cfb-4e13-94a1-6c10f6d9f8e8', 'Jack', 'Black');
insert into warehouse_group (group_id, name) values ('c6e1b3aa-948d-11ee-b9d1-0242ac120002', 'clothes'), ('d985e5e4-948d-11ee-b9d1-0242ac120002', 'toys'), ('e2802bdc-948d-11ee-b9d1-0242ac120002','tools');
insert into warehouse(warehouse_id, name, unit, rows_number) values ('123e4567-e89b-12d3-a456-426614174000', 'warehouse1', 'METER', 3);
insert into worker_warehouse(warehouse_id, worker_id, role) values('123e4567-e89b-12d3-a456-426614174000', '6f1564b1-0cfb-4e13-94a1-6c10f6d9f8e8', 'WORKER');
insert into warehouse(warehouse_id, name, unit, rows_number) values ('16aecbfa-7807-11ee-b962-0242ac120002', 'warehouse2', 'METER', 3);
insert into worker_warehouse(warehouse_id, worker_id, role) values('16aecbfa-7807-11ee-b962-0242ac120002', '6f1564b1-0cfb-4e13-94a1-6c10f6d9f8e8', 'WORKER');

