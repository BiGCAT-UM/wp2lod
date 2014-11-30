export WORKSPACE=`pwd`
rm -rf /tmp/GPML
mkdir /tmp/GPML
rm -rf /tmp/WPREACTRDF
mkdir /tmp/WPREACTRDF
rm -rf /tmp/OPSREACTOMEDUMP
mkdir /tmp/OPSREACTOMEDUMP
cp ../BioDataSource.ttl /tmp
wget -O /tmp/OPSREACTOMEDUMP/reactome.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Homo%20sapiens&fileType=gpml&tag=Curation:Reactome_Approved"
cd /tmp/OPSREACTOMEDUMP
for file in $(ls); do unzip $file; done
rm *.zip
cd /tmp

cd "${WORKSPACE}"
cd ..
ant run

# Generate the triples
rm /tmp/wpContent*
java -Xmx4G -jar WP2Rdf.jar

