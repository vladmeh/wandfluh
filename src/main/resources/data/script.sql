CREATE TABLE wf_category
(
  id          INT AUTO_INCREMENT
    PRIMARY KEY,
  name        VARCHAR(255) NOT NULL,
  image       VARCHAR(128) NULL,
  description VARCHAR(255) NULL,
  parent_id   INT          NULL,
  CONSTRAINT fk_category_category
  FOREIGN KEY (parent_id) REFERENCES wf_category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_category_category_idx
  ON wf_category (parent_id);

CREATE TABLE wf_category_properties
(
  id          INT AUTO_INCREMENT
    PRIMARY KEY,
  value       TEXT NOT NULL,
  category_id INT  NOT NULL,
  CONSTRAINT fk_category_properties_category1
  FOREIGN KEY (category_id) REFERENCES wf_category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_category_properties_category1_idx
  ON wf_category_properties (category_id);

CREATE TABLE wf_product
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
  product_control_id      INT         NULL,
  CONSTRAINT fk_product_category1
  FOREIGN KEY (category_id) REFERENCES wf_category (id)
)
  ENGINE = InnoDB;

CREATE INDEX fk_product_category1_idx
  ON wf_product (category_id);

CREATE INDEX fk_product_product_construction1_idx
  ON wf_product (product_construction_id);

CREATE INDEX fk_product_product_size1_idx
  ON wf_product (product_size_id);

CREATE INDEX fk_product_product_type1_idx
  ON wf_product (product_type_id);

CREATE INDEX fk_product_product_control1_idx
  ON wf_product (product_control_id);

CREATE TABLE wf_product_construction
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(255) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE wf_product
  ADD CONSTRAINT fk_product_product_construction1
FOREIGN KEY (product_construction_id) REFERENCES wf_product_construction (id);

CREATE TABLE wf_product_control
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(255) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE wf_product
  ADD CONSTRAINT fk_product_product_control1
FOREIGN KEY (product_control_id) REFERENCES wf_product_control (id);

CREATE TABLE wf_product_size
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(128) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE wf_product
  ADD CONSTRAINT fk_product_product_size1
FOREIGN KEY (product_size_id) REFERENCES wf_product_size (id);

CREATE TABLE wf_product_type
(
  id   INT AUTO_INCREMENT
    PRIMARY KEY,
  name VARCHAR(128) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE wf_product
  ADD CONSTRAINT fk_product_product_type1
FOREIGN KEY (product_type_id) REFERENCES wf_product_type (id);


