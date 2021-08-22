A utility for rendering the XML output of [Schemaspy](https://github.com/schemaspy/schemaspy) into a PlantUML diagram.


## Goal
In general, schema diagrams are low fidelity representations of the actual schema. The goal of this utility is 
to generate a diagram that provides the user with a **general** idea of what the actual schema is. Ultimately, the fine 
details are in that `.sql` file.


## The Schemaspy part

To generate the schema XML file, the Schemaspy jar is required. Version used here is `6.1.0` . It is available from
[GitHub](https://github.com/schemaspy/schemaspy/releases/download/v6.1.0/schemaspy-6.1.0.jar) 

Since we are not interested in HTML output the `-nohtml` parameter should be used. An invocation may look like this:

`java -jar schemaspy-6.1.0.jar -configFile ./schemaspy.config`

where `schemaspy.config` for Postgres may be:

```properties
schemaspy.nohtml
schemaspy.t=pgsql
schemaspy.dp=<HOME_DIR>/.m2/repository/org/postgresql/postgresql/42.2.16/postgresql-42.2.16.jar
schemaspy.host=localhost
schemaspy.port=5432
schemaspy.db=<DB_NAME>
schemaspy.u=<DB_USER>
schemaspy.p=<DB_PASSWORD>
schemaspy.o=<OUTPUT_DIR>/schemaspy_out
schemaspy.schemas=public
schemaspy.I=<STUFF_TO_IGNORE>
```


### License

Copyright 2021 David Soroko.

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)