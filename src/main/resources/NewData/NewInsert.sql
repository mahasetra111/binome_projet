-- ============================================================
-- 1. NETTOYAGE DE LA BASE
-- ============================================================
SET session_replication_role = 'replica';

TRUNCATE TABLE member_payment RESTART IDENTITY CASCADE;
TRUNCATE TABLE member_referee RESTART IDENTITY CASCADE;
TRUNCATE TABLE collectivity_member RESTART IDENTITY CASCADE;
TRUNCATE TABLE membership_fee RESTART IDENTITY CASCADE;
TRUNCATE TABLE member RESTART IDENTITY CASCADE;
TRUNCATE TABLE collectivity RESTART IDENTITY CASCADE;

SET session_replication_role = 'origin';

-- ============================================================
-- 2. COLLECTIVITÉS
-- ============================================================
INSERT INTO collectivity (id, name, number, location, specialization) VALUES
                                                                          ('col-1', 'Mpanorina', 1, 'Ambatondrazaka', 'Riziculture'),
                                                                          ('col-2', 'Dobo voalahany', 2, 'Ambatondrazaka', 'Pisciculture'),
                                                                          ('col-3', 'Tantely mamy', 3, 'Brickaville', 'Apiculture');

-- ============================================================
-- 3. MEMBRES EXISTANTS (Collectivité 1)
-- ============================================================
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid) VALUES
                                                                                                                                                                          ('C1-M1', 'Prénom membre 1', 'Nom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', 'PRESIDENT', true, true),
                                                                                                                                                                          ('C1-M2', 'Prénom membre 2', 'Nom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', 'VICE_PRESIDENT', true, true),
                                                                                                                                                                          ('C1-M3', 'Prénom membre 3', 'Nom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', 'SECRETARY', true, true),
                                                                                                                                                                          ('C1-M4', 'Prénom membre 4', 'Nom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', 'TREASURER', true, true),
                                                                                                                                                                          ('C1-M5', 'Prénom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C1-M6', 'Prénom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C1-M7', 'Prénom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C1-M8', 'Prénom membre 8', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', 'SENIOR', true, true);

-- ============================================================
-- 4. MEMBRES EXISTANTS (Collectivité 2)
-- ============================================================
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid) VALUES
                                                                                                                                                                          ('C2-M1', 'Prénom membre 1', 'Nom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C2-M2', 'Prénom membre 2', 'Nom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C2-M3', 'Prénom membre 3', 'Nom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C2-M4', 'Prénom membre 4', 'Nom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C2-M5', 'Prénom membre 5', 'Nom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', 'PRESIDENT', true, true),
                                                                                                                                                                          ('C2-M6', 'Prénom membre 6', 'Nom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', 'VICE_PRESIDENT', true, true),
                                                                                                                                                                          ('C2-M7', 'Prénom membre 7', 'Nom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', 'SECRETARY', true, true),
                                                                                                                                                                          ('C2-M8', 'Prénom membre 8', 'Nom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', 'TREASURER', true, true);

-- ============================================================
-- 5. MEMBRES EXISTANTS (Collectivité 3)
-- ============================================================
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid) VALUES
                                                                                                                                                                          ('C3-M1', 'Prénom membre 9', 'Nom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', '034034567', 'member.9@fed-agri.mg', 'PRESIDENT', true, true),
                                                                                                                                                                          ('C3-M2', 'Prénom membre 10', 'Nom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', '0338634567', 'member.10@fed-agri.mg', 'VICE_PRESIDENT', true, true),
                                                                                                                                                                          ('C3-M3', 'Prénom membre 11', 'Nom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', '0338234567', 'member.11@fed-agri.mg', 'SECRETARY', true, true),
                                                                                                                                                                          ('C3-M4', 'Prénom membre 12', 'Nom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', '0382334567', 'member.12@fed-agri.mg', 'TREASURER', true, true),
                                                                                                                                                                          ('C3-M5', 'Prénom membre 13', 'Nom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe.', 'Apiculteur', '0373365567', 'member.13@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C3-M6', 'Prénom membre 14', 'Nom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe.', 'Apiculteur', '0378234567', 'member.14@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C3-M7', 'Prénom membre 15', 'Nom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', '0374914567', 'member.15@fed-agri.mg', 'SENIOR', true, true),
                                                                                                                                                                          ('C3-M8', 'Prénom membre 16', 'Nom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', '0370634567', 'member.16@fed-agri.mg', 'SENIOR', true, true);

