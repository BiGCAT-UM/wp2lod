rm -rf /tmp/GPML
mkdir /tmp/GPML
rm -rf /tmp/OPSWPRDF
mkdir /tmp/OPSWPRDF
rm -rf /tmp/OPSWIKIDUMP
mkdir /tmp/OPSWIKIDUMP
cp ../BioDataSource.ttl /tmp
wget -O /tmp/OPSWIKIDUMP/reactome.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Homo%20sapiens&fileType=gpml&tag=Curation:Reactome_Approved"
cd /tmp/OPSWIKIDUMP
for file in $(ls); do unzip $file; done
rm *.zip
cd /tmp

# Generate the triples
rm /tmp/wpContent*
 java -Xmx4G -jar WP2Rdf.jar

#Empty the triplestore
# isql-vt -U dba -P <PASSWORD> -S <PORT> "EXEC=RDF_GLOBAL_RESET ();"

export TTLFILE=`ls /tmp/wpContent*`

#Fill the triples store with the obtained triple

# infer the biological relations
# java -jar /tmp/inferDirection.jar 19755
