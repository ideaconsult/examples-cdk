examples-maven
==============

Simple Maven Project example with The Chemistry Development Kit dependency. Reads SDF file, generates SMILES, adds SMILES as properties and writes as SDF file.

Compile:
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

Compile with specific CDK version
--------------

    mvn package -P cdk-1.3.8

This command uses Maven profile -P cdk-1.3.8. See below more information on maven profiles. 
The resulting executable jar will include several CDK 1.4.5 jars. To switch the version, just use a different profile.     

Run 
--------------

    java -jar target/example-cdk-maven1-0.0.1-SNAPSHOT.jar -f file.sdf
    


Maven profiles 
--------------

    Maven profiles are essentially set of properties, allowing to customize the build. More information [here](http://maven.apache.org/guides/introduction/introduction-to-profiles.html).  

    This project has defined several maven profiles, allowing to switch between different CDK versions. 
	
* Listing all profiles. 
	
    maven-single-module>mvn help:all-profiles
    [INFO] Scanning for projects...
    [INFO] Searching repository for plugin with prefix: 'help'.
    [INFO] ------------------------------------------------------------------------
    [INFO] Building maven-single-module
    [INFO]    task-segment: [help:all-profiles] (aggregator-style)
    [INFO] ------------------------------------------------------------------------
    [INFO] [help:all-profiles {execution: default-cli}]
    [INFO] Listing Profiles for Project: net.idea.examples.cdk:maven-single-module:jar:0.0.1-SNAPSHOT
      Profile Id: cdk-1.4.10 (Active: true , Source: pom)
      Profile Id: cdk-1.4.9 (Active: false , Source: pom)
      Profile Id: cdk-1.4.8 (Active: false , Source: pom)
      Profile Id: cdk-1.4.5 (Active: false , Source: pom)
      Profile Id: cdk-1.3.8 (Active: false , Source: pom)
      Profile Id: cdk-1.2.3 (Active: false , Source: pom)
      Profile Id: cdk-1.2.0 (Active: false , Source: pom)
      Profile Id: cdk-1.1.6 (Active: false , Source: pom)
      Profile Id: cdk-1.1.5 (Active: false , Source: pom)
      Profile Id: development (Active: false , Source: pom)
      Profile Id: production (Active: false , Source: pom)
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESSFUL
    [INFO] ------------------------------------------------------------------------
	    
* Displaying the active profile. The default active profile is -P cdk-1.4.10 
	    
    maven-single-module>mvn help:active-profiles
    [INFO] Scanning for projects...
    [INFO] Searching repository for plugin with prefix: 'help'.
    [INFO] ------------------------------------------------------------------------
    [INFO] Building maven-single-module
    [INFO]    task-segment: [help:active-profiles] (aggregator-style)
    [INFO] ------------------------------------------------------------------------
    [INFO] [help:active-profiles {execution: default-cli}]
    [INFO]
    Active Profiles for Project 'net.idea.examples.cdk:maven-single-module:jar:0.0.1-SNAPSHOT':
    
    The following profiles are active:
    
     - cdk-1.4.10 (source: pom)	    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESSFUL
    [INFO] ------------------------------------------------------------------------	 
	
* Setting the active profile by -P option. Using -P cdk-1.4.5 will force the Maven build to use the CDK 1.4.5 version
	    
    maven-single-module>mvn help:active-profiles -P cdk-1.4.5
    [INFO] Scanning for projects...
    [INFO] Searching repository for plugin with prefix: 'help'.
    [INFO] ------------------------------------------------------------------------
    [INFO] Building maven-single-module
    [INFO]    task-segment: [help:active-profiles] (aggregator-style)
    [INFO] ------------------------------------------------------------------------
    [INFO] [help:active-profiles {execution: default-cli}]
    [INFO]
    Active Profiles for Project 'net.idea.examples.cdk:maven-single-module:jar:0.0.1-SNAPSHOT':
    
    The following profiles are active:
    
	- cdk-1.4.5 (source: pom)
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESSFUL
    [INFO] ------------------------------------------------------------------------
    
* Combining profiles 
	    
    maven-single-module>mvn help:active-profiles -P cdk-1.1.5 -P production
    [INFO] Scanning for projects...
    [INFO] Searching repository for plugin with prefix: 'help'.
    [INFO] ------------------------------------------------------------------------
    [INFO] Building maven-single-module
    [INFO]    task-segment: [help:active-profiles] (aggregator-style)
    [INFO] ------------------------------------------------------------------------
    [INFO] [help:active-profiles {execution: default-cli}]
    [INFO]
    Active Profiles for Project 'net.idea.examples.cdk:maven-single-module:jar:0.0.1-SNAPSHOT':
    
    The following profiles are active:
    
	 - production (source: pom)
     - cdk-1.1.5 (source: pom)
    
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESSFUL
    [INFO] ------------------------------------------------------------------------
	
Run tests
--------------
	
To run tests use

    mvn test 

Normally, this command launches junit tests, defined in the tets folder. However, for convenience and quick test, this example project 
was configured to launch the main method of (MainApp.java) with mvn test command. The main reason is to be able to switch profiles quickly.

    maven-single-module>mvn test
    [INFO] Scanning for projects...
    [INFO] ------------------------------------------------------------------------
    [INFO] Building maven-single-module
    [INFO]    task-segment: [test]
    [INFO] ------------------------------------------------------------------------
    [INFO] [resources:resources {execution: default-resources}]
    [INFO] Using 'UTF-8' encoding to copy filtered resources.
    ....
    [INFO] Preparing exec:java
    [WARNING] Removing: java from forked lifecycle, to prevent recursive invocation.
    [INFO] No goals needed for project - skipping
    [INFO] [exec:java {execution: default}]

	Simple example of a Maven project
	usage: net.idea.examples.cdk.maven_single_module.MainApp
	 -f,--file <file>   Input file
	 -h,--help          Simple example of a Maven project
	Examples:
	Read file :     java -jar examples-maven-0.0.1-SNAPSHOT.jar     -f filename
	
Create web site:
--------------

    mvn site:site 

Generated HTML files are in the target/site folder