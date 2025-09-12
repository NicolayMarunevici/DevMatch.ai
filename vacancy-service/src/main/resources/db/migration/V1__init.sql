CREATE SEQUENCE IF NOT EXISTS vacancy_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;


CREATE TABLE IF NOT EXISTS vacancies (
                                         id BIGINT NOT NULL DEFAULT nextval('vacancy_seq'),
                                         title VARCHAR(200) NOT NULL,
                                         description TEXT,
                                         location VARCHAR(120),
                                         company_name VARCHAR(120),
                                         employment_type VARCHAR(20) NOT NULL,       -- FULL_TIME, PART_TIME, CONTRACT...
                                         owner_id BIGINT NOT NULL,                   -- ID владельца вакансии (рекрутёра)
                                         status VARCHAR(16) NOT NULL DEFAULT 'OPEN', -- OPEN, PAUSED, CLOSED
                                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                         version BIGINT NOT NULL DEFAULT 0,

                                         CONSTRAINT pk_vacancy PRIMARY KEY (id)
);


CREATE INDEX IF NOT EXISTS idx_vacancy_owner_id ON vacancies(owner_id);
CREATE INDEX IF NOT EXISTS idx_vacancy_created_at ON vacancies(created_at);
CREATE INDEX IF NOT EXISTS idx_vacancy_status ON vacancies(status);


-- Вставка данных (лучше всегда указывать список колонок)
INSERT INTO vacancies
(id, title, description, location, company_name, employment_type, owner_id, status, created_at)
VALUES
    (1, 'Senior Java Developer',
     'We are looking for a skilled backend developer with experience in Spring Boot and PostgreSQL.',
     'Remote', 'DevMatch AI', 'FULL_TIME', 1, 'OPEN', '2025-08-01 14:16:06.191342+00'),

    (2, 'Frontend Engineer (React + TypeScript)',
     $$Description:
A leading product company is hiring a Frontend Engineer to develop the new UI for its SaaS platform. Tech stack: React 19, TypeScript, TailwindCSS, Zustand. You will work closely with designers and backend developers.
Requirements:

3+ years of professional React experience

Strong JavaScript / TypeScript skills

Experience with REST API and GraphQL

Performance optimization skills for frontend applications
Salary: €3500–€4800 / month$$,
     'Berlin, Germany (гибрид)', 'PixelCraft Studio', 'FULL_TIME', 1, 'OPEN', '2025-08-09 17:28:38.933998+00'),

    (3, 'DevOps Engineer (AWS)',
     $$We are seeking an experienced DevOps Engineer to build a fault-tolerant cloud infrastructure on AWS. The project uses Terraform, Kubernetes, and ArgoCD.
     Requirements:

     Experience with AWS (EKS, S3, RDS)

     Proficiency in Terraform and Kubernetes

     Knowledge of monitoring tools (Prometheus, Grafana)

     GitOps experience is a plus$$,
     'Remote', 'CloudForge', 'FULL_TIME', 2, 'OPEN', '2025-08-09 17:29:41.543808+00'),

    (4, 'Data Scientist (NLP)',
     $$We are looking for a Data Scientist specialized in Natural Language Processing to develop next-gen chatbots. Tech stack: Python, Hugging Face Transformers, LangChain, PostgreSQL.
         Requirements:

Experience with LLMs (GPT, LLaMA, Claude)

Strong Python and PyTorch skills

Experience in text data preprocessing and annotation

RAG system experience is a plus$$,
     'Paris, France (Remote-friendly)', 'AI Dynamics', 'FULL_TIME', 2, 'OPEN', '2025-08-09 17:30:50.634500+00'),

    (5, 'QA Automation Engineer',
     $$We are hiring a QA Automation Engineer to test web and mobile applications. Tech stack: Java, Selenium, Playwright, Appium, TestNG.
         Requirements:

3+ years in test automation

Java or Python test scripting experience

Knowledge of Selenium or Playwright

CI/CD integration experience for automated tests$$,
     'Bucharest, Romania', 'TestPro Labs', 'FULL_TIME', 2, 'OPEN', '2025-08-09 17:31:46.744151+00');

-- Синхронизация последовательности (обязательно, раз вставляли явные id)
SELECT setval('vacancy_seq', (SELECT COALESCE(MAX(id), 0) FROM vacancies), true);
