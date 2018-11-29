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
	content VARCHAR(255) NOT NULL,
	start_time INT NOT NULL,
	end_time INT NOT NULL,
	location_lon INT NOT NULL,
	location_lat INT NOT NULL,
	location_range INT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Archive (
	id INT NOT NULL AUTO_INCREMENT,
	content VARCHAR(255) NOT NULL,
	start_time INT NOT NULL,
	end_time INT NOT NULL,
	location_lon INT NOT NULL,
	location_lat INT NOT NULL,
	location_range INT NOT NULL,
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
	time INT NOT NULL,
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
