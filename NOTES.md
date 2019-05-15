# Releasing
Releases of this project are provided via Maven Central Repository.

Perform snapshot release:

    mvn clean deploy -Possrh
    
Perform release:

    mvn release:clean release:prepare -Possrh
    mvn release:perform -Possrh

Detailed instructions on how to set up release infrastructure:
- http://central.sonatype.org/pages/ossrh-guide.html
- http://central.sonatype.org/pages/apache-maven.html

The files are hosted at:
- releases: http://central.maven.org/maven2/at/ac/ait/ariadne-json-route-format/
- snapshots: https://oss.sonatype.org/content/repositories/snapshots/at/ac/ait/ariadne-json-route-format/

# Possible future features & improvements
- meta / hierarchical instructions (e.g. You are now entering/leaving the park/district/..)
- better differentiation between car/goods vehicle/hgv/.. required?
- public transport station details: differentiate between station entries & platforms, add travel time to transfer segments e.g. 5 min walk, 1 min escalators down, 2 min walk, 1 min elevator up;
- v4 schema generation
