# URL Collector

The URL collector is an application that crawls the [Common Crawl](https://commoncrawl.org/the-data/get-started/) corpus
for urls with the specified file types.

## Architecture

The application suite is a distributed system to increase the processing speed. It can be deployed to one machine, but
if the crawling speed should be increased, the slave application can be deployed to multiple machines.

The url-collector suite is built from three applications.

### URL Collector Master

Knows what WARC files (work units) should be crawled, store this data in a database and distribute the work to the slave
applications.

### URL Collector Slave

Gets a work unit (effectively a Common Crawl WARC file) from the Master application, that should be parsed for URLs.
Parse the urls then upload the results to a warehouse.

### URL Collector Merger

Merge the crawl results in a warehouse into one big file, while doing so, removing the duplicate entries in the files.

## Installation

To start crawling, you need to have Java and MongoDB installed on your machine.

### Installing Java

First, you need to download the Java 17 Runtime Environment. It's
available [here](https://www.oracle.com/technetwork/java/javase/downloads/index.html). After the download is complete
you should run the installer and follow the directions it provides until the installation is complete.

Once it's done, if you open a command line (write cmd to the Start menu's search bar) you will be able to use the java
command. Try to write `java -version`. You should get something similar:

.... java version "15" 2020-09-15 Java(TM) SE Runtime Environment (build 15+36-1562)
Java HotSpot(TM) 64-Bit Server VM (build 15+36-1562, mixed mode, sharing)
....

### Installing MongoDB

Download MongoDB 5.0 from https://www.mongodb.com/download-center/community[here]. After the download is complete run
the installer and follow the directions it provides. If it's possible, install the MongoDB Compass tool as well because
you might need it later for administrative tasks.

### Running the Master Application

You can download the Master Application from
our [release page](https://github.com/bottomless-archive-project/url-collector/releases). Please take care to choose a
non "pre-release" version!

After the download is complete run the application via the following command:

```bash
java -jar url-collector-master-application-{release-number}.jar ...
```

In the place of the ... you should write the various parameters with two dashes in the beginning and an equal sign
between the parameter's name and it's value. For example:

```bash
java -jar url-collector-master-application-{release-number}.jar --example.parameter=value
```

#### Parameters

| Name          | Description |
| ------------- | ----------- |
| database.host | Title       |
| database.port | Text        |
| database.uri  | Text        |

### Running the Slave Application

You can download the Slave Application from
our [release page](https://github.com/bottomless-archive-project/url-collector/releases). Please take care to choose a
non "pre-release" version!

After the download is complete run the application via the following command:

```bash
java -jar url-collector-slave-application-{release-number}.jar ...
```

You can start the slave application on more than one machines if necessary, to improve the crawling speed.

In the place of the ... you should write the various parameters with two dashes in the beginning and an equal sign
between the parameter's name and it's value. For example:

```bash
java -jar url-collector-slave-application-{release-number}.jar --example.parameter=value
```

#### Parameters

| Name                         | Description |
| ---------------------------- | ----------- |
| execution.parallelism-target | Title       |
| warehouse.aws.region         | Text        |
| warehouse.aws.bucket-name    | Text        |
| warehouse.aws.access-key     | Text        |
| warehouse.aws.secret-key     | Text        |
| validation.types             | Text        |
| master.host                  | Text        |
| master.port                  | Text        |

### Starting a crawl

To start a crawl, the Master application's crawl initialization endpoint should be called. The request should be a POST
request to the /crawl endpoint on the master with a body the contains the crawlId for the Common Crawl dataset that
should be processed.

For example:

```bash
curl --location --request POST 'http://185.191.228.214:8081/crawl' \
--header 'Content-Type: application/json' \
--data-raw '{
"crawlId": "CC-MAIN-2021-31"
}'
```

Once it is done, the Slave application should automatically pick up the new work units in a matter of minutes.

### Starting the Merger Application

You can download the Merger Application from
our [release page](https://github.com/bottomless-archive-project/url-collector/releases). Please take care to choose a
non "pre-release" version!

After the download is complete run the application via the following command:

```bash
java -jar url-collector-merger-application-{release-number}.jar ...
```

In the place of the ... you should write the various parameters with two dashes in the beginning and an equal sign
between the parameter's name and it's value. For example:

```bash
java -jar url-collector-merger-application-{release-number}.jar --example.parameter=value
```

#### Parameters

| Name            | Description |
| --------------- | ----------- |
| database.host   | Title       |
| database.port   | Text        |
| database.uri    | Text        |
| aws.region      | Text        |
| aws.bucket-name | Text        |
| aws.access-key  | Text        |
| aws.secret-key  | Text        |
| result.path     | Text        |
