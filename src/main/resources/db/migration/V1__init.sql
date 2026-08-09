CREATE TABLE roles (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    name VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(120) NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    status VARCHAR(20) NOT NULL
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE verification_tokens (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    type VARCHAR(40) NOT NULL,
    code VARCHAR(12) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE farms (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    owner_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(255) NOT NULL,
    latitude NUMERIC(10,7),
    longitude NUMERIC(10,7),
    area NUMERIC(12,2) NOT NULL,
    area_unit VARCHAR(20) NOT NULL,
    soil_type VARCHAR(20) NOT NULL,
    irrigation_type VARCHAR(20) NOT NULL,
    ownership_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_farms_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE farm_fields (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    farm_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    area NUMERIC(12,2) NOT NULL,
    area_unit VARCHAR(20) NOT NULL,
    notes VARCHAR(255),
    CONSTRAINT fk_farm_fields_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE CASCADE
);

CREATE TABLE crops (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    name VARCHAR(120) NOT NULL UNIQUE,
    category VARCHAR(80) NOT NULL,
    description VARCHAR(500),
    season VARCHAR(120)
);

CREATE TABLE crop_seasons (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    owner_id UUID NOT NULL,
    farm_id UUID NOT NULL,
    field_id UUID,
    crop_id UUID NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(40) NOT NULL,
    notes VARCHAR(255),
    CONSTRAINT fk_crop_seasons_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_crop_seasons_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE CASCADE,
    CONSTRAINT fk_crop_seasons_field FOREIGN KEY (field_id) REFERENCES farm_fields (id) ON DELETE SET NULL,
    CONSTRAINT fk_crop_seasons_crop FOREIGN KEY (crop_id) REFERENCES crops (id) ON DELETE CASCADE
);

CREATE TABLE disease_scans (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    farm_id UUID,
    image_storage_key VARCHAR(255) NOT NULL,
    image_uri VARCHAR(255) NOT NULL,
    original_filename VARCHAR(150) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_disease_scans_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_disease_scans_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE SET NULL
);

CREATE TABLE disease_results (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    disease_scan_id UUID NOT NULL,
    disease_name VARCHAR(150) NOT NULL,
    confidence DOUBLE PRECISION NOT NULL,
    summary VARCHAR(1000),
    recommendation VARCHAR(1000),
    CONSTRAINT fk_disease_results_scan FOREIGN KEY (disease_scan_id) REFERENCES disease_scans (id) ON DELETE CASCADE
);

CREATE TABLE crop_recommendations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    farm_id UUID,
    input_summary VARCHAR(255) NOT NULL,
    recommendation_text VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_crop_recommendations_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_crop_recommendations_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE SET NULL
);

CREATE TABLE fertilizer_recommendations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    farm_id UUID,
    input_summary VARCHAR(255) NOT NULL,
    recommendation_text VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_fertilizer_recommendations_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_fertilizer_recommendations_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE SET NULL
);

CREATE TABLE irrigation_recommendations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    farm_id UUID,
    input_summary VARCHAR(255) NOT NULL,
    recommendation_text VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    CONSTRAINT fk_irrigation_recommendations_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_irrigation_recommendations_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE SET NULL
);

CREATE TABLE recommendation_history (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    recommendation_type VARCHAR(60) NOT NULL,
    reference_id VARCHAR(255) NOT NULL,
    summary VARCHAR(500),
    CONSTRAINT fk_recommendation_history_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE weather_records (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    location VARCHAR(120) NOT NULL,
    latitude NUMERIC(10,7) NOT NULL,
    longitude NUMERIC(10,7) NOT NULL,
    record_type VARCHAR(40) NOT NULL,
    observed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    payload_json VARCHAR(2000)
);

CREATE TABLE markets (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(150) NOT NULL,
    state VARCHAR(120) NOT NULL
);

CREATE TABLE market_prices (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    market_id UUID NOT NULL,
    crop_id UUID NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    unit VARCHAR(40) NOT NULL,
    price_date DATE NOT NULL,
    CONSTRAINT fk_market_prices_market FOREIGN KEY (market_id) REFERENCES markets (id) ON DELETE CASCADE,
    CONSTRAINT fk_market_prices_crop FOREIGN KEY (crop_id) REFERENCES crops (id) ON DELETE CASCADE
);

CREATE TABLE government_schemes (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    title VARCHAR(160) NOT NULL,
    category VARCHAR(80) NOT NULL,
    state VARCHAR(120) NOT NULL,
    eligibility VARCHAR(1000) NOT NULL,
    active BOOLEAN NOT NULL,
    description VARCHAR(2000)
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    type VARCHAR(40) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE ai_conversations (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    title VARCHAR(150) NOT NULL,
    CONSTRAINT fk_ai_conversations_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE ai_messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    conversation_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    content VARCHAR(4000) NOT NULL,
    CONSTRAINT fk_ai_messages_conversation FOREIGN KEY (conversation_id) REFERENCES ai_conversations (id) ON DELETE CASCADE
);

CREATE TABLE reports (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    farm_id UUID,
    report_type VARCHAR(40) NOT NULL,
    title VARCHAR(150) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json VARCHAR(2000),
    CONSTRAINT fk_reports_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_reports_farm FOREIGN KEY (farm_id) REFERENCES farms (id) ON DELETE SET NULL
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    actor_user_id UUID,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(80),
    target_id VARCHAR(255),
    details VARCHAR(1000),
    ip_address VARCHAR(50),
    CONSTRAINT fk_audit_logs_actor FOREIGN KEY (actor_user_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_farms_owner_id ON farms (owner_id);
CREATE INDEX idx_farm_fields_farm_id ON farm_fields (farm_id);
CREATE INDEX idx_crop_seasons_owner_id ON crop_seasons (owner_id);
CREATE INDEX idx_disease_scans_user_id ON disease_scans (user_id);
CREATE INDEX idx_recommendation_history_user_id ON recommendation_history (user_id);
CREATE INDEX idx_weather_records_location ON weather_records (location);
CREATE INDEX idx_notifications_user_id ON notifications (user_id);

INSERT INTO roles (id, created_at, updated_at, name, description) VALUES
    ('00000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'FARMER', 'Farmer role'),
    ('00000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'EXPERT', 'Expert role'),
    ('00000000-0000-0000-0000-000000000003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'DEALER', 'Dealer role'),
    ('00000000-0000-0000-0000-000000000004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'NGO', 'NGO role'),
    ('00000000-0000-0000-0000-000000000005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN', 'Administrator role');
