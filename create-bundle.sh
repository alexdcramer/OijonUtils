echo What is the version of Oijon Utils to bundle?
read version
echo Bundling $version...
cd target
jar -cvf utils-$version-bundle.jar utils-$version.pom utils-$version.pom.asc utils-$version.jar utils-$version.jar.asc utils-$version-javadoc.jar utils-$version-javadoc.jar.asc utils-$version-sources.jar utils-$version-sources.jar.asc
