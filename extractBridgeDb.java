import java.util.Set;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.Xref;
import org.bridgedb.bio.BioDataSource;


public class extractBridgeDb {
	public static void main(String[] args) throws IDMapperException, ClassNotFoundException {
		IDMapperStack geneMapper; 

		// load libs for derby files
		Class.forName("org.bridgedb.rdb.IDMapperRdb");  

		// register two mappers, in this case data-derby mappers given by a properties file
		IDMapper humanMapper = BridgeDb.connect("idmapper-pgdb:/Users/andra/Downloads/bridge/Hs_Derby_20120602.bridge");
		//IDMapper humanMapper = BridgeDb.connect("idmapper-pgdb:"+);

		// create mapper stack
		geneMapper = new IDMapperStack();
		// add mappers to the stack
		//geneMapper.addIDMapper(mouseMapper);
		geneMapper.addIDMapper(humanMapper);

		// the mapper stack can now be used for mapping mouse and also human ids
		//Xref mouseRef = new Xref( "ENSMUSG00000017167", BioDataSource.ENSEMBL_MOUSE );
		//Set<Xref> mouseResults = geneMapper.mapID( mouseRef );
		Xref humanRef = new Xref( "3643", DataSource.getByFullName("Entrez Gene"));
		System.out.println(BioDataSource.ENTREZ_GENE.getFullName());
		Set<Xref> humanResults = geneMapper.mapID(humanRef, DataSource.getBySystemCode("L") );
		System.out.println(humanResults.iterator().next().getId());
	}
}
