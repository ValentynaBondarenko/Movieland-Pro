create TABLE IF NOT EXISTS movie (
    movie_id SERIAL PRIMARY KEY,
    name_ua VARCHAR(255) NOT NULL,
    name_native VARCHAR(255) NOT NULL,
    release_year INTEGER NOT NULL,
    description TEXT NOT NULL,
    rating DECIMAL(3, 1) NOT NULL,
    price DECIMAL(5, 2) NOT NULL,
    poster VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS movie_genre (
    movie_id INT REFERENCES movie(movie_id),
    genre_id INT REFERENCES genre(genre_id),
    PRIMARY KEY (movie_id, genre_id)
);

create TABLE IF NOT EXISTS movie_country (
    movie_id INT REFERENCES movie(movie_id),
    country_id INT REFERENCES country(country_id),
    PRIMARY KEY (movie_id, country_id)
);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Втеча з Шоушенка', 'The Shawshank Redemption', 1994,
    'Успішний банкір Енді Дюфрейн обвинувачений у вбивстві власної дружини і її коханця. Заарештований та висланий до вязниці під назвою Шоушенк, він зіткнувся з жорстокістю і беззаконням, що панує з обох сторін ґрат. Кожен, хто потрапляє в ці стіни, стає рабом до кінця свого життя. Але Енді, збройний розумом і доброю душею, відмовляється миритися з вироком долі і розробляє неймовірно сміливий план свого звільнення.',
    8.9, 123.45, 'https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (1, 1),
    (1, 2);
    
insert into movie_country (movie_id, country_id)
values
    (1, 1);


insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Зелена миля', 'The Green Mile', 1999,
    'Обвинувачений у жахливому злочині, Джон Коффі опиняється в блоку смертників вязниці "Холодна гора". Він має надзвичайний зріст і завжди спокійний, але це не впливає на ставлення начальника блоку Пола Еджкомба, який звик виконувати смертний вирок.',
    8.9, 134.67, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTUxMzQyNjA5MF5BMl5BanBnXkFtZTYwOTU2NTY3._V1._SY209_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (2, 1),
    (2, 2),
    (2, 3),
    (2, 4);
    
insert into movie_country (movie_id, country_id)
values
    (2, 1);

insert into movie (name_ua, name_native, release_year,  description, rating, price, poster)
values ('Форрест Гамп', 'Forrest Gump', 1994,
    'Зі сторони головного героя Форреста Гампа, простого і доброго чоловіка з благородним і відкритим серцем, розповідається історія його надзвичайного життя. Він неймовірним чином перетворюється у відомого футболіста, героя війни та успішного бізнесмена. Він стає мільярдером, але залишається таким самим простим, добрим і несхитним. Форреста чекає постійний успіх у всьому, і він любить дівчину, з якою дружив у дитинстві, але взаємність приходить занадто пізно.',
    8.6, 200.60, 'https://images-na.ssl-images-amazon.com/images/M/MV5BNWIwODRlZTUtY2U3ZS00Yzg1LWJhNzYtMmZiYmEyNmU1NjMzXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR2,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (3, 1),
    (3, 5);
insert into movie_country (movie_id, country_id)
values
    (3, 1);
    
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values  ('Список Шиндлера', 'Schindlers List', 1993,
    'Фільм розповідає реальну історію загадкового Оскара Шиндлера, члена нацистської партії, успішного фабриканта, який врятував під час Другої світової війни майже 1200 євреїв.',
    8.7, 150.50, 'https://images-na.ssl-images-amazon.com/images/M/MV5BNDE4OTMxMTctNmRhYy00NWE2LTg3YzItYTk3M2UwOTU5Njg4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SX140_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (4, 1),
    (4, 6);
insert into movie_country (movie_id, country_id)
values
    (4, 1);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('1+1', 'Intouchables', 2011,
    'Внаслідок нещасного випадку багатий аристократ Філіпп наймає в помічники чоловіка, який зовсім не підходить для цієї роботи - молодого жителя передмістя Дрісса, щойно вийшовши з в''язниці. Незважаючи на те, що Філіпп перебуває в інвалідному візку, Дріссу вдається внести дух пригод у рутинне життя аристократа.',
    8.3, 120.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTYxNDA3MDQwNl5BMl5BanBnXkFtZTcwNTU4Mzc1Nw@@._V1._SY209_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (5, 1),
    (5, 7),
    (5, 6);
insert into movie_country (movie_id, country_id)
values
    (5, 2);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values  ('Початок', 'Inception', 2010,
    'Кобб - талановитий злодій, кращий з кращих у небезпечному мистецтві вилучення: він краде цінні секрети з глибин підсвідомості під час сну, коли людський розум найбільше вразливий. Рідкісні здібності Кобба зробили його цінним гравцем у світі промислового шпигунства, але вони ж привернули його до вічного бігства і позбавили всього, що він колись кохав.',
    8.6, 130.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1._SY209_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (6, 1),
    (6, 3),
    (6, 4),
    (6, 9),
    (6, 10);
insert into movie_country (movie_id, country_id)
values
    (6, 1),
    (6, 3);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Життя прекрасне', 'La vita è bella', 1997,
    'Під час ІІ Світової війни в Італії в концтабір були відправлені євреї, батько і його маленький син. Дружина, італійка, добровільно пішла за ними. У таборі батько сказав синові, що все, що відбувається навколо, - це дуже велика гра за приз в настоящий танк, який отримає той хлопчик, який зможе не потрапити в поле зору наглядачів. Він зробив все, щоб син повірив у гру і залишився живим, приховуючись в бараку.',
    8.2, 145.99,'https://images-na.ssl-images-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR0,0,140,209_.jpg');
    
insert into movie_genre (movie_id, genre_id)
values
    (7, 1),
    (7, 3),
    (7, 4),
    (7, 9),
    (7, 10);
    
insert into movie_country (movie_id, country_id)
values
    (7, 1),
    (7, 5),
    (7, 7);
    
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Бійцівський клуб', 'Fight Club', 1999,
    'Терзаний хронічною бессонницею і відчайдушно намагаючись вирватися із нудного життя, клерк зустрічає певного Тайлера Дардена, харизматичного торговця милом із зіпсованою філософією. Тайлер впевнений, що самосамовдосконалення — доля слабких, а самознищення — єдине, ради чого варто жити.',
    8.4, 119.99, 'https://images-na.ssl-images-amazon.com/images/M/MV5BZGY5Y2RjMmItNDg5Yy00NjUwLThjMTEtNDc2OGUzNTBiYmM1XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg');
  
insert into movie_genre (movie_id, genre_id)
values
    (8, 1),
    (8, 2),
    (8, 10);
    
insert into movie_country (movie_id, country_id)
values
    (8, 1),
    (8, 5); 
    
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Зоряні війни: Епізод 4 – Нова надія', 'Star Wars', 1977,
    'Татуїн. Пустельна планета. Вже постарілий лицар Джедай Обі Ван Кенобі рятує молодого Люка Скайвокера, коли той намагається знайти втраченого дроїда. З цього моменту Люк розуміє своє справжнє призначення: він один із лицарів Джедай. У той час як громадянська війна охопила галактику, а повстанські війська ведуть бої проти сил злого Імператора, до Люка і Обі Вана приєднується відчайдушний пілот-найманець Ган Соло, і в супроводі двох дроїдів, R2D2 і C-3PO, ця незвичайна команда вирушає на пошуки лідера повстанців — принцеси Леї. Героям належить відчайдушна битва із жахливим Дартом Вейдером — правою рукою Імператора і його секретною зброєю — «Зіркою Смерті».',
    8.1, 198.98,'https://images-na.ssl-images-amazon.com/images/M/MV5BYTUwNTdiMzMtNThmNS00ODUzLThlMDMtMTM5Y2JkNWJjOGQ2XkEyXkFqcGdeQXVyNzQ1ODk3MTQ@._V1._SX140_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (9, 3),
    (9, 8),
    (9, 11),
    (9, 9);
    
insert into movie_country (movie_id, country_id)
values
    (9, 1);
        
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Зоряні війни: Епізод 5 – Імперія наносить відповідний удар', 'Star Wars: Episode V - The Empire Strikes Back', 1980,
    'Боротьба за Галактику загострюється в пятому епізоді космічної саги. Війська Імператора розпочинають масовану атаку на повстанців і їх союзників. Ган Соло та принцеса Лея приховуються в Хмарному Місті, де їх захоплює Дарт Вейдер, у той час як Люк Скайвокер знаходиться на таємничій планеті джунглів Дагоба. Там Майстер — лицар Джедай Йода навчає молодого лицаря навичкам здобуття Сили. Люк навіть не уявляє, як скоро йому доведеться використовувати знання старого Майстра: перед ним битва із переважаючими силами Імператора і смертельний поєдинок із Дартом Вейдером.',
    8.2, 198.98, 'https://images-na.ssl-images-amazon.com/images/M/MV5BYmViY2M2MTYtY2MzOS00YjQ1LWIzYmEtOTBiNjhlMGM0NjZjXkEyXkFqcGdeQXVyNDYyMDk5MTU@._V1._SX140_CR0,0,140,209_.jpg');
insert into movie_genre (movie_id, genre_id)
values
    (10, 3),
    (10, 8),
    (10, 9),
    (10, 11);
    
insert into movie_country (movie_id, country_id)
values
    (10, 1);
    
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Подорожі Чіхіро у країну привидів', 'Sen to Chihiro no kamikakushi', 2001,
    'Маленька Чіхіро разом із матірю та батьком переїжджає в новий будинок. Заблудившись по дорозі, вони опиняються в дивному порожньому місті, де їх чекає величезний бенкет. Батьки з жадібністю кидаються на їжу, і до жаху дівчинки перетворюються в свиней, стаючи полоненими злої відьми Юбаби, владаря таємничого світу стародавніх богів і могутніх духів.',
    8.6, 145.90,'https://images-na.ssl-images-amazon.com/images/M/MV5BOGJjNzZmMmUtMjljNC00ZjU5LWJiODQtZmEzZTU0MjBlNzgxL2ltYWdlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1._SY209_CR0,0,140,209_.jpg');
    
insert into movie_genre (movie_id, genre_id)
values
    (11, 3),
    (11, 11),
    (11, 12),
    (11, 14),
    (11, 16);
    
insert into movie_country (movie_id, country_id)
values
    (11, 6);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Титанік', 'Titanic', 1997,
    'Молоді закохані Джек і Роза знаходять одне одного під час першого і останнього рейсу "непотопляемого" Титаніка. Вони не могли уявити, що шикарний лайнер зіткнеться з айсбергом в холодних водах Північного Атлантику, і їх палаюча любов перетвориться на боротьбу за виживання...',
    7.9, 150.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMDdmZGU3NDQtY2E5My00ZTliLWIzOTUtMTY4ZGI1YjdiNjk3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1._SY209_CR0,0,140,209_.jpg');
insert into movie_genre (movie_id, genre_id)
values
    (12, 1),
    (12, 5);
    
insert into movie_country (movie_id, country_id)
values
    (12, 1);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Пролітаючи над гніздом зозулі', 'One Flew Over the Cuckoos Nest', 1975,
    'Заради уникнення вязниці, Рендл Патрік МакМерфі симулює душевний розлад і потрапляє в психіатричну клініку, де жорстока медсестра Мілдрід Ретчед майже абсолютною владаркою. МакМерфі дивується тому, що інші пацієнти змирилися з існуючим становищем, а деякі навіть свідомо прийшли до лікарні, ховаючись від жахливого зовнішнього світу. І він вирішує встати на бунт. В одиночку.',
    8.7, 180.00,'https://images-na.ssl-images-amazon.com/images/M/MV5BZjA0OWVhOTAtYWQxNi00YzNhLWI4ZjYtNjFjZTEyYjJlNDVlL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1._SY209_CR0,0,140,209_.jpg');
