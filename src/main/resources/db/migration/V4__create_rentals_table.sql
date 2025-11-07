CREATE TABLE IF NOT EXISTS gallery.rentals (
                                               id BIGSERIAL PRIMARY KEY,
                                               user_id BIGINT NOT NULL,
                                               car_id BIGINT NOT NULL,
                                               start_date DATE NOT NULL,
                                               end_date DATE NOT NULL,
                                               days INTEGER NOT NULL,
                                               currency VARCHAR(10) NOT NULL,
                                               daily_price DECIMAL(12,2) NOT NULL,
                                               total_price DECIMAL(12,2) NOT NULL,
                                               status VARCHAR(20) NOT NULL,
                                               pickup_notes TEXT,
                                               return_notes TEXT,
                                               create_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               update_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               created_by VARCHAR(100),
                                               updated_by VARCHAR(100),
                                               version BIGINT DEFAULT 0,
                                               is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                               CONSTRAINT fk_rental_user FOREIGN KEY (user_id) REFERENCES gallery.users(id) ON DELETE RESTRICT,
                                               CONSTRAINT fk_rental_car FOREIGN KEY (car_id) REFERENCES gallery.car(id) ON DELETE RESTRICT,
                                               CONSTRAINT chk_rental_dates CHECK (end_date >= start_date),
                                               CONSTRAINT chk_rental_days CHECK (days > 0),
                                               CONSTRAINT chk_rental_prices CHECK (daily_price > 0 AND total_price > 0)
);

CREATE INDEX IF NOT EXISTS idx_rentals_car ON gallery.rentals(car_id);
CREATE INDEX IF NOT EXISTS idx_rentals_user ON gallery.rentals(user_id);
CREATE INDEX IF NOT EXISTS idx_rentals_status ON gallery.rentals(status);
CREATE INDEX IF NOT EXISTS idx_rentals_dates ON gallery.rentals(start_date, end_date);


CREATE TABLE IF NOT EXISTS gallery.payments (
                                                id BIGSERIAL PRIMARY KEY,
                                                rental_id BIGINT NOT NULL,
                                                amount DECIMAL(12,2) NOT NULL,
                                                currency VARCHAR(10) NOT NULL,
                                                status VARCHAR(20) NOT NULL,
                                                payment_method VARCHAR(50),
                                                transaction_id VARCHAR(255),
                                                gateway_response TEXT,
                                                create_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                update_time TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                created_by VARCHAR(100),
                                                updated_by VARCHAR(100),
                                                version BIGINT DEFAULT 0,
                                                is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                                CONSTRAINT fk_payment_rental FOREIGN KEY (rental_id) REFERENCES gallery.rentals(id) ON DELETE CASCADE,
                                                CONSTRAINT chk_payment_amount CHECK (amount > 0)
);

CREATE INDEX IF NOT EXISTS idx_payments_rental ON gallery.payments(rental_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON gallery.payments(status);