alter table user_tags
drop foreign key user_tags_tags_id_fk;

alter table user_tags
    add constraint user_tags_tags_id_fk
        foreign key (tag_id) references tags (id)
            on delete cascade;

alter table user_tags
drop foreign key user_tags_users_id_fk;

alter table user_tags
    add constraint user_tags_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade;