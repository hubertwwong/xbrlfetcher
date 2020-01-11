# xbrlfetcher

Fetch xbrl files from the SEC.



## What the program does.

1. Grabs all index files which are gzipped.
2. Unzippes each gzip files read through each index file and grabs the xbrl files.

**WARNING. The actual XBRL files are large. Make sure you have enough space**



## How to run

```
docker-compose up
```

or

```
java -jar target/xbrlfetcher-1.0-SNAPSHOT.jar
```

The program reads the `default.cfg` file. If you want to use your own settings, copy the `default.cfg` to `user.cfg` and modify that file.



## Config file

This env file contains all of the configurations options for this program.
Change them to suite your needs.

```
TMP_DIR=tmp
```
Temp directory to write to

```
XBRL_ACTUAL_DIR=tmp/xbrl/actual
```
Location of the XBRL files.

```
XBRL_DOWNLOAD_DELAY=10
```
Delay in seconds between fetching files.
Please don't spam the SEC.

```
XBRL_FULL_INDEX_URL_PREFIX=https://www.sec.gov/Archives/edgar/full-index
```
The url prefix to grab the XBRL index files.

```
XBRL_OUTPUT_FILENAME=xbrl.idx
```
Filename of the unzip filed or xbrl index files.
I think this is what the SEC uses.

```
XBRL_INDEX_DIR=tmp/xbrl/index
```
Directory of where to store the XBRL index filex.

```
XBRL_START_YEAR=1993
```
Start year of the first document.
From what I checked on 01/08/2020, this was the first year.

```
XBRL_URL_PREFIX=https://www.sec.gov/Archives
```
The url prefix to grab the XBRL files.

```
XBRL_ZIP_FILENAME=xbrl.gz
```
Zip file name in edgar for the index files.




## TODO

Root permission of the output files in docker
- probably can't do much about it using docker.


unit testing

better documentation



## Links

https://www.sec.gov/edgar/searchedgar/accessing-edgar-data.htm
Main page for edgar

https://www.sec.gov/Archives/edgar/full-index/{YEAR}/{QTR1to4}/Company.gz
Index urls are in this format.

https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/Company.gz
An example of the index file.

https://www.sec.gov/Archives/edgar/data/{CIK}/{DOC_FILE_NAME}
Format of the xbrl specific urls

https://www.sec.gov/Archives/edgar/data/1001039/0001001039-19-000062.txt
An example url of the xbrl file.