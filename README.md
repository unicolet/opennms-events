# opennms-events
Analyze opennms events with Kibana

Installation
------------

This is a camel route so it needs Java, Maven 3, Elasticsearch and a database user with access to the postgres db where opennms stores its data.

1. create the views and support tables using the script in the sql folder. It is recommended to create a dedicate postgres user for this process
2. start elasticsearch on the same node where you will run this process. Configure the elasticsearch cluster name to be 'opennms'
3. deploy kibana 
4. start the process by running on a shell:

```
     mvn camel:run -Dpostgres.host=10.12.34.56 -Dpostgres.user=opennmsevents -Dpostgres.password=secretpassword
```

wait for the process to load ALL of your events into elasticsearch. This can take a lot of time/cpu/disk depending on
the number of events. If you want to load only recent events tweak the view created at step 1 by adding a filter on *eventtime*

When you have enough events loaded, point your browser to kibana and start creating your own dashboards!

An init.d script for starting opennms-events as a system service is available in the init.d directory.
The script assumes that you will run the service as *appuser:apache*, edit line
34 to change user/group.

