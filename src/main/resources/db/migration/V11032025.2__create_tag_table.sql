create table if not exists tag
(
    id   bigint primary key generated by default as identity,
    name varchar(50) not null
);
