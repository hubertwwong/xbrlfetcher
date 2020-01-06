# xbrlfetcher

Fetch xbrl files from the SEC.

## Basics

1. Grabs all index files which are gzipped.
2. From the index files, grab the actual files.


## TODO

Probably need a way to display errors.
1. ID the class of errors.
2. Add some meaningful error messages.

probably a way to specify args for to run each command.
- like a command for the grabbing the index.
- one to grab the xbrl files.

env not working when running the jar directly.

docker-compose not reading the idx.
- seems to be something with my setup of compose.
- hardcoding a file allows the parse to happen.
- basically the index files are invalid...

some error checking for fetch

xbrl index decompression and parsing....
FIXED. BASICALLY YOU NEED TO DECLARE THEM in the docker-compose file.


docker env not working. fix it. Not sure why this is broken. I have other docker projects that have this working.

bounds checking on functions

grab the actual xbrl files (rough version is working)

unit testing

better path mechanism...
- instead of string conncat.

better documentation

relative directory output
- not sure how to fix this. I think its a function of docker.
- maybe just provide an options.
- or use a softlink



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