-- ============================================================
-- 6. ASSOCIATION MEMBRES ↔ COLLECTIVITÉS
-- ============================================================
INSERT INTO collectivity_member (id, member_id, collectivity_id, joined_at) VALUES
-- Collectivité 1
('cm1', 'C1-M1', 'col-1', '2026-01-01'),
('cm2', 'C1-M2', 'col-1', '2026-01-01'),
('cm3', 'C1-M3', 'col-1', '2026-01-01'),
('cm4', 'C1-M4', 'col-1', '2026-01-01'),
('cm5', 'C1-M5', 'col-1', '2026-01-01'),
('cm6', 'C1-M6', 'col-1', '2026-01-01'),
('cm7', 'C1-M7', 'col-1', '2026-01-01'),
('cm8', 'C1-M8', 'col-1', '2026-01-01'),
-- Collectivité 2
('cm9', 'C2-M1', 'col-2', '2026-01-01'),
('cm10', 'C2-M2', 'col-2', '2026-01-01'),
('cm11', 'C2-M3', 'col-2', '2026-01-01'),
('cm12', 'C2-M4', 'col-2', '2026-01-01'),
('cm13', 'C2-M5', 'col-2', '2026-01-01'),
('cm14', 'C2-M6', 'col-2', '2026-01-01'),
('cm15', 'C2-M7', 'col-2', '2026-01-01'),
('cm16', 'C2-M8', 'col-2', '2026-01-01'),
-- Collectivité 3
('cm17', 'C3-M1', 'col-3', '2026-01-01'),
('cm18', 'C3-M2', 'col-3', '2026-01-01'),
('cm19', 'C3-M3', 'col-3', '2026-01-01'),
('cm20', 'C3-M4', 'col-3', '2026-01-01'),
('cm21', 'C3-M5', 'col-3', '2026-01-01'),
('cm22', 'C3-M6', 'col-3', '2026-01-01'),
('cm23', 'C3-M7', 'col-3', '2026-01-01'),
('cm24', 'C3-M8', 'col-3', '2026-01-01');

-- ============================================================
-- 7. MISE À JOUR DES POSTES SPÉCIFIQUES DANS COLLECTIVITY
-- ============================================================
UPDATE collectivity SET
                        president_id = 'C1-M1',
                        vice_president_id = 'C1-M2',
                        secretary_id = 'C1-M3',
                        treasurer_id = 'C1-M4'
WHERE id = 'col-1';

UPDATE collectivity SET
                        president_id = 'C2-M5',
                        vice_president_id = 'C2-M6',
                        secretary_id = 'C2-M7',
                        treasurer_id = 'C2-M8'
WHERE id = 'col-2';

UPDATE collectivity SET
                        president_id = 'C3-M1',
                        vice_president_id = 'C3-M2',
                        secretary_id = 'C3-M3',
                        treasurer_id = 'C3-M4'
WHERE id = 'col-3';

