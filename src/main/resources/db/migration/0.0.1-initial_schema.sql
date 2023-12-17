create type roles as ENUM ('Guest', 'User', 'Admin');

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role roles NOT NULL
);

CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    name_ua VARCHAR(255) NOT NULL,
    name_native VARCHAR(255) NOT NULL,
    release_year INTEGER NOT NULL,
    description TEXT NOT NULL,
    rating DECIMAL(3, 1) NOT NULL,
    price DECIMAL(5, 2) NOT NULL,
    poster VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres(
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS movies_genres (
    movie_id INT REFERENCES movies(id),
    genre_id INT REFERENCES genres(id),
    PRIMARY KEY (movie_id, genre_id)
);

CREATE TABLE IF NOT EXISTS countries(
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS movies_countries (
    movie_id INT REFERENCES movies(id),
    country_id INT REFERENCES countries(id),
    PRIMARY KEY (movie_id, country_id)
);

CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    movie_id INT NOT NULL,
    user_id INT NOT NULL,
    review TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO genres (id, name)
VALUES
    (1, 'Драма'),
    (2, 'Кримінал'),
    (3, 'Фентезі'),
    (4, 'Детектив'),
    (5, 'Мелодрама'),
    (6, 'Біографія'),
    (7, 'Комедія'),
    (8, 'Фантастика'),
    (9, 'Бойовик'),
    (10, 'Триллер'),
    (11, 'Пригоди'),
    (12, 'Аніме'),
    (13, 'Мультфільм'),
    (14, 'Сімейний'),
    (15, 'Вестерн'),
    (16, 'Анімація');

insert into countries (id, name)
values
    (1, 'США'),
    (2, 'Франція'),
    (3, 'Великобританія'),
    (4, 'Італія'),
    (5, 'Німеччина'),
    (6, 'Японія'),
    (7, 'Іспанія');