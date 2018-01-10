CREATE TABLE category
(
  id          INT AUTO_INCREMENT
    PRIMARY KEY,
  name        VARCHAR(45)  NOT NULL,
  image       VARCHAR(45)  NULL,
  description VARCHAR(258) NULL,
  parent_id   INT          NULL,
  CONSTRAINT fk_category_category
  FOREIGN KEY (parent_id) REFERENCES category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_category_category_idx
  ON category (parent_id);

CREATE TABLE category_properties
(
  id          INT AUTO_INCREMENT
    PRIMARY KEY,
  value       VARCHAR(258) NOT NULL,
  category_id INT          NOT NULL,
  CONSTRAINT fk_category_properties_category1
  FOREIGN KEY (category_id) REFERENCES category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_category_properties_category1_idx
  ON category_properties (category_id);

CREATE TABLE product
(
  id                      INT AUTO_INCREMENT
    PRIMARY KEY,
  name                    VARCHAR(45) NOT NULL,
  data_sheet_no           VARCHAR(11) NOT NULL,
  data_sheet_pdf          VARCHAR(45) NOT NULL,
  category_id             INT         NOT NULL,
  product_construction_id INT         NULL,
  product_size_id         INT         NULL,
  product_type_id         INT         NULL,
  product_control_id      INT         NOT NULL,
  CONSTRAINT data_sheet_no_UNIQUE
  UNIQUE (data_sheet_no),
  CONSTRAINT fk_product_category1
  FOREIGN KEY (category_id) REFERENCES category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_product_category1_idx
  ON product (category_id);

CREATE INDEX fk_product_product_construction1_idx
  ON product (product_construction_id);

CREATE INDEX fk_product_product_size1_idx
  ON product (product_size_id);

CREATE INDEX fk_product_product_type1_idx
  ON product (product_type_id);

CREATE INDEX fk_product_product_control1_idx
  ON product (product_control_id);

CREATE TABLE product_construction
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(45) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE product
  ADD CONSTRAINT fk_product_product_construction1
FOREIGN KEY (product_construction_id) REFERENCES product_construction (id);

CREATE TABLE product_control
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(45) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE product
  ADD CONSTRAINT fk_product_product_control1
FOREIGN KEY (product_control_id) REFERENCES product_control (id);

CREATE TABLE product_size
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(45) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE product
  ADD CONSTRAINT fk_product_product_size1
FOREIGN KEY (product_size_id) REFERENCES product_size (id);

CREATE TABLE product_type
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(45) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE product
  ADD CONSTRAINT fk_product_product_type1
FOREIGN KEY (product_type_id) REFERENCES product_type (id);


