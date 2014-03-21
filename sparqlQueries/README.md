# Introduction 
The rdf representation of Pathways in WikiPathways are created by serializing GPML. After completion of the RDF creation, bioloigical interactions are extracted by inference through so-called CONSTRUCT queries. WikiPathways captures different biological relations. These are captured as a arrowhead property in the lines drawn. The directionality of the relationship is captured by a GraphRef property of the same line. 

To extract a specific biological relation (e.g. Inhibition), one need to describe these diffent properties in a Sparql query.

The SPARQL query below, will extract all inhibitions drawn into WikiPathways. 

```
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

# Infering biological relation with CONSTRUCT queries.

WikiPathways contains xx Biological relation types. All can be extracted with similar queries as the one above. Due to number of lines involved the query is not only error prone, but also requires considerable amount of time to execute. We use CONSTRUCT queries to simplify the queries by adding new triples. We could infer the triples by usingthe SPARQL query mentioned above in the following CONSTRUCT query.

```
CONSTRUCT {
	?line rdf:type wp:Relation .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Inhibition .
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
        ?point gpml:arrowHead "TBar"^^xsd:string .
        {{?point gpml:graphref ?dn2GraphId .} UNION {?point gpml:graphref ?dn1GraphId}}.
        
	}
```

This CONSTRUCT query adds three triples for each interaction which contains an `arrowHead` `TBar`. First it states that it is a relation
```
   ?line rdf:type wp:Relation .
```
then it indicates it being a `DirectedInteraction`
```
   ?line rdf:type wp:DirectedInteraction .
```
Finally the query states that it is an inhibition
```
   ?line rdf:type wp:Inhibition .
```

With these added triples we can now query for inhibitions with the following SPARQL query:
```
	SELECT * WHERE 
	{	
		?line dcterms:isPartOf ?pathway .
		?line rdf:type wp:Inhibition .
		?line wp:source ?source .
		?line wp:target ?target .
	}
```
NOTE: For the above query to function yet another CONSTRUCT query is needed to introduce the triples `?line wp:source ?source .` and `?line wp:target ?target`.  This CONSTRUCT query will be documented in detail below, together with the individual CONSTRUCT queries for each recognised biological relations in WikiPathways. 

# Interaction hierarchy in WikiPathways
In WikiPathways 13 different types of relations are recognised. All are of type wp:relation. Then we have three main relation types, being: 
### The undirected relation
The undirected relation is a relation type which doesn't contain any directionality. A relationship is drawn as a line without specific attributes.
### The directed relation

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/Arrow.png)

The directed interaction is one that does contain directionality. The directionality is captured by an arrowhead indicating a specific subtype of directionality (e.g. Inhibition, Conversion, etc.). The CONSTRUCT query to extract a directed interaction are similar to each except for one triple where the distinctive arrowhead is expressed. 

```
CONSTRUCT {
        ?line rdf:type wp:<subtype> .
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
        
        ?point gpml:arrowHead "<ARROWHEAD>"^^xsd:string .
        
        {{?point gpml:graphref ?dn2GraphId .} UNION {?point gpml:graphref ?dn1GraphId}}.
        
	}

```
Below the different CONSTRUCT query are described in where they differ from the basic query above. 

#### The basic directed interaction.

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/Arrow.png)

This interaction type is recognised by a simple arrow. In fact it is the directed relation without a subtype. As such the triples in the CONSTRUCT body are missing this subtype and as such only contain two triples.
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:<subtype> .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "Arrow"^^xsd:string .
```

#### The inhibition
![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/TBar.png)


The inhibition is a directed interaction expressed by a TBar as arrow head. 
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:Inhibition .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "TBar"^^xsd:string .
```

#### The Binding

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimBinding.png)

The Binding is a directed interaction expressed by a mim-binding as arrow head. 
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:Binding .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "mim-binding"^^xsd:string .
```

#### The Conversion
![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimConversion.png)

The conversion is a directed interaction expressed by a mim-conversion as arrow head. 
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:Conversion .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "mim-conversion"^^xsd:string .
```

#### The Modification

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimModification.png)

The modification is a directed interaction expressed by a mim-modification as arrow head. 
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:Modification .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "mim-modification"^^xsd:string .
```

#### The TranscriptionTranslation

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimTranscriptionTranslation.png)


The transcriptiontranslation is a directed interaction expressed by a mim-transcription-translation as arrow head. 
The triples in the CONSTRUCT header:
```
        ?line rdf:type wp:TranscriptionTranslation .
	?line rdf:type wp:DirectedInteraction .
	?line rdf:type wp:Relation .
```

The triple recognizing the basic directed relation is:
```
	?point gpml:arrowHead "mim-transcription-translation"^^xsd:string .
```
### The affected interaction
An affected interaction is one of the above-mentioned types being affected by another pathway element. An example of such an interaction is a catalysis where a specific directed relation is catalysed by a catalyst. 

![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimCatalysis.png)

The following CONSTRUCT query will annotate all catalysi accordingly

```
CONSTRUCT {
	?baseline wp:isCatalyzedBy ?datanode .
	?interactionline rdf:type wp:Catalysis .
	?interactionline rdf:type wp:relation .
}
FROM <http://rdf.wikipathways.org/>
WHERE {
    	# target baseline 
    	?baseline gpml:graphid ?edgeGraphId .
    	?baseline rdf:type gpml:Interaction .
	?baseline rdf:type wp:DirectedInteraction .
    	?baseline dcterms:isPartOf ?pathway .

    	# source datanode    	
    	?datanode gpml:graphid ?dnGraphId .
    	?datanode rdf:type gpml:DataNode .
    	?datanode dcterms:isPartOf ?pathway .
    	
    	# constructed interaction
 	?interactionline dcterms:isPartOf ?pathway .
    	?interactionline gpml:graphref ?dnGraphId .
    	?interactionline gpml:graphref ?edgeGraphId .	
    	?interactionline gpml:arrowTowards ?edgeGraphId .
	?interactionline gpml:arrowHead "mim-catalysis"^^xsd:string .
}
```

The following affected relation types are recognized:

#### A Catalysis
![Directed Interaction](https://raw.githubusercontent.com/andrawaag/WPRDFDoc/master/interactionExamples/MimCatalysis.png)

A Catalysis is where a 

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
 
