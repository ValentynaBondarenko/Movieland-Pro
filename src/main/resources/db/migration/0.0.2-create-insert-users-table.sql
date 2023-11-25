CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL
);

INSERT INTO users (name, email, password, role)
VALUES
    ('Рональд Рейнольдс', 'ronald.reynolds66@example.com', 'paco', 'Guest'),
    ('Дарлін Едвардс', 'darlene.edwards15@example.com', 'bricks', 'Guest'),
    ('Габріель Джексон', 'gabriel.jackson91@example.com', 'hjkl', 'Guest'),
    ('Дерил Брайант', 'daryl.bryant94@example.com', 'exodus', 'Guest'),
    ('Ніл Паркер', 'neil.parker43@example.com', '878787', 'Guest'),
    ('Тревіс Райт', 'travis.wright36@example.com', 'smart', 'Guest'),
    ('Амелія Кеннеді', 'amelia.kennedy58@example.com', 'beaker', 'Guest'),
    ('Айда Дейвіс', 'ida.davis80@example.com', 'pepsi1', 'Guest'),
    ('Джессі Паттерсон', 'jessie.patterson68@example.com', 'tommy', 'Guest'),
    ('Деннис Крейг', 'dennis.craig82@example.com', 'coldbeer', 'Guest');



