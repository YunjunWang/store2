alter table categories
    add constraint categories_pk
        primary key (id);

alter table categories
    modify id tinyint auto_increment;

alter table categories
    auto_increment = 1;

alter table categories
    modify name varchar(255) not null;