-- ============================================================
-- 8. RELATIONS DE PARRAINAGE (member_referee)
-- ============================================================
INSERT INTO member_referee (id, member_refereed_id, member_referee_id) VALUES
-- Collectivité 1
('ref1', 'C1-M3', 'C1-M1'), ('ref2', 'C1-M3', 'C1-M2'),
('ref3', 'C1-M4', 'C1-M1'), ('ref4', 'C1-M4', 'C1-M2'),
('ref5', 'C1-M5', 'C1-M1'), ('ref6', 'C1-M5', 'C1-M2'),
('ref7', 'C1-M6', 'C1-M1'), ('ref8', 'C1-M6', 'C1-M2'),
('ref9', 'C1-M7', 'C1-M1'), ('ref10', 'C1-M7', 'C1-M2'),
('ref11', 'C1-M8', 'C1-M6'), ('ref12', 'C1-M8', 'C1-M7'),
-- Collectivité 2
('ref13', 'C2-M3', 'C1-M1'), ('ref14', 'C2-M3', 'C1-M2'),
('ref15', 'C2-M4', 'C1-M1'), ('ref16', 'C2-M4', 'C1-M2'),
('ref17', 'C2-M5', 'C1-M1'), ('ref18', 'C2-M5', 'C1-M2'),
('ref19', 'C2-M6', 'C1-M1'), ('ref20', 'C2-M6', 'C1-M2'),
('ref21', 'C2-M7', 'C1-M1'), ('ref22', 'C2-M7', 'C1-M2'),
('ref23', 'C2-M8', 'C1-M6'), ('ref24', 'C2-M8', 'C1-M7'),
-- Collectivité 3
('ref25', 'C3-M1', 'C1-M1'), ('ref26', 'C3-M1', 'C1-M2'),
('ref27', 'C3-M2', 'C1-M1'), ('ref28', 'C3-M2', 'C1-M2'),
('ref29', 'C3-M3', 'C3-M1'), ('ref30', 'C3-M3', 'C3-M2'),
('ref31', 'C3-M4', 'C3-M1'), ('ref32', 'C3-M4', 'C3-M2'),
('ref33', 'C3-M5', 'C3-M1'), ('ref34', 'C3-M5', 'C3-M2'),
('ref35', 'C3-M6', 'C3-M1'), ('ref36', 'C3-M6', 'C3-M2'),
('ref37', 'C3-M7', 'C3-M1'), ('ref38', 'C3-M7', 'C3-M2'),
('ref39', 'C3-M8', 'C3-M1'), ('ref40', 'C3-M8', 'C3-M2');

-- ============================================================
-- 9. COTISATIONS (membership_fee)
-- ============================================================
INSERT INTO membership_fee (id, label, amount, eligible_from, status, frequency, collectivity_id) VALUES
                                                                                                      ('cot-1', 'Cotisation annuelle', 200000.00, '2026-01-01', 'ACTIVE', 'ANNUALLY', 'col-1'),
                                                                                                      ('cot-2', 'Famangiana', 20000.00, '2026-04-30', 'ACTIVE', 'PUNCTUALLY', 'col-1'),
                                                                                                      ('cot-3', 'Cotisation annuelle', 200000.00, '2026-01-01', 'ACTIVE', 'ANNUALLY', 'col-2'),
                                                                                                      ('cot-4', 'Cotisation 2025', 100000.00, '2025-01-01', 'INACTIVE', 'ANNUALLY', 'col-2'),
                                                                                                      ('cot-5', 'Cotisation mensuelle', 25000.00, '2026-04-01', 'ACTIVE', 'MONTHLY', 'col-3');

