INSERT INTO genres (id, name) VALUES (1, 'test');
INSERT INTO artists (id, name) VALUES (1, 'test');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (1, 1);
INSERT INTO albums (id, name) VALUES (1, 'test');
INSERT INTO album_artist (album_id, artist_id) VALUES (1, 1);
INSERT INTO album_genre (album_id, genre_id) VALUES (1, 1);
INSERT INTO resources (id, name, type, bucket_name) VALUES (1, 'test', 'S3', 'test_bucket');
INSERT INTO songs (id, name, resource_id, album_id) VALUES (1, 'test', 1, 1);