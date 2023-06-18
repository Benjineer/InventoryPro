create TABLE if not exists users (
  id BIGSERIAL PRIMARY KEY,
   firstname VARCHAR(255) NULL,
   lastname VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   `role` VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS items (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  delivery_address_line1 VARCHAR(255) NOT NULL,
  delivery_address_line2 VARCHAR(255),
  delivery_city VARCHAR(255) NOT NULL,
  delivery_state VARCHAR(255) NOT NULL,
  delivery_zip VARCHAR(10) NOT NULL,
  payment_method VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS order_items (
  order_id BIGINT NOT NULL,
  item_id BIGINT NOT NULL,
  PRIMARY KEY (order_id, item_id),
  FOREIGN KEY (order_id) REFERENCES orders (id),
  FOREIGN KEY (item_id) REFERENCES items (id)
);


