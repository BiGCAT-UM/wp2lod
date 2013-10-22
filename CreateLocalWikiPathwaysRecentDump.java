import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.TransformerException;

import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysCache;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class CreateLocalWikiPathwaysRecentDump {


	public static void main(String[] args) throws ParserConfigurationException, SAXException, TransformerException, ServiceException, RemoteException, ConverterException, IOException {
			WikiPathwaysClient client = wpRelatedCalls.startWpApiClient();
			boolean success =(new File(constants.localCurrentGPMLCache())).mkdir();
			for(WSPathwayInfo pathwaylist : client.listPathways()) {           	
				File tempFile = new File(constants.localAllGPMLCacheDir()+pathwaylist.getId()+".gpml");
				if (!(tempFile.exists())){					
					System.out.println(tempFile.getName());
					WSPathway wsPathway = client.getPathway(pathwaylist.getId());
					// This should not run and we need to evaluate if we cane safely delete this file. It goes back to when the pathway data
					// came through the webservice
					// We commented out to lines
					
	//				Document localGpml = basicCalls.saveDOMasXML(xmlDocument fileName).client.getPathway().getGpml();
				//	basicCalls.saveDOMasXML(wpRelatedCalls.addWpProvenance(localGpml, pathwaylist.getId(), pathwaylist.getRevision()), constants.localCurrentGPMLCache() + tempFile.getName());
				}
			}
}

}
