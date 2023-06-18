create TABLE if not exists user_entity (
  id BIGSERIAL PRIMARY KEY,
   firstname VARCHAR(255) NULL,
   lastname VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   password VARCHAR(255) NULL,
   `role` VARCHAR(255) NULL
);