insert into movie_genre (movie_id, genre_id)
values
    (13, 1);
    
insert into movie_country (movie_id, country_id)
values
    (13, 1);


insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Замок що рухається', 'Hauru no ugoku shiro', 2004,
    'Зла відьма закляла 18-річну Софі і перетворила її на стару жінку. Щоб повернутися до свого справжнього вигляду, Софі шукає допомоги в могутнього чарівника Хаула та його демона Кальцифера. Кальцифер зобовязаний служити Хаулу згідно з угодою, деталі якої він не може розголошувати. Дівчина та демон вирішують допомагати одне одному позбавитися злих чаклунств...',
    8.2, 130.50, 'https://images-na.ssl-images-amazon.com/images/M/MV5BZTRhY2QwM2UtNWRlNy00ZWQwLTg3MjktZThmNjQ3NTdjN2IxXkEyXkFqcGdeQXVyMzg2MzE2OTE@._V1._SY209_CR5,0,140,209_.jpg');
insert into movie_genre (movie_id, genre_id)
values
    (14, 16),
    (14, 13),
    (14, 3),
    (14, 11);

insert into movie_country (movie_id, country_id)
values
    (14, 6);
    
insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Гладіатор', 'Gladiator', 2000,
    'У великій Римській імперії не було полководця, який би дорівнював генералу Максимусу. Непереможні легіони, якими керував цей благородний воїн, вболівали за нього і могли слідувати за ним навіть у пекло. Але сталося так, що відважний Максимус, готовий зіткнутися з будь-яким супротивником у чесному бою, опинився безсилля проти зрадливих дворянських інтриг. Генерала зрадили і засудили до смерті. З чудом уникнувши загибелі, Максимус стає гладіатором.',
    8.6, 175.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMDliMmNhNDEtODUyOS00MjNlLTgxODEtN2U3NzIxMGVkZTA1L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR0,0,140,209_.jpg');
    
    insert into movie_genre (movie_id, genre_id)
