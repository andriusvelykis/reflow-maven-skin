# Page section layouts

When writing Maven site pages using most of the available file formats, such as
[APT, XDoc or Markdown][doxia-formats], it is difficult to write anything more but a
straightforward linear page with headings to split the content. Reflow skin allows customizing
the page layout and reflowing the page using columns, thumbnails, or with the _carousel_ component.

The page is divided into _sections_ and each section can be assigned a special layout for its
contents. The different layouts are discussed in more detail below. The sections are specified
using `<sections>` element in the per-page (or global) configuration:

```xml
<custom>
  <reflowSkin>
    ...
    <pages>
      <index>
        <!-- index.html sections (examples of all available sections) -->
        <sections>
          <carousel />
          <thumbs>2</thumbs>
          <body />
          <sidebar />
          <columns>3</columns>
        </sections>
        ...
      </index>
      ...
    </pages>
    ...
  </reflowSkin>
</custom>
```

[doxia-formats]: http://maven.apache.org/doxia/references/index.html


## Defining sections

Page sections are indicated using _horizontal rule_ (`<hr/>`) elements in the generated page.
Check your file format reference on how to write them, e.g. `"==="` for [APT][apt-ref] or `"---"`
for [Markdown][markdown-hr-ref].

If sections are configured for a page, its contents are split on the `<hr/>` element
and indicated section layout is used sequentially. For all sections that have undefined
layout, `<body/>` is used (just plain body text). So for the example configuration above, the index
page is expected to have at least 5 sections, separated by _horizontal rules_. Then the first
one will be formatted as _carousel_, followed by a 2-column _thumbnails_ section, followed by
a _body_ text with _sidebar_ (2 sections here), then with 3-_column_ section. If there are more
sections after this in the text, they will have usual _body_ text layout.

Note that the layout actually defines how the _subsections_ of each section are laid out.
_Subsections_ currently are produced by splitting the section at the inner headings.

[apt-ref]: http://maven.apache.org/doxia/references/apt-format.html
[markdown-hr-ref]: http://daringfireball.net/projects/markdown/syntax#hr


## Carousel

_Carousel_ component provides a slideshow of images with captions. It is normally used in front page
to advertise main features or showcase a portfolio, etc. See one in action at the top of the
[front page of this site][carousel-reflow], or as a [Bootstrap component][carousel-bootstrap].

Carousel section is indicated using `<carousel />` element without any attributes:

```xml
...
<sections>
  <carousel />
  ...
</sections>
...
```

Carousel is constructed by displaying each subsection in the text as a carousel slide. The first
image in the section becomes the carousel image. The subsection header and text consitute the slide
caption. For best results, aim for images of the same size, and use `h4` headings for the
subsections.

[carousel-reflow]: ../
[carousel-bootstrap]: http://twitter.github.com/bootstrap/javascript.html#carousel


## Columns

_Columns_ layout puts the section parts into columns. Each subsection is placed in the subsequent
column. The layout requires the number of columns to be indicated. Bootstrap uses a 12-column
ruler, so for best results, use 1, 2, 3, 4, 6 or 12 column layout. A 2-column layout is used
[here][columns-reflow].

Columns section with the number of columns is indicated using `<columns>num</columns>` element:

```xml
...
<sections>
  ...
  <columns>2</columns>
  ...
</sections>
...
```

[columns-reflow]: ./#Usage


## Thumbnails

_Thumbnails_ layout is best suited to produce a gallery of images. Each subsection constitutes a
thumbnail block, with its first image followed by the subsection heading and text. The [themes
page][themes-reflow] uses thumbnails to showcase examples of Bootswatch themes.

Thumbnails are placed in columns, so the same requirements as in _columns_ layout apply. To set
the layout with the number of columns, use `<thumbs>num</thumbs>` element:

```xml
...
<sections>
  ...
  <thumbs>3</thumbs>
  ...
</sections>
...
```

[themes-reflow]: themes/#theme-bootswatch


## Body

_Body_ layout just outputs the page contents as they are. The subsections are output sequentially.
If no _sidebar_ (or sidebar ToC) is indicated, the _body_ text occupies full page width. Otherwise
it accommodates the sidebar and takes 3/4 of the page.

Body section is indicated using `<body />` element without any attributes. The layout is also used
as default for sections without explicit layout.

```xml
...
<sections>
  ...
  <body />
  ...
</sections>
...
```


## Sidebar

_Sidebar_ layout places the section contents into a sidebar with shaded background. If the sidebar
follows _body_ section, it is placed side-by-side with the body text. Otherwise it occupies a whole
row.

If sidebar ToC is used, it is placed within the first sidebar in the page.

Sidebar section is indicated using `<sidebar />` element without any attributes.

```xml
...
<sections>
  ...
  <body />
  <sidebar />
  ...
</sections>
...
```


## Responsive design

Reflow skin is built on [Bootstrap][bootstrap] and provides responsive design features out of the
box. The page layouts are rearranged for smaller screens to provide a good viewing experience.

[bootstrap]: http://getbootstrap.com
