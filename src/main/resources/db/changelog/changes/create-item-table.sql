CREATE TABLE IF NOT EXISTS item (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  quantity INT NOT NULL
);
