-- 1. Red Bull Racing
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Red Bull Racing', 'Milton Keynes, UK', 'Christian Horner', 'Red Bull Powertrains', 6)
ON CONFLICT (name) DO NOTHING;

-- 2. Mercedes
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Mercedes', 'Brackley, UK', 'Toto Wolff', 'Mercedes', 8)
ON CONFLICT (name) DO NOTHING;

-- 3. Ferrari
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Ferrari', 'Maranello, Italy', 'Frédéric Vasseur', 'Ferrari', 16)
ON CONFLICT (name) DO NOTHING;

-- 4. McLaren
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('McLaren', 'Woking, UK', 'Andrea Stella', 'Mercedes', 8)
ON CONFLICT (name) DO NOTHING;

-- 5. Aston Martin
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Aston Martin', 'Silverstone, UK', 'Mike Krack', 'Mercedes', 0)
ON CONFLICT (name) DO NOTHING;

-- 6. Alpine
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Alpine', 'Enstone, UK', 'Oliver Oakes', 'Renault', 2)
ON CONFLICT (name) DO NOTHING;

-- 7. Williams
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Williams', 'Grove, UK', 'James Vowles', 'Mercedes', 9)
ON CONFLICT (name) DO NOTHING;

-- 8. RB (Racing Bulls)
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('RB', 'Faenza, Italy', 'Laurent Mekies', 'Red Bull Powertrains', 0)
ON CONFLICT (name) DO NOTHING;

-- 9. Kick Sauber
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Kick Sauber', 'Hinwil, Switzerland', 'Alessandro Alunni Bravi', 'Ferrari', 0)
ON CONFLICT (name) DO NOTHING;

-- 10. Haas
INSERT INTO teams (name, base_location, team_principal, engine_supplier, championships)
VALUES ('Haas F1 Team', 'Kannapolis, USA', 'Ayao Komatsu', 'Ferrari', 0)
ON CONFLICT (name) DO NOTHING;