values
    (15, 1),
    (15, 9);

insert into movie_country (movie_id, country_id)
values
    (15,1),
    (15,3);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Великий куш', 'Snatch', 2000,
    'Чотирипалець Френкі мав переправити вкрадений діамант з Англії в США своєму босу Еві. Але, замість цього, герой опинився в епіцентрі великих неприємностей. Зробивши ставку на підпільному боксерському поєдинку, Френкі потрапляє в круговорот дуже небажаних подій. Навколо героя та його вантажу розгортається складна інтрига з участю багатьох колоритних персонажів лондонського підпілля — російського гангстера, трьох нещасних грабіжників, хитрого боксера та зловмисного громилі мафіозі.',
    8.5, 160.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTA2NDYxOGYtYjU1Mi00Y2QzLTgxMTQtMWI1MGI0ZGQ5MmU4XkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1._SY209_CR1,0,140,209_.jpg');
    
     insert into movie_genre (movie_id, genre_id)
values
    (16, 2),
    (16, 7);

insert into movie_country (movie_id, country_id)
values
    (16,1),
    (16,3);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Темний лицар', 'The Dark Knight', 2008,
    'Бетмен підвищує ставки в боротьбі зі злочинністю. За допомогою лейтенанта Джима Гордона та прокурора Харві Дента він має намір очистити вулиці від злочинності, яка отруює місто. Співпраця виявляється ефективною, але незабаром вони виявляють себе серед хаосу, який розпочинається за участю злочинного генія, відомого жахливим городянам як Джокер.',
    8.5, 199.99, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1._SY209_CR0,0,140,209_.jpg');
     
     insert into movie_genre (movie_id, genre_id)
