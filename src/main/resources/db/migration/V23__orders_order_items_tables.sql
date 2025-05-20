create table orders
(
    id          bigint auto_increment
        primary key,
    customer_id bigint                             not null,
    status      varchar(20)                        not null,
    created_at  datetime       default (curtime()) not null,
    total_price decimal(10, 2) default 0           not null
);

create table order_items
(
    id          bigint auto_increment
        primary key,
    order_id    bigint                   not null,
    product_id  bigint                   not null,
    unit_price  decimal(10, 2) default 0 not null,
    quantity    int            default 1 not null,
    total_price decimal(10, 2) default 0 not null,
    constraint order_items_orders_id_fk
        foreign key (order_id) references products (id)
            on delete cascade,
    constraint order_items_products_id_fk
        foreign key (product_id) references products (id)
            on update cascade on delete cascade
);

