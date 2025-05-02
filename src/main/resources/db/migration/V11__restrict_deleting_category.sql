alter table products
    drop foreign key products_categories_id_fk;

alter table products
    add constraint products_categories_id_fk
        foreign key (category_id) references categories (id);