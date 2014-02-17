# [Reflow Maven skin]( http://andriusvelykis.github.com/reflow-maven-skin/ )

Reflow is an Apache Maven site skin built on [Bootstrap][bootstrap]. It allows various structural
and stylistic customizations to create a modern-looking Maven-generated website.

To get started and see how the skin looks by default, check out
http://andriusvelykis.github.com/reflow-maven-skin!

[bootstrap]: http://getbootstrap.com

## Usage

To use this Maven skin, include it in your `site.xml` file:

```xml
<project>
  ...
  <skin>
    <groupId>lt.velykis.maven.skins</groupId>
    <artifactId>reflow-maven-skin</artifactId>
    <version>1.1.0</version>
  </skin>
  ...
</project>
```

The skin requires accompanying Reflow Velocity tools (`reflow-velocity-tools`) to be available when
generating Maven site. Add them as a dependency to `maven-site-plugin` in your POM file:

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.3</version>
      <dependencies>
        ...
        <dependency>
          <groupId>lt.velykis.maven.skins</groupId>
          <artifactId>reflow-velocity-tools</artifactId>
          <version>1.1.0</version>
        </dependency>
        <!-- Reflow skin requires Velocity >= 1.7  -->
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

Note that _Apache Velocity 1.7_ is also required by the template.

The skin is provided on the "works on my computer" basis at the moment. I am using the newest
versions of `maven-site-plugin` and other components and at the moment do not have any feedback
on using the skin with Maven 2 site or other configurations.


### Configuration

The skin is configurable using the `<custom><reflowSkin>` element in your `site.xml` file.
Refer to [documentation][reflow-config] for all configuration options.

[reflow-config]: http://andriusvelykis.github.com/reflow-maven-skin/skin/config.html

A sample configuration file is given below:

```xml
<project>
  ...
  <custom>
    <reflowSkin>
      <theme>bootswatch-spacelab</theme>
      <brand>
        <name>My Project</name>
        <href>http://andriusvelykis.github.com/reflow-maven-skin/</href>
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

Have a bug or a feature request? Please create an issue here on GitHub that conforms with
[necolas's guidelines](http://github.com/necolas/issue-guidelines).

http://github.com/andriusvelykis/reflow-maven-skin/issues


## Contributing

Fork the repository and submit pull requests.


## Author

**Andrius Velykis**

+ http://andrius.velykis.lt
+ http://github.com/andriusvelykis



## Copyright and license

Copyright 2012-2013 Andrius Velykis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this work except in compliance with the License.
You may obtain a copy of the License in the LICENSE file, or at:

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
