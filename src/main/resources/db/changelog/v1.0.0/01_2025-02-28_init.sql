create schema parser;

create table parser.bdu (
    id text primary key,
    name text,
    description text
);

create table parser.cwe (
    id text primary key,
    name text,
    uri text,
    bdu_id text references parser.bdu(id)
);

create table parser.capec (
    id text primary key,
    name text,
    likelihood text,
    uri text,
    cwe_id text references parser.cwe(id)
);