values
    (17, 8),
    (17, 9),
    (17, 10),
    (17, 2),
    (17, 1);

insert into movie_country (movie_id, country_id)
values
    (17,1),
    (17,3);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Як приручити дракона', 'How to Train Your Dragon', 2010,
    'Ви дізнаєтеся історію підлітка Іккінга, якому не надто близькі традиції його героїчного племені, яке десятиліттями веде війну з драконами. Світ Іккінга перевертається з ніг на голову, коли він несподівано зустрічає дракона беззубика, який допоможе йому та іншим вікінгам побачити звичний світ з зовсім іншого боку...',
    8.2, 182.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMjA5NDQyMjc2NF5BMl5BanBnXkFtZTcwMjg5ODcyMw@@._V1._SY209_CR0,0,140,209_.jpg');
 
insert into movie_genre (movie_id, genre_id)
values
    (18, 13),
    (18, 3),
    (18, 7),
    (18, 11),
    (18, 14);

insert into movie_country (movie_id, country_id)
values
    (18,1);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Мовчання ягнят', 'The Silence of the Lambs', 1990,
    'Психопат похоплює та вбиває молодих жінок по всьому Середньому Заходу Америки. ФБР, переконане, що всі злочини вчинені однією і тією ж особою, доручає агентці Кларіссі Старлінг зустрітися з увязненим маніяком, який міг би пояснити слідству психологічні мотиви серійного вбивці та таким чином вивести на його слід.',
    8.3, 150.50, 'https://images-na.ssl-images-amazon.com/images/M/MV5BNjNhZTk0ZmEtNjJhMi00YzFlLWE1MmEtYzM1M2ZmMGMwMTU4XkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1._SY209_CR1,0,140,209_.jpg');
 
 insert into movie_genre (movie_id, genre_id)
values
    (19, 10),
    (19, 2),
    (19, 4),
    (19, 1);

insert into movie_country (movie_id, country_id)
values
    (19,1);

