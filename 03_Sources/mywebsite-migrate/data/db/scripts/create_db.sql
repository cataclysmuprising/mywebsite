USE [master]
GO  
/** Kill all connections before drop **/
DECLARE @kill varchar(8000) = '';  
SELECT @kill = @kill + 'kill ' + CONVERT(varchar(5), session_id) + ';'  
FROM sys.dm_exec_sessions
WHERE database_id  = db_id('mywebsite')

EXEC(@kill);

USE [master]
GO

IF EXISTS(select * from sys.databases where name='mywebsite')
DROP DATABASE [mywebsite]
GO

CREATE DATABASE [mywebsite]
GO

USE [mywebsite]
GO
