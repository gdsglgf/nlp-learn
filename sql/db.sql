
CREATE DATABASE IF NOT EXISTS openie DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use openie;

drop table if exists `t_entity_html`, `t_relation_html`, `t_relation`, `t_relation_type`, `t_entity`, `t_entity_type`, `t_task_item`, `t_task`, `t_html`, `t_link`, `t_url`, `t_host`, `t_file`, `t_directory`;

create table if not exists t_directory(
	dir_id int(10) not null auto_increment,
	disk_id int(10) not null,
	dir_path varchar(128) not null,

	primary key(dir_id)
);

create table if not exists t_file(
	file_id int(10) unsigned not null auto_increment,
	dir_id int(10) not null,
	filename varchar(32) not null,
	bytesize int(10) not null,
	webcount int(10) not null,
	status int(10) not null,

	primary key(file_id),
	constraint `file_dir` foreign key (dir_id) references t_directory(dir_id),
	constraint `uc_file` unique(dir_id, filename)
);

create table if not exists t_task(
	task_id int(10) unsigned not null auto_increment,
	pub_time datetime not null,
	last_modified datetime not null,
	end_time datetime,
	status int(10) not null,

	primary key(task_id)
);

create table if not exists t_task_item(
	item_id int(10) unsigned not null auto_increment,
	task_id int(10) unsigned not null,
	file_id int(10) unsigned not null,

	primary key(item_id),
	constraint `item_task` foreign key (task_id) references t_task(task_id),
	constraint `item_file` foreign key (file_id) references t_file(file_id)
);

create table if not exists t_host(
	host_id int(10) unsigned not null auto_increment,
	hostname varchar(64) not null,

	primary key(host_id),
	constraint `uc_host` unique(hostname)
);

create table if not exists t_url(
	url_id int(10) unsigned not null auto_increment,
	host_id int(10) unsigned not null,
	url varchar(128) not null,

	primary key(url_id),
	constraint `url_host` foreign key (host_id) references t_host(host_id),
	constraint `uc_url` unique(url)
);

create table if not exists t_html(
	html_id int(10) unsigned not null auto_increment,
	url_id int(10) unsigned not null,
	docno char(66) not null,
	title varchar(256),

	primary key(html_id),
	constraint `html_url` foreign key (url_id) references t_url(url_id)
);

-- in_url_id 应该改成html_id是不是会更好???
create table if not exists t_link(
	link_id int(10) unsigned not null auto_increment,
	in_url_id int(10) unsigned not null,
	out_url_id int(10) unsigned not null,
	link_text varchar(128),

	primary key(link_id),
	constraint `link_url1` foreign key (in_url_id) references t_url(url_id),
	constraint `link_url2` foreign key (out_url_id) references t_url(url_id),
	constraint `uc_link` unique(in_url_id, out_url_id)
);

create table if not exists t_entity_type(
	entity_type_id int(10) unsigned not null auto_increment,
	entity_type_pid int(10) unsigned,
	description varchar(32) not null,

	primary key(entity_type_id)
);

create table if not exists t_entity(
	entity_id int(10) unsigned not null auto_increment,
	entity_type_id int(10) unsigned not null,
	name varchar(32) not null,

	primary key(entity_id),
	constraint `entity_type` foreign key (entity_type_id) references t_entity_type(entity_type_id),
	constraint `uc_entity` unique(entity_type_id, name)
);

create table if not exists t_entity_html(
	uid int(10) unsigned not null auto_increment,
	entity_id int(10) unsigned not null,
	html_id int(10) unsigned not null,
	count int(10) default 1,

	primary key(uid),
	constraint `enth_en` foreign key (entity_id) references t_entity(entity_id),
	constraint `enth_th` foreign key (html_id) references t_html(html_id),
	constraint `uc_entity_count` unique(entity_id, html_id)
);

create table if not exists t_relation_type(
	rela_type_id int(10) unsigned not null auto_increment,
	rela_type_pid int(10) unsigned,
	description varchar(32) not null,

	primary key(rela_type_id)
);

create table if not exists t_relation(
	rela_id int(10) unsigned not null auto_increment,
	entity_id1 int(10) unsigned not null,
	entity_id2 int(10) unsigned not null,
	rela_type_id int(10) unsigned not null,

	primary key(rela_id),
	constraint `uc_entity` unique(entity_id1, entity_id2),
	constraint `rela_entity1` foreign key (entity_id1) references t_entity(entity_id),
	constraint `rela_entity2` foreign key (entity_id2) references t_entity(entity_id),
	constraint `rela_type` foreign key (rela_type_id) references t_relation_type(rela_type_id)
);

create table if not exists t_relation_html(
	uid int(10) unsigned not null auto_increment,
	rela_id int(10) unsigned not null,
	html_id int(10) unsigned not null,
	count int(10) default 1,

	primary key(uid),
	constraint `rela_html1` foreign key (rela_id) references t_relation(rela_id),
	constraint `rela_html2` foreign key (html_id) references t_html(html_id)
);
