create table if not exists followers
(
    chat_id bigint primary key
);
create table if not exists users_followers
(
    user_id     bigint,
    follower_id bigint,
    primary key (user_id, follower_id),
    foreign key (user_id) references users (id),
    foreign key (follower_id) references followers (chat_id)
);

