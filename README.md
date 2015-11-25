# pod-ITBA

It's a Practical Work for the Distributed Objects Programming 2015 Course at ITBA.

The purpose of this work was to simulate a Distributed System using **Hazelcast**.

## How to run it

1. Modify the interfaces tag from the file *hazelcast.xml* and put the interface where you want to run the distributed system.
2. Run the script *server.sh* with the command `sh server.sh` on all the machines you want, simulating to be all the different nodes of your distributed system.
3. Modify *client.sh* to run the query you want.
4. Run *client.sh* with the command `sh client.sh`.

### Different Queries
#### Query 1
```
java -Dquery=1 -DN=5 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
```

#### Query 2
```
java -Dquery=2 -Dtope=1994 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
```

#### Query 3
```
java -Dquery=3 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
```
#### Query 4
```
java -Dquery=4 -Dpath=./jsons/imdb-500.json edu.itba.pod.hazel.tp.Main
```

