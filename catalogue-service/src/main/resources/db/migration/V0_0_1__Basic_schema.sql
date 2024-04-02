create schema if not exists catalogue;

create table if not exists catalogue.t_brand
(
    id serial primary key,
    c_brand_id varchar(255) not null,
    c_title varchar(255) not null
);

create table if not exists catalogue.t_category
(
    id serial primary key ,
    c_category_id varchar(255) not null,
    c_title varchar not null
);

create table if not exists catalogue.t_product
(
    id serial primary key,
    c_product_id varchar(255) not null,
    c_title varchar(255) not null ,
    c_price numeric(10, 2) not null,
    c_attributes jsonb,
    c_category_id int,
    c_brand_id int,
    constraint fk_category foreign key (c_category_id) references catalogue.t_category(id),
    constraint fk_brand foreign key (c_brand_id) references catalogue.t_brand(id)
);