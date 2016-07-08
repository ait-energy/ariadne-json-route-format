#!/bin/bash
set -x
mvn clean source:jar javadoc:jar package install deploy
