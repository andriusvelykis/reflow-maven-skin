# Advanced configuration

Reflow skin allows configuring various parts on the Maven site and updates the pages to use
HTML5 and Bootstrap components. This page outlines the various configuration options.
All options can be specified globally or per-page. Refer to the [Basic configuration][basic]
page for details on per-page configuration.

The configuration is specified in the [`site.xml` site descriptor][site-xml] file,
using the `<custom><reflowSkin>` element.

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html
[basic]: config.html


## Override publish date

The website publish date can be indicated explicitly in the configuration by using `<publishDate>`
element:

```xml
<publishDate>2013-01-08</publishDate>
```


## Modern website

The skin performs several website modernisation actions by default, such as update CSS or use
matching icons and other bells and whistles. The flags to disable them are described below.


### Bootstrap CSS

[Bootstrap][bootstrap] provides nice CSS effects for certain elements, such as tables, etc. Reflow
skin rewrites some Maven site generated elements to add Bootstrap classes and configurations.
This can be disabled using `bootstrapCSS` flag:

```xml
<bootstrapCSS>true|false</bootstrapCSS>
```

-   **true (default)** - adds Bootstrap CSS classes to tables; fixes table headings
-   **false** - keep original table HTML

[bootstrap]: http://twitter.github.com/bootstrap/


### Bootstrap icons

To match [Bootstrap][bootstrap] themes, Reflow skin replaces some icons used by Maven site with
corresponding Bootstrap icons, e.g. in change log page and others:

![Add](images/add.gif)
![Fix](images/fix.gif)
![Error](images/icon_error_sml.gif)
![Success](images/icon_success_sml.gif)

This can be disabled using `bootstrapIcons` flag:

```xml
<bootstrapIcons>true|false</bootstrapIcons>
```

-   **true (default)** - replaces images (add, remove, warning, info etc) with corresponding
    Bootstrap icons
-   **false** - keep original images


### HTML5-style anchors

Anchors (links to page elements) in HTML5 are defined using `id` attribute on any element.
Previously anchors used the `<a name="...">` element. Reflow transforms these `<a>` anchors
to HTML5 equivalents where applicable by default. Disable with `html5Anchor` flag.

```xml
<html5Anchor>true|false</html5Anchor>
```

-   **true (default)** - HTML4-style anchors `<a name="...">` are replaced with `id` attributes
    where applicable
-   **false** - keep the original anchors


### Image previews (lightbox)

Reflow skin enables CSS pop-ups for image links on the website using [Lightbox 2][lightbox].
See one in action by opening [this link to an image][lightbox-test]. The previews are enabled by
default but can be disabled using `<imgLightbox>` flag:

```xml
<imgLightbox>true|false</imgLightbox>
```

-   **true (default)** - when image link is opened in the website, opens a lightweight CSS pop-up
    with the image over the page
-   **false** - disable lightbox, image link will open the image itself

[lightbox]: http://lokeshdhakar.com/projects/lightbox2/
[lightbox-test]: img/snowdon.jpg "Lightbox example - climbing Snowdon in Wales, UK"


### Code highlight

Code highlighting can be provided for source code sections in pages using
[highlight.js][highlight-js]. It detects the language automatically and provides code
highlighting functionality. To enable it, use `<highlightJs>` flag:
  
```xml
<highlightJs>true|false</highlightJs>
```

-   **true** - Source code is higlighted (syntax colouring) - requires JavaScript
-   **false (default)** - Source code is displayed in fixed font without syntax colouring

[highlight-js]: http://softwaremaniacs.org/soft/highlight/en/


#### Invert top navigation colour

