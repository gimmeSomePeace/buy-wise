CREATE TABLE users (
    id UUID PRIMARY KEY,
    login VARCHAR(64) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE products (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name TEXT NOT NULL,

    CONSTRAINT fk_product_user
        FOREIGN KEY (owner_id)
            REFERENCES users(id)
);

CREATE TABLE stores (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    name TEXT NOT NULL,

    CONSTRAINT fk_store_user
        FOREIGN KEY (owner_id)
            REFERENCES users(id)
);

CREATE TABLE offers (
    id UUID PRIMARY KEY,
    store_id UUID NOT NULL,
    product_id UUID NOT NULL,
    price NUMERIC(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,

    CONSTRAINT fk_offer_store
        FOREIGN KEY (store_id)
            REFERENCES stores(id),

    CONSTRAINT fk_offer_product
        FOREIGN KEY (product_id)
            REFERENCES products(id),

    CONSTRAINT uk_offer_store_product
        UNIQUE (store_id, product_id)
);

CREATE TABLE basket (
    product_id UUID PRIMARY KEY,
    quantity INT NOT NULL CHECK ( quantity > 0 ),
    CONSTRAINT fk_basket_product
        FOREIGN KEY (product_id)
            REFERENCES products(id)
);

CREATE INDEX idx_offer_product
    ON offers(product_id);

CREATE INDEX idx_offer_store
    ON offers(store_id);
