# Basic configuration

Reflow skin supports extensive configuration options, including theme, layout, page elements
and different other flags.

The configuration is specified in the [`site.xml` site descriptor][site-xml] file,
using the `<custom><reflowSkin>` element.

This page outlines the main elements of the configuration: themes, per-page configuration, and
the available content layout options. For further configuration options, see the
[Advanced configuration][advanced] and [Multi-project site configuration][multi-proj] pages.

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html
[advanced]: advanced.html
[multi-proj]: multi-project.html


## Themes

Reflow skin builds on [Twitter Bootstrap][bootstrap] and supports predefined and custom
Bootstrap themes. They are set using the `<theme>` element in the customization:

[bootstrap]: http://twitter.github.com/bootstrap/

```xml
<custom>
  <reflowSkin>
    ...
    <theme>default|site|bootswatch-*</theme>
    ...
  </reflowSkin>
</custom>
```

There are several options for the `<theme>` value:

-   **`default`**
    
    (**Default**: will be used if `<theme>` is not set).
    
    Default Bootstrap theme (version 2.1.1). The default theme (with minor customizations)
    is used for this website.
    
    The default Bootstrap theme is loaded from the
    [BootstrapCDN Content Delivery Network][bootstrapcdn], which improves website loading
    by hosting the Bootstrap CSS and JavaScript files.

-   **`site`**
    
    A customized (local) Bootstrap theme. This option should be used when a custom Bootstrap
    theme is developed and included with the Maven site. The theme expects the following
    Bootstrap CSS and JavaScript files to be available in `src/site/resources`:
    
    -   `css/bootstrap.min.css`
    -   `css/bootstrap-responsive.min.css`
    -   `js/bootstrap.min.js`
    
    The files can be customized and generated at [Bootstrap's Customize page][bootstrap-custom].

-   **`bootswatch-*`**
    
    One of free Bootstrap themes from [Bootswatch][bootswatch]. Append the lowercase theme name
    in the [Bootswatch gallery][bootswatch-gallery], e.g. `bootswatch-readable`.
    
    Bootswatch themes (version 2.1.0) are loaded from the
    [BootstrapCDN Content Delivery Network][bootstrapcdn].
    
    Preview full themes at the [Bootswatch gallery][bootswatch-gallery],
    or check out several examples of Reflow skin using Bootswatch themes below.

[bootstrapcdn]: http://bootstrapcdn.com
[bootstrap-custom]: http://twitter.github.com/bootstrap/customize.html
[bootswatch]: http://bootswatch.com
[bootswatch-gallery]: http://bootswatch.com/#gallery

---

### [Amelia][theme-amelia]

[![Amelia Bootswatch theme](img/bootswatch-amelia.png)][theme-amelia]

```xml
<theme>bootswatch-amelia</theme>
```

[theme-amelia]: themes/bootswatch-amelia.html


### [Cerulean][theme-cerulean]

[![Cerulean Bootswatch theme](img/bootswatch-cerulean.png)][theme-cerulean]

```xml
<theme>bootswatch-cerulean</theme>
```

[theme-cerulean]: themes/bootswatch-cerulean.html


### [Readable][theme-readable]

[![Readable Bootswatch theme](img/bootswatch-readable.png)][theme-readable]

```xml
<theme>bootswatch-readable</theme>
```

[theme-readable]: themes/bootswatch-readable.html



### [Spacelab][theme-spacelab]

[![Spacelab Bootswatch theme](img/bootswatch-spacelab.png)][theme-spacelab]

```xml
<theme>bootswatch-spacelab</theme>
```

[theme-spacelab]: themes/bootswatch-spacelab.html



### [Spruce][theme-spruce]

[![Spruce Bootswatch theme](img/bootswatch-spruce.png)][theme-spruce]

```xml
<theme>bootswatch-spruce</theme>
```

[theme-spruce]: themes/bootswatch-spruce.html



### [United][theme-united]

[![United Bootswatch theme](img/bootswatch-united.png)][theme-united]

```xml
<theme>bootswatch-united</theme>
```

[theme-united]: themes/bootswatch-united.html


---


## Per-page configuration

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
