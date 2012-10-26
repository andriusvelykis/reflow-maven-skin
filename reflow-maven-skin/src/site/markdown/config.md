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
Bootstrap themes. They are set using the `theme` element in the customization:

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

-    **`default`**
     
     (**Default**: will be used if `<theme>` is not set).
     
     Default Bootstrap theme (version 2.1.1). The default theme (with minor customizations)
     is used for this website.
     
     The default Bootstrap theme is loaded from the
     [BootstrapCDN Content Delivery Network][bootstrapcdn], which improves website loading
     by hosting the Bootstrap CSS and JavaScript files.

-    **`site`**
     
     A customized (local) Bootstrap theme. This option should be used when a custom Bootstrap
     theme is developed and included with the Maven site. The theme expects the following
     Bootstrap CSS and JavaScript files to be available in `src/site/resources`:
     
     -    `css/bootstrap.min.css`
     -    `css/bootstrap-responsive.min.css`
     -    `js/bootstrap.min.js`
     
     The files can be customized and generated at [Bootstrap's Customize page][bootstrap-custom].

-    **`bootswatch-*`**
     
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
