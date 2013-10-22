import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class getMeriamSources {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sparqlQueryString =  "prefix dc:      <http://purl.org/dc/elements/1.1/>"+
		"prefix wp:      <http://vocabularies.wikipathways.org/wp#>"+
		"prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>" +
		"prefix dcterms:  <http://purl.org/dc/terms/>"+
		"select distinct ?source { "+
		"?geneProduct dc:source ?source ."+ 
		"}";
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://sparql.wikipathways.org", query);
		ResultSet resultSet = queryExecution.execSelect();
		HashMap meriamHm = new HashMap();
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			String resource = solution.get("source").toString().substring(0, solution.get("source").toString().indexOf("^^"));
			meriamHm.put(resource, basicCalls.getMiriamUri(resource));
			System.out.println(resource+"\t"+basicCalls.getMiriamUri(resource));
		}
		System.out.println(meriamHm);
		Set set = meriamHm.entrySet(); 
		// Get an iterator 
		Iterator i = set.iterator(); 
		// Display elements 
		while(i.hasNext()) { 
			Map.Entry me = (Map.Entry)i.next(); 
			System.out.print("hm.put(\""+me.getKey()+"\", "); 
			System.out.println("\""+me.getValue()+"\");"); 
		} 
		System.out.println(); 
	}

}
