echo What is the version of Oijon Utils to bundle?
read version
echo Bundling $version...
jar -cvf ./target/utils-$version-bundle.jar ./target/utils-$version.pom ./target/utils-$version.pom.asc ./target/utils-$version.jar ./target/utils-$version.jar.asc ./target/utils-$version-javadoc.jar ./target/utils-$version-javadoc.jar.asc ./target/utils-$version-sources.jar ./target/utils-$version-sources.jar.asc
