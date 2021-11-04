create table notes
(
    id bigserial not null
        constraint notes_prim_key primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    title varchar(50) not null,
    note varchar(1000),
    user_id bigint not null
        constraint note_user_id_fkey
            references users
);