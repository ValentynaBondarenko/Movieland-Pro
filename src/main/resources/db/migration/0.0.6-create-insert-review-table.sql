CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    movie_id INT NOT NULL,
    user_id INT NOT NULL,
    review TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Втеча з Шоушенка'),
    (SELECT user_id FROM users WHERE name = 'Дарлін Едвардс'),
    'Геніальний фільм! Дивишся і думаєш «Так не буває!», але пізніше розумієш, що саме так і має бути. Починаєш знову осмислювати значення фрази, яку постійно використовуєш у своєму житті, «Надія помирає останньою». Адже якщо ти не надієшся, то все в твоєму житті гасне, не залишається сенсу. Фільм наповнений безліччю правильних афоризмів. Я впевнена, що буду переглядати його сотні разів.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Втеча з Шоушенка'),
    (SELECT user_id FROM users WHERE name = 'Габріель Джексон'),
    'Кіно це, безумовно, «з відзнакою якості». Що ж до першого місця в рейтингу, то, думаю, тут мало місце було для виставлення «десяток» від більшості глядачів разом із надутими відгуками кінокритиків. Фільм атмосферний. Він драматичний. І, звісно, заслуговує на те, щоб знаходитися досить високо в світовому кінематографі.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Зелена миля'),
    (SELECT user_id FROM users WHERE name = 'Рональд Рейнольдс'),
    'Перестав дивуватися тому, що цей фільм займає постійно перше місце у всіляких кіно рейтингах. Особливо я люблю, коли при екранізації літературного твору з нього зникає іронія і зявляється певний сверхреалізм, зобовязаний утримувати глядача при екрані кожну окрему секунду.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Форрест Гамп'),
    (SELECT user_id FROM users WHERE name = 'Ніл Паркер'),
    'Багато ще можна сказати про цей шедевр. І те, що він навчає вірити, і те, щоб ніколи не здаватися... Але найголовніший девіз я побачив ось у цій фразі: «Займайся життям, або займайся смертю».'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Список Шиндлера'),
    (SELECT user_id FROM users WHERE name = 'Тревіс Райт'),
    'Дуже хороший фільм, неординарний сюжет, я б навіть сказав непередбачуваний. Такі фільми вже стали рідкістю.'
);
INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = '1+1'),
    (SELECT user_id FROM users WHERE name = 'Джессі Паттерсон'),
    'У мене не знайдеться слів, щоб описати цей фільм. Не хочеться бути банальною і говорити, наскільки він відмінний, непередбачуваний і т. д., але від цього нікуди не дітися, адже він ШЕДЕВРАЛЬНИЙ!'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = '1+1'),
    (SELECT user_id FROM users WHERE name = 'Амелія Кеннеді'),
    'Скажу відразу — фільм виглядав багатообіцяюче, навіть якщо не брати до уваги те, що він знаходився в топ-250 найкращих фільмів. Відомі голлівудські актори на головних ролях. Але немає в цьому фільмі відповідної атмосфери. Немає такого відчуття, що ось зараз станеться щось страшне. Що герої потрапили в ситуацію, з якої не зможуть вибратися. Загалом, не сподобалося.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Життя прекрасне'),
    (SELECT user_id FROM users WHERE name = 'Габріель Джексон'),
    '«Все повинно бути супер! Супер! Су-пер!» І це саме супер, ну інших слів не підбереш.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Бійцівський клуб'),
    (SELECT user_id FROM users WHERE name = 'Деннис Крейг'),
    'Фільм дуже красивий. Не в усьому, звісно, але яскраві персонажі і костюми — це вже щось.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Як приручити дракона'),
    (SELECT user_id FROM users WHERE name = 'Габріель Джексон'),
    'Цей фільм з розряду тих, що можуть забезпечити гарний відпочинок і піднесений настрій завдяки своїй легкості, зовсім непошлому гумору, помірній дозі напруги та динаміці належних швидкостей.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Як приручити дракона'),
    (SELECT user_id FROM users WHERE name = 'Ніл Паркер'),
    'Призначається Кіношедевром серед розважальних фільмів.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Гладіатор'),
    (SELECT user_id FROM users WHERE name = 'Амелія Кеннеді'),
    'Даний кінофільм — нестаріюча класика світового кінематографа, який можна переглядати до нескінченності і, що дивовижно, він не може набриднути.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Темний лицар'),
    (SELECT user_id FROM users WHERE name = 'Дерил Брайант'),
    'Рекомендую дивитися всім і не звертати уваги на набридле вже врятування цілого світу однією людиною.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Великий куш'),
    (SELECT user_id FROM users WHERE name = 'Дерил Брайант'),
    'Здивований. Ніхто не відгукнувся погано? Насправді було створено твір мистецтва, який подобається всім, і це абсолютно? Ні. Може, я один такий? Фільм не викликає в мене жодних емоцій. Непогана казочка. Чудова наївна атмосфера. Місцями є смішні жарти. І, як мені здалося, цей фільм — свого роду пародія на інші бойовики. При цьому він перевершує багато інших бойовиків.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Подорожі Чіхіро у країну привидів'),
    (SELECT user_id FROM users WHERE name = 'Деннис Крейг'),
    'Несподівано позитивний фільм. Його можна переглядати багато разів, щоб підняти настрій, знаходячи багато смішних, непомітних на перший погляд моментів.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Зоряні війни: Епізод 4 – Нова надія'),
    (SELECT user_id FROM users WHERE name = 'Амелія Кеннеді'),
    'Легендарний. Культовий. Безсмертний. Три слова. Всього лише три. А скільки вони виражають подільних емоцій і радісних вражень щодо ще одного улюбленого і поважаного фільму з минулої дитинства? Багато. Занадто багато. І описати ці серцеві і розгортаються в розсудливому здоровому глузді почуття іноді не представляється можливим.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Великий куш'),
    (SELECT user_id FROM users WHERE name = 'Тревіс Райт'),
    'Приємного перегляду для всіх, хто ще не бачив цього шедевра, більше вражень для тих, хто переглядає вже вдесяте. =)'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Великий куш'),
    (SELECT user_id FROM users WHERE name = 'Ніл Паркер'),
    'Це один з моїх улюблених фільмів з самого дитинства. Я бачила його стільки разів, що знаю практично напам``ять. І можу сказати з упевненістю, що подивлюся ще не один раз.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Гран Торіно'),
    (SELECT user_id FROM users WHERE name = 'Рональд Рейнольдс'),
    'У фільмі є свої невеликі недоліки та неточності, але численні переваги у кілька разів переважають. Багато натхненного креативу!'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Мовчання ягнят'),
    (SELECT user_id FROM users WHERE name = 'Дарлін Едвардс'),
    'Хоч і не за віком мені заводити сорочку з мелодією «Раніше і дерева були вищі, і трава зеленіша…», але хочеться. Виражати свою любов до настільки близького твору дуже складно.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Блеф'),
    (SELECT user_id FROM users WHERE name = 'Айда Дейвіс'),
    'Вердикт: чудова, нестаріюча класика, яку рекомендую всім.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Добрий, поганий, злий'),
    (SELECT user_id FROM users WHERE name = 'Дерил Брайант'),
    'Для недільного вечірнього перегляду підходить за всіма критеріями.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Приборкання норовливого'),
    (SELECT user_id FROM users WHERE name = 'Ніл Паркер'),
    'Добрий і справжньо цікавий фільм, з гарним сюжетом і непоганим музичним супроводом. Всім раджу переглянути.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Джанго визволений'),
    (SELECT user_id FROM users WHERE name = 'Тревіс Райт'),
    'Вважаю, що цей фільм повинен бути в колекції кожного поважаючого себе кіномана.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Танцівник з вовками'),
    (SELECT user_id FROM users WHERE name = 'Амелія Кеннеді'),
    'Нестаріючий шедевр світового кінематографа, який можна переглядати десятки разів і отримувати такий же, вибачте за вираз, кайф від перегляду. Мінуси у фільму, звісно, є, але чорт бери. Їх просто не хочеться помічати! Ти настільки поглиблений у перегляді фільму, що просто не хочеться придиратися до різних дрібниць.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Джанго визволений'),
    (SELECT user_id FROM users WHERE name = 'Айда Дейвіс'),
    'Фільм лише виграє від частого перегляду і завжди піднімає мені настрій (нарівні з драмою, тут ще й тонкий гумор).'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Приборкання норовливого'),
    (SELECT user_id FROM users WHERE name = 'Джессі Паттерсон'),
    'Звісно, безсумнівно, культовий фільм, не реалістичний, наївний, десь глупий, але такий же захоплюючий і дивовижний, як і багато років тому.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Титанік'),
    (SELECT user_id FROM users WHERE name = 'Джессі Паттерсон'),
    'У підсумку у нас є чудовий представник свого жанру, який пройшов перевірку часом і досі чудово дивиться, незважаючи на класичний сюжет.'
);

INSERT INTO review (movie_id, user_id, review)
VALUES (
    (SELECT movie_id FROM movie WHERE name_ua = 'Пролітаючи над гніздом зозулі'),
    (SELECT user_id FROM users WHERE name = 'Деннис Крейг'),
    'Скажу лише одне — як я жалкую, що не подивилася його раніше!'
);
