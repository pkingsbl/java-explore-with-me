DROP TABLE IF EXISTS comments, compilation_events, participations, events, locations, compilations, users, categories;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_categories UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(255) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR(7000)NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(10) NOT NULL,
    title VARCHAR(120) NOT NULL,
    views BIGINT NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT FK_CATEGORY_ID FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_INITIATOR_ID FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_LOCATION_ID FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS participations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(10),
    CONSTRAINT pk_participations PRIMARY KEY (id),
    CONSTRAINT FK_EVENT_ID FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_REQUESTER_ID FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations (id) ON DELETE CASCADE ON UPDATE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pk_compilation_events PRIMARY KEY (compilation_id, event_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(1000) NOT NULL,
    event_id BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE,
    author_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    edited BOOLEAN NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);
