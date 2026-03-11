-- Inserting data
INSERT INTO clients (first_name, last_name, email) VALUES
('Robert', 'Lewandowski', 'robert.lewandowski@fcbarcelona.com'),
('Lionel', 'Messi', 'lionel.messi@fcbarcelona.com'),
('Cristiano', 'Ronaldo', 'cristiano.ronaldo@realmadrid.com'),
('Kylian', 'Mbappe', 'kylian.mbappe@psg.fr'),
('Neymar', 'Junior', 'neymar.junior@psg.fr'),
('Luka', 'Modric', 'luka.modric@realmadrid.com'),
('Mohamed', 'Salah', 'mohamed.salah@lfc.co.uk'),
('Kevin', 'De Bruyne', 'kevin.debruyne@mancity.com'),
('Erling', 'Haaland', 'erling.haaland@mancity.com'),
('Zlatan', 'Ibrahimovic', 'zlatan.ibrahimovic@acmilan.com'),
('Andres', 'Iniesta', 'andres.iniesta@fcbarcelona.com'),
('Xavi', 'Hernandez', 'xavi.hernandez@fcbarcelona.com'),
('Gerard', 'Pique', 'gerard.pique@fcbarcelona.com'),
('Sergio', 'Ramos', 'sergio.ramos@realmadrid.com'),
('Karim', 'Benzema', 'karim.benzema@realmadrid.com'),
('David', 'Beckham', 'david.beckham@manutd.com'),
('Ronaldinho', 'Gaucho', 'ronaldinho.gaucho@fcbarcelona.com'),
('Thierry', 'Henry', 'thierry.henry@arsenal.com'),
('Francesco', 'Totti', 'francesco.totti@asroma.com'),
('Wayne', 'Rooney', 'wayne.rooney@manutd.com');

------------------
-- INSERT USERS --
------------------
-- user:user
-- manager:manager
-- admin:admin
INSERT INTO app_users (username, password, enabled) VALUES
('user', '$2b$12$AT9RkuLVMRFuwY57UUvoN.fM6l0BwbLqqaRdXsCXn4iASJOksLfaS', true),
('manager', '$2b$12$UonNVS9gGkjr7Ww7PfUfYeSoAb/CD/LWn4ld1PNAk0JVh4bXqtr1i', true),
('admin', '$2b$12$k76Gs7Ae1gALTZC31Xu8tup7mA2ws.EK6lqA5fKdl9HikIMoCSKAO', true);

-- INSERT AUTHORITIES / ROLES
INSERT INTO app_authorities (username, authority) VALUES
('user', 'ROLE_USER'),
('manager', 'ROLE_USER'),
('manager', 'ROLE_MANAGER'),
('admin', 'ROLE_USER'),
('admin', 'ROLE_MANAGER'),
('admin', 'ROLE_ADMIN');

