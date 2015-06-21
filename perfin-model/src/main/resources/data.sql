INSERT INTO Currency (code, name) VALUES ('AUD', ''), ('BGN', ''), ('BRL', ''), ('CAD', ''), ('CHF', ''), ('CNY', ''), ('CZK', ''), ('DKK', ''), ('EUR', ''), ('GBP', ''), ('HKD', ''), ('HRK', ''), ('HUF', ''), ('IDR', ''), ('ILS', ''), ('INR', ''), ('JPY', ''), ('KRW', ''), ('MXN', ''), ('MYR', ''), ('NOK', ''), ('NZD', ''), ('PHP', ''), ('PLN', ''), ('RON', ''), ('RUB', ''), ('SEK', ''), ('SGD', ''), ('THB', ''), ('TRY', ''), ('USD', ''), ('ZAR', '');

INSERT INTO User (id, userName, currency) VALUES (1, 'jomarko', 9), (2, 'mmacik', 9), (3, 'tlivora', 7);

INSERT INTO Category (id, name, user) VALUES (1, 'Food', 3), (2, 'Books', 3), (3, 'Sport', 3), (4, 'Salary', 3);

INSERT INTO Resource (id, name, currency, balance, user) VALUES (1, 'CZK', 7, 0, 3), (2, 'Equabank', 7, 1000, 3), (3, 'EUR', 9, 5, 3), (4, 'Fio', 9, 10, 3);
