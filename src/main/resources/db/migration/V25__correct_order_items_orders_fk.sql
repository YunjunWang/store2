alter table order_items
    drop foreign key order_items_orders_id_fk;

alter table order_items
    add constraint order_items_orders_id_fk
        foreign key (order_id) references orders (id);