create table wishlist
(
    user_id    bigint not null,
    product_id bigint not null,
    constraint wishlist_pk
        primary key (user_id, product_id),
    constraint wishlist_products_id_fk
        foreign key (product_id) references products (id),
    constraint wishlist_users_id_fk
        foreign key (user_id) references users (id)
);