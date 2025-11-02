-- Red Bull Racing
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Max', 'Verstappen', 1, 'Dutch', 1)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Yuki', 'Tsunoda', 22, 'Japanese', 8)
ON CONFLICT (number) DO NOTHING;

-- Mercedes
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('George', 'Russell', 63, 'British', 2)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Andrea', 'Kimi Antonelli', 12, 'Italian', 2)
ON CONFLICT (number) DO NOTHING;

-- Ferrari
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Charles', 'Leclerc', 16, 'Monegasque', 3)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Lewis', 'Hamilton', 44, 'British', 3)
ON CONFLICT (number) DO NOTHING;

-- McLaren
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Lando', 'Norris', 4, 'British', 4)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Oscar', 'Piastri', 81, 'Australian', 4)
ON CONFLICT (number) DO NOTHING;

-- Aston Martin
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Fernando', 'Alonso', 14, 'Spanish', 5)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Lance', 'Stroll', 18, 'Canadian', 5)
ON CONFLICT (number) DO NOTHING;

-- Alpine
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Pierre', 'Gasly', 10, 'French', 6)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Jack', 'Doohan', 7, 'Australian', 6)
ON CONFLICT (number) DO NOTHING;

-- Williams
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Alex', 'Albon', 23, 'Thai', 7)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Carlos', 'Sainz', 55, 'Spanish', 7)
ON CONFLICT (number) DO NOTHING;

-- RB (Racing Bulls)
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Liam', 'Lawson', 30, 'New Zealand', 1)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Isack', 'Hadjar', 6, 'French', 8)
ON CONFLICT (number) DO NOTHING;

-- Kick Sauber
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Nico', 'Hulkenberg', 27, 'German', 9)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Gabriel', 'Bortoleto', 5, 'Brazilian', 9)
ON CONFLICT (number) DO NOTHING;

-- Haas
INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Esteban', 'Ocon', 31, 'French', 10)
ON CONFLICT (number) DO NOTHING;

INSERT INTO drivers (first_name, last_name, number, nationality, team_id)
VALUES ('Oliver', 'Bearman', 87, 'British', 10)
ON CONFLICT (number) DO NOTHING;
