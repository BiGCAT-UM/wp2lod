rm -rf /tmp/GPML
mkdir /tmp/GPML
rm -rf /tmp/OPSWPRDF
mkdir /tmp/OPSWPRDF
rm -rf /tmp/OPSWIKIDUMP
mkdir /tmp/OPSWIKIDUMP
cp ../BioDataSource.ttl /tmp
cp ../WP2Rdf.jar /tmp
wget -O /tmp/OPSWIKIDUMP/Anopheles%20gambiae.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Anopheles%20gambiae&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Arabidopsis%20thaliana.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Arabidopsis%20thaliana&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Bos%20taurus.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Bos%20taurus&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Bacillus%20subtilis.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Bacillus%20subtilis&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Caenorhabditis%20elegans.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Caenorhabditis%20elegans&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Canis%20familiaris.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Canis%20familiaris&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Danio%20rerio.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Danio%20rerio&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Drosophila%20melanogaster.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Drosophila%20melanogaster&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Equus%20caballus.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Equus%20caballus&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Gallus%20gallus.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Gallus%20gallus&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Homo%20sapiens.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Homo%20sapiens&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Mus%20musculus.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Mus%20musculus&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Mycobacterium%20tuberculosis.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Mycobacterium%20tuberculosis&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Oryza%20sativa.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Oryza%20sativa&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Pan%20troglodytes.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Pan%20troglodytes&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Rattus%20norvegicus.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Rattus%20norvegicus&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Saccharomyces%20cerevisiae.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Saccharomyces%20cerevisiae&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Zea%20mays.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Zea%20mays&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Gibberella%20zeae.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Gibberella%20zeae&fileType=gpml&tag=Curation:AnalysisCollection"
wget -O /tmp/OPSWIKIDUMP/Escherichia%20coli.zip "http://www.wikipathways.org//wpi/batchDownload.php?species=Escherichia%20coli&fileType=gpml&tag=Curation:AnalysisCollection"
cd /tmp/OPSWIKIDUMP
for file in $(ls); do unzip $file; done
rm *.zip
cd /tmp
