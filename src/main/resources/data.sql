INSERT INTO opendoor.account (id, username, password) VALUES (1, 'leslie', '$2a$11$6pDFsImmV8N0AcTA0D9dNuJOz2gFFYedoz1ukW7CAteay6sqqfGfK');
INSERT INTO opendoor.account (id, username, password) VALUES (2, 'regalmeagle', '$2a$11$IU1sl..AwDgDa/DEf9FjKO1lQXHWS7szrFqGj.Mn/8ZUexlrca/CO');
INSERT INTO opendoor.account (id, username, password) VALUES (3, 'tommyfresh', '$2a$11$N.A6fRCj4gyibe7aTLPAtu.MUcvXak4cYShFZ3fBhlBw6TLym1A8y');
INSERT INTO opendoor.account (id, username, password) VALUES (4, 'lesliesbff', '$2a$11$Wo5sXTt21Cg7LksosailTeFv0eXa3veXU5SDpm2J4kz/IBWA8kVaW');
INSERT INTO opendoor.account (id, username, password) VALUES (5, 'janet_snakehole', '$2a$11$BqQcZrqtHy.iEp1T/Ws7M.yNZCZrht8pxnAgEKUefkE1WRlH/qP/u');
INSERT INTO opendoor.account (id, username, password) VALUES (6, 'burt_macklin', '$2a$11$F90QhgV58V3ECaWFH7pcxencleAyMk3bQEu/OmTdrh6VNxSqH7j5y');
INSERT INTO opendoor.account (id, username, password) VALUES (7, 'ben_wyatt', '$2a$11$jCpH4PZB8FegWGefJlVad.L32ZznXXzKes3YLtfVKJsUDXfGxtX2a');

INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (1, 'goodbyelilsebastian@pawnee_pnr.gov', 1, 'Leslie', 'Knope', 1);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (2, 'donna@pawnee_pnr.gov', 1, 'Donna', 'Meagle', 2);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (3, 'tom@pawnee_pnr.gov', 1, 'Tom', 'Haverford', 3);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (4, 'ann@pawnee_pnr.gov', 1, 'Ann', 'Perkins', 4);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (5, 'janet_snakehole@pawnee_pnr.gov', 1, 'April', 'Ludgate-Dwyer', 5);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (6, 'andyyyyyyyyyy@pawnee_pnr.gov', 1, 'Andy', 'Dwyer', 6);
INSERT INTO opendoor.user (id, email, enabled, first_name, last_name, account_id) VALUES (7, 'ben_wyatt@pawnee_pnr.gov', 1, 'Ben', 'Wyatt', 7);

INSERT INTO opendoor.connection (id, status, user1_id, user2_id) VALUES (1, 2, 1, 2);
INSERT INTO opendoor.connection (id, status, user1_id, user2_id) VALUES (2, 2, 1, 3);
INSERT INTO opendoor.connection (id, status, user1_id, user2_id) VALUES (3, 2, 1, 4);
INSERT INTO opendoor.connection (id, status, user1_id, user2_id) VALUES (4, 2, 1, 5);
INSERT INTO opendoor.connection (id, status, user1_id, user2_id) VALUES (5, 2, 1, 6);

INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (1, 0, 'Donna Meagle', 2, 1);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (2, 0, 'Tom Haverford', 3, 1);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (3, 0, 'Ann Perkins', 4, 1);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (4, 0, 'April Ludgate-Dwyer', 5, 1);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (5, 0, 'Andy Dwyer', 6, 1);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (6, 1, 'Leslie Knope', 1, 2);
INSERT INTO opendoor.friend (id, available, name, guest_id, host_id) VALUES (7, 1, 'Leslie Knope', 1, 6);

INSERT INTO opendoor.groups (id, name) VALUES (1, 'All Friends');
INSERT INTO opendoor.groups (id, name) VALUES (2, 'All Friends');
INSERT INTO opendoor.groups (id, name) VALUES (3, 'All Friends');
INSERT INTO opendoor.groups (id, name) VALUES (4, 'All Friends');
INSERT INTO opendoor.groups (id, name) VALUES (5, 'All Friends');
INSERT INTO opendoor.groups (id, name) VALUES (6, 'All Friends');

INSERT INTO opendoor.groups_friends (group_id, friends_id) VALUES (1, 1);
INSERT INTO opendoor.groups_friends (group_id, friends_id) VALUES (1, 2);
INSERT INTO opendoor.groups_friends (group_id, friends_id) VALUES (1, 3);
INSERT INTO opendoor.groups_friends (group_id, friends_id) VALUES (1, 4);
INSERT INTO opendoor.groups_friends (group_id, friends_id) VALUES (1, 5);

INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (1, 1);
INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (2, 2);
INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (3, 3);
INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (4, 4);
INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (5, 5);
INSERT INTO opendoor.user_groups (user_id, groups_id) VALUES (6, 6);

INSERT INTO opendoor.availability (id, activity, location) VALUES (1, 'Treat Yo Self', 'Crabtree Valley Mall');
UPDATE opendoor.user SET availability_id = 1 WHERE id = 2;

INSERT INTO opendoor.availability (id, activity, location) VALUES (2, 'Jamminnnnnnnn', 'The Pit');
UPDATE opendoor.user SET availability_id = 2 WHERE id = 6;
