
/* Drop Tables */

IF ObJECt_ID('[hst_role]') IS NOT NULL DROP TABLE [hst_role];
IF ObJECt_ID('[hst_setting]') IS NOT NULL DROP TABLE [hst_setting];
IF ObJECt_ID('[hst_static_content]') IS NOT NULL DROP TABLE [hst_static_content];
IF ObJECt_ID('[hst_user]') IS NOT NULL DROP TABLE [hst_user];
IF ObJECt_ID('[mjr_role_action]') IS NOT NULL DROP TABLE [mjr_role_action];
IF ObJECt_ID('[mjr_action]') IS NOT NULL DROP TABLE [mjr_action];
IF ObJECt_ID('[mjr_login_history]') IS NOT NULL DROP TABLE [mjr_login_history];
IF ObJECt_ID('[mjr_user_role]') IS NOT NULL DROP TABLE [mjr_user_role];
IF ObJECt_ID('[mjr_role]') IS NOT NULL DROP TABLE [mjr_role];
IF ObJECt_ID('[mjr_setting]') IS NOT NULL DROP TABLE [mjr_setting];
IF ObJECt_ID('[mjr_user]') IS NOT NULL DROP TABLE [mjr_user];
IF ObJECt_ID('[mjr_static_content]') IS NOT NULL DROP TABLE [mjr_static_content];




/* Create Tables */

CREATE TABLE [hst_role]
(
	[id] bigint,
	[name] nvarchar(20),
	[description] nvarchar(200),
	[transaction_date] datetime DEFAULT getDate() NOT NULL,
	[transaction_type] tinyint DEFAULT 0,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL
);


CREATE TABLE [hst_setting]
(
	[id] bigint,
	[setting_name] nvarchar(50),
	[setting_value] nvarchar(200),
	[setting_type] nvarchar(50),
	[setting_group] nvarchar(50),
	[setting_sub_group] nvarchar(50),
	[transaction_date] datetime DEFAULT getDate() NOT NULL,
	[transaction_type] tinyint DEFAULT 0,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL
);


CREATE TABLE [hst_static_content]
(
	[id] bigint,
	[file_name] nvarchar(max),
	[file_path] nvarchar(max),
	[file_size] varchar(20),
	[file_type] tinyint,
	[transaction_date] datetime DEFAULT getDate() NOT NULL,
	[transaction_type] tinyint DEFAULT 0,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL
);


CREATE TABLE [hst_user]
(
	[id] bigint,
	[login_id] nvarchar(50),
	[content_id] int,
	[age] int,
	[name] nvarchar(50),
	[gender] tinyint,
	[email] nvarchar(50),
	[password] nvarchar(500),
	[nrc] nvarchar(50),
	[phone] nvarchar(50),
	[dob] date,
	[address] nvarchar(max),
	[transaction_date] datetime DEFAULT getDate() NOT NULL,
	[transaction_type] tinyint DEFAULT 0,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL
);


CREATE TABLE [mjr_action]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[module] nvarchar(20) NOT NULL,
	[page] nvarchar(20) NOT NULL,
	[action_name] nvarchar(50) NOT NULL UNIQUE,
	[display_name] nvarchar(50) NOT NULL UNIQUE,
	-- 0[main-action that is the main page action] 
	-- 1[sub-action that process within a page]
	-- 
	[action_type] bit NOT NULL,
	[url] nvarchar(max) NOT NULL,
	[description] nvarchar(200) NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id])
);


CREATE TABLE [mjr_login_history]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[user_id] int NOT NULL,
	[ip_address] nvarchar(45) NOT NULL,
	-- Operating System
	[os] nvarchar(100),
	[user_agent] nvarchar(100),
	[login_date] datetime NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id])
);


CREATE TABLE [mjr_role]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[name] nvarchar(20) NOT NULL UNIQUE,
	[description] nvarchar(200),
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id])
);


CREATE TABLE [mjr_role_action]
(
	[action_id] bigint NOT NULL,
	[role_id] bigint NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	CONSTRAINT complx_action_role UNIQUE ([action_id], [role_id])
);


CREATE TABLE [mjr_setting]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[setting_name] nvarchar(50) NOT NULL UNIQUE,
	[setting_value] nvarchar(200) NOT NULL,
	[setting_type] nvarchar(50) NOT NULL,
	[setting_group] nvarchar(50) NOT NULL,
	[setting_sub_group] nvarchar(50) NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id]),
	CONSTRAINT complx_setting UNIQUE ([setting_name], [setting_value], [setting_type], [setting_group], [setting_sub_group])
);


CREATE TABLE [mjr_static_content]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[file_name] nvarchar(max) NOT NULL,
	[file_path] nvarchar(max) NOT NULL,
	[file_size] varchar(20),
	[file_type] tinyint NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id])
);


CREATE TABLE [mjr_user]
(
	[id] bigint NOT NULL UNIQUE IDENTITY ,
	[content_id] bigint NOT NULL UNIQUE,
	[login_id] nvarchar(50) NOT NULL UNIQUE,
	[age] int NOT NULL,
	[name] nvarchar(50) NOT NULL,
	[gender] tinyint NOT NULL,
	[email] nvarchar(50) NOT NULL UNIQUE,
	[password] nvarchar(500) NOT NULL,
	[nrc] nvarchar(50) NOT NULL,
	[phone] nvarchar(50) NOT NULL,
	[dob] date,
	[address] nvarchar(max),
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	PRIMARY KEY ([id])
);


CREATE TABLE [mjr_user_role]
(
	[user_id] bigint NOT NULL,
	[role_id] bigint NOT NULL,
	[record_reg_id] bigint NOT NULL,
	[record_upd_id] bigint NOT NULL,
	[record_reg_date] datetime DEFAULT getDate() NOT NULL,
	[record_upd_date] datetime DEFAULT getDate() NOT NULL,
	CONSTRAINT complx_user_role UNIQUE ([user_id], [role_id])
);



/* Create Foreign Keys */

ALTER TABLE [mjr_role_action]
	ADD CONSTRAINT [frk_action_role] FOREIGN KEY ([action_id])
	REFERENCES [mjr_action] ([id])
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE [mjr_role_action]
	ADD CONSTRAINT [frk_role_action] FOREIGN KEY ([role_id])
	REFERENCES [mjr_role] ([id])
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE [mjr_user_role]
	ADD CONSTRAINT [frk_role_user] FOREIGN KEY ([role_id])
	REFERENCES [mjr_role] ([id])
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE [mjr_user]
	ADD CONSTRAINT [frk_user_content] FOREIGN KEY ([content_id])
	REFERENCES [mjr_static_content] ([id])
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;


ALTER TABLE [mjr_user_role]
	ADD CONSTRAINT [frk_user_role] FOREIGN KEY ([user_id])
	REFERENCES [mjr_user] ([id])
	ON UPDATE NO ACTION
	ON DELETE NO ACTION
;



