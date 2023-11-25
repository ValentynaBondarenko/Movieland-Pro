create TABLE IF NOT EXISTS country(
  country_id SERIAL PRIMARY KEY,
  country_name VARCHAR(255) NOT NULL
);

insert into country (country_name)
values
    ('США'),
    ('Франція'),
    ('Великобританія'),
    ('Італія'),
    ('Німеччина'),
    ('Японія'),
    ('Іспанія');
