/*
 * @author Mg Than Htike Aung {@literal <rage.cataclysm@gmail.com@address>}
 * @Since 1.0
 * 
 */
package com.mycom.products.mywebsite.migrate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.flywaydb.core.Flyway;

public class MigrateDB {
	public static void main(String[] args) throws IOException {
		InputStream stream = null;
		InputStream jdbcSettingStream = null;
		try {
			stream = MigrateDB.class.getResourceAsStream("/migrate.properties");
			Properties migrateProperties = new Properties();
			migrateProperties.load(stream);
			String migrationPath = migrateProperties.get("migrationPath").toString();
			String jdbcSettingPath = migrateProperties.get("jdbcSettingAbsolutePath").toString();

			Properties jdbcSettings = new Properties();
			jdbcSettingStream = new FileInputStream(jdbcSettingPath);
			jdbcSettings.load(jdbcSettingStream);
			String url = jdbcSettings.get("jdbc.url").toString();
			String userName = jdbcSettings.get("jdbc.username").toString();
			String password = jdbcSettings.get("jdbc.password").toString();
			Flyway flyway = new Flyway();
			flyway.setSqlMigrationPrefix("");
			flyway.setDataSource(url, userName, password);
			flyway.setLocations(migrationPath);
			flyway.clean();
			flyway.migrate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stream.close();
			jdbcSettingStream.close();
		}

	}
}
