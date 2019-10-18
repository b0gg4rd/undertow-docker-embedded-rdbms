package net.coatli.rule;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;
import org.junit.rules.ExternalResource;

public class DbUnit extends ExternalResource {

  private static final Logger LOGGER = Logger.getLogger(DbUnit.class.getName());

  private static final Map<String, String> NO_PK_TABLES;

  static {

    final var noPkTables = new HashMap<String, String>();
    noPkTables.put("PERSON", "id");

    NO_PK_TABLES = unmodifiableMap(noPkTables);

  }

  private IDatabaseConnection connection;

  private DataFileLoader dataFileLoader;

  public IDatabaseConnection getConnection() {
    return connection;
  }

  public DataFileLoader getDataFileLoader() {
    return dataFileLoader;
  }

  @Override
  protected void before() throws Throwable {

    final var properties = new Properties();

    try (final var inputStream = DbUnit.class.getResourceAsStream("/conf/test.properties")) {
      properties.load(inputStream);
    }

    final var databaseTester = new JdbcDatabaseTester(properties.getProperty("dbunit.driverClass"),
                                                      properties.getProperty("dbunit.connectionUrl"),
                                                      properties.getProperty("dbunit.username"),
                                                      properties.getProperty("dbunit.password"));

    connection = databaseTester.getConnection();

    connection.getConfig().setProperty(DatabaseConfig.PROPERTY_PRIMARY_KEY_FILTER,
        (IColumnFilter )(tableName, column) -> column.getColumnName().equalsIgnoreCase(NO_PK_TABLES.get(tableName)));

    dataFileLoader = new FlatXmlDataFileLoader();

    LOGGER.info(format("Database tester '%s'", databaseTester));

  }

  @Override
  protected void after() {

    try {

      connection.close();

    } catch (final Exception exc) {
      exc.printStackTrace();
    }

  }

}
