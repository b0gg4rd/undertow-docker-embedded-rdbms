package net.coatli.rule;

import static java.util.Map.of;
import static org.dbunit.operation.DatabaseOperation.DELETE;
import static org.dbunit.operation.DatabaseOperation.INSERT;

import java.util.Map;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class GetPersonsDataSets extends TestWatcher {

  private static final Map<String, String> METHODS = of("thatGetEventsWithValidUserIdReturn200Works", null,
                                                        "thatGetEventsWithValidUserIdReturnNotEmptyListWorks", null);

  private final DbUnit dbUnit;

  public GetPersonsDataSets(final DbUnit dbUnit) {
    this.dbUnit = dbUnit;
  }

  @Override
  protected void starting(final Description description) {

    if (METHODS.containsKey(description.getMethodName())) {

      try {

        INSERT.execute(dbUnit.getConnection(), dbUnit.getDataFileLoader().load("/datasets/get-persons.xml"));

      } catch (final Exception exc) {
        throw new RuntimeException(exc);
      }

    }

  }

  @Override
  protected void finished(final Description description) {

    if (METHODS.containsKey(description.getMethodName())) {

      try {

        DELETE.execute(dbUnit.getConnection(), dbUnit.getDataFileLoader().load("/datasets/get-persons.xml"));

      } catch (final Exception exc) {
        throw new RuntimeException(exc);
      }

    }

  }

}
