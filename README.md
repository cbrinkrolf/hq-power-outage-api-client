![Java CI](https://github.com/cbrinkrolf/hq-power-outage-api-client/actions/workflows/maven-build.yml/badge.svg?branch=main)

# Hydro Quebec Power Outage API Client
This is a hobby project utilizing the public power outage API of Hydro Quebec to retrieve data of current power outages in Quebec.

more information TBA

## Hydro Quebec API Documentation
The documentation and examples of the API can be found here: [Hydro Quebec API Doc](https://donnees.hydroquebec.com/explore/dataset/pannes-interruptions/information/)

In this project, the following requests are used:

1. get current Service Interruption Report (BIS) file number:\
   URL: [https://pannes.hydroquebec.com/pannes/donnees/v3_0/bisversion.json](https://pannes.hydroquebec.com/pannes/donnees/v3_0/bisversion.json)\
   returns: string formatted "yyyyMMddHHmmss" (length: 16, including 2 quotation marks)
   
3. get list of power outages for specific BIS file number:\
   URL: [https://pannes.hydroquebec.com/pannes/donnees/v3_0/bismarkers{BIS_VERSION}.json](https://pannes.hydroquebec.com/pannes/donnees/v3_0/bismarkers{BIS_VERSION}.json)\
   returns: list of power outages in JSON format. Each list entry consists of multiple properties, such as: numbe of customers affected, outage start date and time, coordinates of the outage area, amont others.
   
