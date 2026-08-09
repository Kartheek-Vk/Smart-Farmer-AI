-- Refresh tokens and OTP codes are stored as SHA-256 hashes so a database dump cannot be replayed.
DELETE FROM refresh_tokens;
ALTER TABLE refresh_tokens DROP COLUMN token;
ALTER TABLE refresh_tokens ADD COLUMN token_hash VARCHAR(128) NOT NULL;
ALTER TABLE refresh_tokens ADD CONSTRAINT uq_refresh_tokens_token_hash UNIQUE (token_hash);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);

DELETE FROM verification_tokens;
ALTER TABLE verification_tokens DROP COLUMN type;
ALTER TABLE verification_tokens DROP COLUMN code;
ALTER TABLE verification_tokens ADD COLUMN purpose VARCHAR(40) NOT NULL;
ALTER TABLE verification_tokens ADD COLUMN code_hash VARCHAR(128) NOT NULL;
CREATE INDEX idx_verification_tokens_lookup ON verification_tokens (user_id, purpose, code_hash);

CREATE TABLE user_preferences (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    language VARCHAR(10) NOT NULL,
    measurement_unit VARCHAR(20) NOT NULL,
    email_notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    push_notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    weather_alerts_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_market_prices_market_crop_date ON market_prices (market_id, crop_id, price_date DESC);
CREATE INDEX idx_disease_results_scan_id ON disease_results (disease_scan_id);
CREATE INDEX idx_crop_recommendations_user_id ON crop_recommendations (user_id);
CREATE INDEX idx_fertilizer_recommendations_user_id ON fertilizer_recommendations (user_id);
CREATE INDEX idx_irrigation_recommendations_user_id ON irrigation_recommendations (user_id);
CREATE INDEX idx_ai_conversations_user_id ON ai_conversations (user_id);
CREATE INDEX idx_ai_messages_conversation_id ON ai_messages (conversation_id);
CREATE INDEX idx_reports_user_id ON reports (user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs (created_at DESC);
CREATE INDEX idx_government_schemes_filters ON government_schemes (state, category, active);
CREATE UNIQUE INDEX uq_markets_name_state ON markets (name, state);
