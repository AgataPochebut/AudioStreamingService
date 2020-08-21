INSERT INTO genres (id, name) VALUES (1, 'genre');
INSERT INTO artists (id, name) VALUES (1, 'artist');
INSERT INTO albums (id, title) VALUES (1, 'album');
INSERT INTO artist_genre (artist_id, genre_id) VALUES (1, 1);
INSERT INTO album_artist (album_id, artist_id) VALUES (1, 1);
INSERT INTO resources (id, name) VALUES (1, 'resource');
INSERT INTO songs (id, title, resource_id) VALUES (1, 'song', 1);