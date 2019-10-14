Summary of task
----------------------------------------
Our custom-build server logs different events to a file named logfile.txt. Every event has 2 entries in the file - one
entry when the event was started and another when the event was finished. The entries in the file have no specific
order (a finish event could occur before a start event for a given id)

Every line in the file is a JSON object containing the following event data:

• id - the unique event identifier
• state - whether the event was started or finished (can have values "STARTED" or "FINISHED"
• timestamp - the timestamp of the event in milliseconds
Application Server logs also have the following additional attributes:

• type - type of log\
• host - hostname\

Example contents of logfile.txt:

{"id":"scsmbstgra", "state":"STARTED", "type":"APPLICATION_LOG", "host":"12345", "timestamp":1491377495212}
{"id":"scsmbstgrb", "state":"STARTED", "timestamp":1491377495213}
{"id":"scsmbstgrc", "state":"FINISHED", "timestamp":1491377495218}
{"id":"scsmbstgra", "state":"FINISHED", "type":"APPLICATION_LOG", "host":"12345", "timestamp":1491377495217
}
{"id":"scsmbstgrc", "state":"STARTED", "timestamp":1491377495210}
{"id":"scsmbstgrb", "state":"FINISHED", "timestamp":1491377495216}
...

In the example above, the event scsmbstgrb duration is 1491377495216 - 1491377495213 = 3ms
The longest event is scsmbstgrc (1491377495218 - 1491377495210 = 8ms)

Steps for Running:

1. perform "mvn clean install".
2. After creating jar run
             "java -jar log-event-generator.jar ${program arguments for log files}".
             eg: "java -jar eventgenerator-0.0.1-SNAPSHOT.jar F:\\logback.txt"
3. After successful start on localhost open the browser or rest services client and\
          open "localhost:8080/api/events" to get list of all logs with event duration.\
          open "localhost:8080/api/events/largerevents" for get largest events in the above input logs.



