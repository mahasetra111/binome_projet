INSERT INTO collectivity (id, number, name, location, specialization) VALUES
                                                                            ('col-1', 1, 'Mpanorina', 'Ambatondrazaka', 'Riziculture'),
                                                                            ('col-2', 2, 'Dobo voalohany', 'Ambatondrazaka', 'Pisciculture'),
                                                                            ('col-3', 3, 'Tantely mamy', 'Brickaville', 'Apiculture');


INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, registration_fee_paid) VALUES

-- Collectivité 1
('C1-M1', 'Prenom1', 'Nom1', '1980-02-01', 'M', 'Ambato', 'Riziculteur', '0341234567', 'member1@fed.mg', 'PRESIDENT', 'col-1'),
('C1-M2', 'Prenom2', 'Nom2', '1982-03-05', 'M', 'Ambato', 'Agriculteur', '0321234567', 'member2@fed.mg', 'VICE_PRESIDENT', 'col-1'),
('C1-M3', 'Prenom3', 'Nom3', '1992-03-10', 'M', 'Ambato', 'Collecteur', '0331234567', 'member3@fed.mg', 'SECRETARY', 'col-1'),
('C1-M4', 'Prenom4', 'Nom4', '1988-05-22', 'F', 'Ambato', 'Distributeur', '0381234567', 'member4@fed.mg', 'TREASURER', 'col-1'),
('C1-M5', 'Prenom5', 'Nom5', '1999-08-21', 'M', 'Ambato', 'Riziculteur', '0373434567', 'member5@fed.mg', 'CONFIRMED', 'col-1'),
('C1-M6', 'Prenom6', 'Nom6', '1998-08-22', 'F', 'Ambato', 'Riziculteur', '0372234567', 'member6@fed.mg', 'CONFIRMED', 'col-1'),
('C1-M7', 'Prenom7', 'Nom7', '1998-01-31', 'M', 'Ambato', 'Riziculteur', '0374234567', 'member7@fed.mg', 'CONFIRMED', 'col-1'),
('C1-M8', 'Prenom8', 'Nom8', '1975-08-20', 'M', 'Ambato', 'Riziculteur', '0370234567', 'member8@fed.mg', 'CONFIRMED', 'col-1');


INSERT INTO financial_accounts (id, collectivite_id, type, balance, holder_name, phone, bank_name, account_number) VALUES

-- Collectivité 1
('C1-A-CASH', 'col-1', 'CASH', 0, NULL, NULL, NULL, NULL),
('C1-A-MOBILE-1', 'col-1', 'MOBILE_MONEY', 0, 'Mpanorina', '0370489612', 'ORANGE_MONEY', NULL),

-- Collectivité 2
('C2-A-CASH', 'col-2', 'CASH', 0, NULL, NULL, NULL, NULL),
('C2-A-MOBILE-1', 'col-2', 'MOBILE_MONEY', 0, 'Dobo voalohany', '0320489612', 'ORANGE_MONEY', NULL),

-- Collectivité 3 (ajout 6 mai)
('C3-A-CASH', 'col-3', 'CASH', 0, NULL, NULL, NULL, NULL),
('C3-BANK-1', 'col-3', 'BANK', 0, 'Koto', NULL, 'BMOI', '0000400001123456789012'),
('C3-BANK-2', 'col-3', 'BANK', 0, 'Naivo', NULL, 'BRED', '0000800003456789012358'),
('C3-MOBILE-1', 'col-3', 'MOBILE_MONEY', 0, 'Kolo', '0341889612', 'MVOLA', NULL);



INSERT INTO membership_fee (id, collectivite_id, label, status, frequency, start_date, amount) VALUES

                                                                                                    ('cot-1', 'col-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUAL', '2026-01-01', 100000),
                                                                                                    ('cot-2', 'col-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUAL', '2026-01-01', 100000),
                                                                                                    ('cot-3', 'col-3', 'Cotisation annuelle', 'ACTIVE', 'ANNUAL', '2026-01-01', 50000),

-- Ajout 6 mai
                                                                                                    ('cot-4', 'col-1', 'Cotisation exceptionnelle', 'ACTIVE', 'ONCE', '2026-05-01', 20000),
                                                                                                    ('cot-5', 'col-2', 'Cotisation exceptionnelle', 'ACTIVE', 'ONCE', '2026-05-01', 30000),
                                                                                                    ('cot-6', 'col-3', 'Cotisation exceptionnelle', 'ACTIVE', 'ONCE', '2026-05-01', 15000);


INSERT INTO payments (id, member_id, collectivite_id, amount, account_id, payment_method, payment_date) VALUES

                                                                                                            ('pay-1', 'C1-M1', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-2', 'C1-M2', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-3', 'C1-M3', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-4', 'C1-M4', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-5', 'C1-M5', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-6', 'C1-M6', 'col-1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-7', 'C1-M7', 'col-1', 60000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                            ('pay-8', 'C1-M8', 'col-1', 90000, 'C1-A-CASH', 'CASH', '2026-01-01');


