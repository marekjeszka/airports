 [![Build Status](https://travis-ci.org/marekjeszka/airports.svg?branch=master)](https://travis-ci.org/marekjeszka/airports.svg?branch=master)
 [![Coverage Status](https://coveralls.io/repos/github/marekjeszka/airports/badge.svg?branch=master)](https://coveralls.io/github/marekjeszka/airports?branch=master) 

# airports (forked from vijaykiran/random-repo)

This is back-end of web application that provides some insight for airport data.

Available endpoints:
- `/` - index page (quite empty at the moment)
- `/countries/:name` - country with it's airports and runways (for given parameter)
- `/reports/topCountries` - top 10 countries (with highest and lowest number of airports)
- `/reports/runwaysPerCountry` - runways per country
- `/reports/topIdentifications` - top 10 most common identifications

# Resources

Airports data from http://ourairports.com/data/ (released under public domain)
