-- ############# CUSTOMER
CREATE DATABASE IF NOT EXISTS customer;
CREATE USER IF NOT EXISTS customer@'%' IDENTIFIED BY 'secret';
GRANT ALL ON customer.* TO customer@'%';


FLUSH PRIVILEGES;
