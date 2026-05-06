-- Enum pour le type d'activité
DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_type WHERE typname = 'activity_type') THEN
        CREATE TYPE activity_type AS ENUM ('MEETING', 'TRAINING', 'OTHER');
    END IF;
END $$;

-- Enum pour le statut de présence
DO $$ BEGIN
    IF NOT EXISTS (SELECT FROM pg_type WHERE typname = 'attendance_status') THEN
        CREATE TYPE attendance_status AS ENUM ('MISSING', 'ATTENDED', 'UNDEFINED');
    END IF;
END $$;

-- Table activité
CREATE TABLE IF NOT EXISTS collectivity_activity (
                                                     id varchar PRIMARY KEY,
                                                     label varchar,
                                                     activity_type activity_type,
                                                     collectivity_id varchar REFERENCES collectivity(id),
                                                     executive_date date,
                                                     recurrence_week_ordinal integer,
                                                     recurrence_day_of_week varchar
);

-- Table occupation concernée par activité
CREATE TABLE IF NOT EXISTS activity_occupation_concerned (
                                                             id varchar PRIMARY KEY,
                                                             activity_id varchar REFERENCES collectivity_activity(id),
                                                             occupation varchar
);

-- Table présence
CREATE TABLE IF NOT EXISTS activity_attendance (
                                                   id varchar PRIMARY KEY,
                                                   activity_id varchar REFERENCES collectivity_activity(id),
                                                   member_id varchar REFERENCES member(id),
                                                   attendance_status attendance_status DEFAULT 'UNDEFINED'
);