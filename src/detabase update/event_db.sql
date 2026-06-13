
CREATE TABLE suppliers (
    sup_id INT AUTO_INCREMENT PRIMARY KEY,
    sup_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(150),
    contact_number VARCHAR(15) NOT NULL,
    sup_email VARCHAR(100),
    sup_address VARCHAR(200)
);