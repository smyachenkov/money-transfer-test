create schema if not exists public;

create table account (
   id identity primary key,
   account_number integer,
   name varchar(100) not null,
   balance decimal(20,2) not null
);

create table transfer_transaction (
   id identity primary key,
   sender bigint references account(id),
   receiver bigint references account(id),
   amount decimal(20,2) not null,
   tx_date timestamp default CURRENT_TIMESTAMP
);
