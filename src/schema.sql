-- 1. Créer la base et l'utilisateur
CREATE DATABASE federation_db;
CREATE USER federation_db_user WITH PASSWORD '12345678';
GRANT ALL PRIVILEGES ON DATABASE federation_db TO federation_db_user;

-- 2. Se connecter à la base
\c federation_db

-- 3. Créer les tables
CREATE TABLE IF NOT EXISTS member (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name      VARCHAR(100)  NOT NULL,
    last_name       VARCHAR(100)  NOT NULL,
    birth_date      DATE          NOT NULL,
    gender          VARCHAR(10)   NOT NULL CHECK (gender IN ('MALE', 'FEMALE')),
    address         VARCHAR(255)  NOT NULL,
    profession      VARCHAR(100)  NOT NULL,
    phone_number    VARCHAR(20)   NOT NULL,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    occupation      VARCHAR(20)   NOT NULL CHECK (occupation IN ('JUNIOR','SENIOR','SECRETARY','TREASURER','VICE_PRESIDENT','PRESIDENT')),
    collectivity_id UUID,
    joined_at       DATE          NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS collectivity (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    number              INTEGER UNIQUE,
    name                VARCHAR(150) UNIQUE,
    location            VARCHAR(150) NOT NULL,
    federation_approval BOOLEAN      NOT NULL DEFAULT FALSE,
    president_id        UUID REFERENCES member(id),
    vice_president_id   UUID REFERENCES member(id),
    treasurer_id        UUID REFERENCES member(id),
    secretary_id        UUID REFERENCES member(id),
    created_at          DATE         NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS member_referee (
    member_id   UUID NOT NULL REFERENCES member(id),
    referee_id  UUID NOT NULL REFERENCES member(id),
    PRIMARY KEY (member_id, referee_id)
);

ALTER TABLE member
    ADD CONSTRAINT fk_member_collectivity
    FOREIGN KEY (collectivity_id) REFERENCES collectivity(id)
    DEFERRABLE INITIALLY DEFERRED;

-- 4. Donner les droits sur les tables
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO federation_db_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO federation_db_user;

-- 5. Données de test (2 membres SENIOR pour parrainer)
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, joined_at)
VALUES
  ('a0000000-0000-0000-0000-000000000001', 'Jean', 'Rakoto', '1990-01-01', 'MALE', 'Tana', 'Agriculteur', '034000001', 'jean@test.com', 'SENIOR', CURRENT_DATE),
  ('a0000000-0000-0000-0000-000000000002', 'Marie', 'Rabe', '1992-05-10', 'FEMALE', 'Tana', 'Agriculteur', '034000002', 'marie@test.com', 'SENIOR', CURRENT_DATE);

CREATE TABLE membership_fee (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                collectivity_id UUID REFERENCES collectivity(id),
                                eligible_from DATE,
                                frequency VARCHAR(20),
                                amount DECIMAL,
                                label VARCHAR(100),
                                status VARCHAR(20)
);

CREATE TABLE member_payment (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                member_id UUID REFERENCES member(id),
                                membership_fee_id UUID,
                                amount INTEGER,
                                payment_mode VARCHAR(30),
                                account_id UUID,
                                creation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE collectivity_transaction (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          collectivity_id UUID REFERENCES collectivity(id),
                                          amount DECIMAL,
                                          payment_mode VARCHAR(30),
                                          account_id UUID,
                                          member_id UUID,
                                          creation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE financial_account (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   type VARCHAR(30),
                                   amount DECIMAL
);