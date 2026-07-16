-- Tabela de Inventory
create table inventories
(
    id              VARCHAR(100) primary key,
    total_available BIGINT    not null,
    created_at      TIMESTAMP not null default current_timestamp
);

create index idx_inventories_created_at on inventories (created_at);

-- Tabela de Idempotencia
create table idempotency_keys
(
    idempotency_key          VARCHAR(100) not null primary key,
    request_hash TEXT        not null,
    result_body  TEXT,
    created_at   TIMESTAMP not null default current_timestamp
);