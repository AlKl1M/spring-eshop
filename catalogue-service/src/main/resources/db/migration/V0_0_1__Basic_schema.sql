create schema if not exists catalogue;

create table if not exists catalogue.t_brand
(
    id serial primary key not null ,
    c_brand_id varchar(255) UNIQUE not null,
    c_title varchar not null
);

create table if not exists catalogue.t_category
(
    id serial primary key not null,
    c_category_id varchar(255) UNIQUE not null,
    c_title varchar not null
);

create table if not exists catalogue.t_product
(
    id serial primary key not null,
    c_product_id varchar(255) UNIQUE not null,
    c_title varchar not null ,
    c_price numeric(10, 2) not null,
    c_attributes jsonb,
    c_description text,
    c_category_id int,
    c_brand_id int,
    constraint fk_category foreign key (c_category_id) references catalogue.t_category(id),
    constraint fk_brand foreign key (c_brand_id) references catalogue.t_brand(id)
);
create table if not exists catalogue.t_product_photo
(
    c_product_id varchar(255) UNIQUE not null,
    c_is_preview boolean,
    c_photo_id varchar(255) UNIQUE not null,
    c_url varchar UNIQUE not null,
    constraint fk_photo foreign key (c_product_id) references catalogue.t_product(c_product_id)
);
insert into catalogue.t_brand(c_brand_id, c_title) VALUES ('0e2e4f1c-af5a-4', 'другое');
insert into catalogue.t_category(c_category_id, c_title) VALUES ('afe9b2cb-891b-4', 'другое');