INSERT INTO Currency (id, code, name) VALUES (1, 'CZK', 'Czech koruna'), (2, 'EUR', 'Euro'), (3, 'USD', 'US dollar');

INSERT INTO User (id, userName, defaultCurrency) VALUES (1, 'jomarko', 1), (2, 'mmacik', 1), (3, 'tlivora', 1);

INSERT INTO Category (id, name, user_id) VALUES (1, 'Food', 3), (2, 'Books', 3), (3, 'Sport', 3), (4, 'Salary', 3);

INSERT INTO Resource (id, name, currency_id, balance, user_id) VALUES (1, 'CZK', 1, 0, 3), (2, 'Equabank', 1, 1000, 3), (3, 'EUR', 2, 5, 3), (4, 'Fio', 2, 10, 3);
