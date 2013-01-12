# Components


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


## Header

Standard Maven site allows customising the website _banner_. Reflow skin adds further components to
the site header: _brand_ and _slogan_.

### Banner

The banner (big _Reflow Maven Skin_ text on this website) is defined using 
[standard `<bannerLeft>` or `<bannerRight>` elements][mvn-site-banner] in `site.xml` site
descriptor instead of using `<custom><reflowSkin>`:

```xml
<project ...>
...
  <bannerLeft>
    <!-- Reflow Maven Skin, but with "Reflow" highlighted -->
    <name><![CDATA[
      <span class="color-highlight">Reflow</span> Maven Skin
      ]]>
    </name>
    <href>http://andriusvelykis.github.com/reflow-maven-skin</href>
  </bannerLeft>
  ...
</project>
```xml

Note that `<name>` element can have `CDATA` element as its contents, allowing custom HTML content
in the banner.

[mvn-site-banner]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Banner

### Brand

The brand text (or image) appearing in the left of the top navigation bar can be customised using
`<brand>` element. You can set both the contents and the link.

```xml
<brand>
  <name>text</name>
  <href>link</href>
</brand>
```

-   **name** element sets the text of the top-left brand
-   **href** element sets the link to open when the brand text is clicked

Note that the `<name>` element can be used to embed custom HTML content. To do that, use `CDATA`
element as the contents of `<name>` (writing HTML without `CDATA` will not work):

```xml
<brand>
  <name>
    <![CDATA[
    <span class="color-highlight">Reflow</span> Maven Skin
    ]]>
  </name>
  ..
</brand>
```

The above example is taken from this website, in order to have the _Reflow_ part in different
colour. Images could also be added there in a similar manner.


### Slogan

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



## Menus

Menus in Reflow skin can be placed both at the top navigation bar and at the bottom navigation
columns. The menus themselves are defined as normally in Maven site, 
[using `<project><body><menu>` XML items][mvn-site-menus]. The skin allows customizing where which
menu items are placed in the website.

Both top and bottom navigation allows specifying regular expressions that filter the menus
to be displayed in the particular place. The regular expressions can match both the name of the
menu item and its `ref` value, e.g. to match `<menu ref="modules" inherit="bottom" />`.

[mvn-site-menus]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Including_Generated_Content


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


### Links

All links from defined in [`<body><links>` element][mvn-site-links] of `site.xml` site descriptor
are placed in the top navigation bar. This allows having top-level links in navigation (menus are
always drop-down).

[mvn-site-links]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Links


### Bottom navigation

The bottom navigation is placed in the footer, to the left of website description. It can feature
a number of columns with menu items. The columns are indicated using the `bottomNav` element.
If configuration is not available, lists all menus in a single column.

```
<bottomNav maxSpan="8">
  <column>RegEx</column>
  <column>RegEx</column>
  <column>RegEx</column>
</bottomNav>
```

The `maxSpan` attribute defines the total width that bottom navigation columns can take
(out of 12). By default, the **`maxSpan = 9`**. The columns are then placed equally within
this span.

Each column in the configuration defines a regular expression for menu items that will be listed
in said column. For example, to include only _Home_ and _Download_ items, use `Home|Download`
for the regular expression.


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


## Bottom description

The bottom navigation provides space for site description, logo or other content. It is placed on
the right of the bottom navigation area. Site description takes up space remaining after the bottom
navigation (controlled via [`maxSpan` attribute](#Bottom_navigation), site description occupies
`12 - maxSpan` then). The bottom description is set using `<bottomDescription>` element:

```xml
<bottomDescription quote="true|false">description</bottomDescription>
```

The `quote` attribute wraps the text into a `<blockquote>` element:

-   **true (default)** - Text is wrapped into a `<blockquote>` element
-   **false** - Text is printed as-is

The **description** can be plain text or a `CDATA` element and thus wrap the text with HTML formatting.

Alternatively, `<bottomDescription>` can contain HTML elements directly, which will be embedded
in the bottom description area.

Note that the bottom description must be enabled if Maven site
[date or version are set to position **navigation-bottom**](#Date_and_version).


## Breadcrumbs

Breadcrumbs are defined using standard Maven site element
[`<body><breadcrumbs>`][mvn-site-breadcrumbs] in `site.xml`.
Reflow skin provides a `<breadcrumbs>` flag to enable/disable them:

```xml
<breadcrumbs>true|false</breadcrumbs>
```

-   **true (default)** - Breadcrumb trail is displayed below the banner
-   **false** - Do not display breadcrumbs

Note that the breadcrumbs bar must be enabled if Maven site
[date or version are set to position **left** or **right**](#Date_and_version).

[mvn-site-breadcrumbs]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Breadcrumbs


## Date and version

Reflow skin supports displaying Maven site publish date and version. They are defined using
standard Maven site elements [`<publishDate>`][mvn-site-date] and [`<version>`][mvn-site-version].

Reflow skin supports the following values for `position` of these components:

-   **left** - Left in breadcrumbs bar (requires [`<breadcrumbs>` flag](#Breadcrumbs)
    to be enabled)
-   **right** - Right in breadcrumbs bar (requires [`<breadcrumbs>` flag](#Breadcrumbs)
    to be enabled)
-   **navigation-bottom** - Bottom right in the bottom navigation (requires 
    [`<bottomDescription>`](#Bottom_description) to be enabled)
-   **bottom (default)** - Subfooter (the last part of the page, as in this website)
-   **none** - Date or version are disabled

Note that **navigation-top** position is not supported by Reflow skin.

[mvn-site-date]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Publish_Date
[mvn-site-version]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Version


### Override publish date

The website publish date can be indicated explicitly in the configuration by using `<publishDate>`
element:

```xml
<publishDate>2013-01-08</publishDate>
```
