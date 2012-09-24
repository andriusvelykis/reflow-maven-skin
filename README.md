# Reflow Maven skin

Reflow is an Apache Maven site skin built on Twitter Bootstrap. It allows various structural
and stylistic customizations to create a modern-looking Maven-generated website.



## Usage

To use this Maven skin, include it in your `site.xml` file:

```xml
<project>
  ...
  <skin>
    <skin>
      <groupId>lt.velykis.maven.skins</groupId>
      <artifactId>reflow-maven-skin</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </skin>
  </skin>
  ...
</project>
```

Note that the skin has not been released yet, so you need to use the `-SNAPSHOT` version.
Add the snapshot repository to your POM file until the skin is released (both as `repository`
and `pluginRepository`):

```xml
<repositories>
  <repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>
<pluginRepositories>
  <pluginRepository>
    <id>snapshots-plugin-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </pluginRepository>
</pluginRepositories>
```

The skin requires custom Velocity tools (`reflow-velocity-tools`) to be available when
generating Maven site. The tools are available from the snapshots repository and need to be
included as dependency to `maven-site-plugin` in your POM file:

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.1</version>
      <dependencies>
        ...
        <dependency>
          <groupId>lt.velykis.maven.skins</groupId>
          <artifactId>reflow-velocity-tools</artifactId>
          <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
          <groupId>org.apache.velocity</groupId>
          <artifactId>velocity</artifactId>
          <version>1.7</version>
        </dependency>
        ...
      </dependencies>
      ...
    </plugin>
    ...
  </plugins>
</build>
```

Note that _Velocity 1.7_ is also required by the template.

The skin is provided on the "works on my computer" basis at the moment. I am using the newest
versions of `maven-site-plugin` and other components and at the moment do not have any feedback
on using the skin with Maven 2 site or other configurations.


### Configuration

The skin is configurable using the `<custom>` element in your `site.xml` file. Documentation
on the configuration options is in the works. For now, here is a sample configuration file:

```xml
<project>
  ...
  <custom>
    <reflowSkin>
      <theme>bootswatch-spacelab</theme>
      <brand>
        <name>My Project</name>
        <href>http://github.com/andriusvelykis/reflow-maven-skin</href>
      </brand>
      <slogan>Super interesting project doing good things.</slogan>
      <titleTemplate>%2$s | %1$s</titleTemplate>
      <toc>top</toc>
      <topNav>Download|reports</topNav>
      <bottomNav>
        <column>Main|Download</column>
        <column>Documentation</column>
        <column>reports|modules</column>
      </bottomNav>
      <bottomDescription>This is a very good project doing interesting
        and valuable things.</bottomDescription>
      <pages>
        <index project="project-id">
          <shortTitle>Welcome</shortTitle>
          <breadcrumbs>false</breadcrumbs>
          <toc>false</toc>
          <sections>
            <carousel />
            <body />
            <sidebar />
            <thumbs>2</thumbs>
            <columns>3</columns>
          </sections>
        </index>
        <developer-info>
          <toc>sidebar</toc>
        </developer-info>
      </pages>
    </reflowSkin>
  </custom>
  ...
</project>
```



## Bug tracker

Have a bug? Please create an issue here on GitHub that conforms with [necolas's guidelines](https://github.com/necolas/issue-guidelines).

http://github.com/andriusvelykis/reflow-maven-skin/issues



## Author

**Andrius Velykis**

+ http://andrius.velykis.lt
+ http://github.com/andriusvelykis



## Copyright and license

Copyright 2012 Andrius Velykis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
