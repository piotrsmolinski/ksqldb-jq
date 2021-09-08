package dev.psmolinski.ksql.jq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.ksql.function.udf.UdfDescription;
import net.thisptr.jackson.jq.JsonQuery;
import net.thisptr.jackson.jq.Scope;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UdfDescription(
        name = "jq",
        description = "Execute jq query and return result as list of strings"
)
public class JqUdf {

  public List<String> query(String query, String json) {

    try {

      ObjectMapper om = new ObjectMapper();
      JsonNode parsed = om.readTree(json);

      Scope rootScope = Scope.newEmptyScope();
      rootScope.loadFunctions(Scope.class.getClassLoader());

      JsonQuery q = JsonQuery.compile(query);

      final List<JsonNode> result = q.apply(rootScope, parsed);

      if (result == null) {
        return Collections.emptyList();
      }

      return result.stream()
              .map(JsonNode::asText)
              .collect(Collectors.toList());

    } catch (Exception e) {

      return Collections.emptyList();

    }

  }

}
