DROP DATABASE app;
CREATE DATABASE IF NOT EXISTS app;
use app;
CREATE TABLE IF NOT EXISTS Users (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	type INT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Users_archive (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	type INT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Subcategories (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Categories (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Messages (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	content VARCHAR(255) NOT NULL,
	start_time BIGINT NOT NULL,
	end_time BIGINT NOT NULL,
	location_lat DECIMAL(7, 4) NOT NULL,
	location_lon DECIMAL(7, 4) NOT NULL,
	location_range DECIMAL(7, 4) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Messages_archive (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	content VARCHAR(255) NOT NULL,
	start_time BIGINT NOT NULL,
	end_time BIGINT NOT NULL,
	location_lat DECIMAL(7, 4) NOT NULL,
	location_lon DECIMAL(7, 4) NOT NULL,
	location_range DECIMAL(7, 4) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Verification (
	email VARCHAR(255) NOT NULL,
	hash VARCHAR(255) NOT NULL,
	sent_time BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS Subscription (
	uid INT NOT NULL,
	sid INT NOT NULL,
	PRIMARY KEY(uid, sid),
	FOREIGN KEY (uid) REFERENCES Users(id),
	FOREIGN KEY (sid) REFERENCES Subcategories(id)
);

CREATE TABLE IF NOT EXISTS Sub_of (
	sid INT NOT NULL,
	cid INT NOT NULL,
	PRIMARY KEY(sid, cid),
	FOREIGN KEY (sid) REFERENCES Subcategories(id),
	FOREIGN KEY (cid) REFERENCES Categories(id)
);

CREATE TABLE IF NOT EXISTS Publish (
	uid INT NOT NULL,
	mid INT NOT NULL,
	time BIGINT NOT NULL,
	PRIMARY KEY(uid, mid),
	FOREIGN KEY (uid) REFERENCES Users(id),
	FOREIGN KEY (mid) REFERENCES Messages(id)
);

CREATE TABLE IF NOT EXISTS Published (
	uid INT NOT NULL,
	mid INT NOT NULL,
	time INT NOT NULL,
	PRIMARY KEY(uid, mid),
	FOREIGN KEY (uid) REFERENCES Users(id),
	FOREIGN KEY (mid) REFERENCES Archive(id)
);

CREATE TABLE IF NOT EXISTS Belong (
	mid INT NOT NULL,
	sid INT NOT NULL,
	PRIMARY KEY(mid, sid),
	FOREIGN KEY (mid) REFERENCES Messages(id),
	FOREIGN KEY (sid) REFERENCES Subcategories(id)
);

CREATE TABLE IF NOT EXISTS Belonged (
	mid INT NOT NULL,
	sid INT NOT NULL,
	PRIMARY KEY(mid, sid),
	FOREIGN KEY (mid) REFERENCES Archive(id),
	FOREIGN KEY (sid) REFERENCES Subcategories(id)
);

INSERT INTO users(name, email, address, username, password, type) 
VALUES("Nick", "nicklin1219@gmail.com", "College Park", "nicklin", "202cb962ac59075b964b07152d234b70", 1);
INSERT INTO categories(name) VALUES("sports");
INSERT INTO categories(name) VALUES("food");
INSERT INTO subcategories(name) VALUES("basketball");
INSERT INTO subcategories(name) VALUES("football");
INSERT INTO subcategories(name) VALUES("baseball");
INSERT INTO sub_of(cid, sid) VALUES(1, 1);
INSERT INTO sub_of(cid, sid) VALUES(1, 2);
INSERT INTO sub_of(cid, sid) VALUES(1, 3);
INSERT INTO subscription(uid, sid) VALUES(1, 1);
INSERT INTO messages(title, content, start_time, end_time, location_lat, location_lon, location_range)
VALUES("UMD Baseball", "UMD beats Mars today!", 1543618829188, 1543628829188, 38.9897, 76.9378, 0.05);
INSERT INTO belong(mid, sid) VALUES(1, 1);
