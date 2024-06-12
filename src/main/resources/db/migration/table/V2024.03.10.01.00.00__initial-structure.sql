CREATE TABLE budget_category (
    id                      VARCHAR(255) PRIMARY KEY,
    name                    VARCHAR(255) NOT NULL
);

CREATE TABLE budget (
    id                      VARCHAR(255) PRIMARY KEY,
    amount                  DECIMAL(10, 2) NOT NULL,
    category_id             VARCHAR(255) NOT NULL,
    user_id                  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES budget_category(id)
);
