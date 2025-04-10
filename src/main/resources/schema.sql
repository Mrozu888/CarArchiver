CREATE TABLE IF NOT EXISTS cars (
    id BIGSERIAL PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INTEGER NOT NULL CHECK (year >= 1886 AND year <= 2100),
    color VARCHAR(30),
    price NUMERIC(12,2) CHECK (price > 0),
    image_url VARCHAR(1024),
    mileage INTEGER CHECK (mileage >= 0),
    fuel_type VARCHAR(30),
    accident_free BOOLEAN,
    publish_date DATE
);