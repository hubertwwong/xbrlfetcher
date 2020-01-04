# xbrlfetcher

Fetch xbrl files from the SEC.

## Basics

1. Grabs all index files which are gzipped.
2. From the index files, grab the actual files.


## TODO

some error checking for fetch

xbrl index decompression and parsing....

docker env not working. fix it.

bounds checking on functions

grab the actual xbrl files

unit testing

better path mechanism...
- instead of string conncat.

better documentation



## Notes

https://www.sec.gov/edgar/searchedgar/accessing-edgar-data.htm

https://www.sec.gov/Archives/edgar/full-index/
YEAR/QTR{1-4}/Company.gz

https://www.sec.gov/Archives/edgar/full-index/2019/QTR1/Company.gz


Go here and grab all of the xbrl gz
From there. You can generate the
One thing in the parser of the idx files.
grab the cik and company names.

https://www.sec.gov/Archives/edgar/data/1001039/0001001039-19-000062.txt
