import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.Xref;
import org.bridgedb.bio.BioDataSource;
import org.bridgedb.bio.Organism;
import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class bridgedbTest {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IDMapperException 
	 * @throws ServiceException 
	 * @throws ConverterException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IDMapperException, ServiceException, ConverterException, ParserConfigurationException, SAXException, IOException {




	
		BioDataSource.init();

		Class.forName("org.bridgedb.rdb.IDMapperRdb");

		File dir = new File("/Users/andra/Downloads/bridge");
		File[] bridgeDbFiles = dir.listFiles();
		IDMapperStack mapper = new IDMapperStack();
		for (File bridgeDbFile : bridgeDbFiles) {
			System.out.println(bridgeDbFile.getAbsolutePath());
			
				mapper.addIDMapper("idmapper-pgdb:" + bridgeDbFile.getAbsolutePath());
		}
		mapper.setTransitive(false);

        System.out.println("boebaboebi");
		Xref src = new Xref("ENSG00000205581", BioDataSource.ENSEMBL);
		Set<Xref> dests = mapper.mapID(src, DataSource.getBySystemCode("S"));
		Iterator<Xref> iter = dests.iterator();
		while (iter.hasNext()){
			Xref tempId = (Xref) iter.next();

			System.out.println(tempId.getId());
			System.out.println(tempId.getURN());
		}
	 
	}
}


