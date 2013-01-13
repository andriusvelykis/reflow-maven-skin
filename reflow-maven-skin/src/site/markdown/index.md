# Reflow Maven skin

Reflow is an Apache Maven site skin built on Twitter Bootstrap. It allows various structural
and stylistic customizations to create a modern-looking Maven-generated website.

---


## Usage

To use this Maven skin, include it in your [`site.xml` site descriptor][site-xml] file:

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html

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

Add [`reflow-velocity-tools`][reflow-tools] dependency to `maven-site-plugin` in the POM file:

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

Reflow Velocity tools ([`reflow-velocity-tools`][reflow-tools]) are required by the Reflow skin
to read skin configuration and provides other functionality. The library must be available when
generating Maven site.

Note that _Velocity 1.7_ is also required by the template.

---


## Configuration

The skin is configurable using the `<custom>` element in your `site.xml` file. The available
options are [described in the documentation][doc]. A sample configuration file is below.
Note that all configuration options can be set on a per-page basis
([see below](#Per-page_configuration)).

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

### Per-page configuration

Reflow skin has quite a number of configuration options, which can be applied either globally or
limited to a specific page. The _per-page configuration_ allows setting page-specific layouts,
disable breadcrumbs or table of contents, or even change the theme for a single page.

All configuration options can be specified _per-page_. If the same configuration is specified
both _globally_ and _per-page_, the _per-page_ one overrides the global option.

The configuration of a page is set using the `<pages>` element of the skin configuration.
Each element within `<pages>` indicates the name of the page file being customized.

```xml
<custom>
  <reflowSkin>
    ...
    <pages>
      <index>
        <!-- index.html (generated from index.apt, index.md or others):
             will set the configuration for index.html in submodules as well. -->
        ...
      </index>
      <usage-basic project="my-project">
        <!-- usage/basic.html in "my-project" project only: submodules will not inherit -->
        ...
      </usage-basic>
      ...
    </pages>
    ...
  </reflowSkin>
</custom>
```

#### Page ID

Each page configuration is set within a `<pages><[pageId]>` element.
    
The page ID is its file name without extension. This is because the pages may be generated
from different file formats, e.g. [Apt, Xdoc, Markdown, etc][doxia-formats]. The page ID
characters must be of the same case as the generated file name.
    
For generated files that are in directories, `"-"` is used as the separator for directory
structure. So a file in _./subdir/foo.html_ is referred as `<subdir-foo>` in the
configuration.

[doxia-formats]: http://maven.apache.org/doxia/references/index.html


#### Configuration inheritance

The page configuration is applied to all generated pages that have the indicated file name.
For multi-module builds, child sites will inherit the configuration from parent site.
So if the parent has defined a configuration for `<index>`, it will also be applied to
_index.html_ in the child project.
    
Sometimes this is undesirable, so one can use `project` attribute to indicate the project
that the page applies to. For child pages, the project will be different from indicated
and thus the configuration will not apply. The project is identified using its `artifactId`.


## Learn by example

This website itself is generated using Reflow Maven skin and is written in Markdown.
The source code is [available on GitHub][reflow-src].

Look for the site configuration and web page sources in `/src/site` of each module;
and for plug-in configuration in respective POM files.

[reflow-src]: http://github.com/andriusvelykis/reflow-maven-skin "Reflow Maven skin source code"
