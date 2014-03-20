# Introduction 
The rdf representation of Pathways in WikiPathways are created by serializing GPML. After completion of the RDF creation, bioloigical interactions are extracted by inference through so-called CONSTRUCT queries. WikiPathways captures different biological relations. These are captured as a arrowhead property in the lines drawn. The directionality of the relationship is captured by a GraphRef property of the same line. 

To extract a specific biological relation (e.g. Inhibition), one need to describe these diffent properties in a Sparql query.
```sparql
SELECT * WHERE {
	# Get the pathway identifier
	?pathway dc:identifier ?wpIdentifier .

	# An interaction is between 2 datanodes
	# DataNode 1   	
	?datanode1 dc:identifier ?dn1Identifier .
   	?datanode1 gpml:graphid ?dn1GraphId .
   	?datanode1 rdf:type gpml:DataNode .
   	?datanode1 dcterms:isPartOf ?pathway .

	# DataNode 2
	?datanode2 dc:identifier ?dn2Identifier .
	?datanode2 rdf:type gpml:DataNode .
	?datanode2 dcterms:isPartOf ?pathway .
	?datanode2 gpml:graphid ?dn2GraphId .

	# Some DataNodes don't contain an identifier
	FILTER (!regex(str(?datanode2), "noIdentifier")) .  
        FILTER (!regex(str(?datanode1), "noIdentifier")) .

   	# The base of an interaction is the line of type gpml:Interaction
   	?line rdf:type gpml:Interaction .
   	?line dcterms:isPartOf ?pathway .
   	?line gpml:graphid ?lineGraphId . 
   	# A line is linked to a DataNodes by it graphref.
	?line gpml:graphref ?dn1GraphId .
	?line gpml:graphref ?dn2GraphId .
	FILTER (?datanode2 != ?datanode1)

        # Directionality is captured in the Points attached to 
        # a line. Since both datanodes can be a target of a direction
        # we need to use a UNION to capture both
        ?point rdf:type gpml:Point .
        ?point dcterms:isPartOf ?line .
        ?point gpml:arrowHead "TBar"^^xsd:string .
        {{?point gpml:graphref ?dn2GraphId .} UNION {?point gpml:graphref ?dn1GraphId}}.
        
	}
``` 


# Interaction hierarchy in WikiPathways
* wp:relation
 * wp:unDirectedInteration
 * wp:DirectedInteraction
  * wp:Inhibition
  * wp:TranscriptionTranslation
  * wp:Modification
  * wp:Conversion
 - wp:AffectedInteraction
  - wp:Stimulation
  - wp:NecessaryStimulation
  - wp:Cleavage
  - wp:Catalysis
 
