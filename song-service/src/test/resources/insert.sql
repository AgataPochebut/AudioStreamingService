INSERT INTO genres (id, name) VALUES (1, 'test');
INSERT INTO artists (id, name) VALUES (1, 'test');
INSERT INTO albums (id, title) VALUES (1, 'test');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (1, 1);
INSERT INTO album_artist (album_id, artist_id) VALUES (1, 1);
INSERT INTO resources (id, name, type) VALUES (1, 'test', 'S3');
INSERT INTO songs (id, title, resource_id) VALUES (1, 'test', 1);