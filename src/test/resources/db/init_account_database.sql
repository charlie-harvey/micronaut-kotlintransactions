-- ############# ACCOUNT
CREATE DATABASE IF NOT EXISTS accounts;
CREATE USER IF NOT EXISTS accounts@'%' IDENTIFIED BY 'secret';
GRANT ALL ON accounts.* TO accounts@'%';

FLUSH PRIVILEGES;
