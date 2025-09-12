ALTER SEQUENCE vacancy_seq INCREMENT BY 50;
ALTER SEQUENCE vacancy_seq CACHE 50;

ALTER SEQUENCE vacancy_seq OWNED BY vacancies.id;

ALTER TABLE vacancies ALTER COLUMN id SET DEFAULT nextval('vacancy_seq');

SELECT setval('vacancy_seq', (SELECT COALESCE(MAX(id), 0) FROM vacancies), true);
