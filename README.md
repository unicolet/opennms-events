# opennms-events
Analyze opennms events with Kibana

![Kibana showing graphs of OpenNMS events](https://pbs.twimg.com/media/B-3V2MDUMAEMN0s.jpg:large "Kibana showing OpenNMS events")


Installation (from source)
--------------------------

This is a camel route so it needs Java, Maven 3, Elasticsearch and a database user with access to the postgres db where opennms stores its data.

1. clone this repository, then cd into it
2. create the views and support tables using the script in the sql folder. It is recommended to create a dedicate postgres user for this process
3. start elasticsearch on the same node where you will run this process. Configure the elasticsearch cluster name to be '**opennms**'
4. deploy kibana (optional can be done later)
5. start the process by running on a shell:

```
     mvn camel:run -Dpostgres.host=10.12.34.56 -Dpostgres.user=opennmsevents -Dpostgres.password=secretpassword
```

wait for the process to load ALL of your events into elasticsearch. This can take a lot of time/cpu/disk depending on
the number of events. If you want to load only recent events tweak the view created at step 1 by adding a filter on *eventtime*

When you have enough events loaded, point your browser to kibana and start creating your own dashboards!

An init.d script (named camel-opennms) for starting opennms-events as a system service is available in the init.d directory.
The script assumes that you will run the service as *appuser:apache*, edit line
34 to change user/group.

Installation (RPM)
------------------

A RPM (tested on Centos 6, should work on 7, not on 5) is available at:

     https://packagecloud.io/unicoletti/opennms

JDK 7 or higher is required, make sure it is installed, because the rpm does not require it.

Install the latest opennms-events rpm and elasticsearch. A tipical one-node elasticsearch configuration would be the following:

     cluster.name: opennms
     network.host: 127.0.0.1
     discovery.zen.ping.multicast.enabled: false
     discovery.zen.ping.unicast.hosts: ["127.0.0.1"]

Remember that opennms-events and elastisearch shoud be running on the same node as they will commmunicate over 127.0.0.1!

Configure opennms-events by editing the */etc/sysconfig/opennms-events* file. Set database host, user and password
according to your setup. If you are running opennms-events on the same host as opennms *and* opennms uses the default database config leave the defaults.

Create the views and support table in the opennms database using the /opt/opennms-events/opennms_events.sql script.
For example, copy the sql script over to the database host and run as postgres user:

    psql opennms < opennms_events.sql

Start elastiscsearch and opennms-events. Review the logs in /var/log/opennms-events.{log,err}.
If all goes well after a while (~20s) elasticsearch should start ingesting data and the */var/lib/elasticsearch* directory
should start growing in size. 

Install Kibana and start browsing your events! 

