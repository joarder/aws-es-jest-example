# AWS Elasticsearch Service JEST Example 

This project aims to provide a working example of using [JEST](https://github.com/searchbox-io/Jest/tree/master/jest) (Java HTTP REST client for [Elasticsearch](https://www.elastic.co/) package to programmatically interact with the [AWS Elasticsearch Service](https://aws.amazon.com/elasticsearch-service/) domains. The primary contribution in this project is to deliver a new AWSSignerInterceptor class that only uses AWS SDK and Apache HTTP client functionality to sign the HTTP requests directed to the AWS Elasticsearch domains.   

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need to install [Gradle](https://gradle.org/) build system in your local machine to compile this project from the source. If you are using Mac OSX then you can install Gradle using [Brew](https://brew.sh/).

```
% brew install gradle
```

For installation in other OS environment, please refer to the [installation notes](https://gradle.org/install/) in the Gradle Web site. 

### Installation and Usage

Once you download the source files into your local machine, you can simply run the below command to compile and build the JAR file.

```
% gradle clean uber
```

The packaged JAR file (named as ```aws-es-jest-example-latest.jar```) can be found navigating to the ```/build/libs/``` director. 

You can run the JAR file with ```--help or -h``` option to view the usage options for this package.

```
% java -jar aws-es-jest-example-latest.jar --help
```

This will give the following output:

```
Usage: <main class> [options]
  Options:
  * --aws-es-endpoint, -endpoint
      AWS Elasticsearch Service domain endpoint: <URL>
    --aws-region, -region
      AWS region: <String>
      Default: us-east-1
    --debug, -d
      Enable debug logs: [true, false]
      Default: false
    --help, -h
      Help
    --index-name, -index
      Elasticsearch index name: <String>
      Default: diary
    --index-type-name, -type
      Elasticsearch index type name: <String>
      Default: notes
```

Below is an example run and the corresponding output:

```
% java -jar aws-es-jest-example-latest.jar --aws-es-endpoint https://<domain-name>.us-east-1.es.amazonaws.com
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
2018-08-14 15:14:21 INFO  AWSESJestExample:94 - Creating index "diary" ...
2018-08-14 15:14:23 INFO  AWSESJestExample:117 - Inserting a single document ...
Note [id=null, note=Note1: do u see this - 1534223663439, createdOn=Tue Aug 14 15:14:23 AEST 2018, userName=User1]
2018-08-14 15:14:24 INFO  AWSESJestExample:137 - Inserting a single document asynchronously ...
Note [id=null, note=Note2: do u see this - 1534223664724, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User2]
2018-08-14 15:14:24 INFO  AWSESJestExample:167 - Inserting two documents using bulk API ...
Note [id=null, note=Note3: do u see this - 1534223664735, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User3]
Note [id=null, note=Note4: do u see this - 1534223664735, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User4]
2018-08-14 15:14:27 INFO  AWSESJestExample:190 - Querying index "diary" for 'note' and 'see' ...
2018-08-14 15:14:27 INFO  AWSESJestExample:191 - {"query":{"term":{"note":{"value":"see","boost":1.0}}}}
Note [id=AWU220pW7NKGiym564Ch, note=Note2: do u see this - 1534223664724, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User2]
Note [id=AWU220cCt0hvvkvNoe_4, note=Note3: do u see this - 1534223664735, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User3]
Note [id=AWU220Ib7NKGiym564Cg, note=Note1: do u see this - 1534223663439, createdOn=Tue Aug 14 15:14:23 AEST 2018, userName=User1]
Note [id=AWU220cCt0hvvkvNoe_5, note=Note4: do u see this - 1534223664735, createdOn=Tue Aug 14 15:14:24 AEST 2018, userName=User4]
2018-08-14 15:14:27 INFO  AWSESJestExample:214 - Deleting index "diary" ...
2018-08-14 15:14:28 INFO  AWSESJestExample:88 - AWS ES JEST example testing is now completed.
```

Note that, this package has been tested with AWS ES domains running with Elasticsearch version 5.5 and 6.2 which were launched within the span of past several months of 2018.

Additionally, the package by default chose the AWS region as ```us-east-1``` US North Virginia. If your target AWS ES domain is located in a different AWS region then please use the command line option ```-region``` to explicitly overwrite the default one.  

## Deployment

Build and deploy as a JAR package

## Built With

* [Gradle](https://gradle.org/) - Build tool

## Contributing

Please read [CONTRIBUTING.md](https://github.com/joarder/aws-es-jest-example/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/joarder/aws-es-jest-example/tags). 

## Authors

* **Joarder Kamal** - [GitHub](https://github.com/joarder)

See also the list of [contributors](https://github.com/joarder/aws-es-jest-example/contributors) who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](https://github.com/joarder/aws-es-jest-example/LICENSE.md) file for details