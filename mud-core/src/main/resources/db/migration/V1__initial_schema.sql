create table if not exists locations
(
    location_id varchar not null
        constraint locations_pk
            primary key,
    name        varchar not null
);

create table if not exists players
(
    name                varchar                 not null
        constraint players_pk
            primary key,
    current_location_id varchar                 not null
        constraint players_locations_location_id_fk
            references locations,
    past_location_id    varchar                 not null
        constraint players_locations_location_id_fk_2
            references locations,
    last_activity       timestamp default now() not null
);

create table if not exists equipments
(
    player_name   varchar not null
        constraint equipments_players_name_fk
            references players,
    head          varchar,
    torso         varchar,
    hands         varchar,
    legs          varchar,
    feet          varchar,
    melee_weapon  varchar,
    ranged_weapon varchar
);

create unique index equipments_player_name_uindex
    on equipments (player_name);

create table if not exists player_stats
(
    player_name  varchar not null
        constraint player_stats_players_name_fk
            references players,
    strength     integer default 0,
    dexterity    integer default 0,
    endurance    integer default 0,
    intelligence integer default 0,
    wisdom       integer default 0,
    charisma     integer default 0
);

create unique index player_stats_player_name_uindex
    on player_stats (player_name);

create table if not exists location_items
(
    location_id varchar           not null
        constraint location_items_locations_location_id_fk
            references locations,
    item_id     varchar,
    item_name   varchar           not null,
    amount      integer default 0 not null,
    constraint location_items_pk
        unique (location_id, item_id)
);

create index location_items_location_id_index
    on location_items (location_id);

create table if not exists player_params
(
    player_name varchar not null
        constraint player_params_players_name_fk
            references players,
    key         varchar not null,
    value       jsonb   not null,
    constraint player_params_pk
        unique (player_name, key)
);

create index player_params_player_name_index
    on player_params (player_name);

create table if not exists backpack_items
(
    player_name varchar           not null
        constraint backpack_items_players_name_fk
            references players,
    item_id     varchar           not null,
    item_name   varchar           not null,
    amount      integer default 0 not null,
    constraint backpack_items_pk
        unique (player_name, item_id)
);

create index backpack_items_player_name_index
    on backpack_items (player_name);
