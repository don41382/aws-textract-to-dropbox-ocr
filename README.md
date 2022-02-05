# AWS Textract To Dropbox Ocr PDF Generator

As the name suggests, this CLI tool lets you create a searchable PDF from a given folder of jpeg images and uploads the generated PDF to Dropbox.

The following parts were use:

* Dropbox
* Amazon Textract

## Pre-Requirements

* existing aws profile (`~/.aws/config` + `~/.aws/credentials`)
* dropbox auth token

## Build

The project is build with maven and creates a fat-jar on:

```shell
  mvn package
```

## How to run

After you build the fat jar, you can run it with:

```shell
  java -jar targets/aws-textract-to-dropbox-ocr-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --input-folder /folder/images-jpg \
    --output "/DropboxFolder/pi2.pdf" \
    --dropbox-auth-token "<your-token>"
```