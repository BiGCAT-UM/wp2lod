import org.junit.Assert;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

public class WpRDFFunctionLibraryTest {

	@Test
	public void testParsing() throws Exception {
    	Model bridgeDbmodel = WpRDFFunctionLibrary.createBridgeDbModel();
    	Assert.assertNotNull(bridgeDbmodel);
	}
}
