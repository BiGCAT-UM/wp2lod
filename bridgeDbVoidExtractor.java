import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.bio.BioDataSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.DCTypes;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.icu.text.SimpleDateFormat;


public class bridgeDbVoidExtractor {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IDMapperException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IDMapperException, FileNotFoundException {
		BioDataSource.init();
		Class.forName("org.bridgedb.rdb.IDMapperRdb");
		File dir = new File("/Users/andra/Downloads/bridge");
		File[] bridgeDbFiles = dir.listFiles();
		Model bridgeDbmodel = ModelFactory.createDefaultModel();
		Resource ensemblVersionResource = bridgeDbmodel.createResource("http://may2012.archive.ensembl.org/index.html");
		Resource bridgeDb = bridgeDbmodel.createResource("/tmp/bridgeDbVoid.ttl");
		bridgeDb.addProperty(Pav.createdBy, "Andra_Waagmeester");
		bridgeDb.addProperty(Pav.contributedBy, "Martina_Kutmon");
		bridgeDb.addProperty(Pav.contributedBy, "Anwesha_Dutta");
		Resource githubResource = bridgeDbmodel.createResource("https://github.com/andrawaag/bridgeDbVoidHeaders/blob/master/bridgeDbVoidExtractor.java");
        bridgeDb.addProperty(Pav.createdWith, githubResource);
        Date myDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        bridgeDb.addLiteral(Pav.createdOn, sdf.format(myDate) );

		bridgeDb.addLiteral(DCTerms.title, "BridgeDb example files");
		bridgeDb.addLiteral(DCTerms.description, "BridgeDb is an id mapping framework for bioinformatics applications.\n" + 
				"\n" + 
				"BridgeDb lets you add the following capabilities quickly and easily:\n" + 
				"\n" + 
				"translate identifiers from one system to another\n" + 
				"search references by id or symbol\n" + 
				"link out to online information for an identifier\n" + 
		"BridgeDb is not tied to a specific source of mapping information. Instead it provides an abstraction layer so you can switch easily between flat files, relational databases and several different web services.Information about the BridgeDb layout: http://bridgedb.org/wiki/GeneDatabaseLayout");
		Property dulExpresses = bridgeDbmodel.createProperty(DUL.NS+"expresses");
		Resource peptideSequenceResource = bridgeDbmodel.createResource("http://dbpedia.org/page/Peptide_sequence");
		for (File bridgeDbFile : bridgeDbFiles) {
			Long lastModified = bridgeDbFile.lastModified(); 
			Date date = new Date(lastModified); 
			Resource mainResource = bridgeDbmodel.createResource("http://bridgedb.org/data/gene_database/"+bridgeDbFile.getName());
			mainResource.addProperty(Pav.createdBy, "Alexander_Pico");
			mainResource.addProperty(Pav.createdBy, "Martijn_van_Iersel");
			if (bridgeDbFile.getName().startsWith("metabol")){
				mainResource.addProperty(dulExpresses, "Wishart DS");
				mainResource.addProperty(Pav.derivedBy, bridgeDbmodel.createResource("http://svn.bigcat.unimaas.nl/bridgedb/trunk/dbbuilder/src/org/bridgedb/util/hmdb/")); 
			    mainResource.addLiteral(Pav.version, "HMDB Release 2.5 - August 1, 2009");
			    mainResource.addProperty(Pav.createdBy, "Martijn_van_Iersel");
			}
			else {
				mainResource.addProperty(dulExpresses, peptideSequenceResource);
				mainResource.addProperty(Pav.derivedBy, bridgeDbmodel.createResource("http://bridgedb.org/browser/trunk/dbbuilder/src/org/bridgedb/README"));
				mainResource.addProperty(Pav.version, ensemblVersionResource);
				mainResource.addProperty(Pav.createdBy, "Alexander_Pico");
			}
			IDMapper mapper = BridgeDb.connect("idmapper-pgdb:" + bridgeDbFile.getAbsolutePath());
			Set<DataSource> bridgeDbSrcDataSources = mapper.getCapabilities().getSupportedSrcDataSources();
			for (DataSource bridgeDbSrcDataSource  : bridgeDbSrcDataSources){
				if (bridgeDbSrcDataSource.getMainUrl() != null)
					mainResource.addProperty(Void.subjectsTarget, bridgeDbSrcDataSource.getMainUrl());
			}
			Set<DataSource> bridgeDbTrgDataSources = mapper.getCapabilities().getSupportedTgtDataSources();
			for (DataSource bridgeDbTDataSource : bridgeDbTrgDataSources){
				if (bridgeDbTDataSource.getMainUrl() != null)
				mainResource.addProperty(Void.objectsTarget,  bridgeDbTDataSource.getMainUrl());
			}
			mapper.close();
			
			mainResource.addProperty(Void.dataDump, mainResource);
			mainResource.addProperty(RDF.type, DCTypes.Dataset);
			mainResource.addLiteral(DCTerms.title, "BridgeDb mappings in bridgeDb database :"+bridgeDbFile.getName());
			mainResource.addProperty(DCTerms.license, bridgeDbmodel.createResource("http://creativecommons.org/licenses/by-sa/3.0/"));
			mainResource.addProperty(Pav.derivedFrom, bridgeDbmodel.createResource("http://www.ensembl.org/info/docs/Doxygen/core-api/index.html")); //TODO adapt to cover metabolites as well
			mainResource.addLiteral(Pav.derivedDate, date);

			mainResource.addProperty(DCTerms.subject, bridgeDbmodel.createResource("http://dbpedia.org/resource/Identifier"));

			System.out.println(bridgeDbFile.getAbsolutePath());
			System.out.println(bridgeDbFile.getName());
		}
		basicCalls.saveRDF2File(bridgeDbmodel, "/tmp/bridgeDbVoid.ttl", "TURTLE");
	}

}
