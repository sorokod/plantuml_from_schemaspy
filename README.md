A utility for rendering the XML output of Schemaspy.  

## The Schemaspy part
Since we are not interested in HTML output the `-nohtml` parameter should be used. An invocation may look like this:

`java -jar schemaspy-6.1.0.jar -configFile ./schemaspy.config`

where `schemaspy.config` for Postgres may be

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