INSERT INTO movie (name_ua, name_native, release_year, description, rating, price, poster)
VALUES ('Гран Торіно', 'Gran Torino', 2008,
    'Уолт Ковальськи, який вийшов на пенсію як автомеханік, проводить дні ремонтуючи щось вдома, попиваючи пиво і, час від часу, відвідуючи парикмахера...', 8.1, 100.50, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTc5NTk2OTU1Nl5BMl5BanBnXkFtZTcwMDc3NjAwMg@@._V1._SY209_CR0,0,140,209_.jpg');

INSERT INTO movie_genre (movie_id, genre_id)
VALUES (20, 1);

INSERT INTO movie_country (movie_id, country_id)
VALUES (20, 1), (20, 5);

INSERT INTO movie (name_ua, name_native, release_year, description, rating, price, poster)
VALUES ('Добрий, поганий, злий', 'Il buono, il brutto, il cattivo', 1966, 'У розпал громадянської війни таємничий стрілець подорожує просторами Дикого Заходу...', 8.5, 130.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BOTQ5NDI3MTI4MF5BMl5BanBnXkFtZTgwNDQ4ODE5MDE@._V1._SX140_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (21, 5);

insert into movie_country (movie_id, country_id)
values
    (21,4),
    (21,5),
    (21,1),
    (21,7);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Приборкання норовливого', 'Il bisbetico domato', 1980,
    'Суворий фермер, який категорично не терпить жінок, щасливо живе і задоволений своїм холостяцьким життям. Проте несподівана жінка, що зявилася в його житті, намагається змінити його погляди на життя та очарувати його. Що з цього вийде...',
    7.7, 120.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTc5NTM5OTY0Nl5BMl5BanBnXkFtZTcwNjg1MjcyMQ@@._V1._SY209_CR3,0,140,209_.jpg');
    
 insert into movie_genre (movie_id, genre_id)
values
    (22, 7);

insert into movie_country (movie_id, country_id)
values
    (22,4);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Блеф', 'Bluff storia di truffe e di imbroglioni' , 1976,
    'Бель Дюк має старі рахунки з Філіппом Бенгом, який відбуває своє покарання за гратами. Щоб розрахуватися з Філіппом, Бель Дюк вступає в змову з іншим шахраєм на імя Фелікс, щоб організувати втечу Філіппа Бенга з вязниці. Втеча вдається, але вони обманюють як Бель Дюк, так і Фелікса, і зникають прямо перед ними. Виявляється, що Філіпп Бенг також не проти помститися Бель Дюку. З цією метою він задумує величезну містифікацію, схожу на покерний блеф...'
    , 7.6, 100.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMjk5YmMxMjMtMTlkNi00YTI5LThhYTMtOTk2NmNiNzQwMzI0XkEyXkFqcGdeQXVyMTQ3Njg3MQ@@._V1._SX140_CR0,0,140,209_.jpg');
 insert into movie_genre (movie_id, genre_id)
values
    (23, 7),
    (23, 2);

insert into movie_country (movie_id, country_id)
values
    (23,4);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Джанго визволений', 'Django Unchained', 2012,
    'Ексцентричний головоріз, також відомий як "Зубний лікар", займається полюванням на найнебезпечніших злочинців. Робота не для слабких духом, і йому потрібен надійний помічник. Але як знайти такого помічника, який не обійдеться дорого? Біглець із невільників на імя Джанго - відмінний кандидат. Проте у нового помічника є свої мотиви, і їх потрібно розглянути...',
    8.5, 170.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMjIyNTQ5NjQ1OV5BMl5BanBnXkFtZTcwODg1MDU4OA@@._V1._SY209_CR0,0,140,209_.jpg');
    
 insert into movie_genre (movie_id, genre_id)
values
    (24, 1),
    (24, 15),
    (24, 11),
    (24, 7);

insert into movie_country (movie_id, country_id)
values
    (24,1);

insert into movie (name_ua, name_native, release_year, description, rating, price, poster)
values ('Танцівник з вовками', 'Dances with Wolves',1990,
    'Дія фільму відбувається в США під час Громадянської війни. Лейтенант американської армії Джон Данбар після поранення в бою просить перевести його на нове місце служби ближче до заход',
    8.5, 170.00, 'https://images-na.ssl-images-amazon.com/images/M/MV5BMTY3OTI5NDczN15BMl5BanBnXkFtZTcwNDA0NDY3Mw@@._V1._SX140_CR0,0,140,209_.jpg');

insert into movie_genre (movie_id, genre_id)
values
    (25, 1),
    (25, 11),
    (25, 15);

insert into movie_country (movie_id, country_id)
values
    (25,1),
    (25,3);