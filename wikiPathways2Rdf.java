import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class wikiPathways2Rdf {

	/**
	 * @param args
	 */

		  public static boolean fileExists(String filename) {
		    File f = new File(filename);
		    boolean exists;
		    if (f.exists()) {
		    	exists = true;
		    } else {
		    	exists = false;
		    }
		    	return exists;
		  }
		
			public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
			  public static String now() {
				    Calendar cal = Calendar.getInstance();
				    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
				    return sdf.format(cal.getTime());

				  }
		  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

		try {

			
			Pathway2Rdf p2r = new Pathway2Rdf();
			File dir = new File("/tmp/WpGPML/");
			System.out.println("Start : " + now());
			String[] pathwayFiles = dir.list(new SuffixFileFilter(".gpml"));
			for (int i=0; i<pathwayFiles.length; i++) {
				File fileName = new File(dir.getPath()+"/"+pathwayFiles[i]);
				String gpml = FileUtils.readFileToString(fileName);
				String wpIdentifier = fileName.getName().substring(0, fileName.getName().indexOf("_"));
				System.out.println(wpIdentifier);
				String wpRevision = fileName.getName().substring(fileName.getName().indexOf("_")+1, fileName.getName().indexOf("."));
				System.out.println(wpRevision);
				p2r.addPathway2Rdf(wpIdentifier, wpRevision, gpml);
		    }
			System.out.println("End : " + now());
			
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
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConverterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
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
