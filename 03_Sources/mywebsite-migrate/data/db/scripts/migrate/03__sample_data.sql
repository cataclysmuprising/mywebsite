/******      static content        ******/
SET IDENTITY_INSERT mjr_static_content ON;
INSERT INTO mjr_static_content 
(id,		file_name,				file_path,														file_size,		file_type,	record_reg_id,record_upd_id,record_reg_date,record_upd_date) 
VALUES 
(2,	   		'admin.jpg',		'D:/web-resources/mywebsite/uploadFiles/avatar/admin.jpg',			'10KB',			0,			1,1,'2017-01-01','2017-01-01'),
(3,	   		'user.jpg',			'D:/web-resources/mywebsite/uploadFiles/avatar/user.jpg',			'10KB',			0,			1,1,'2017-01-01','2017-01-01');
SET IDENTITY_INSERT mjr_static_content OFF;

/******      Login History        ******/
SET IDENTITY_INSERT mjr_login_history ON;
INSERT INTO mjr_login_history( 
 id, user_id, login_date, 	ip_address, 	os, 		 user_agent,   		record_reg_id,record_upd_id,record_reg_date,record_upd_date ) values 
(1,  1, 	 '2017-01-01',  '192.168.0.1',  'Window 10', 'Firefox 50', 		1,1,'2017-01-01','2017-01-01');
SET IDENTITY_INSERT mjr_login_history OFF;

/******      role        ******/
SET IDENTITY_INSERT mjr_role ON;
INSERT INTO mjr_role 
(id,	name,			description,																							record_reg_id,record_upd_id,record_reg_date,record_upd_date) 
VALUES 
(2,	   'ADMIN',	'This role will assign for administrator right persons.',														1,1,'2017-01-01','2017-01-01');
SET IDENTITY_INSERT mjr_role OFF;