package net.coatli.handler;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpClient.newHttpClient;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.URI;
import java.net.http.HttpClient;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import net.coatli.rule.DbUnit;
import net.coatli.rule.GetPersonsDataSets;
import net.coatli.rule.TestProperties;

public class GetPersonsHandlerIT {

  @ClassRule
  public static DbUnit DBUNIT = new DbUnit();

  @ClassRule
  public static TestProperties TEST_PROPERTIES = new TestProperties();

  @Rule
  public final GetPersonsDataSets dataSets = new GetPersonsDataSets(DBUNIT);

  private final String url = TEST_PROPERTIES.props().getProperty("get.persons.test.url");

  private final HttpClient client = newHttpClient();

  @Test
  public void thatGetPersonsReturns200Works() throws Exception {
    // given
    final var request = newBuilder(URI.create(url)).GET().build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals(HTTP_OK, response.statusCode());
  }

  @Test
  public void thatGetPersonsReturnsEmptyListWorks() throws Exception {
    // given
    final var request = newBuilder(URI.create(url)).GET().build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertEquals("[]", response.body());
  }

  @Test
  public void thatGetPersonsReturnsNotEmptyListWorks() throws Exception {
    // given
    final var request = newBuilder(URI.create(url)).GET().build();

    // when
    final var response = client.send(request, ofString());

    // then
    assertNotEquals("[]", response.body());
  }

}
