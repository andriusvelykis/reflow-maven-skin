## Usage

To use this Maven skin, include it in your [`site.xml` site descriptor][site-xml] file:

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html

```xml
<project>
  ...
  <skin>
    <groupId>lt.velykis.maven.skins</groupId>
    <artifactId>reflow-maven-skin</artifactId>
    <version>1.1.1</version>
  </skin>
  ...
</project>
```

The skin is provided on the _works on my computer_ basis at the moment. I am using the newest
versions of `maven-site-plugin` and other components and at the moment do not have any feedback
on using the skin with Maven 2 site or other configurations.


## POM dependencies

Add [`reflow-velocity-tools`][reflow-tools] dependency to `maven-site-plugin` in the POM file:

[reflow-tools]: ../reflow-velocity-tools

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.4</version>
      <dependencies>
        ...
        <dependency>
          <groupId>lt.velykis.maven.skins</groupId>
          <artifactId>reflow-velocity-tools</artifactId>
          <version>1.1.1</version>
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

Reflow Velocity tools ([`reflow-velocity-tools`][reflow-tools]) are required by the Reflow skin
to read skin configuration and provides other functionality. The library must be available when
generating Maven site.

Note that _Velocity 1.7_ is also required by the template.

---


## Configuration

The skin is configurable using the `<custom>` element in your `site.xml` file. The available
options are [described in the documentation][doc]. A sample configuration file is below:

[doc]: config.html

```xml
<project>
  ...
  <custom>
    <reflowSkin>
      <theme>bootswatch-spacelab</theme>
      <highlightJs>true</highlightJs>
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
      <bottomDescription>
        This is a very good project doing interesting and valuable things.
      </bottomDescription>
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


## Learn by example

This website itself is generated using Reflow Maven skin and is written in Markdown.
The source code is [available on GitHub][reflow-src].

Look for the site configuration and web page sources in `/src/site` of each module;
and for plug-in configuration in respective POM files.

[reflow-src]: http://github.com/andriusvelykis/reflow-maven-skin "Reflow Maven skin source code"
