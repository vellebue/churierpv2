create table C_ADDRESS_TYPES(TYPE_ID varchar(4) PRIMARY KEY, DESCRIPTION varchar(50) NOT NULL, KEY varchar(100) NOT NULL);

create table C_COUNTRIES(COUNTRY_ID varchar(2) PRIMARY KEY, NAME varchar(100) NOT NULL, KEY varchar(512) NOT NULL);

create table C_REGIONS(COUNTRY_ID varchar(2) REFERENCES C_COUNTRIES(COUNTRY_ID), REGION_ID varchar(10),
                       NAME varchar(100) NOT NULL, KEY varchar(512) NOT null,
                       primary key(COUNTRY_ID, REGION_ID));

create table ADDRESSES(ADDRESS_ID int PRIMARY KEY, TYPE_ID varchar(4) NOT NULL REFERENCES C_ADDRESS_TYPES(TYPE_ID),
                       CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                       ADDRESS varchar(512) NOT NULL, POSTAL_CODE varchar(15) NOT NULL, CITY varchar(100) NOT NULL,
                       COUNTRY_ID varchar(2) NOT NULL REFERENCES C_COUNTRIES(COUNTRY_ID),
                       REGION_ID varchar(10) NOT NULL,
                       constraint fk_addresses_c_regions FOREIGN KEY (COUNTRY_ID, REGION_ID) REFERENCES C_REGIONS(COUNTRY_ID, REGION_ID));

create sequence SEQ_ADRESSES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by ADDRESSES.ADDRESS_ID;

create table SOCIETIES(SOCIETY_ID int PRIMARY KEY, NAME varchar(100),COMMERCIAL_NAME varchar(100) , SOCIAL_NAME varchar(100), VAT_NUMBER varchar(15),
                       CREATION_USER VARCHAR(100) NOT NULL, CREATION_TIME TIMESTAMP NOT NULL, UPDATE_USER VARCHAR(100) NOT NULL, UPDATE_TIME TIMESTAMP NOT NULL,
                       ADDRESS_ID INT NOT NULL REFERENCES ADDRESSES(ADDRESS_ID));

create sequence SEQ_SOCIETIES
    increment by 1
    minvalue 0
    maxvalue 999999999
    start with 0
    cycle
    owned by SOCIETIES.SOCIETY_ID;