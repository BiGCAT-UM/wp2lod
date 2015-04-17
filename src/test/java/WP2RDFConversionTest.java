import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperCapabilities;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.Xref;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.hpl.jena.rdf.model.Model;

public class WP2RDFConversionTest {

	@Test
	public void testParsing() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(this.getClass().getResourceAsStream("/WP2836_79835.gpml"));
		Assert.assertNotNull(doc);
	}

	@Test
	public void testConversion() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document currentGPML = db.parse(this.getClass().getResourceAsStream("/WP2836_79835.gpml"));
		Assert.assertNotNull(currentGPML);
		Properties props = new Properties();
		props.setProperty("rdfRepository", System.getProperty("java.io.tmpdir"));
		Element pathwayElement = (Element) currentGPML.getElementsByTagName("Pathway").item(0);
    	String wpIdentifier = "WP2836";
    	String wpRevision = "79835";
    	pathwayElement.setAttribute("identifier", wpIdentifier);
    	pathwayElement.setAttribute("revision", wpRevision);
    	Model bridgeDbmodel = WpRDFFunctionLibrary.createBridgeDbModel();
    	IDMapperStack stack = new IDMapperStack();
    	stack.addIDMapper(new DummyIDMapper());
    	WP2RDFConversion.processGPMLFile(
    		currentGPML, 79835, props, bridgeDbmodel, stack,
    		WpRDFFunctionLibrary.getOrganismsTaxonomyMapping()
    	);
	}

	class DummyIDMapper implements IDMapper, IDMapperCapabilities {

		@Override
		public void close() throws IDMapperException {}

		@Override
		public Set<Xref> freeSearch(String arg0, int arg1)
				throws IDMapperException {
			return Collections.emptySet();
		}

		@Override
		public IDMapperCapabilities getCapabilities() {
			return this;
		}

		@Override
		public boolean isConnected() {
			return false;
		}

		@Override
		public Map<Xref, Set<Xref>> mapID(Collection<Xref> arg0,
				DataSource... arg1) throws IDMapperException {
			return Collections.emptyMap();
		}

		@Override
		public Set<Xref> mapID(Xref arg0, DataSource... arg1)
				throws IDMapperException {
			return Collections.emptySet();
		}

		@Override
		public boolean xrefExists(Xref arg0) throws IDMapperException {
			return false;
		}

		@Override
		public Set<String> getKeys() {
			return Collections.emptySet();
		}

		@Override
		public String getProperty(String arg0) {
			return null;
		}

		@Override
		public Set<DataSource> getSupportedSrcDataSources()
				throws IDMapperException {
			return Collections.emptySet();
		}

		@Override
		public Set<DataSource> getSupportedTgtDataSources()
				throws IDMapperException {
			return Collections.emptySet();
		}

		@Override
		public boolean isFreeSearchSupported() {
			return false;
		}

		@Override
		public boolean isMappingSupported(DataSource arg0, DataSource arg1)
				throws IDMapperException {
			return false;
		}
		
	}

}
