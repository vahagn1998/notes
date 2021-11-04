create table users
(
    id bigserial not null
        constraint users_prim_key primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    email varchar(255) not null
        constraint email_unique unique,
    password varchar(63) not null
        constraint password_min_check check (length(password) >= 8)
);