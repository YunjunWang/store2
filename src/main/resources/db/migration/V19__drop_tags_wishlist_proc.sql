DELIMITER $$

CREATE PROCEDURE dropTagsWishlistTables()
    BEGIN
        alter table user_tags
            drop foreign key user_tags_tags_id_fk;

        alter table user_tags
            add constraint user_tags_tags__fk
                foreign key (tag_id) references tags (id)
                    on delete cascade;

        alter table user_tags
            drop foreign key user_tags_users_id_fk;

        alter table user_tags
            add constraint user_tags_users__fk
                foreign key (user_id) references users (id)
                    on delete cascade;
        drop table tags;

        alter table wishlist
            drop foreign key wishlist_products_id_fk;

        alter table wishlist
            add constraint wishlist_products__fk
                foreign key (product_id) references products (id);

        alter table wishlist
            drop foreign key wishlist_users_id_fk;

        alter table wishlist
            add constraint wishlist_users__fk
                foreign key (user_id) references users (id);
        drop table wishlist;
    END $$

DELIMITER ;