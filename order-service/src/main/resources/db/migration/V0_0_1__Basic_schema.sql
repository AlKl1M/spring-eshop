create schema if not exists orderService;

create table if not exists orderService.t_order
(
    id serial primary key not null ,
    c_date varchar not null,
    c_status varchar not null
);