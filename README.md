The semver groovy utility
=========================

semver is a little tool to manipulate the version bumping in a project that follows the [semver 2.x][semver] specification.

Its use are:
  - bump version
  - extract specific version part

A  version must match the following regular expression:
```
^[vV]?(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)(\-(0|[1-9][0-9]*|[0-9]*[A-Za-z-][0-9A-Za-z-]*)(\.(0|[1-9][0-9]*|[0-9]*[A-Za-z-][0-9A-Za-z-]*))*)?(\+[0-9A-Za-z-]+(\.[0-9A-Za-z-]+)*)?$
```

[![Build Status](https://travis-ci.org/Wonno/groovy-semver-tool.svg?branch=master)](https://travis-ci.org/Wonno/groovy-semver-tool)
[![License](https://img.shields.io/badge/license-GPL--3.0-blue.svg?style=flat)](https://travis-ci.org/Wonno/groovy-semver-tool/blob/master/LICENSE)


In English:
- The version must match _X.Y.Z[-PRERELEASE][+BUILD]_ where _X_, _Y_ and _Z_ are non-negative integers.
- _PRERELEASE_ is a dot separated sequence of non-negative integers and/or identifiers composed of alphanumeric 
  characters and hyphens (with at least one non-digit). Numeric identifiers must not have leading zeros. A hyphen 
  (\"-\") introduces this optional part.
- _BUILD_ is a dot separated sequence of identifiers composed of alphanumeric characters and hyphens. A plus ("+") 
  introduces this optional part.

## Build
```
mvn clean install
```
     
## Examples
```$groovy
import com.github.wonno.semver.Semver

//version validation
assert !Semver.validate("1.2.invalid")

//version change
assert Semver.parse("1.2.3+abcd").prerel("rc1").minor().text()=="1.3.0"

//version comparison
assert Semver.parse("1.0.7+acf430") < new Semver("1.0.6").patch()
```

## Links
* [Semver Specification 2.0](https://semver.org/spec/v2.0.0.html) 
* Inspired by [semver-tool](https://github.com/fsaintjacques/semver-tool/) written in bash.

## Credits
*  [semver-tool](https://github.com/fsaintjacques/semver-tool/) project for the regex and the testcases
