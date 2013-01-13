# Configuration

Reflow skin supports extensive configuration options, including theme, layout, new/updated
page components and a large number of various other flags.

The configuration is specified in the [`site.xml` site descriptor][site-xml] file,
using the `<custom><reflowSkin>` element.

The documentation is split over several pages, describing the various configuration options:

-   **[Themes]( themes/index.html )**

    How to configure own theme or select one of the excellent [Bootswatch][bootswatch] themes.
-   **[Layouts]( layout.html )**

    Reflow the page with different layouts: multi-column text, carousel, thumbnails and others.
-   **[Components]( components.html )**

    Enable and configure various existing and new components: table of contents, headers, menus,
    etc.
-   **[Other configuration]( misc.html )**

    Various other configuration options: JavaScript goodies, CSS updates and more.
-   **[Multi-module site]( multi-module.html )**

    Additional support and notes for generating multi-module Maven sites.

Note that all configuration options can be set on a per-page basis
([see below](#Per-page_configuration)).

[site-xml]: http://maven.apache.org/doxia/doxia-sitetools/doxia-decoration-model/decoration.html
[bootswatch]: http://bootswatch.com


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


### Page ID

Each page configuration is set within a `<pages><[pageId]>` element.
    
The page ID is its file name without extension. This is because the pages may be generated
from different file formats, e.g. [Apt, Xdoc, Markdown, etc][doxia-formats]. The page ID
characters must be of the same case as the generated file name.
    
For generated files that are in directories, `"-"` is used as the separator for directory
structure. So a file in _./subdir/foo.html_ is referred as `<subdir-foo>` in the
configuration.

[doxia-formats]: http://maven.apache.org/doxia/references/index.html


### Configuration inheritance

The page configuration is applied to all generated pages that have the indicated file name.
For multi-module builds, child sites will inherit the configuration from parent site.
So if the parent has defined a configuration for `<index>`, it will also be applied to
_index.html_ in the child project.
    
Sometimes this is undesirable, so one can use `project` attribute to indicate the project
that the page applies to. For child pages, the project will be different from indicated
and thus the configuration will not apply. The project is identified using its `artifactId`.

If you still encounter inheritance problems, try using
[`combine.self="override"` attribute][mvn-merge] on the `site.xml` element to explicitly override
it.

[mvn-merge]: http://www.sonatype.com/people/2011/01/maven-how-to-merging-plugin-configuration-in-complex-projects/
