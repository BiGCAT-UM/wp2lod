import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;

public class FederatedQuery {

final static String serviceEndpoint = "http://ops-virtuoso.scai.fraunhofer.de:8891/sparql";
//final static String serviceEndpoint = "http://beta.sparql.uniprot.org";

public static void main(String[] args) {

    String queryString = 
    	    "PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
    		"PREFIX biopax: <http://www.biopax.org/release/biopax-level3.owl#>" +
    		"PREFIX : <http://dbpedia.org/resource/> " +
    			"PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
    			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
    			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
    			"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
    			"PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
    			"PREFIX dbpedia2: <http://dbpedia.org/property/>" +
    			"PREFIX dbpedia: <http://dbpedia.org/>" +
    			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
    			"PREFIX dbo: <http://dbpedia.org/ontology/>" +
    			"PREFIX dcterms: <http://purl.org/dc/terms/>" +
    		"SELECT DISTINCT * " +
    		"   from <http://www.biopax.org/release/biopax-level3.owl#> " +
    		"WHERE " +
    		"{ " +
    		" SERVICE <http://semantics.bigcat.unimaas.nl:8000/sparql/>" +
    		"{ " +
    		"?evidence rdfs:type biopax:Evidence ." +
    		"?evidence dcterms:identifier ?pmid" +
    		"} " +
    		" SERVICE <http://ops-virtuoso.scai.fraunhofer.de:8891/sparql>" +
    		"{ " +
    		"?pmid rdf:type <http://nlp2rdf.lod2.eu/schema/string/Document> ." +
    		"} " +
    	
    		/*" SERVICE <http://rdf.farmbio.uu.se/chembl/sparql>" +
    		"{ " +
                  "?paper <http://purl.org/ontology/bibo/pmid> ?pmid ."+ 
                  "?object ?cites ?paper . " +
    		"} " +
            */
    		"} "+
    		"limit 10";

    Query query = QueryFactory.create(queryString);  // exception happens here
    QueryExecution qe = QueryExecutionFactory.sparqlService(serviceEndpoint,query);

    try {
        ResultSet rs = qe.execSelect();
        if ( rs.hasNext() ) {
            // show the result, more can be done here
            System.out.println(ResultSetFormatter.asText(rs));
        }
    } 
    catch(Exception e) { 
        System.out.println(e.getMessage());
    }
    finally {
        qe.close();
    }
    System.out.println("\nall done.");
}

}
