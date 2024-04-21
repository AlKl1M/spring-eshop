create schema if not exists order_service;

create table if not exists order_service.t_order
(
    id serial primary key not null,
    c_user_id varchar not null,
    c_order_id varchar not null,
    c_date varchar not null,
    c_status varchar not null
);

create table if not exists order_service.t_order_products(
    id serial primary key not null,
    c_product_id varchar not null,
    c_name varchar not null,
    c_quantity integer not null,
    c_price integer not null,
    c_order int,
    constraint fk_products foreign key (c_order) references order_service.t_order(id)
);