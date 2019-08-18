create table question
(
	id int auto_increment,
	title varchar(50) null,
	description text null,
	gmt__create BIGINT null,
	gmt_modified bigint null,
	creator int null,
	comment_count int default 0 null,
	view_count int default 0 null,
	like_count int default 0 null,
	tags varchar(256) null,
	constraint question_pk
		primary key (id)
);

