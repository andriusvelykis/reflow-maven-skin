# Reflow Maven skin

Reflow is an Apache Maven site skin built on Twitter Bootstrap. It allows various structural
and stylistic customizations to create a modern-looking Maven-generated website.

---


## Usage

To use this Maven skin, include it in your `site.xml` file:

```xml
<project>
  ...
  <skin>
    <skin>
      <groupId>lt.velykis.maven.skins</groupId>
      <artifactId>reflow-maven-skin</artifactId>
      <version>1.0.0</version>
    </skin>
  </skin>
  ...
</project>
```

The skin is provided on the _works on my computer_ basis at the moment. I am using the newest
versions of `maven-site-plugin` and other components and at the moment do not have any feedback
on using the skin with Maven 2 site or other configurations.


## POM dependencies

The skin requires custom Velocity tools ([`reflow-velocity-tools`][reflow-tools])
to be available when generating Maven site. The tools library needs to be included
as dependency to `maven-site-plugin` in your POM file:

[reflow-tools]: ../reflow-velocity-tools

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.2</version>
      <dependencies>
        ...
        <dependency>
          <groupId>lt.velykis.maven.skins</groupId>
          <artifactId>reflow-velocity-tools</artifactId>
          <version>1.0.0</version>
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

Note that _Velocity 1.7_ is also required by the template.

---


### Configuration

The skin is configurable using the `<custom>` element in your `site.xml` file. The avaliable options
are described in the documentation. A sample configuration file is below:

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
