-- My Script file
-- Setting start for auto_increment

alter table city_state_storedprocedure_lobby.t_city auto_increment=100;

-- Function that retuirns last id being inserted
select last_insert_id();