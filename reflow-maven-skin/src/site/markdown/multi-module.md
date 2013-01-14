# Multi-module site

Aimed at providing good support for websites of multi-module Maven projects, Reflow skin has
several options enhancing such configurations.

## Reuse resources

In a multi-module project, the Maven site for each submodule is generated separately, and only then
brought together for deployment. Reflow skin allows reusing resources of top-level module using
`<absoluteResourceURL>` element:

```xml
<custom>
  <reflowSkin>
    ...
    <absoluteResourceURL>http://mysite.com/</absoluteResourceURL>
    ...
  </reflowSkin>
</custom>
```

The absolute resource URL should be relative to project URL set in POM. Then all modules
will resolve a relative path to resources under this URL.

Setting the absolute resource URL allows configuring CSS, images and other site resources
for the top-level module only. Normally, when generating a multi-module Maven site, each module
gets a copy of Maven skin resources together with that module's local `site.css` file and others.
This means, however, that the users must either create their own skin to ensure that resources are
copied to all modules or manually copy these resources. Furthermore, each resource has to be loaded
separately, so that expensive brand image could be loaded again for each submodule.

Using `absoluteResourceURL` element allows referencing the same resources. This way a skin does not
need to be extended, and all customisations can be done using CSS and JavaScript files in the
top-level module. Note that the skin resources are still copied to each submodule and will be
there in the generated site, however they are not referenced by the module site files.


## Project URLs

If you encounter problems with relative links (e.g. if the links are not correctly highlighted),
ensure that URLs are indicated (or derived automatically) correctly for each module. Reflow skin
uses project URLs to calculate correct relative paths between modules, e.g. especially when the
links reference pages in other submodules.

Project URLs are indicated using the [`<url>` element in the POM file][pom-url] of each project.

[pom-url]: http://maven.apache.org/pom.html#More_Project_Information
