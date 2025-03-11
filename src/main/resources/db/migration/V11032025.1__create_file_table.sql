create sequence file_id_seq;
create table file
(
    id       bigint primary key not null default nextval('file_id_seq'),
    capacity bigint             not null,
    name     varchar(50)        not null
);

