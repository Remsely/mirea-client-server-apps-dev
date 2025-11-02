-- Состоявшиеся гонки 2025
INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Australian Grand Prix', 'Albert Park Circuit', 'Australia', '2025-03-16', 4, 4)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Chinese Grand Prix', 'Shanghai International Circuit', 'China', '2025-03-23', 4, 4)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Japanese Grand Prix', 'Suzuka Circuit', 'Japan', '2025-04-06', 1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Bahrain Grand Prix', 'Bahrain International Circuit', 'Bahrain', '2025-04-13', 5, 3)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Saudi Arabian Grand Prix', 'Jeddah Corniche Circuit', 'Saudi Arabia', '2025-04-20', 5, 3)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Miami Grand Prix', 'Miami International Autodrome', 'USA', '2025-05-04', 4, 4)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Emilia Romagna Grand Prix', 'Autodromo Enzo e Dino Ferrari', 'Italy', '2025-05-18', 1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Monaco Grand Prix', 'Circuit de Monaco', 'Monaco', '2025-05-25', 5, 3)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Spanish Grand Prix', 'Circuit de Barcelona-Catalunya', 'Spain', '2025-06-01', 1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Canadian Grand Prix', 'Circuit Gilles Villeneuve', 'Canada', '2025-06-15', 3, 2)
ON CONFLICT DO NOTHING;

-- Предстоящие гонки 2025
INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Austrian Grand Prix', 'Red Bull Ring', 'Austria', '2025-06-29', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('British Grand Prix', 'Silverstone Circuit', 'United Kingdom', '2025-07-06', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Belgian Grand Prix', 'Circuit de Spa-Francorchamps', 'Belgium', '2025-07-27', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Hungarian Grand Prix', 'Hungaroring', 'Hungary', '2025-08-03', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Dutch Grand Prix', 'Circuit Zandvoort', 'Netherlands', '2025-08-31', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Italian Grand Prix', 'Autodromo Nazionale di Monza', 'Italy', '2025-09-07', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Azerbaijan Grand Prix', 'Baku City Circuit', 'Azerbaijan', '2025-09-21', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Singapore Grand Prix', 'Marina Bay Street Circuit', 'Singapore', '2025-10-05', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('United States Grand Prix', 'Circuit of the Americas', 'USA', '2025-10-19', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Mexico City Grand Prix', 'Autodromo Hermanos Rodriguez', 'Mexico', '2025-10-26', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('São Paulo Grand Prix', 'Autodromo Jose Carlos Pace', 'Brazil', '2025-11-09', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Las Vegas Grand Prix', 'Las Vegas Street Circuit', 'USA', '2025-11-22', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Qatar Grand Prix', 'Lusail International Circuit', 'Qatar', '2025-11-30', NULL, NULL)
ON CONFLICT DO NOTHING;

INSERT INTO races (name, circuit, country, race_date, winner_id, winning_team_id)
VALUES ('Abu Dhabi Grand Prix', 'Yas Marina Circuit', 'UAE', '2025-12-07', NULL, NULL)
ON CONFLICT DO NOTHING;
