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

create table t_prs
(
	a_prs_id varchar(37),
	a_usr_id varchar(37) not null,
	a_prs_img_url varchar(255) not null,
	a_lat numeric(20,10) null,
	a_long numeric(20,10) null,
	a_cr_dtm timestamp null default timezone('UTC', now()),
	a_upd_dtm timestamp null default timezone('UTC', now()),
	constraint xpkpsr primary key(a_prs_id),
	constraint xukprs unique(a_prs_id, a_usr_id)
);

create table t_usr_rep
(
	a_usr_id varchar(37),
	a_prs_id varchar(37),
	a_rep_id varchar(37),
	constraint xpkusrrep primary key(a_usr_id,a_prs_id,a_rep_id)
);

create table t_invc
(
	a_invc_id varchar(37),
	a_usr_id varchar(37) not null,
	a_prs_id varchar(37) not null,
	a_inv_no varchar(8) not null,
	a_invc_json varchar(600) not null,
	a_cr_dtm timestamp null default timezone('UTC', now()),
	a_upd_dtm timestamp null default timezone('UTC', now()),
	constraint xpkinvc primary key(a_invc_id),
	constraint xukinvc unique(a_invc_id,a_inv_no, a_usr_id)
);

create table t_mdcn
(
	a_mdcn_id varchar(37),
	a_usr_id varchar(37) not null,
	a_mdcn_nm varchar(255) not null,
	a_dsc varchar(255) not null,
	a_mrp numeric(10,2) not null,
	a_sp numeric(10,2) not null,
	a_qty numeric(100) not null,
	a_cr_dtm timestamp null default timezone('UTC', now()),
	a_upd_dtm timestamp null default timezone('UTC', now()),
	constraint xpkmdcn primary key(a_mdcn_id),
	constraint xukmdcn unique(a_mdcn_id,a_usr_id,a_mdcn_nm)
);