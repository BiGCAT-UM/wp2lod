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

import javax.xml.rpc.ServiceException;

import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysCache;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;


public class CreateLocalWikiPathwaysHistoryDump {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			
			WikiPathwaysClient client;
			client = new WikiPathwaysClient(new URL("http://www.wikipathways.org/wpi/webservice/webservice.php"));
			String wpIdentifier;
			String dirName = "/tmp/WpGPMLHistory/";
			boolean success =(new File(dirName)).mkdir();

			WSPathwayInfo[] pathwayList = client.listPathways();
			for(WSPathwayInfo pathwaylist : pathwayList) {
				wpIdentifier = pathwaylist.getId();
				DateFormat formatter;
				Date date;
				formatter = new SimpleDateFormat("yyyymmdd");

				date = (Date) formatter.parse("20000101");
				int pathwayNumber = Integer.parseInt(wpIdentifier.substring(2));
				System.out.println(wpIdentifier);
				System.out.println(pathwayNumber);
				if( (pathwayNumber>0)){
					
					WSPathwayHistory pathwayHistory = client.getPathwayHistory(wpIdentifier, date);
					for (int i = 0; i < pathwayHistory.getHistory().length; i++) {


						File tempFile = new File(dirName+wpIdentifier+"_"+pathwayHistory.getHistory(i).getRevision()+".gpml");
						System.out.println(tempFile.getName());
						if (!(tempFile.exists())){
							int revision = Integer.parseInt(pathwayHistory.getHistory(i).getRevision());
							WSPathway wsPathway = client.getPathway(wpIdentifier, revision);
							Writer output = null;
							System.out.println(tempFile.getName());
							output = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8");
							output.write(wsPathway.getGpml());
							output.close();
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConverterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
