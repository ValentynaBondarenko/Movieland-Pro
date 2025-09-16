INSERT INTO users (name, email, password, role)
VALUES
    ('Рональд Рейнольдс', 'ronald.reynolds66@example.com', '$2a$10$DoD4pxn7JfHB2xyEukxzquyZXm1W6gJE24CEkX9H3nvow6oLPQ6vS', 'ADMIN'),
    ('Дарлін Едвардс', 'darlene.edwards15@example.com', '$2a$10$plDTe25ciQ.s.aLcVkEWludU5MFozlg5Gw44z9ZtqzDRi1KPPqWay', 'User'),
    ('Габріель Джексон', 'gabriel.jackson91@example.com', '$2a$10$LKiBXj5EgcMm3eebg3H9Uef6yKhw8blzY6adN4GuS3exMn1bd9fxu', 'User'),
    ('Деріл Брайант', 'daryl.bryant94@example.com', '$2a$10$pNt2HB0yUFAMgT5.w5Ctd.wvDEMq9yVs.ynCqRX4CbzmpTvRkr/e2', 'User'),
    ('Ніл Паркер', 'neil.parker43@example.com', '$2a$10$Ie/kNpm.qKdGqXHs.XUDvORwuNaGjjN0g7Ea5aTyyQnqiXeXf45QC', 'User'),
    ('Тревіс Райт', 'travis.wright36@example.com', '$2a$10$wLvmlEN2XS2LA2Yq5j.xfu6cmI3TXHMu9Hfdg7dinSNEoFu.7zjLK', 'User'),
    ('Амелія Кеннеді', 'amelia.kennedy58@example.com', '$2a$10$OByknBWbLBfdvZhHBiK.KeA5HKUDkc..WfiH1Sn/tTRsnw63sGvoK', 'User'),
    ('Айда Девіс', 'ida.davis80@example.com', '$2a$10$MWqNG3gWIi8DAXHI9SkPEe7BrGyScHBMbYm76CekQpIzlo597d2LG', 'User'),
    ('Джессі Паттерсон', 'jessie.patterson68@example.com', '$2a$10$BobdJmrbCs6VEKW6kBpbUu4pZI62kM2aNVkyVhzbpx2po2RzxzHli', 'User'),
    ('Деніс Крейг', 'dennis.craig82@example.com', '$2a$10$ARHGC5oZD1TPr8UzcYeP9uG3P6QWTPTOWK4acpvOzxZP1I7MWoyK.', 'User')

ON CONFLICT (email) DO NOTHING;

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Втеча з Шоушенка', 'The Shawshank Redemption', 1994,
        'Два засуджені утворюють глибоку дружбу в в''язниці Шоушенк.', 9.3, 19.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Зелена миля', 'The Green Mile', 1999, 'Історія про дивовижні події в смертній камері в''язниці.', 8.6, 17.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Форрест Гамп', 'Forrest Gump', 1994, 'Життєвий шлях простої людини, що впливає на історію США.', 8.8, 15.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR2,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Список Шиндлера', 'Schindler''s List', 1993,
        'Драматична історія німецького промисловця, який врятував понад тисячу євреїв.', 8.9, 18.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SX140_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('1+1', 'Intouchables', 2011, 'Історія дружби багатого інваліда та його доглядача.', 8.5, 14.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMTYxNDA3MDQwNl5BMl5BanBnXkFtZTcwNTU4Mzc1Nw@@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Початок', 'Inception', 2010, 'Група крадіїв проникає в сни, щоб викрасти секрети.', 8.8, 16.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Життя прекрасне', 'La vita è bella', 1997,
        'Чоловік намагається захистити сина в часи війни, перетворюючи життя у гру.', 8.6, 13.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Бійцівський клуб', 'Fight Club', 1999, 'Історія про двох чоловіків, які заснували нелегальний клуб бійок.',
        8.8, 17.50,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BZGY5Y2RjMmItNDg5Yy00NjUwLThjMTEtNDc2OGUzNTBiYmM1XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Зоряні війни: Епізод IV – Нова надія', 'Star Wars: Episode IV – A New Hope', 1977,
        'Молода героїня та повстанці борються проти імперії в галактиці.', 8.6, 18.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BYTUwNTdiMzMtNThmNS00ODUzLThlMDMtMTM5Y2JkNWJjOGQ2XkEyXkFqcGdeQXVyNzQ1ODk3MTQ@._V1._SX140_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Зоряні війни: Епізод V – Імперія завдає удару у відповідь', 'Star Wars: Episode V – The Empire Strikes Back',
        1980, 'Повстанці продовжують боротьбу з імперією, але зазнають важких втрат.', 8.7, 18.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BYmViY2M2MTYtY2MzOS00YjQ1LWIzYmEtOTBiNjhlMGM0NjZjXkEyXkFqcGdeQXVyNDYyMDk5MTU@._V1._SX140_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Віднесені привидами', 'Spirited Away', 2001, 'Дівчина потрапляє в магічний світ і шукає шлях додому.', 8.6,
        15.50,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BOGJjNzZmMmUtMjljNC00ZjU5LWJiODQtZmEzZTU0MjBlNzgxL2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Титанік', 'Titanic', 1997, 'Трагічна історія кохання на борту легендарного лайнера.', 7.8, 16.00,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1._SY209_CR0,0,140,209_.jpg');
INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Пролітаючи над гніздом зозулі', 'One Flew Over the Cuckoo''s Nest', 1975,
        'Чоловік у психіатричній лікарні боровся за свободу.', 8.7, 14.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BZjA0OWVhOTAtYWQxNi00YzNhLWI4ZjYtNjFjZTEyYjJlNDVlL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Ходячий замок', 'Howl''s Moving Castle', 2004,
        'Дівчина потрапляє у чарівний замок і переживає дивовижні пригоди.', 8.2, 13.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BZTRhY2QwM2UtNWRlNy00ZWQwLTg3MjktZThmNjQ3NTdjN2IxXkEyXkFqcGdeQXVyMzg2MzE2OTE@._V1._SY209_CR5,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Гладіатор', 'Gladiator', 2000, 'Римський генерал стає гладіатором і шукає помсту.', 8.5, 16.50,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMDliMmNhNDEtODUyOS00MjNlLTgxODEtN2U3NzIxMGVkZTA1L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Великий куш', 'Snatch', 2000, 'Кримінальна комедія про крадіжку діамантів.', 8.3, 14.50,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMTA2NDYxOGYtYjU1Mi00Y2QzLTgxMTQtMWI1MGI0ZGQ5MmU4XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1._SY209_CR1,0,140,209_.jpg');

INSERT INTO movies (name_ukrainian, name_native, year_of_release, description, rating, price, poster)
VALUES ('Темний лицар', 'The Dark Knight', 2008, 'Боротьба Бетмена проти Джокера у Готемі.', 9.0, 19.99,
        'https://images-na.ssl-images-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO reviews (movie_id, user_id, review)
VALUES (1, 1, 'Вважаю, цей фільм має бути в колекції кожного поважного кіномана.'),
       (2, 2,
        'Вічний шедевр світового кінематографа, який можна переглядати десятки разів і отримувати той самий — вибачте за вираз — кайф від перегляду. У фільму, звичайно, є мінуси, але, чорт забирай, їх просто не хочеться помічати! Ти настільки захоплений переглядом, що просто не хочеш придиратися до дрібниць.'),
       (1, 3,
        'Фільм лише виграє від частого перегляду і завжди піднімає мені настрій (поряд із драмою, тут ще й тонкий гумор).'),
       (3, 4,
        'Безперечно культовий фільм, нереалістичний, наївний, іноді дурнуватий, але такий же захопливий і дивовижний, як і багато років тому.'),
       (4, 4,
        'У підсумку ми маємо відмінного представника свого жанру, який пройшов перевірку часом і досі чудово виглядає, незважаючи на класичний сюжет.'),
       (5, 5, 'Скажу тільки одне — як шкодую, що не подивилася цей фільм раніше!');
