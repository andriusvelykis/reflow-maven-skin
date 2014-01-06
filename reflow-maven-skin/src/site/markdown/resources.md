# Local/custom resources

Reflow Maven skin utilises content delivery networks (CDN) for a number of CSS and JavaScript
resources. These include Bootstrap files, themes, the jQuery library, etc. The reuse of
popular resources via CDN provides better performance. However, such resources may not be
accessible when the site is used in an offline/intranet setting.

To support such deployments of Reflow skin, the configuration allows using local resources.
Furthermore, custom resources can also be easily added to the generated website.
These include newer versions of the used libraries as well as arbitrary scripts.

## Local Bootstrap & jQuery

**Requires Reflow Maven skin &ge; v1.1.0.**

To force the generated site use only local CSS/JavaScript resources, use the `<localResources>`
flag:

```xml
<localResources>true|false</localResources>
```

-   **true** - Locally provided Bootstrap and jQuery files will be used in the generated site,
    not the CDN versions. This forces [`site` Bootstrap theme][bootstrap-theme], which
    uses local Bootstrap files. Furthermore, other non-local resources such as _highlight.js_
    are disabled and their [local versions need to be added manually][local-css].
    See below for the list of files that need to be available locally.

-   **false (default)** - CDN delivered resources are used, where available.

Note that when using `<localResources>`, you need to provide the necessary local versions of
Bootstrap and jQuery files. Reflow skin does not package the default files at the moment, mainly
because overriding packaged files is not possible at the moment (see [issue MSITE-702][msite-702]).
Therefore it is easier to get appropriate Bootstrap files directly from the
[customiser][bootstrap-cust] or [Bootswatch][bootswatch2] and use them with the Reflow skin.
Furthermore, such approach allows you to customise the Bootstrap and jQuery versions used.
Use at your own risk, however, as major version changes are likely to break the website!

When using `<localResources>`, the following files need to be available in the corresponding
directories within your project's `src/site/resources` folder:

-   **`css/bootstrap.min.css`** - Reflow skin currently uses Bootstrap 2.3.2
-   **`css/bootstrap-responsive.min.css`** - Bootstrap 2.3.2
-   **`js/bootstrap.min.js`** - Bootstrap 2.3.2
-   **`js/jquery.min.js`** - jQuery 1.9.1
-   **`js/html5.js`** - HTML5 support for IE6-8, get it from [here][html5shiv].

For multi-module site, remember to enable [resource reuse flag][resource-reuse], otherwise
these files will have to be repeated for each sub-module.


[bootstrap-theme]: themes/
[local-css]: #Additional_CSS_files
[msite-702]: https://jira.codehaus.org/browse/MSITE-702
[bootstrap-cust]: http://getbootstrap.com/2.3.2/customize.html
[bootswatch2]: http://bootswatch.com/2/
[html5shiv]: http://html5shim.googlecode.com/svn/trunk/html5.js


## Additional CSS files

Additional CSS files (or other resources) can be added to the page head using [`<body><head>`
configuration][body-head-config] in the `site.xml` site descriptor.
This configuration is available to all Maven site skins and is part of the standard
site descriptor configuration.

The `<head>` element allows arbitrary XHTML, which will be injected in the generated page.
Also, it can be used for custom (local or remote) versions of hard-coded Reflow skin resources.
For example, to use a custom version of [highlight.js syntax highlighting][highlight-js-config],
you can disable the default configuration (`<highlightJs>false</highlightJs>`) and
add the relevant CSS and JavaScript manually:

```xml
<project>
  ...
  <body>
    ...
    <head>
      <link rel="stylesheet" href="http://yandex.st/highlightjs/7.5/styles/default.min.css">
      <script src="http://yandex.st/highlightjs/7.5/highlight.min.js"></script>
    </head>
    ...
  </body>
  ...
</project>
```

Note: the `<head>` element is within `<body>`, not `<custom>` element of `site.xml` site
descriptor.

Furthermore, the injected XHTML allows using Velocity variables. Therefore you can [reuse
a local version of the library][resource-reuse] by indicating its path with a `$resourcePath`
variable, e.g.: 

```xml
<head>
  <link rel="stylesheet" href="$resourcePath/css/default.min.css">
</head>
```

[body-head-config]: http://maven.apache.org/plugins/maven-site-plugin/examples/sitedescriptor.html#Inject_xhtml_into_head
[highlight-js-config]: misc.html#Code_highlight
[resource-reuse]: multi-module.html#Reuse_resources


## Additional JavaScript files

**Requires Reflow Maven skin &ge; v1.1.0.**

JavaScript files can be added to the page head together with CSS files. However, for performance
reasons, it can be better to reference JavaScript files at the end of the document.
To place custom XHTML at the end of the page (before the `</body>` tag), use the `<endContent>`
element, which also supports Velocity variables, e.g.:

```xml
<custom>
  <reflowSkin>
    ...
    <endContent>
      <script src="$resourcePath/js/highlight.min.js"></script>
    </endContent>
    ...
  </reflowSkin>
</custom>
```

Note: the `<endContent>` element is specific to Reflow skin and needs to be placed within the
`<custom><reflowSkin>` element.
