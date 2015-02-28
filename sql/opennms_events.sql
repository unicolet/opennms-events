CREATE OR REPLACE FUNCTION commacat(text, text) RETURNS text AS '
    SELECT CASE WHEN $1 IS NULL OR $1 = '''' THEN $2
            WHEN $2 IS NULL OR $2 = '''' THEN $1
            ELSE $1 || '','' || $2
            END; 
'
 LANGUAGE SQL;

drop aggregate IF EXISTS textcat_all(text);

CREATE AGGREGATE textcat_all(
  basetype    = text,
  sfunc       = commacat,
  stype       = text,
  initcond    = ''
);

create view v_node_categories as
select nodeid, textcat_all(categoryname) as categories
from
categories c,
category_node cn
where cn.categoryid=c.categoryid
group by nodeid;

create view v_es_messages as
select
e.eventid as "id",
e.eventuei as "eventuei",
e.eventtime as "@timestamp",
date_part('dow', e.eventtime) as "dow",
date_part('hour', e.eventtime) as "hour",
e.eventsource as "eventsource",
e.ipaddr as "ipaddr",
s.servicename as "servicename",
e.eventparms as "eventparms",
e.eventdescr as "eventdescr",
e.eventlogmsg as "eventlogmsg",
e.eventseverity as "eventseverity",
CASE WHEN e.eventseverity  = 2 THEN 'cleared' WHEN e.eventseverity  = 3 THEN 'normal' WHEN e.eventseverity  = 4 THEN 'warning' WHEN e.eventseverity  = 5 THEN 'minor' WHEN e.eventseverity  = 6 THEN 'major' WHEN e.eventseverity  = 7 THEN 'critical' ELSE 'indeterminate' END
as "eventseverity_text",
n.nodeid as "nodei",
n.nodesysname as "nodesysname",
n.nodesyslocation as "nodesyslocation",
n.nodelabel as "nodelabel",
n.foreignsource as "foreignsource",
cn.categories as "categories"
from
	events e,
	node n left outer join v_node_categories cn on n.nodeid=cn.nodeid,
	service s
where
e.nodeid=n.nodeid
and s.serviceid=e.serviceid
;

create table es_messages_processed (id int4);

create index idx_esmsg_proc_id on es_messages_processed(id);
