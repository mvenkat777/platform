
INSERT INTO DEALERS (ID, NAME, CREATED_AT, UPDATED_AT) VALUES
  (1, 'Dealer 1', to_date('2020-05-25', 'YYYY-MM-DD'), to_date('2020-05-26', 'YYYY-MM-DD')),
  (2, 'Dealer 2', to_date('2020-05-27', 'YYYY-MM-DD'), to_date('2020-05-27', 'YYYY-MM-DD')),
  (3, 'Dealer 3', to_date('2020-05-28', 'YYYY-MM-DD'), to_date('2020-05-28', 'YYYY-MM-DD'));

INSERT INTO PROVIDERS (ID, NAME, DEALER_ID, CREATED_AT, UPDATED_AT) VALUES
  (1, 'BMV Provider', 1, to_date('2020-05-25', 'YYYY-MM-DD'), to_date('2020-05-25', 'YYYY-MM-DD')),
  (2, 'AUDI Provider', 2, to_date('2020-05-26', 'YYYY-MM-DD'), to_date('2020-05-26', 'YYYY-MM-DD')),
  (3, 'TESLA Provider', 3, to_date('2020-05-27', 'YYYY-MM-DD'), to_date('2020-05-27', 'YYYY-MM-DD'));