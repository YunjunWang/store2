alter table orders
    alter column created_at set default (current_timestamp);

alter table orders
    alter column total_price drop default;

alter table orders
    add constraint orders_users_id_fk
        foreign key (customer_id) references users (id);

alter table order_items
    alter column unit_price drop default;

alter table order_items
    alter column total_price drop default;

alter table order_items
    drop foreign key order_items_orders_id_fk;

alter table order_items
    add constraint order_items_orders_id_fk
        foreign key (order_id) references products (id);

alter table order_items
    drop foreign key order_items_products_id_fk;

alter table order_items
    add constraint order_items_products_id_fk
        foreign key (product_id) references products (id);

