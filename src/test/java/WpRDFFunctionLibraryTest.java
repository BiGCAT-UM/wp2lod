import java.util.Properties;

import org.bridgedb.IDMapperStack;
import org.junit.Assert;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;

public class WpRDFFunctionLibraryTest {

	@Test
	public void testLoadingBridgeDbTurtle() throws Exception {
    	Model bridgeDbmodel = WpRDFFunctionLibrary.createBridgeDbModel();
    	Assert.assertNotNull(bridgeDbmodel);
	}

	@Test
	public void testLoadingBridgeDbDerbyFiles() throws Exception {
		Properties props = new Properties();
		props.put("bridgefiles", ".");
		props.put("species", "Sc,Hs");
        IDMapperStack stack = WpRDFFunctionLibrary.createBridgeDbMapper(props);
        Assert.assertNotNull(stack);
	}
}
