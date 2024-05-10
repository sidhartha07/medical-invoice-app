create table t_usr
(
	a_usr_id varchar(37),
	a_em varchar(100) not null,
	a_pwd varchar(255) not null,
	a_nm varchar(30) not null,
	a_role varchar(15) not null,
	a_lat numeric(20,10) null,
	a_long numeric(20,10) null,
	a_phn varchar(10) not null,
	a_shp_nm varchar(100) null,
	a_cr_dtm timestamp null default timezone('UTC', now()),
	a_upd_dtm timestamp null default timezone('UTC', now()),
	constraint xpkusr primary key(a_usr_id),
	constraint xuqphn unique(a_phn),
	constraint xuqem unique(a_em)
);

select * from t_usr order by a_upd_dtm desc;