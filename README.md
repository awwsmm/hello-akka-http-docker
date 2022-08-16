# hello-akka-http-docker

Minimal project showing how to build and run a Dockerized `akka-http` server.

Note: in the examples below, the ports `9999` and `8888` can be replaced with any appropriate port, but the hostnames (`localhost` when running locally, `0.0.0.0` when running within a Docker container) should not be changed.

## build and run using `sbt`

### build

Run the `assembly` command in the `sbt` shell

`sbt> assembly`

This uses the `sbt-assembly` plugin added to `project/target/plugins.sbt`.

The artifact produced by this command will be found at

`target/scala-2.13/out.jar`

The name (`"out.jar"`) is set in the `build.sbt` file.

### run

You must provide a `host` (aka. `interface`) and `port` to run the server.

From within the `sbt` shell, run

`sbt> run localhost 9999`

You can then `curl` the server from a terminal

```sh
$ curl localhost:9999/hello
<h1>Bonjour!</h1>
```

You can also run the `jar` outside the `sbt` shell with

`$ java -jar target/scala-2.13/out.jar localhost 9999`

## build and run using `docker`

### build

Build and tag the Docker image with

`$ docker build -t akka-http-docker:my-tag` .

### run

Then, run with

`$ docker run -d -p 9999:8888 akka-http-docker:my-tag 0.0.0.0 8888`

Communicate with the container using the same command (and host, `localhost`) as above

```sh
$ curl localhost:9999/hello
<h1>Bonjour!</h1>
```

Note that we `docker run` using `0.0.0.0`, but we `curl` using `localhost`