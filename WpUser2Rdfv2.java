import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class WpUser2Rdfv2
	{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Model model = ModelFactory.createDefaultModel();
			DateFormat formatter;
			Date date;
			formatter = new SimpleDateFormat("yyyymmdd");
			

			
			date = (Date) formatter.parse("20000101");
			WikiPathwaysClient client = new WikiPathwaysClient(new URL(
			"http://www.wikipathways.org/wpi/webservice/webservice.php"));
			WSPathwayInfo[] allPathways = client.listPathways();
			for (WSPathwayInfo pathway : allPathways){
				String wpIdentifier = pathway.getId();
				Resource pathwayResource = model.createResource("http://rdf.wikipathways.org/"+wpIdentifier);
				System.out.println(wpIdentifier);
				WSPathwayHistory pathwayHistory = client.getPathwayHistory(wpIdentifier, date);
				for (int i = 0; i < pathwayHistory.getHistory().length; i++) {
					
					String user = pathwayHistory.getHistory(.i).getUser();
					pathwayHistory.getHistory(i).getTimestamp();
					Resource wpUser = model
							.createResource("http://www.wikipathways.org/index.php/User:"
									+ user.replace(" ", "_")); //TODO Needs to change to VIVO instance
					pathwayResource.addProperty(Pav.contributedBy, wpUser);
					wpUser.addProperty(RDF.type, FOAF.Person);
					wpUser.addProperty(RDFS.label, user);
				}
			}
			FileOutputStream fout;
			fout = new FileOutputStream("/tmp/WpUsers.ttl");
			model.write(fout, "TURTLE");
			
		
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
