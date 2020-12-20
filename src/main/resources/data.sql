/**
 * CREATE Script for init of DB
 */
DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS drink;

CREATE TABLE drink (
  name VARCHAR(250) PRIMARY KEY,
  price INT NOT NULL
);

DROP TABLE IF EXISTS topping;

CREATE TABLE topping (
  name VARCHAR(250) PRIMARY KEY,
  price INT NOT NULL
);

DROP TABLE IF EXISTS customer_order_detail;

CREATE TABLE customer_order_detail (
  id INT NOT NULL,
  element INT NOT NULL,
  drink VARCHAR(250) NOT NULL,
  topping VARCHAR(250),
  user_id INT NOT NULL,
  primary key (id, element),
  foreign key (user_id) references user(id)
);

DROP TABLE IF EXISTS customer_order;

CREATE TABLE customer_order (
  id INT PRIMARY KEY,
  user_id INT NOT NULL,
  original_amount INT NOT NULL,
  discounted_amount INT NOT NULL,
  foreign key (user_id) references user(id)
);

 -- Create 2 Users

insert into user (name) values ('Patrick');
insert into user (name) values ('Sara');
insert into user (name) values ('Cees');
insert into user (name) values ('Mary');
insert into user (name) values ('Luke');
insert into user (name) values ('Anna');
insert into user (name) values ('Mark');

 -- Create Drinks from Assignment

 insert into drink (name, price) values ('Black Coffee', 4);
 insert into drink (name, price) values ('Latte', 5);
 insert into drink (name, price) values ('Mocha', 6);
 insert into drink (name, price) values ('Tea', 3);

 -- Create Toppings from Assignment

 insert into topping (name, price) values ('Milk', 2);
 insert into topping (name, price) values ('Hazelnut syrup', 3);
 insert into topping (name, price) values ('Chocolate sauce', 5);
 insert into topping (name, price) values ('Lemon', 2);

-- Create some sample customer_orders and their details

 insert into customer_order_detail (id, element, drink, topping, user_id) values (-2, 1, 'Latte', 'Milk', 3);
 insert into customer_order_detail (id, element, drink, topping, user_id) values (-2, 2, 'Tea', '', 3);
 insert into customer_order (id, user_id, original_amount, discounted_amount) values (-2, 3, 10, 10);

 insert into customer_order_detail (id, element, drink, topping, user_id) values (-1, 1, 'Mocha', 'Chocolate sauce', 3);
 insert into customer_order_detail (id, element, drink, topping, user_id) values (-1, 2, 'Tea', 'Milk', 3);
 insert into customer_order (id, user_id, original_amount, discounted_amount) values (-1, 3, 16, 12);
