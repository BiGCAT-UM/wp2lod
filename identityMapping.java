import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class identityMapping {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Model bridgeDbmodel = ModelFactory.createDefaultModel();
		InputStream in = new FileInputStream("/tmp/BioDataSource.ttl");
		bridgeDbmodel.read(in, "", "TURTLE");
		bridgeDbmodel.setNsPrefix("bridgeDb", "http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#");
		Property bio2rdfProperty = bridgeDbmodel.createProperty("http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#mainUrl");
		
		System.out.println(bridgeDbmodel.listObjectsOfProperty(bio2rdfProperty).next());
		
		
		Resource affyMetrix = bridgeDbmodel.createResource("http://bio2rdf.org/affymetrix:$id");
		
		Resource affy = bridgeDbmodel.createResource("DataSource_Affy");
		affy.addProperty(bio2rdfProperty, affyMetrix);
		
		basicCalls.saveRDF2File(bridgeDbmodel, "/tmp/test.ttl", "TURTLE");


	}

}
