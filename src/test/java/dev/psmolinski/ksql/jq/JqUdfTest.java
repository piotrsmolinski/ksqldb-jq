package dev.psmolinski.ksql.jq;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JqUdfTest {

  @Test
  public void extractEventSource() {

    JqUdf udf = new JqUdf();

    String json = new String(getResourceAsBytes("/event.json"));

    List<String> result = udf.query(".event.eventSource", json);

    Assert.assertEquals(1, result.size());
    Assert.assertEquals("TEST", result.get(0));

  }

  private byte[] getResourceAsBytes(String resource) {
    try (InputStream stream = getClass().getResourceAsStream(resource)) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[8192];
      for (;;) {
        int r = stream.read(buffer);
        if (r<0) break;
        baos.write(buffer, 0, r);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
