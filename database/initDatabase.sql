drop database if exists pokemon_inventory_manager;

create database pokemon_inventory_manager;

use pokemon_inventory_manager;

create table inventory
(
    id            binary(16) unique primary key not null,
    user_id       varchar(128)                  not null,
    pokemon_name  varchar(64)                   not null,
    base_hp       int                           not null,
    base_attack   int                           not null,
    base_defense  int                           not null,
    base_speed    int                           not null,
    is_legendary  boolean                       not null,
    type          varchar(32)                   not null,
    total_rarity  int                           not null,
    front_default varchar(255)                  not null,
    front_shiny   varchar(255)                  not null
)