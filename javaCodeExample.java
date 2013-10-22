import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class javaCodeExample {

	public static void main(String[] args) {
		String sparqlQueryString = "SELECT * WHERE {?s ?p ?o} LIMIT 10";
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://sparql.wikipathways.org", query);
		ResultSet resultSet = queryExecution.execSelect();
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			System.out.print(solution.get("s"));
			System.out.print("\t"+solution.get("p"));
			System.out.println("\t"+solution.get("o"));
		}
	}
}
