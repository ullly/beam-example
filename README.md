# beam-example
An example of a Beam pipeline that includes a call to a Google Cloud Functions web service.

## Installation 

* Set up your development environment for Beam. See how [here](https://beam.apache.org/get-started/quickstart-java/#set-up-your-development-environment).
* Ensure `jq` is installed. See details [here](https://stedolan.github.io/jq/download/).
* Ensure ['Before You Begin'](https://cloud.google.com/functions/docs/quickstart#before-you-begin) is completed
if Option 2 (see next section) is used. 
* Clone the repo.

Note: JDK 17.0.2 was used.

## Usage

Option 1 uses the Cloud Functions Framework to run the web service locally. 

Option 2 deploys to Cloud Functions. 

### Option 1

Pre-process step: `source src/main/scripts/pre-process.sh`

Cloud Functions step: `mvn com.google.cloud.functions:function-maven-plugin:0.10.0:run -Drun.functionTarget=web.WebService`

Pipeline step: `mvn compile exec:java -Dexec.mainClass=Example -Dexec.args="http://localhost:8080/"`

### Option 2 

Pre-process step: `source src/main/scripts/pre-process.sh`

Cloud Functions step: 

```
gcloud functions deploy web.WebService --region europe-west1 --entry-point web.WebService --ignore-file .gcloudignorefunctions --runtime java11 --trigger-http --allow-unauthenticated
```

Pipeline step: `mvn compile exec:java -Dexec.mainClass=Example -Dexec.args="<Trigger-URL>/"` where `<Trigger-URL>` 
is the endpoint of the deployed cloud function.

## Credits

Exercise by Stephan K.

## License

Licensed under the MIT License. 
