/**
 * CREATE Script for init of DB
 */

-- Create 3 manufacturers
insert into manufacturer (id, date_created, name, description) values (1, now(), 'BMW', 'The best sports cars manufacturer');
insert into manufacturer (id, date_created, name, description) values (2, now(), 'VW', 'The best car city cars manufacturer');
insert into manufacturer (id, date_created, name, description) values (3, now(), 'AUDI', 'The best design manufacturer');

-- Create 3 cars
insert into car (id, date_created, license_plate, seat_count, convertible, engine_type, entity_manufacturer_id, rating)
values (1, now(), 'NCA-3432', 4, true, 'ELECTRIC', 1, 3);
insert into car (id, date_created, license_plate, seat_count, convertible, engine_type, entity_manufacturer_id, rating)
values (2, now(), 'AD-43422', 2, true, 'GAS', 2, 5);
insert into car (id, date_created, license_plate, seat_count, convertible, engine_type, entity_manufacturer_id)
values (3, now(), '3432DADA', 5, false, 'HYBRID', 3);
insert into car (id, date_created, license_plate, seat_count, convertible, engine_type, entity_manufacturer_id)
values (4, now(), 'ZAFER', 5, false, 'HYBRID', 3);

-- Create 3 OFFLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username) values (1, now(), false, 'OFFLINE',
'driver01pw', 'driver01');

insert into driver (id, date_created, deleted, online_status, password, username) values (2, now(), false, 'OFFLINE',
'driver02pw', 'driver02');

insert into driver (id, date_created, deleted, online_status, password, username) values (3, now(), false, 'OFFLINE',
'driver03pw', 'driver03');

insert into driver (id, date_created, deleted, online_status, password, username) values (9, now(), false, 'OFFLINE',
'driver09pw', 'driver09');


-- Create 3 ONLINE drivers

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (4, now(), false, 'ONLINE',
'driver04pw', 'driver04', 4);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (5, now(), false, 'ONLINE',
'driver05pw', 'driver05', 3);

insert into driver (id, date_created, deleted, online_status, password, username, car_id) values (6, now(), false, 'ONLINE',
'driver06pw', 'driver06', 2);

-- Create 1 OFFLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username)
values
 (7,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'OFFLINE',
'driver07pw', 'driver07');

-- Create 1 ONLINE driver with coordinate(longitude=9.5&latitude=55.954)

insert into driver (id, coordinate, date_coordinate_updated, date_created, deleted, online_status, password, username, car_id)
values
 (8,
 'aced0005737200226f72672e737072696e676672616d65776f726b2e646174612e67656f2e506f696e7431b9e90ef11a4006020002440001784400017978704023000000000000404bfa1cac083127', now(), now(), false, 'ONLINE',
'driver08pw', 'driver08', 1);