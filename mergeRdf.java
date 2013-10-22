import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class mergeRdf {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		 // create an empty model
		 Model model = ModelFactory.createDefaultModel();

		File dir = new File("WpRDF/");

		String[] children = dir.list();
		// It is also possible to filter the list of returned files.
		// This example does not return any files that start with `.'.
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".rdf");
		    }
		};
		children = dir.list(filter);
		if (children == null) {
		    // Either dir does not exist or is not a directory
		} else {
		    for (int i=0; i<children.length; i++) {
		        // Get filename of file or directory
		        String filename = children[i];
		        System.out.println(filename);
		     // use the FileManager to find the input file
		        InputStream in = FileManager.get().open( "WpRDF/"+filename );
		       if (in == null) {
		           throw new IllegalArgumentException(
		                                        "File: " + filename + " not found");
		       }

		       // read the RDF/XML file
			       model.read(in, "http://www.wikipathways.org", "N-TRIPLE");

		       // write it to standard out
		      
		    }
		    FileOutputStream ftotalout;
		    ftotalout = new FileOutputStream("Wikipathways.rdf");
		    model.write(ftotalout, "N-TRIPLE");
		}

	}

}
