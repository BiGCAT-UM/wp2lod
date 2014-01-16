import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;


public class inferDirectedInteractions {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void runConstructQuery(VirtGraph wpGraph, String constructFileName){
		Query query = QueryFactory.read(constructFileName);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (query, wpGraph);

		Model model = vqe.execConstruct();
		Graph g = model.getGraph();
		System.out.println(constructFileName+ " Relations found:" + model.size());
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stmt      = iter.nextStatement();  // get next statement
			wpGraph.add(new Triple(stmt.getSubject().asNode(), stmt.getPredicate().asNode(), stmt.getObject().asNode()));
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {

		/**
		 * Executes a SPARQL query against a virtuoso url and prints results.
		 */
		String url = "jdbc:virtuoso://localhost:"+args[0];
		
		VirtGraph wpGraph = new VirtGraph (url, "dba", "dba");
		
		
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		// String url = "jdbc:virtuoso://localhost:"+args[0];
		
		runConstructQuery(wpGraph, "sparqlQueries/unDirectedInteraction.construct");
        runConstructQuery(wpGraph, "sparqlQueries/DirectedInteraction.construct");
        runConstructQuery(wpGraph, "sparqlQueries/InhibitionInteraction.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimBinding.construct");
        runConstructQuery(wpGraph, "sparqlQueries/LigandRound.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimConversion.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimModification.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimTranscriptionTranslation.construct"); 
        runConstructQuery(wpGraph, "sparqlQueries/AddTargetUri.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimCatalysis.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimNecessaryStimulation.construct");
        runConstructQuery(wpGraph, "sparqlQueries/MimStimulation.construct");
        runConstructQuery(wpGraph, "sparqlQueries/AddTargetUri.construct");
	}
}
