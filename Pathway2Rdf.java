import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.text.*;

import org.apache.commons.io.FileUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.pathvisio.model.ConverterException;
import org.pathvisio.model.ObjectType;
import org.pathvisio.model.Pathway;
import org.pathvisio.model.PathwayElement;
import org.pathvisio.view.GeneProduct;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.biopax.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import com.hp.hpl.jena.sparql.vocabulary.DOAP;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Pathway2Rdf {

	/**
	 * @param args
	 * @throws XPathExpressionException
	 * @throws DOMException
	 * @throws ServiceException
	 * @throws ConverterException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParseException
	 */

	private static boolean isValidXML(String gpml, String wpIdentifier,
			String revision) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();

		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			StringReader reader = new StringReader(gpml);
			InputSource inputSource = new InputSource(reader);
			Document doc = docBuilder.parse(inputSource);
			reader.close();
			doc.getDocumentElement().normalize();

			return true;

		} catch (SAXParseException spe) {
			System.out
					.println("Invalid GPML: " + wpIdentifier + "/" + revision);
			return false;

		} catch (SAXException sxe) {
			System.out
					.println("Invalid GPML: " + wpIdentifier + "/" + revision);
			return false;

		} catch (ParserConfigurationException pce) {
			System.out
					.println("Invalid GPML: " + wpIdentifier + "/" + revision);
			return false;

		} catch (IOException ioe) {
			System.out
					.println("Invalid GPML: " + wpIdentifier + "/" + revision);
			return false;
		}

	}

	public static void addPathway2Rdf(String wpIdentifier, String wpRevision,
			String gpml) throws DOMException, XPathExpressionException,
			ServiceException, ConverterException, ParserConfigurationException,
			SAXException, IOException, ParseException, NullPointerException {

		Model model = ModelFactory.createDefaultModel();
		// Declare the Prefixes

		// Model wikipathways level into RDF
		Resource wikipathwaysResource = model
				.createResource(WpNamespaces.nsWikipathways);
		
		Property xCoordinate = model.createProperty(WpNamespaces.nsWikipathways+"GPML/xCoordinate");
		Property yCoordinate = model.createProperty(WpNamespaces.nsWikipathways+"GPML/yCoordinate");
		Property color = model.createProperty(WpNamespaces.nsWikipathways+"GPML/color");
		Property width = model.createProperty(WpNamespaces.nsWikipathways+"GPML/width");
		Property height = model.createProperty(WpNamespaces.nsWikipathways+"GPML/height");
		
		Resource wikiPathwaysPaperResource = model
				.createResource("http://www.ncbi.nlm.nih.gov/pubmed/18651794");
		wikipathwaysResource.addProperty(DCTerms.bibliographicCitation,
				wikiPathwaysPaperResource);
		Resource wikiPathwaysSparqlEndpointResource = model.createResource("http://semantics.bigcat.unimaas.nl:8000/sparql");
		wikipathwaysResource.addProperty(Void.sparqlEndpoint, wikiPathwaysSparqlEndpointResource);
		wikipathwaysResource.addProperty(RDF.type, Void.Dataset);
	
		String DefinitionURI = WpNamespaces.nsWikipathways + "Definition/";
		Resource wikipathwaysGroupDefinitionResource = model
				.createResource(DefinitionURI + "Group/");

		// State that GeneProduct can either be a Protein or DNA
		RDFNode[] geneProductelems = new RDFNode[] { Biopax_level3.Dna,
				Biopax_level3.Protein };
		RDFList geneProductRDFNode = model.createList(geneProductelems);

		String pathwayURI = WpNamespaces.nsWikipathways + "Pathway/"
				+ wpIdentifier + "/";
		//System.out.println();
		//System.out.print(wpIdentifier);
		Resource abstractPathwayResource = model.createResource(pathwayURI);
		//System.out.print(wpRevision);
		String pathwayResourceURI = pathwayURI + wpRevision + "/";
		Resource centralPathwayResource = model.createResource(pathwayResourceURI);
		abstractPathwayResource.addProperty(DCTerms.hasVersion,
				centralPathwayResource);
		Resource pathwayResource = model.createResource(pathwayResourceURI);

		// A Pathway in Wikipathways is identified by its WP identifier and
		// its revision number;
		Resource pathwayIdentifierResource = model
				.createResource(WpNamespaces.nsIdentifiers + "/WikiPathways/"
						+ wpIdentifier + "/" + wpRevision + "/");
		pathwayIdentifierResource.addProperty(RDFS.label, wpIdentifier);
		pathwayResource.addProperty(DC.identifier, pathwayIdentifierResource);
		pathwayResource.addProperty(RDFS.label, wpIdentifier);

		// PARSE GPML
		if (!gpml.startsWith("{{deleted|")
				&& isValidXML(gpml, wpIdentifier, wpRevision)) {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			StringReader reader = new StringReader(gpml);
			InputSource inputSource = new InputSource(reader);
			Document doc = docBuilder.parse(inputSource);
			reader.close();
			doc.getDocumentElement().normalize();
			try {
				// Get the Pathway Nodes
				XPath xPath = XPathFactory.newInstance().newXPath();

				Node pathwayLicense = ((Node) xPath.evaluate(
						"/Pathway/@License", doc, XPathConstants.NODE));
				Node pathwayName = ((Node) xPath.evaluate("/Pathway/@Name",
						doc, XPathConstants.NODE));
				Node pathwayOrganism = ((Node) xPath.evaluate(
						"/Pathway/@Organism", doc, XPathConstants.NODE));

				// Map the organism to DbPedia
				if (pathwayOrganism != null) {
					Resource dbPediaSpeciesResource = model
							.createResource("http://dbpedia.org/page/"
									+ pathwayOrganism.getNodeValue().replace(
											" ", "_"));
					wikipathwaysResource.addProperty(DC.coverage,
							dbPediaSpeciesResource);
				}

				// Add pathway level details to the RDF model
				if (pathwayName != null)
					pathwayResource.addProperty(RDFS.label,
							pathwayName.getNodeValue());
				if (pathwayIdentifierResource != null)
					pathwayResource.addProperty(DC.identifier,
							pathwayIdentifierResource);
				if (pathwayLicense != null)
					pathwayResource.addProperty(DCTerms.license,
							pathwayLicense.getNodeValue());
				pathwayResource.addProperty(RDF.type, Biopax_level3.Pathway);
				pathwayResource.addProperty(DCTerms.isPartOf,
						wikipathwaysResource);


				// Get the Group References by calling the getGroupIds from
				// the
				// wikipathways webservices
				NodeList groupIdsNL = doc.getElementsByTagName("Group");
				Set<String> groupIdsSet = new TreeSet<String>();
				for (int t = 0; t < groupIdsNL.getLength(); t++) {
					groupIdsSet.add(groupIdsNL.item(t).getAttributes()
							.getNamedItem("GroupId").getTextContent());
					// System.out.println(groupIdsNL.item(t).getAttributes()
					// .getNamedItem("GroupId").getTextContent());
				}

				for (String groupRef : groupIdsSet) {
					Resource groupResource = model
							.createResource(pathwayResourceURI + "Group/"
									+ groupRef);
					groupResource.addProperty(RDF.type,
							wikipathwaysGroupDefinitionResource);
				}

				// Add pathwayElements to the RDF model
				NodeList dataNodeNL = doc.getElementsByTagName("DataNode");

				// for (PathwayElement pwElm : pathway.getDataObjects()) {
				for (int dnlint = 0; dnlint < dataNodeNL.getLength(); dnlint++) {
					// Only take elements with type DATANODE (genes,
					// proteins,
					// metabolites)
					String datanodeType = null;
					if (dataNodeNL.item(dnlint).getAttributes().getNamedItem("Type") != null) {
					datanodeType = dataNodeNL.item(dnlint)
							.getAttributes().getNamedItem("Type")
							.getTextContent();
					}
					String datanodeTextLabel = dataNodeNL.item(dnlint)
							.getAttributes().getNamedItem("TextLabel")
							.getTextContent();
					String datanodeGraphId = dataNodeNL.item(dnlint)
							.getAttributes().getNamedItem("GraphId")
							.getTextContent();
					String datanodeGroupRef = null;
					if (dataNodeNL.item(dnlint).getAttributes()
							.getNamedItem("GroupRef") != null) {
						datanodeGroupRef = dataNodeNL.item(dnlint)
								.getAttributes().getNamedItem("GroupRef")
								.getTextContent();
					}
					NodeList datanodeChilds = dataNodeNL.item(dnlint)
							.getChildNodes();
					// Get ChildNodes Xref, Graphics

					String datanodeXrefId = null;
					String datanodeXrefDatabase = null;
					String dataNodeGraphicsColor = null;
					Float dataNodeGraphicsCenterX = null;
					Float dataNodeGraphicsCenterY = null;
					Float dataNodeGraphicsWidth = null;
					Float dataNodeGraphicsHeight = null;
					for (int dnchildint = 0; dnchildint < datanodeChilds
							.getLength(); dnchildint++) {
						// Xref
						if (datanodeChilds.item(dnchildint).getNodeName()
								.equals("Xref")) {
							datanodeXrefId = datanodeChilds.item(dnchildint)
									.getAttributes().getNamedItem("ID")
									.getTextContent();
							datanodeXrefDatabase = datanodeChilds
									.item(dnchildint).getAttributes()
									.getNamedItem("Database").getTextContent();
						}
						//Graphics
						if (datanodeChilds.item(dnchildint).getNodeName().equals("Graphics")){
							if (datanodeChilds.item(dnchildint).getAttributes().getNamedItem("Color") != null) {
							     dataNodeGraphicsColor = datanodeChilds.item(dnchildint).getAttributes().getNamedItem("Color").getTextContent();
							}
							dataNodeGraphicsCenterX = Float.valueOf(datanodeChilds.item(dnchildint).getAttributes().getNamedItem("CenterX").getTextContent());
							dataNodeGraphicsCenterY = Float.valueOf(datanodeChilds.item(dnchildint).getAttributes().getNamedItem("CenterY").getTextContent());
							dataNodeGraphicsWidth = Float.valueOf(datanodeChilds.item(dnchildint).getAttributes().getNamedItem("Width").getTextContent());
							dataNodeGraphicsHeight = Float.valueOf(datanodeChilds.item(dnchildint).getAttributes().getNamedItem("Height").getTextContent());
						}
					}

					// if (pwElm.getObjectType() == ObjectType.DATANODE) {
					Resource pathwayEntity = model
							.createResource(pathwayResourceURI + "Datanode/"
									+ datanodeGraphId);
					pathwayEntity
							.addProperty(DCTerms.isPartOf, pathwayResource);
					pathwayEntity.addProperty(RDF.type, Spatial.Feature);
					pathwayEntity.addLiteral(xCoordinate, dataNodeGraphicsCenterX);
					pathwayEntity.addLiteral(yCoordinate, dataNodeGraphicsCenterX);
					if (dataNodeGraphicsColor != null){
						pathwayEntity.addLiteral(color, dataNodeGraphicsColor);
					}
					pathwayEntity.addLiteral(width, dataNodeGraphicsWidth);
					pathwayEntity.addLiteral(height, dataNodeGraphicsHeight);
					

					pathwayEntity.addProperty(RDFS.label, datanodeTextLabel);
					// System.out.println(pwElm.getDataNodeType().toString());
					if (datanodeType != null) {
					if (datanodeType.equals("Metabolite")) {
						pathwayEntity.addProperty(RDF.type,
								Biopax_level3.SmallMolecule);
					} else if (datanodeType.equals("Gene")) {
						pathwayEntity.addProperty(RDF.type, Biopax_level3.Dna);
					} else if (datanodeType.equals("Protein")) {
						pathwayEntity.addProperty(RDF.type,
								Biopax_level3.Protein);
					} else if (datanodeType.equals("GeneProduct")) {
						pathwayEntity
								.addProperty(RDF.type, OWL.equivalentClass); // See
																				// for
																				// details:
																				// http://answers.semanticweb.com/questions/12177/how-do-you-map-an-object-to-be-either-a-or-b-but-nothing-else-in-rdf
						pathwayEntity.addProperty(OWL.unionOf,
								geneProductRDFNode);
					} else if (datanodeType.equals("Pathway")) {
						Resource interactingPathwayResource = model
								.createResource(pathwayResourceURI
										+ "Interaction/" + datanodeGraphId);
						interactingPathwayResource.addProperty(RDF.type,
								Biopax_level3.Interaction);
						interactingPathwayResource.addProperty(RDF.subject,
								pathwayResource);
						interactingPathwayResource
								.addProperty(
										RDF.object,
										model.createResource(WpNamespaces.nsWikipathways
												+ "Pathway/" + datanodeXrefId));
					} else if (datanodeType.equals("Pathway")) {
						pathwayEntity.addProperty(RDF.type,
								Biopax_level3.Pathway);
					} else if (datanodeType.equals("Complex")) {
						pathwayEntity.addProperty(RDF.type,
								Biopax_level3.Complex);
					} else {
						pathwayEntity.addProperty(RDF.type,
								Biopax_level3.Entity);
					}
					}

					if (datanodeXrefDatabase != null) {
						String xRefDataSource = datanodeXrefDatabase.toString()
								.replace(" ", "_");
						Resource pwElmIdentifierResource = model
								.createResource(WpNamespaces.nsIdentifiers
										+ "WikiPathways/" + xRefDataSource
										+ "/" + datanodeXrefId);
						pwElmIdentifierResource.addProperty(RDFS.label,
								datanodeXrefId);
						Resource pwElmSourceResource = model
								.createResource(WpNamespaces.nsIdentifiers
										+ "WikiPathways/" + xRefDataSource);
						pwElmSourceResource.addProperty(RDFS.label,
								xRefDataSource);
						pathwayEntity.addProperty(DC.identifier,
								pwElmIdentifierResource);
						pathwayEntity.addProperty(DC.source,
								pwElmSourceResource);
					}

					if (datanodeGroupRef != null) { // Element is part of a
						// group
						pathwayEntity.addProperty(
								DCTerms.isPartOf,
								model.getResource(pathwayResourceURI + "Group/"
										+ datanodeGroupRef));
					}
				}

				NodeList lineNL = doc.getElementsByTagName("Line");
				for (int llint = 0; llint < lineNL.getLength(); llint++) {
					// Only take elements with type DATANODE (genes,
					// proteins,
					// metabolites)
					if (lineNL.item(llint).getAttributes()
							.getNamedItem("GraphId") != null) {
						String lineGraphId = lineNL.item(llint).getAttributes()
								.getNamedItem("GraphId").getTextContent();

						// Xref
						Element LineGraphElement = (Element) lineNL.item(llint);
						NodeList pointGraphrefs = ((NodeList) xPath.evaluate(
								"//Point/@GraphRef", LineGraphElement,
								XPathConstants.NODESET));
						String leftLineGraphRefs = null;
						String rightLineGraphRefs = null;

						if ((pointGraphrefs.item(0) != null)
								&& ((pointGraphrefs.item(1) != null))) {
							leftLineGraphRefs = pointGraphrefs.item(0)
									.getTextContent();
							rightLineGraphRefs = pointGraphrefs.item(1)
									.getTextContent();
						}
						if ((leftLineGraphRefs != null)
								&& (rightLineGraphRefs != null)) {

							String startGroupOrDatanode;
							String endGroupOrDatanode;

							// if
							// (pwElm.getObjectType().equals(ObjectType.LINE)) {
							Resource pathwayLine = model
									.createResource(pathwayResourceURI
											+ "Interaction/" + lineGraphId);
							pathwayLine.addProperty(RDF.type,
									Biopax_level3.Interaction);
							if (groupIdsSet.contains(leftLineGraphRefs)) {
								startGroupOrDatanode = "Group/";
							} else {
								startGroupOrDatanode = "Datanode/";
							}
							if (groupIdsSet.contains(rightLineGraphRefs)) {
								endGroupOrDatanode = "Group/";
							} else {
								endGroupOrDatanode = "Datanode/";
							}
							pathwayLine.addProperty(
									RDF.predicate,
									model.getResource(pathwayResourceURI
											+ startGroupOrDatanode
											+ leftLineGraphRefs));
							pathwayLine.addProperty(
									RDF.predicate,
									model.getResource(pathwayResourceURI
											+ endGroupOrDatanode
											+ rightLineGraphRefs));
						}
					}
				}
				// TODO Seek a pathway with a state
				/*
				 * if (pwElm.getObjectType() == ObjectType.STATE) { Resource
				 * pathwayEntity = model .createResource(pathwayResourceURI +
				 * "State/" + pwElm.getGraphId()); pathwayEntity
				 * .addProperty(DCTerms.isPartOf, pathwayResource);
				 * pathwayEntity.addProperty(RDFS.label, pwElm.getTextLabel());
				 * } }
				 */

				// Get the Biopax References
				NodeList bpRef = doc.getElementsByTagName("BiopaxRef");
				HashMap<String, String> bpRefmap = new HashMap<String, String>();
				if (bpRef != null && bpRef.getLength() > 0) {

					for (int j = 0; j < bpRef.getLength(); j++) {
						if (bpRef.item(j).getParentNode().getNodeName()
								.equals("DataNode")) {
							bpRefmap.put(bpRef.item(j).getTextContent(),
									pathwayResourceURI
											+ "Datanode/"
											+ bpRef.item(j).getParentNode()
													.getAttributes()
													.getNamedItem("GraphId")
													.getNodeValue());
						}
				
						if (bpRef.item(j).getParentNode().getNodeName()
								.equals("Pathway")) {
							bpRefmap.put(bpRef.item(j).getTextContent(),
									pathwayResourceURI);
						}
						if (bpRef.item(j).getParentNode().getNodeName()
								.equals("Line")) {
							// TODO make sure every entity has a graphId
							if (bpRef.item(j).getParentNode().getAttributes()
									.getNamedItem("GraphId") != null)
								bpRefmap.put(
										bpRef.item(j).getTextContent(),
										pathwayResourceURI
												+ "Line/"
												+ bpRef.item(j)
														.getParentNode()
														.getAttributes()
														.getNamedItem("GraphId")
														.getNodeValue());
						}
						if (bpRef.item(j).getParentNode().getNodeName()
								.equals("State")) {
							bpRefmap.put(
									bpRef.item(j).getTextContent(),
									pathwayResourceURI
											+ "State/"
											+ bpRef.item(j).getParentNode()
													.getAttributes()
													.getNamedItem("GraphId")
													.getNodeValue());
						}
						if (bpRef.item(j).getParentNode().getNodeName()
								.equals("Group")) {
							bpRefmap.put(
									bpRef.item(j).getTextContent(),
									pathwayResourceURI
											+ "Group/"
											+ bpRef.item(j).getParentNode()
													.getAttributes()
													.getNamedItem("GroupId")
													.getNodeValue());
						}
					}
				}

				NodeList cv = doc
						.getElementsByTagName("bp:openControlledVocabulary");
				if (cv != null && cv.getLength() > 0) {
					for (int a = 0; a < cv.getLength(); a++) {
						NodeList cvList = cv.item(a).getChildNodes();
						String OntologyName = cvList.item(2).getTextContent();
						String OntologyId = cvList.item(1).getTextContent();
						if (OntologyName.equals("Pathway Ontology")) {
							Resource ontologyResource = model
									.createResource("http://purl.org/obo/owl/PW#"
											+ OntologyId.replace(":", "_"));
							
							pathwayResource.addProperty(RDF.type,
									ontologyResource);
						}

						System.out.println(OntologyName + ": " + OntologyId);

					}
				}
				NodeList nl = doc.getElementsByTagName("bp:PublicationXRef");
				System.out.println("test" + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					 System.out.println("test");
					for (int k = 0; k < nl.getLength(); k++) {
						NodeList refId = nl.item(k).getChildNodes();
						if (refId.getLength() > 3) {
							if (refId.item(3).getTextContent().equals("PubMed")
									&& (refId.item(1).getTextContent() != null)) {
								Resource pubmedEntity = model
										.createResource("http://www.ncbi.nlm.nih.gov/pubmed/"
												+ refId.item(1)
														.getTextContent());
								pubmedEntity.addProperty(RDF.type,
										Biopax_level3.Evidence);
								pubmedEntity.addProperty(DCTerms.identifier,
										refId.item(1).getTextContent());
								if (bpRefmap.get(nl.item(k).getAttributes()
										.item(0).getNodeValue()) != null) {

									Resource tempItem = model
											.getResource(bpRefmap.get(
													nl.item(k).getAttributes()
															.item(0)
															.getNodeValue())
													.toString());
									tempItem.addProperty(
											DCTerms.bibliographicCitation,
											pubmedEntity);
								}

							}
						} else {
							System.out.println("PROBLEM with: " + wpIdentifier);
						}
					}
				}
				nl = doc.getElementsByTagName("bp:PublicationXref");
				System.out.println("test" + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					 System.out.println("test");
					for (int k = 0; k < nl.getLength(); k++) {
						NodeList refId = nl.item(k).getChildNodes();
						if (refId.getLength() > 3) {
							if (refId.item(3).getTextContent().equals("PubMed")
									&& (refId.item(1).getTextContent() != null)) {
								Resource pubmedEntity = model
										.createResource("http://www.ncbi.nlm.nih.gov/pubmed/"
												+ refId.item(1)
														.getTextContent());
								pubmedEntity.addProperty(RDF.type,
										Biopax_level3.Evidence);
								pubmedEntity.addProperty(DCTerms.identifier,
										refId.item(1).getTextContent());
								if (bpRefmap.get(nl.item(k).getAttributes()
										.item(0).getNodeValue()) != null) {

									Resource tempItem = model
											.getResource(bpRefmap.get(
													nl.item(k).getAttributes()
															.item(0)
															.getNodeValue())
													.toString());
									tempItem.addProperty(
											DCTerms.bibliographicCitation,
											pubmedEntity);
								}

							}
						} else {
							System.out.println("PROBLEM with: " + wpIdentifier);
						}
					}
				}
				nl = doc.getElementsByTagName("bp:publicationXref");
				System.out.println("test" + nl.getLength());
				if (nl != null && nl.getLength() > 0) {
					 System.out.println("test");
					for (int k = 0; k < nl.getLength(); k++) {
						NodeList refId = nl.item(k).getChildNodes();
						if (refId.getLength() > 3) {
							if (refId.item(3).getTextContent().equals("PubMed")
									&& (refId.item(1).getTextContent() != null)) {
								Resource pubmedEntity = model
										.createResource("http://www.ncbi.nlm.nih.gov/pubmed/"
												+ refId.item(1)
														.getTextContent());
								pubmedEntity.addProperty(RDF.type,
										Biopax_level3.Evidence);
								pubmedEntity.addProperty(DCTerms.identifier,
										refId.item(1).getTextContent());
								if (bpRefmap.get(nl.item(k).getAttributes()
										.item(0).getNodeValue()) != null) {

									Resource tempItem = model
											.getResource(bpRefmap.get(
													nl.item(k).getAttributes()
															.item(0)
															.getNodeValue())
													.toString());
									tempItem.addProperty(
											DCTerms.bibliographicCitation,
											pubmedEntity);
								}

							}
						} else {
							System.out.println("PROBLEM with: " + wpIdentifier);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		FileOutputStream fout;
		fout = new FileOutputStream("/tmp/WpRDF/" + wpIdentifier + "_"
				+ wpRevision + ".nt");
		model.write(fout, "N-TRIPLE");

	}

	public static void main(String[] args) {
		// The identifer of the pathway under scrutiny

		// Initiate the model
		Model model = ModelFactory.createDefaultModel();
		try {
			File fileName = new File("/tmp/WpGPML/WP1531_45011.gpml");
			String gpml = FileUtils.readFileToString(fileName);
			String wpIdentifier = fileName.getName().substring(0,
					fileName.getName().indexOf("_"));
			String wpRevision = fileName.getName().substring(
					fileName.getName().indexOf("_") + 1,
					fileName.getName().indexOf("."));
			addPathway2Rdf(wpIdentifier, wpRevision, gpml);
			System.out.println(FOAF.NS);
			// FileOutputStream fout;
			// fout = new FileOutputStream("/tmp/" + wpIdentifier + ".rdf");
			// model.write(fout, "N-TRIPLE");
		} catch (DOMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConverterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
