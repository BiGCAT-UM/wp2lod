

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.XrefIterator;
import org.bridgedb.bio.BioDataSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

import de.fuberlin.wiwiss.ng4j.swp.vocabulary.FOAF;


public class inspectBridgeDb {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IDMapperException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IDMapperException, ClassNotFoundException, IOException {

		HashMap hm = new HashMap();
		hm.put("Ensembl Horse", "http://identifiers.org/ensembl/");
		hm.put("CTD Chemical", "http://identifiers.org/ctd.chemical/");
		hm.put("UniProt", "http://identifiers.org/uniprot/");
		hm.put("LIPID MAPS", "http://identifiers.org/lipidmaps/");
		hm.put("UniGene", "http://identifiers.org/unigene/");
		hm.put("ENSEMBL", "http://identifiers.org/ensembl/");
		hm.put("ZFIN", "http://identifiers.org/zfin/");
		hm.put("Entrez Gene", "http://identifiers.org/ncbigene/");
		hm.put("Gramene Genes DB", "http://identifiers.org/gramene.gene/");
		hm.put("ChemIDplus", "http://identifiers.org/chemidplus/");
		hm.put("Rice Ensembl Gene", "http://identifiers.org/ensembl/");
		hm.put("RGD", "http://identifiers.org/rgd/");
		hm.put("Wikipedia", "http://identifiers.org/wikipedia.en/");
		hm.put("Gramene Maize", "http://identifiers.org/gramene.gene/");
		hm.put("Ensembl Zebrafish", "http://identifiers.org/ensembl/");
		hm.put("Pubmed", "http://identifiers.org/pubmed/");
		hm.put("Ensembl Chimp", "http://identifiers.org/ensembl/");
		hm.put("enzyme", "http://identifiers.org/ec-code/");
		hm.put("miRBase", "http://identifiers.org/mirbase/");
		hm.put("Uniprot/TrEMBL", "http://identifiers.org/uniprot/");
		hm.put("RefSeq", "http://identifiers.org/refseq/");
		hm.put("TAIR", "http://identifiers.org/tair.locus/");
		hm.put("CAS", "http://identifiers.org/cas/");
		hm.put("COMPOUND", "http://identifiers.org/kegg.compound/");
		hm.put("WormBase", "http://identifiers.org/wormbase/");
		hm.put("HUGO", "http://identifiers.org/hgnc/");
		hm.put("KEGG Pathway", "http://identifiers.org/kegg.pathway/");
		hm.put("PubChem-compound", "http://identifiers.org/pubchem.compound/");
		hm.put("Other", "http://internal.wikipathways.org/datasource/other/");
		hm.put("Pubchem-compound", "http://identifiers.org/pubchem.compound/");
		hm.put("Chemspider", "http://identifiers.org/chemspider/");
		hm.put("Kegg enzyme", "http://identifiers.org/ec-code/");
		hm.put("SwissProt", "http://identifiers.org/uniprot/");
		hm.put("GeneDB", "http://identifiers.org/genedb/");
		hm.put("KEGG Genes", "http://identifiers.org/kegg.genes/");
		hm.put("Kegg ortholog", "http://identifiers.org/kegg.orthology/");
		hm.put("MGI", "http://identifiers.org/mgd/");
		hm.put("Ensembl Cow", "http://identifiers.org/ensembl/");
		hm.put("Enzyme Nomenclature", "http://identifiers.org/ec-code/");
		hm.put("Pubchem compound", "http://identifiers.org/pubchem.compound/");
		hm.put("Ensembl Fruitfly", "http://identifiers.org/ensembl/");
		hm.put("HGNC", "http://identifiers.org/hgnc/");
		hm.put("EMBL", "http://internal.wikipathways.org/datasource/embl/");
		hm.put("Ensembl", "http://identifiers.org/ensembl/");
		hm.put("3DMET", "http://identifiers.org/3dmet/");
		hm.put("InChI", "http://identifiers.org/inchi/");
		hm.put("ISBN", "http://identifiers.org/isbn/");
		hm.put("EC Number", "http://identifiers.org/ec-code/");
		hm.put("Pubchem", "http://identifiers.org/pubchem.compound/");
		hm.put("Ensembl Rat", "http://identifiers.org/ensembl/");
		hm.put("PubChem", "http://identifiers.org/pubchem.compound/");
		hm.put("InChIKey", "http://identifiers.org/inchi/");
		hm.put("Kegg Compound", "http://identifiers.org/kegg.compound/");
		hm.put("Ensembl C. elegans", "http://identifiers.org/ensembl/");
		hm.put("Ensembl Celegans", "http://identifiers.org/ensembl/");
		hm.put("TubercuList", "http://identifiers.org/myco.tuber/");
		hm.put("Ensembl Yeast", "http://identifiers.org/ensembl/");
		hm.put("Reactome", "http://identifiers.org/reactome/");
		hm.put("Ensembl Chicken", "http://identifiers.org/ensembl/");
		hm.put("hmdbid", "http://identifiers.org/hmdb/");
		hm.put("Ensembl Pig", "http://identifiers.org/ensembl/");
		hm.put("FlyBase", "http://identifiers.org/flybase/");
		hm.put("miRBase Sequence", "http://identifiers.org/mirbase/");
		hm.put("Ensembl Human", "http://identifiers.org/ensembl/");
		hm.put("PubChem-substance", "http://identifiers.org/pubchem.substance/");
		hm.put("SGD", "http://identifiers.org/sgd/");
		hm.put("Pfam", "http://identifiers.org/pfam/");
		hm.put("Ensembl Mosquito_Ag", "http://identifiers.org/ensembl/");
		hm.put("Affy", "http://internal.wikipathways.org/datasource/affy/");
		hm.put("Ensembl M. tuberculosis", "http://identifiers.org/ensembl/");
		hm.put("Ensembl Mouse", "http://identifiers.org/ensembl/");
		hm.put("PubMed", "http://identifiers.org/pubmed/");
		hm.put("GenBank", "http://identifiers.org/insdc/");
		hm.put("OMIM", "http://identifiers.org/omim/");
		hm.put("WikiPathways", "http://identifiers.org/wikipathways/");
		hm.put("GLYCAN", "http://identifiers.org/kegg.glycan/");
		hm.put("Gramene Rice", "http://identifiers.org/gramene.gene/");
		hm.put("Gramene Arabidopsis", "http://identifiers.org/gramene.gene/");
		hm.put("ChEMBL compound", "http://identifiers.org/chembl.compound/");
		hm.put("HMDB", "http://identifiers.org/hmdb/");
		hm.put("ChEBI", "http://identifiers.org/obo.chebi/");
		hm.put("Ensembl B. subtilis", "http://identifiers.org/ensembl/");
		hm.put("Ensembl Dog", "http://identifiers.org/ensembl/");

