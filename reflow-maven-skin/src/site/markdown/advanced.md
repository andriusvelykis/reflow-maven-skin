# Advanced configuration

Reflow skin allows configuring various parts on the Maven site and updates the pages to use
HTML5 and Bootstrap components. This page outlines the various configuration options.
All options can be specified globally or per-page. Refer to the [Basic configuration][basic]
page for details on per-page configuration.

The configuration is specified in the [`site.xml` site descriptor][site-xml] file,
using the `<custom><reflowSkin>` element.

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html
[basic]: config.html


## Table of contents

The skin supports automatic generation of table of contents (ToC) for any page based on the headings
appearing in the page. Clicking on the ToC item jumps to the corresponding heading in the page.

The ToC style can be chosen with `<toc>` element:
  
```xml
<custom>
  <reflowSkin>
    ...
    <toc>top|sidebar</theme>
    ...
  </reflowSkin>
</custom>
```

-   **top** - top ToC bar (subnav)
-   **sidebar** - sidebar ToC tree
-   **any other value or no `<toc>` element (default)** - do not create ToC

Both styles are exclusive: either the top or the sidebar can be used in a single page. The
different styles and their further options are explained below.


### Top ToC

Top ToC displays the table of contents as a horizontal menu just below the site header. Top items
provide drop-down menus for subsections. Such menu is used throughout the whole site here.

The ToC snaps to the top of the screen to allow easy navigation in long pages.

```xml
<toc>top</toc>
```

---


#### Limit the number of top ToC items

Top ToC can be limited to a certain number of items in configuration. All menu entries exceeding
that number are grouped under the last dropdown menu. Exclude the option if unlimited.

```xml
<tocTopMax>6</tocTopMax>
```

-   **number** - limit to this number of items
-   **-1 or no `<tocTopMax>` element (default)** - unlimited


#### Flatten first 2 levels of ToC

Top ToC for pages that have only a single top heading (e.g. `h1`) also includes the 2nd level
headings as top menu items. This is to avoid having a single item in the whole menu. Otherwise
only the top level headings are included. This option can be overridden to force always or
disable altogether.

```xml
<tocTopFlatten>true|false</tocTopFlatten>
```
-   **true** - force flattening 2 ToC levels
-   **false** - never flatten, always just use 1st level ToC items
-   **no `<tocTopFlatten>` element (default)** - flatten if single top heading

---


### Sidebar ToC

The ToC can be displayed as a tree in the sidebar. This style does not snap to the page when
scrolling, but scrolls with the whole page. See it in action in the [sample page][toc-sidebar].

```xml
<toc>sidebar</toc>
```

[toc-sidebar]: toc-sidebar.html


## Slogan

The skin allows setting a website slogan in the banner. It would appear underneath the website
title, either on the left or the right side.

This website features a slogan
"_Responsive Apache Maven skin to reflow the standard Maven site with a modern feel_".

```xml
<slogan position="bannerRight|bannerLeft">The best website in the world</slogan>
```

The `position` attribute indicates where to place the slogan:

-   **bannerLeft (default)** - aligned to the left (as in this website)
-   **bannerRight** - aligned to the right



## Menu filtering

Menus in Reflow skin can be placed both at the top navigation bar and at the bottom navigation
columns. The menus themselves are defined as normally in Maven site, using `<project><body><menu>`
XML items. The skin allows customizing where which menu items are placed in the website.

Both top and bottom navigation allows specifying regular expressions that filter the menus
to be displayed in the particular place. The regular expressions can match both the name of the
menu item and its `ref` value, e.g. to match `<menu ref="modules" inherit="bottom" />`.


### Top navigation

Top navigation is right-aligned at the top of the screen. It sticks to the screen when scrolling.
The menu items can be filtered using regular expression to indicate which of the menu items
are displayed there:

```xml
<topNav>RegEx<topNav>
```

-   **RegEx** - applies the regular expression to menu names and `ref` attributes. If the regular
    expression matches, adds the menu to the top navigation.
-   **no `<topNav>` element (default)** - list all menus


### Bottom navigation

The bottom navigation is placed in the footer, to the left of website description. It can feature
a number of columns with menu items. The columns are indicated using the `bottomNav` element.
If configuration is not available, lists all menus in a single column.

```
<bottomNav maxSpan="8">
  <column>regex</column>
  <column>regex</column>
  <column>regex</column>
</bottomNav>
```

The `maxSpan` attribute defines the total width that bottom navigation columns can take
(out of 12). By default, the **`maxSpan = 9`**. The columns are then placed equally within
this span.

Each column in the configuration defines a regular expression for menu items that will be listed
in said column. For example, to include only _Home_ and _Download_ items, use `Home|Download`
for the regular expression.


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

Code highlighting is provided for source code sections in pages using
[highlight.js][highlight-js]. It detects the language automatically and provides code
highlighting functionality. It is not enabled by default can be disabled using `<highlightJs>` flag:
  
```xml
<highlightJs>true|false</highlightJs>
```

Source code sections in pages are enhanced Code highlight/formatting is provided 
Reflow skin enables CSS pop-ups for image links on the website using [Lightbox 2][lightbox].
See one in action by opening [this link to an image][lightbox-test]. The previews are enabled by
default but can be disabled using `<imgLightbox>` flag:

```xml
<imgLightbox>true|false</imgLightbox>
```

-   **true (default)** - when image link is opened in the website, opens a lightweight CSS pop-up
    with the image over the page
-   **false** - disable lightbox, image link will open the image itself

[highlight-js]: http://softwaremaniacs.org/soft/highlight/en/
[lightbox-test]: img/snowdon.jpg "Lightbox example - climbing Snowdon in Wales, UK"


## Page title

### Generate or set short title

Short title is used in site breadcrumbs and other places. In some cases it is created by Maven
site (e.g. when set in APT), etc. In other cases, however, it is missing - a glaring omission
from Markdown pages.

Reflow skin provides a couple options to circumvent this, either generate or set the short title
explicitly using `<shortTitle>` element:

```xml
<shortTitle>generate|*</shortTitle>
```

-   **generate** - a short title will always be generated from the headings in the page. If `<h1>`
    heading is available, it is used. Otherwise first `<h2>` heading is used.
-   **Some short title** - use this explicitly indicated short title
-   **false** - disable any short title generation altogether
-   **no `<shortTitle>` element (default)** - short title will be generated (as above) if one is
    not available


### Custom page title

By default, the page title is generated from the project name and its short title. The skin allows
setting own page title, e.g. to allow for titles such as "_My Page | My Super Website_", etc.

```xml
<titleTemplate>template</titleTemplate>
```

-   **Some template** - use [Java format String][java-format] here, where `%1$s` is the project
    name and `%2$s` is the short title of the page.
    
    This allows omitting any of these names, or just specifying custom uniform title for pages
    without any of the variables.
    
-   **no `<titleTemplate>` element (default)** - default title template `%1$s - %2$s` is used

[java-format]: http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html


#### Invert top navigation colour

#### Brand


