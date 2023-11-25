CREATE TABLE IF NOT EXISTS genre(
  genre_id SERIAL PRIMARY KEY,
  genre_name VARCHAR(255) NOT NULL
);

INSERT INTO genre (genre_name)
VALUES
    ('Драма'),
    ('Кримінал'),
    ('Фентезі'),
    ('Детектив'),
    ('Мелодрама'),
    ('Біографія'),
    ('Комедія'),
    ('Фантастика'),
    ('Бойовик'),
    ('Триллер'),
    ('Пригоди'),
    ('Аніме'),
    ('Мультфільм'),
    ('Сімейний'),
    ('Вестерн'),
    ('Анімація');