-- ============================================================
-- 10. PAIEMENTS DES MEMBRES (member_payment)
-- ============================================================
INSERT INTO member_payment (id, member_id, membership_fee_id, amount, payment_mode, creation_date) VALUES
-- Collectivité 1 (cot-1)
('pay1', 'C1-M1', 'cot-1', 200000.00, 'CASH', '2026-01-01'),
('pay2', 'C1-M2', 'cot-1', 200000.00, 'CASH', '2026-01-01'),
('pay3', 'C1-M3', 'cot-1', 200000.00, 'MOBILE_MONEY', '2026-01-01'),
('pay4', 'C1-M4', 'cot-1', 200000.00, 'MOBILE_MONEY', '2026-01-01'),
('pay5', 'C1-M5', 'cot-1', 150000.00, 'MOBILE_MONEY', '2026-01-01'),
('pay6', 'C1-M6', 'cot-1', 100000.00, 'CASH', '2026-05-01'),
('pay7', 'C1-M7', 'cot-1', 60000.00, 'CASH', '2026-05-01'),
('pay8', 'C1-M8', 'cot-1', 90000.00, 'CASH', '2026-05-01'),
-- Collectivité 2 (cot-3)
('pay9', 'C2-M1', 'cot-3', 120000.00, 'CASH', '2026-01-01'),
('pay10', 'C2-M2', 'cot-3', 180000.00, 'CASH', '2026-01-01'),
('pay11', 'C2-M3', 'cot-3', 200000.00, 'CASH', '2026-01-01'),
('pay12', 'C2-M4', 'cot-3', 200000.00, 'CASH', '2026-01-01'),
('pay13', 'C2-M5', 'cot-3', 200000.00, 'CASH', '2026-01-01'),
('pay14', 'C2-M6', 'cot-3', 200000.00, 'CASH', '2026-01-01'),
('pay15', 'C2-M7', 'cot-3', 80000.00, 'MOBILE_MONEY', '2026-01-01'),
('pay16', 'C2-M8', 'cot-3', 120000.00, 'MOBILE_MONEY', '2026-01-01');

-- ============================================================
-- 11. NOUVEAUX MEMBRES JUNIORS
-- ============================================================
INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid, membership_dues_paid) VALUES
-- Collectivité 1
('C1-M9', 'Nouveau1', 'Prenom1', '1990-01-01', 'MALE', 'Adresse1', 'Agriculteur', '0312345678', 'new1@mail.com', 'JUNIOR', false, false),
('C1-M10', 'Nouveau2', 'Prenom2', '1991-02-02', 'FEMALE', 'Adresse2', 'Agriculteur', '0323456789', 'new2@mail.com', 'JUNIOR', false, false),
('C1-M11', 'Nouveau3', 'Prenom3', '1992-03-03', 'MALE', 'Adresse3', 'Agriculteur', '0334567890', 'new3@mail.com', 'JUNIOR', false, false),
('C1-M12', 'Nouveau4', 'Prenom4', '1993-04-04', 'FEMALE', 'Adresse4', 'Agriculteur', '0345678901', 'new4@mail.com', 'JUNIOR', false, false),

-- Collectivité 2
('C2-M9', 'NouveauC2_1', 'PrenomC2_1', '1990-05-05', 'MALE', 'AdresseC2_1', 'Agriculteur', '0356789012', 'newc2_1@mail.com', 'JUNIOR', false, false),
('C2-M10', 'NouveauC2_2', 'PrenomC2_2', '1991-06-06', 'FEMALE', 'AdresseC2_2', 'Agriculteur', '0367890123', 'newc2_2@mail.com', 'JUNIOR', false, false),
('C2-M11', 'NouveauC2_3', 'PrenomC2_3', '1992-07-07', 'MALE', 'AdresseC2_3', 'Agriculteur', '0378901234', 'newc2_3@mail.com', 'JUNIOR', false, false),

-- Collectivité 3
('C3-M9', 'NouveauC3_1', 'PrenomC3_1', '1989-08-08', 'MALE', 'AdresseC3_1', 'Apiculteur', '0389012345', 'newc3_1@mail.com', 'JUNIOR', false, false),
('C3-M10', 'NouveauC3_2', 'PrenomC3_2', '1990-09-09', 'FEMALE', 'AdresseC3_2', 'Apiculteur', '0390123456', 'newc3_2@mail.com', 'JUNIOR', false, false),
('C3-M11', 'NouveauC3_3', 'PrenomC3_3', '1991-10-10', 'MALE', 'AdresseC3_3', 'Apiculteur', '0301234567', 'newc3_3@mail.com', 'JUNIOR', false, false),
('C3-M12', 'NouveauC3_4', 'PrenomC3_4', '1992-11-11', 'FEMALE', 'AdresseC3_4', 'Apiculteur', '0312345679', 'newc3_4@mail.com', 'JUNIOR', false, false),
('C3-M13', 'NouveauC3_5', 'PrenomC3_5', '1993-12-12', 'MALE', 'AdresseC3_5', 'Apiculteur', '0323456780', 'newc3_5@mail.com', 'JUNIOR', false, false),
('C3-M14', 'NouveauC3_6', 'PrenomC3_6', '1994-01-01', 'FEMALE', 'AdresseC3_6', 'Apiculteur', '0334567891', 'newc3_6@mail.com', 'JUNIOR', false, false);