		Model bridgeDbModel = ModelFactory.createDefaultModel();

		//Store earlier miriam urls
		HashMap<String, String> miriamLinks = new HashMap<String, String>();	
		Class.forName("org.bridgedb.rdb.IDMapperRdb");

		BioDataSource.init();  
		File dir = new File("/home/manager/bridge");

		String[] children = dir.list();

		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i=0; i<children.length; i++) {

				String filename = children[i];
				IDMapper mapper = BridgeDb.connect("idmapper-pgdb:/home/manager/bridge/"+filename);  
				for (Xref src : ((XrefIterator) mapper).getIterator())
				{
					String sourceUri = (String) hm.get(src.getDataSource().getFullName());
					if (sourceUri!=null){
						Resource sourceResource = bridgeDbModel.createResource(sourceUri+src.getId());
						for (Xref dest : mapper.mapID(src)) //, DataSource.getBySystemCode("L"))) //
						{
							String destUri = (String) hm.get(dest.getDataSource().getFullName());
							if (destUri!=null){
								Resource destResource = bridgeDbModel.createResource(destUri+dest.getId());
								sourceResource.addProperty(Skos.relatedMatch, destResource);
							}
						}
					}
				}
				basicCalls.saveRDF2File(bridgeDbModel, "/home/manager/bridgeTtl/"+filename+".ttl", "TURTLE");
				System.out.println("/tmp/"+filename+".ttl");
			}
		}
	}
}


