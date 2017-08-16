wp2lod
=========

This repo is a collection of all the files needed to run the wp2lod pipeline. Follow the steps below to clone and run this code.

## Command line
```git clone <paste wp2lod repo url>```

## Set config properties
* Rename config.properties.default to config.properties
* Edit the ```config.properties``` with relevant file locations

## With Ant

* ```ant clean runTests```

## In Eclipse
* Create a new project and point to cloned code
* Add lib jars to Build Path
* Specify (create?) src folder
* Check BridgeDb file path in WP2RDFConversion.java:91 (should point to derby files on local or server)
* RunConfiguration: main=Pathway2RDFv2
* File>Export>Runnable JAR file (to tmp/WP2Rdf.jar; expect warnings)

## Command line
```sh scripts/masterScript.sh``` (references runnable jars)

## Ragamuffin
* output files from pipeline:
 * wpContent_…_ttl
 * void.ttl
* These files get copied to:
```/home/virtuoso/andra/wp/db```
* Other ontologies files go into this same dir, e.g., *.owl and *.obo files

# Check new files in test instance

```cd /home/virtuoso/andra/```

```sudo cp -R wp wpcache  ##takes a while!```

```cd wpcache/db/```

```vi wp.ini```

* change [paramerter] ServerPort to 5556  ##any free port numbers will do: netstat -aunt
* change [HTTPServer] ServerPort to 5557

```virtuoso-t +conf  ##launch test instance```

* Open virtuoso command line and load new ttl and owl files (see below)
* Run benchmark queries and compare to live sparql endpoint
  * Need to tunnel to reach alt HTTPServer port (e.g., 5557):
   * 	Open new terminal and run this from localhost:
   * 	```ssh -T -N -L 5557:localhost:1234 <username>@ragamuffin.bigcat.unimaas.nl```
   * 	In browser, navigate to ```localhost:1234/sparql```
  * Close tunnel by:
   * 	```ps aux | grep localhost``` (note <pid>)
   * 	```sudo kill -9 <pid>```
* Should include Raptor validation step

* If ready then shutdown:

```SQL> DELETE FROM DB.DBA.RDF_QUAD;```

```SQL> shutdown();```

```cd ..```

```sudo rm -rf wpcache/*```

* Then go to live dir:
```cd ../../wp/db``` and load new files into

* Open command line for virtuoso:
```isql-vt -S 1973```  #port specified in wp.ini file

* Load new ttl file:

```SQL> DELETE FROM DB.DBA.RDF_QUAD;```  #warning: this deletes everything! This is useful during dev periods and for major db reconstruction; should NOT be used during normal production updates.

```SQL> DB.DBA.TTLP_MT (file_to_string_output ('wpContent_v0.0.69675_20130710.ttl'), '', 'http:/opsv13.wikipathways.org');```

```SQL> DB.DBA.RDF_LOAD_RDFXML (...);``` for all .owl files ## .obo files should be converted to .owl files prior to loading with this same syntax (http://www.bioontology.org/wiki/index.php/OboInOwl:Main_Page). 

* need to look into batch loading script

* Now, the sparql is live and ready for queries. 
* Note: sparql.wikipathways.org is proxy to 1974 port via apache.


