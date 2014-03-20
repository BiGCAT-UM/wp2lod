# Introduction 
The rdf representation of Pathways in WikiPathways are created by serializing GPML. After completion of the RDF creation, bioloigical interactions are extracted by inference through so-called CONSTRUCT queries. WikiPathways captures different biological relations. These are captured as a arrowhead property in the lines drawn. The directionality of the relationship is captured by a GraphRef property of the same line. 

To extract a specific biological relation (e.g. Inhibition), one need to describe these diffent properties in a Sparql query.

The SPARQL query below, will extract all inhibitions drawn into WikiPathways. 

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

An inhibition is a biological relation that in the context of WikiPathways consist of two DataNodes that are connected with a line. On the line an inhibition is drawn by a vertical line, called a TBar. Both the line and the datanodes are part of the same pathway. So the SPARQL query above is build as follows. 

First we identify the pathway and its identifier.

```
?pathway dc:identifier ?wpIdentifier .
```

Then we identify the two datanodes.:
```
?datanode1 dc:identifier ?dn1Identifier .
   	?datanode1 gpml:graphid ?dn1GraphId .
   	?datanode1 rdf:type gpml:DataNode .
   	?datanode1 dcterms:isPartOf ?pathway .

	# DataNode 2
	?datanode2 dc:identifier ?dn2Identifier .
	?datanode2 rdf:type gpml:DataNode .
	?datanode2 dcterms:isPartOf ?pathway .
	?datanode2 gpml:graphid ?dn2GraphId .
```
Some datanodes don't contain an identifier. We want to ignore those:
```	
	# Some DataNodes don't contain an identifier
	FILTER (!regex(str(?datanode2), "noIdentifier")) .  
        FILTER (!regex(str(?datanode1), "noIdentifier")) .
```

Finally we capture the interaction and its type (i.e. TBar)

```
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
```

# Infering biological relation with Construct queries.

WikiPathways contains xx Biological relation types. All can be extracted with similar queries as the one above. Due to number of lines involved the query is not only error prone, but also requires considerable amount of time to execute. We use CONSTRUCT queries to simplify the queries by adding new triples. We could infer the triples by usingthe SPARQL query mentioned above in the following CONSTRUCT query.

```
CONSTRUCT {
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:relation .
}
FROM <http://rdf.wikipathways.org/> 
WHERE {
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
        ?point gpml:arrowHead "Arrow"^^xsd:string .
        {{?point gpml:graphref ?dn2GraphId .} UNION {?point gpml:graphref ?dn1GraphId}}.
        
	}
```



The semantics of the inhibition are captured in the GPML node `ArrowHead` which has an attribute `arrowHead`. The follwoing triple will extract the inhibition `?point gpml:arrowHead "TBar"^^xsd:string .` 


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
 