-- ============================================================
-- 12. ASSOCIATION NOUVEAUX MEMBRES JUNIORS ↔ COLLECTIVITÉS
-- ============================================================
INSERT INTO collectivity_member (id, member_id, collectivity_id, joined_at) VALUES
-- Collectivité 1
('cm25', 'C1-M9', 'col-1', '2026-04-01'),
('cm26', 'C1-M10', 'col-1', '2026-04-01'),
('cm27', 'C1-M11', 'col-1', '2026-05-01'),
('cm28', 'C1-M12', 'col-1', '2026-06-01'),
-- Collectivité 2
('cm29', 'C2-M9', 'col-2', '2026-03-01'),
('cm30', 'C2-M10', 'col-2', '2026-03-01'),
('cm31', 'C2-M11', 'col-2', '2026-03-01'),
-- Collectivité 3
('cm32', 'C3-M9', 'col-3', '2026-01-01'),
('cm33', 'C3-M10', 'col-3', '2026-02-01'),
('cm34', 'C3-M11', 'col-3', '2026-02-01'),
('cm35', 'C3-M12', 'col-3', '2026-03-01'),
('cm36', 'C3-M13', 'col-3', '2026-03-01'),
('cm37', 'C3-M14', 'col-3', '2026-03-01');

-- ============================================================
-- 13. PARRAINAGES DES NOUVEAUX MEMBRES JUNIORS
-- ============================================================
INSERT INTO member_referee (id, member_refereed_id, member_referee_id) VALUES
-- Collectivité 1
('ref41', 'C1-M9', 'C1-M1'), ('ref42', 'C1-M9', 'C1-M2'),
('ref43', 'C1-M10', 'C1-M1'), ('ref44', 'C1-M10', 'C1-M2'),
('ref45', 'C1-M11', 'C1-M1'), ('ref46', 'C1-M11', 'C1-M2'),
('ref47', 'C1-M12', 'C1-M1'), ('ref48', 'C1-M12', 'C1-M2'),
-- Collectivité 2
('ref49', 'C2-M9', 'C1-M1'), ('ref50', 'C2-M9', 'C1-M2'),
('ref51', 'C2-M10', 'C1-M1'), ('ref52', 'C2-M10', 'C1-M2'),
('ref53', 'C2-M11', 'C1-M1'), ('ref54', 'C2-M11', 'C1-M2'),
-- Collectivité 3
('ref55', 'C3-M9', 'C3-M1'), ('ref56', 'C3-M9', 'C3-M2'),
('ref57', 'C3-M10', 'C3-M1'), ('ref58', 'C3-M10', 'C3-M2'),
('ref59', 'C3-M11', 'C3-M1'), ('ref60', 'C3-M11', 'C3-M2'),
('ref61', 'C3-M12', 'C3-M1'), ('ref62', 'C3-M12', 'C3-M2'),
('ref63', 'C3-M13', 'C3-M1'), ('ref64', 'C3-M13', 'C3-M2'),
('ref65', 'C3-M14', 'C3-M1'), ('ref66', 'C3-M14', 'C3-M2');

-- ============================================================
-- 14. VÉRIFICATION FINALE
-- ============================================================
SELECT 'collectivity' as table_name, COUNT(*) FROM collectivity
UNION ALL
SELECT 'member', COUNT(*) FROM member
UNION ALL
SELECT 'collectivity_member', COUNT(*) FROM collectivity_member
UNION ALL
SELECT 'member_referee', COUNT(*) FROM member_referee
UNION ALL
SELECT 'membership_fee', COUNT(*) FROM membership_fee
UNION ALL
SELECT 'member_payment', COUNT(*) FROM member_payment;