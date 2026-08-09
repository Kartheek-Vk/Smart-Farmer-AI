CREATE TABLE farmer_profiles (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    experience_level VARCHAR(120),
    primary_crop VARCHAR(120),
    address VARCHAR(255),
    CONSTRAINT fk_farmer_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_farmer_profiles_user_id ON farmer_profiles (user_id);
