CREATE TABLE blocks
(
    id          UUID NOT NULL,
    property_id UUID,
    date_from   date,
    date_to     date,
    CONSTRAINT pk_blocks PRIMARY KEY (id)
);

CREATE TABLE bookings
(
    id            UUID NOT NULL,
    property_id   UUID,
    date_from     date,
    date_to       date,
    main_guest_id UUID,
    message       VARCHAR(255),
    canceled      BOOLEAN,
    adults        INT,
    children      INT,
    CONSTRAINT pk_bookings PRIMARY KEY (id)
);

CREATE TABLE properties
(
    id             UUID NOT NULL,
    name           VARCHAR(255),
    description    VARCHAR(255),
    address        VARCHAR(255),
    owner_id       UUID,
    check_in_time  VARCHAR(255),
    check_out_time VARCHAR(255),
    nightly_price  DOUBLE PRECISION,
    CONSTRAINT pk_properties PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID NOT NULL,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255),
    legal_id_type VARCHAR(255),
    legal_id      VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE blocks
    ADD CONSTRAINT FK_BLOCKS_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES properties (id);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_MAIN_GUEST FOREIGN KEY (main_guest_id) REFERENCES users (id);

ALTER TABLE bookings
    ADD CONSTRAINT FK_BOOKINGS_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES properties (id);

ALTER TABLE properties
    ADD CONSTRAINT FK_PROPERTIES_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);