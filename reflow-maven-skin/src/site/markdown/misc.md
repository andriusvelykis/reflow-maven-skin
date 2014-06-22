# Other configuration

This page lists various other configuration options for Reflow Maven skin.

## JavaScript goodies

### Code highlight

Code highlighting can be provided for source code sections in pages using
[highlight.js][highlight-js]. It detects the language automatically and provides code
highlighting functionality. To enable it, use `<highlightJs>` flag:

```xml
<highlightJs>true|false</highlightJs>
```

-   **true** - Source code is highlighted (syntax colouring) - requires JavaScript
-   **false (default)** - Source code is displayed in fixed font without syntax colouring

Code highlighting theme can be changed to match your website theme. Available themes are listed in 
[highlight.js website][highlight-js-themes]. The theme can be selected using `<highlightJsTheme>`
element:

```xml
<highlightJsTheme>default|*</highlightJsTheme>
```

-   **default (default)** - Default code highlighting theme is used
-   **theme name** - Entered theme is used

[highlight-js]: http://softwaremaniacs.org/soft/highlight/en/
[highlight-js-themes]: http://softwaremaniacs.org/media/soft/highlight/test.html#styleswitcher


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


### Smooth scroll

Reflow skin enables smooth scrolling for in-page navigation (e.g. for table of contents links).
Smooth scrolling can be disabled using `<smoothScroll>` flag:

```xml
<smoothScroll>true|false</smoothScroll>
```

-   **true (default)** - smooth scrolling for in-page navigation (requires JavaScript)
-   **false** - default in-page jumps


## Bootstrap

The generated Maven site code is updated by default, e.g. update CSS of generated tables, use
Bootstrap icons and other goodies. The flags to disable these updates are described below.


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


## Navbar colour

Bootstrap themes provide an [alternative colour for top navigation bar][navbar-inverse].
To use the inverse navigation bar colour, add the `<navbarInverse>` flag:

```xml
<navbarInverse>true|false</navbarInverse>
```

-   **true** - Inverted navigation bar colour is used
-   **false (default)** - Default Bootstrap theme colour is used for navigation bar

[navbar-inverse]: http://twitter.github.com/bootstrap/components.html#navbar


## Mark page header

By default, the first `<h1>` or `<h2>` heading in the page body is marked as _page header_.
It allows Bootstrap CSS to give it an emphasised presentation. To disable page header being
marked, use the `<markPageHeader>` flag:

```xml
<markPageHeader>true|false</markPageHeader>
```

-   **true (default)** - The first heading (`<h1>` or else `<h2>`) in page is marked
-   **false** - Page content is unchanged


## Protocol-relative URLs

By default, a number of core resources are loaded from the cloud. Bootstrap, JQuery,
Bootstrap themes and other components are not local to the website, but loaded from
corresponding Content Delivery Networks. This reduces the load on the server and improves
latency for users, who may have these resources already cached.

To support both `http://` and `https://` websites, these resources can be referenced
using [protocol-relative URLs][protocol-url], starting with `//`. However, this is not supported
when building the website locally, for `file://` URLs. 

For this reason, the protocol-relative URLs are disabled by default. Enable them using
`<protocolRelativeURLs>` flag:

```xml
<protocolRelativeURLs>true|false</protocolRelativeURLs>
```

-   **true** - Bootstrap CSS, JS and other resources are referenced using protocol-relative
    URLs: `//`
-   **false (default)** - Resources are referenced using `http://` protocol

[protocol-url]: http://paulirish.com/2010/the-protocol-relative-url/


## Skin attribution

The Reflow skin is released under the [Apache license][apache-license] and thus can be used freely.
By default, a link to the skin website and the author homepage is added at the bottom of the skin:
_Reflow Maven skin by Andrius Velykis_. I would appreciate if you kept the link - it will help with
the popularity of the skin. If you want, it can be disabled using `<skinAttribution>` flag:

```xml
<skinAttribution>true|false</skinAttribution>
```

-   **true (default)** - Include links to Reflow skin homepage and author's website
-   **false** - Remove skin attribution text and links

[apache-license]: http://www.apache.org/licenses/LICENSE-2.0

