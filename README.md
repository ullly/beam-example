# beam-example
An example of a Beam pipeline that includes a call to a Google Cloud Functions web service.

## Installation 

* Set up your development environment for Beam. See how [here](https://beam.apache.org/get-started/quickstart-java/#set-up-your-development-environment).
* Ensure `jq` is installed. See details [here](https://stedolan.github.io/jq/download/).
* Clone the repo.

Note: JDK 17.0.2 was used. 

## Usage

Pre-process step: `source src/main/scripts/pre-process.sh`

Pipeline step: `mvn compile exec:java -Dexec.mainClass=Example`

## Credits

Exercise by Stephan K.

## License

Licensed under the MIT License. 
