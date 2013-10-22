import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;


public class DownloadAllPathways {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws ConverterException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ServiceException, ConverterException, IOException {
		WikiPathwaysClient client = new WikiPathwaysClient(new URL("http://www.wikipathways.org/wpi/webservice/webservice.php"));
		String wpIdentifier;
		File file=new File("/tmp/WpGPML/");
		WSPathwayInfo[] pathwayList = client.listPathways();
		for(WSPathwayInfo pathwaylist : pathwayList) {
			wpIdentifier = pathwaylist.getId();
			WSPathway wsPathway = client.getPathway(wpIdentifier);
			Writer output = null;
			if (!(file.exists())){
				file.createNewFile();	
			}
			File tempFile = new File(file.getName()+wpIdentifier+".gpml");
			System.out.println(tempFile.getName());
			output = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8");
			output.write(wsPathway.getGpml());
			output.close();
		}
	}
}
