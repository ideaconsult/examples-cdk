examples-maven
==============

Simple Maven Project example with The Chemistry Development Kit dependency. Reads SDF file, generates SMILES, adds SMILES as properties and writes as SDF file.

Make:
--------------

    mvn package

Jars are created in the target folder.

* Executable jar (with dependencies)
        target/example-cdk-maven1-0.0.1-SNAPSHOT.jar
* jar (without dependencies)
        target/example-cdk-maven1.jar
* Sources 
        target/example-cdk-maven1-sources.jar
* Test sources 
        target/example-cdk-maven1-test-sources.jar
* Test classes 
        target/example-cdk-maven1-tests.jar

   

Run 
--------------

    java -jar target/example-cdk-maven1-0.0.1-SNAPSHOT.jar -f file.sdf

Create web site:
--------------

    mvn site:site 

Generated HTML files are in the target/site folder