/* 
 * Copyright 2012 Andrius Velykis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lt.velykis.maven.skins.reflow;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.velocity.tools.config.DefaultKey;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 * An Apache Velocity tool that provides utility methods to manipulate HTML code using
 * <a href="http://jsoup.org/">jsoup</a> HTML5 parser.
 * <p>
 * The methods utilise <a href="http://jsoup.org/cookbook/extracting-data/selector-syntax">CSS
 * selectors</a> to refer to specific elements for manipulation.
 * </p>
 * 
 * @author Andrius Velykis
 * @since 1.0
 * @see <a href="http://jsoup.org/">jsoup HTML parser</a>
 * @see <a href="http://jsoup.org/cookbook/extracting-data/selector-syntax">jsoup CSS selectors</a>
 */
@DefaultKey("htmlTool")
public class HtmlTool {
	
	/** A list of all HTML heading classes (h1-6) */
	private static List<String> HEADINGS = Collections.unmodifiableList(
			Arrays.asList("h1", "h2", "h3", "h4", "h5", "h6"));
	
	
	
	/** Enum indicating separator handling strategy for document partitioning. */
	public enum JoinSeparator {
		/**
		 * Keep separators at the start of partitions. The first partition will not have a
		 * separator.
		 */
		AFTER,
		/**
		 * Keep separators at the end of partitions. The last partition will not have a separator.
		 */
		BEFORE,
		/** Drop separators altogether. */
		NO
	}

	/**
	 * Splits the given HTML content into partitions based on the given separator selector. The
	 * separators themselves are dropped from the results.
	 * 
	 * @param content
	 *            HTML content to split
	 * @param separatorCssSelector
	 *            CSS selector for separators.
	 * @return a list of HTML partitions split on separator locations, but without the separators.
	 * @since 1.0
	 * @see #split(String, String, JoinSeparator)
	 */
	public static List<String> split(String content, String separatorCssSelector) {
		return split(content, separatorCssSelector, JoinSeparator.NO);
	}

	/**
	 * Splits the given HTML content into partitions based on the given separator selector. The
	 * separators are kept as first elements of the partitions.
	 * <p>
	 * Note that the first part is removed if the split was successful. This is because the first
	 * part does not include the separator.
	 * </p>
	 * 
	 * @param content
	 *            HTML content to split
	 * @param separatorCssSelector
	 *            CSS selector for separators
	 * @return a list of HTML partitions split on separator locations (except the first one), with
	 *         separators at the beginning of each partition
	 * @since 1.0
	 * @see #split(String, String, JoinSeparator)
	 */
	public static List<String> splitOnStarts(String content, String separatorCssSelector) {

		List<String> result = split(content, separatorCssSelector, JoinSeparator.AFTER);

		if (result == null || result.size() <= 1) {
			// no result or just one part - return what we have
			return result;
		}

		// otherwise, drop the first part - the first split will be the first 'start'
		// e.g. if we split on headings, the first part will contain everything
		// before the first heading.
		return result.subList(1, result.size());
	}

	/**
	 * Splits the given HTML content into partitions based on the given separator selector. The
	 * separators are either dropped or joined with before/after depending on the indicated
	 * separator strategy.
	 * 
	 * @param content
	 *            HTML content to split
	 * @param separatorCssSelector
	 *            CSS selector for separators
	 * @param separatorStrategy
	 *            strategy to drop or keep separators, one of "after", "before" or "no"
	 * @return a list of HTML partitions split on separator locations.
	 * @since 1.0
	 * @see #split(String, String, JoinSeparator)
	 */
	public static List<String> split(String content, String separatorCssSelector,
			String separatorStrategy) {

		JoinSeparator sepStrategy;
		if ("before".equals(separatorStrategy)) {
			sepStrategy = JoinSeparator.BEFORE;
		} else if ("after".equals(separatorStrategy)) {
			sepStrategy = JoinSeparator.AFTER;
		} else {
			sepStrategy = JoinSeparator.NO;
		}

		return split(content, separatorCssSelector, sepStrategy);
	}

	/**
	 * Splits the given HTML content into partitions based on the given separator selector.The
	 * separators are either dropped or joined with before/after depending on the indicated
	 * separator strategy.
	 * <p>
	 * Note that splitting algorithm tries to resolve nested elements so that returned partitions
	 * are self-contained HTML elements. The nesting is normally contained within the first
	 * applicable partition.
	 * </p>
	 * 
	 * @param content
	 *            HTML content to split
	 * @param separatorCssSelector
	 *            CSS selector for separators
	 * @param separatorStrategy
	 *            strategy to drop or keep separators
	 * @return a list of HTML partitions split on separator locations. If no splitting occurs,
	 *         returns the original content as the single element of the list
	 * @since 1.0
	 */
	public static List<String> split(String content, String separatorCssSelector,
			JoinSeparator separatorStrategy) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();

		List<Element> separators = body.select(separatorCssSelector);
		if (separators.size() > 0) {
			List<List<Element>> partitions = split(separators, separatorStrategy, body);

			List<String> sectionHtml = new ArrayList<String>();

			for (List<Element> partition : partitions) {
				sectionHtml.add(outerHtml(partition));
			}

			return sectionHtml;
		} else {
			// nothing to split
			return Collections.singletonList(content);
		}
	}

	/**
	 * Recursively splits the {@code parent} element based on the given {@code separators}. If a
	 * separator is encountered in the parent, it is split on that position. The outstanding nested
	 * elements go with the first of the partitions in each case.
	 * 
	 * @param separators
	 * @param separatorStrategy
	 * @param parent
	 * @return list of partitions (as lists of root elements for each partition). Partition can be
	 *         an empty list, e.g. if the separator is at the start of the content.
	 */
	private static List<List<Element>> split(Collection<Element> separators,
			JoinSeparator separatorStrategy, Element parent) {

		List<List<Element>> partitions = new LinkedList<List<Element>>();

		for (Element child : parent.children()) {

			if (separators.contains(child)) {
				// split here and do not go deeper

				// first ensure there was a partition before
				// otherwise the split is not recognised on an outer level
				getLastPartition(partitions);

				if (separatorStrategy == JoinSeparator.BEFORE) {
					// add to the last partition
					getLastPartition(partitions).add(child);
				}

				// add an empty new partition
				List<Element> newPartition = new LinkedList<Element>();
				partitions.add(newPartition);

				if (separatorStrategy == JoinSeparator.AFTER) {
					// add to the new partition
					newPartition.add(child);
				}

			} else {
				// go deeper
				List<List<Element>> childPartitions = split(separators, separatorStrategy, child);

				// add the child to the last partition
				getLastPartition(partitions).add(child);

				if (childPartitions.size() > 1) {
					// more than one partition:
					// only keep the first partition elements in the child
					// so for all other partitions, remove them from their parents

					List<Element> allChildren = child.children();
					List<Element> firstPartition = childPartitions.get(0);

					allChildren.removeAll(firstPartition);
					for (Element removeChild : allChildren) {
						removeChild.remove();
					}

					// add the remaining partitions
					for (List<Element> nextPartition : childPartitions.subList(1, childPartitions.size())) {
						partitions.add(nextPartition);
					}
				}
			}
		}

		return partitions;
	}

	/**
	 * Retrieves the last partition (as list of elements) or creates a new one if there was none
	 * before.
	 * 
	 * @param partitions
	 * @return
	 */
	private static List<Element> getLastPartition(List<List<Element>> partitions) {
		if (partitions.isEmpty()) {
			List<Element> newPartition = new LinkedList<Element>();
			partitions.add(newPartition);
			return newPartition;
		} else {
			return partitions.get(partitions.size() - 1);
		}
	}

	/**
	 * Outputs the list of partition root elements to HTML.
	 * 
	 * @param elements
	 * @return
	 */
	private static String outerHtml(List<Element> elements) {

		switch (elements.size()) {
		case 0:
			return "";
		case 1:
			return elements.get(0).outerHtml();
		default: {
			// more than one element
			// wrap into <div> which we will remove afterwards
			Element root = new Element(Tag.valueOf("div"), "");
			for (Element elem : elements) {
				root.appendChild(elem);
			}

			return root.html();
		}
		}
	}
	
	
	
	/**
	 * Reorders elements in HTML content so that selected elements are found at the top of the
	 * content. Can be limited to a certain amount, e.g. to bring just the first of selected
	 * elements to the top.
	 * 
	 * @param content
	 *            HTML content to reorder
	 * @param selector
	 *            CSS selector for elements to bring to top of the content
	 * @param amount
	 *            Maximum number of elements to reorder
	 * @return HTML content with reordered elements, or the original content if no such elements
	 *         found.
	 * @since 1.0
	 */
	public static String reorderToTop(String content, String selector, int amount) {

		// extract the elements and then prepend them to the remaining body
		List<Element> extracted = extractElements(content, selector, amount);

		if (extracted.size() > 1) {

			Element body = extracted.get(0);
			List<Element> elements = extracted.subList(1, extracted.size());

			// now prepend extracted elements to the body (in backwards to preserve original order)
			for (int index = elements.size() - 1; index >= 0; index--) {
				body.prependChild(elements.get(index));
			}

			return body.html();
		} else {
			// nothing to reorder
			return content;
		}
	}
	
	/**
	 * Extracts elements from the HTML content.
	 * 
	 * @param content
	 * @param selector
	 * @param amount
	 * @return the remainder and a list of extracted elements. The main body (remainder after
	 *         extraction) is always returned as the first element of the list.
	 */
	private static List<Element> extractElements(String content, String selector, int amount) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();

		List<Element> elements = body.select(selector);
		if (elements.size() > 0) {

			elements = filterParents(elements);

			if (amount >= 0) {
				// limit to the indicated amount
				elements = elements.subList(0, Math.min(amount, elements.size()));
			}

			// remove all from their parents
			for (Element element : elements) {
				element.remove();
			}
		}

		List<Element> results = new ArrayList<Element>();
		// first element is the body
		results.add(body);
		results.addAll(elements);
		return results;
	}
	
	/**
	 * Filters the list of elements to only contain parent elements. This is to avoid both parent
	 * and child being in the list of elements.
	 * 
	 * @param elements
	 * @return
	 */
	private static List<Element> filterParents(List<Element> elements) {
		List<Element> filtered = new ArrayList<Element>();
		for (Element element : elements) {
			// get the intersection of parents and selected elements
			List<Element> parentsInter = element.parents();
			parentsInter.retainAll(elements);
			if (parentsInter.isEmpty()) {
				// no intersection - element's parents are not in the selected list
				filtered.add(element);
			}
		}

		return filtered;
	}

	/**
	 * Extracts HTML elements from the main HTML content. The result consists of the extracted HTML
	 * elements and the remainder of HTML content, with these elements removed. Can be limited to a
	 * certain amount, e.g. to extract just the first of selected elements.
	 * 
	 * @param content
	 *            HTML content to extract elements from
	 * @param selector
	 *            CSS selector for elements to extract
	 * @param amount
	 *            Maximum number of elements to extract
	 * @return HTML content of the extracted elements together with the remainder of the original
	 *         content. If no elements are found, the remainder contains the original content.
	 * @since 1.0
	 */
	public static ExtractResult extract(String content, String selector, int amount) {

		List<Element> extracted = extractElements(content, selector, amount);

		if (extracted.size() > 1) {

			// first element is the remaining body, the rest are extracted
			Element body = extracted.get(0);
			List<Element> elements = extracted.subList(1, extracted.size());

			// convert to HTML
			List<String> elementStr = new ArrayList<String>();
			for (Element el : elements) {
				elementStr.add(el.outerHtml());
			}

			return new DefaultExtractResult(elementStr, body.html());
		} else {
			// nothing to extract
			return new DefaultExtractResult(Collections.<String> emptyList(), content);
		}
	}
	
	/**
	 * A container to carry element extraction results. Contains the extracted element HTML
	 * code and the remainder of the body content with elements removed.
	 * 
	 * @author Andrius Velykis
	 * @since 1.0
	 */
	public static interface ExtractResult {
		
		/**
		 * Retrieves the extracted HTML elements.
		 * 
		 * @return List of HTML of extracted elements. Can be empty if no elements found.
		 */
		public List<String> getExtracted();

		/**
		 * Retrieves the content from which elements were extracted.
		 * 
		 * @return The HTML content with extracted elements removed.
		 */
		public String getRemainder();
	}
	
	private static class DefaultExtractResult implements ExtractResult {
		private final List<String> extracted;
		private final String remainder;
		
		public DefaultExtractResult(List<String> extracted, String remainder) {
			this.extracted = extracted;
			this.remainder = remainder;
		}
		
		@Override
		public List<String> getExtracted() {
			return Collections.unmodifiableList(extracted);
		}
		
		@Override
		public String getRemainder() {
			return remainder;
		}
	}
	
	
	/**
	 * Sets attribute to the given value on elements in HTML.
	 * 
	 * @param content
	 *            HTML content to set attributes on
	 * @param selector
	 *            CSS selector for elements to modify
	 * @param attributeKey
	 *            Attribute name
	 * @param value
	 *            Attribute value
	 * @return HTML content with modified elements. If no elements are found, the original content
	 *         is returned.
	 * @since 1.0
	 */
	public static String setAttr(String content, String selector, String attributeKey, String value) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		List<Element> elements = body.select(selector);
		if (elements.size() > 0) {
			
			for (Element element : elements) {
				element.attr(attributeKey, value);
			} 
			
			return body.html();
		} else {
			// nothing to update
			return content;
		}
	}
	
	/**
	 * Adds given class names to the elements in HTML.
	 * 
	 * @param content
	 *            HTML content to modify
	 * @param selector
	 *            CSS selector for elements to add classes to
	 * @param classNames
	 *            Names of classes to add to the selected elements
	 * @return HTML content with modified elements. If no elements are found, the original content
	 *         is returned.
	 * @since 1.0
	 */
	public static String addClass(String content, String selector, List<String> classNames) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		List<Element> elements = body.select(selector);
		if (elements.size() > 0) {
			
			for (Element element : elements) {
				for (String className : classNames) {
					element.addClass(className);
				}
			} 
			
			return body.html();
		} else {
			// nothing to update
			return content;
		}
	}
	
	/**
	 * Adds given class to the elements in HTML.
	 * 
	 * @param content
	 *            HTML content to modify
	 * @param selector
	 *            CSS selector for elements to add the class to
	 * @param className
	 *            Name of class to add to the selected elements
	 * @return HTML content with modified elements. If no elements are found, the original content
	 *         is returned.
	 * @since 1.0
	 */
	public static String addClass(String content, String selector, String className) {
		return addClass(content, selector, Collections.singletonList(className));
	}
	
	/**
	 * Removes elements from HTML.
	 * 
	 * @param content
	 *            HTML content to modify
	 * @param selector
	 *            CSS selector for elements to remove
	 * @return HTML content with removed elements. If no elements are found, the original content is
	 *         returned.
	 * @since 1.0
	 */
	public static String remove(String content, String selector) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		List<Element> elements = body.select(selector);
		if (elements.size() > 0) {
			for (Element element : elements) {
				element.remove();
			}
			
			return body.html();
		} else {
			// nothing changed
			return content;
		}
	}
	
	/**
	 * Retrieves text content of the selected elements in HTML. Renders the element's text as it
	 * would be displayed on the web page (including its children).
	 * 
	 * @param content
	 *            HTML content with the elements
	 * @param selector
	 *            CSS selector for elements to extract contents
	 * @return A list of element texts as rendered to display. Empty list if no elements are found.
	 * @since 1.0
	 */
	public static List<String> text(String content, String selector) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		List<Element> elements = body.select(selector);
		List<String> texts = new ArrayList<String>();
		
		for (Element element : elements) {
			texts.add(element.text());
		}
		
		return texts;
	}
	
	/**
	 * Transforms the given HTML content by moving anchor ({@code <a name="myheading">}) names to
	 * IDs for heading elements.
	 * <p>
	 * The anchors are used to indicate positions within a HTML page. In HTML5, however, the
	 * {@code name} attribute is no longer supported on {@code <a>) tag. The positions within pages
	 * are indicated using {@code id} attribute instead, e.g. {@code <h1 id="myheading">}.
	 * </p>
	 * <p>
	 * The method finds anchors inside, immediately before or after the heading tags and uses their
	 * name as heading {@code id} instead. The anchors themselves are removed.
	 * </p>
	 * 
	 * @param content
	 *            HTML content to modify
	 * @return HTML content with modified elements. Anchor names are used for adjacent headings, and
	 *         anchor tags are removed. If no elements are found, the original content is returned.
	 * @since 1.0
	 */
	public static String headingAnchorToId(String content) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		// selectors for headings without IDs
		List<String> headNoIds = concat(HEADINGS, ":not([id])", true);
		
		// selector for anchor with name attribute only
		String nameA = "a[name]:not([href])";
		
		// select all headings that have inner named anchor
		List<Element> headingsInnerA = body.select(StringUtil.join(
				concat(headNoIds, ":has(" + nameA + ")", true), ", "));
		
		boolean modified = false;
		for (Element heading : headingsInnerA) {
			List<Element> anchors = heading.select(nameA);
			// take first
			if (!anchors.isEmpty()) {
				anchorToId(heading, anchors.get(0));
				modified = true;
			}
		}
		
		// select all headings that have a preceding named anchor
		List<Element> headingsPreA = body.select(StringUtil.join(
				concat(headNoIds, nameA + " + ", false), ", "));
		
		for (Element heading : headingsPreA) {
			Element anchor = heading.previousElementSibling();
			if (anchor != null) {
				anchorToId(heading, anchor);
				modified = true;
			}
		}
		
		// select all headings that are followed by a named anchor
		// no selector available for that, so first select the anchors
		// then retrieve the headings
		List<Element> anchorsPreH = body.select(StringUtil.join(
				concat(headNoIds, " + " + nameA, true), ", "));
		
		for (Element anchor : anchorsPreH) {
			Element heading = anchor.previousElementSibling();
			if (heading != null) {
				anchorToId(heading, anchor);
				modified = true;
			}
		}
		
		if (modified) {
			return body.html();
		} else {
			// nothing to update
			return content;
		}
	}
	
	/**
	 * Moves anchor name to heading id, if one does not exist. Removes the anchor.
	 * 
	 * @param heading
	 * @param anchor
	 */
	private static void anchorToId(Element heading, Element anchor) {
		
		if ("a".equals(anchor.tagName()) && heading.id().isEmpty()) {
			String aName = anchor.attr("name");
			if (!aName.isEmpty()) {
				// set the anchor name as heading ID
				heading.attr("id", aName);
				
				// remove the anchor
				anchor.remove();
			}
		}
	}
	
	
	/**
	 * Utility method to concatenate a String to a list of Strings. The text can be either appended
	 * or prepended.
	 * 
	 * @param elements
	 *            list of elements to append/prepend the text to
	 * @param text
	 *            the given text to append/prepend
	 * @param append
	 *            if {@code true}, text will be appended to the elements. If {@code false}, it will
	 *            be prepended
	 * @return list of elements with the text appended/prepended
	 * @since 1.0
	 */
	public static List<String> concat(List<String> elements, String text, boolean append) {
		List<String> concats = new ArrayList<String>();
		
		for (String element : elements) {
			concats.add(append ? element + text : text + element);
		}
		
		return concats;
	}
	
	
	/**
	 * Transforms the given HTML content by adding IDs to all heading elements ({@code h1-6}) that
	 * do not have one.
	 * <p>
	 * IDs on heading elements are used to indicate positions within a HTML page in HTML5. If a
	 * heading tag without an {@code id} is found, its "slug" is generated automatically based on
	 * the heading contents and used as the ID.
	 * </p>
	 * <p>
	 * Note that the algorithm also modifies existing IDs that have symbols not allowed in CSS
	 * selectors, e.g. ":", ".", etc. The symbols are removed.
	 * </p>
	 * 
	 * @param content
	 *            HTML content to modify
	 * @return HTML content with all heading elements having {@code id} attributes. If all headings
	 *         were with IDs already, the original content is returned.
	 * @since 1.0
	 */
	public static String ensureHeadingIds(String content, String idSeparator) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		// first find all existing IDs (to avoid generating duplicates)
		List<Element> idElems = body.select("*[id]");
		Set<String> ids = new HashSet<String>();
		boolean modified = false;
		for (Element idElem : idElems) {
			
			// fix all existing IDs - remove colon and other symbols which mess up jQuery
			String id = idElem.id();
			idElem.attr("id", adaptSlug(id, idSeparator));
			modified = true;
			
			ids.add(idElem.id());
		}
		
		List<String> headNoIds = concat(HEADINGS, ":not([id])", true);
		
		// select all headings that do not have an ID
		List<Element> headingsNoId = body.select(StringUtil.join(headNoIds, ", "));
		
		if (!headingsNoId.isEmpty() || modified) {
			for (Element heading : headingsNoId) {
				
				String headingText = heading.text();
				String headingSlug = slug(headingText, idSeparator);
				// also limit slug to 50 symbols
				if (headingSlug.length() > 50) {
					headingSlug = headingSlug.substring(0, 50);
				}
				String headingId = generateUniqueId(ids, headingSlug);
				
				heading.attr("id", headingId);
			}
			
			return body.html();
		} else {
			// nothing to update
			return content;
		}
	}
	
	/**
	 * Generated a unique ID within the given set of IDs. Appends an incrementing number for
	 * duplicates.
	 * 
	 * @param ids
	 * @param idBase
	 * @return
	 */
	private static String generateUniqueId(Set<String> ids, String idBase) {
		String id = idBase;
		int counter = 1;
		while (ids.contains(id)) {
			id = idBase + String.valueOf(counter++);
		}
		
		// put the newly generated one into the set
		ids.add(id);
		return id;
	}
	
	/**
	 * Fixes table heads: wraps rows with {@code <th>} (table heading) elements into {@code <thead>}
	 * element if they are currently in {@code <tbody>}.
	 * 
	 * @param content
	 *            HTML content to modify
	 * @return HTML content with all table heads fixed. If all heads were correct, the original
	 *         content is returned.
	 * @since 1.0
	 */
	public static String fixTableHeads(String content) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();
		
		// select rows with <th> tags within <tbody>
		List<Element> tableHeadRows = body.select("table > tbody > tr:has(th)");
		if (tableHeadRows.size() > 0) {
			for (Element row : tableHeadRows) {
				
				// get the row's table
				Element table = row.parent().parent();
				
				// remove row from its original position
				row.remove();
				
				// create table header element with the row
				Element thead = new Element(Tag.valueOf("thead"), "");
				thead.appendChild(row);
				// add at the beginning of the table
				table.prependChild(thead);
			}
			
			return body.html();
		} else {
			// nothing changed
			return content;
		}
	}
	
	
	private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
	
	/**
	 * Creates a slug (latin text with no whitespace or other symbols) for a longer text (i.e. to
	 * use in URLs).
	 * 
	 * @param input
	 *            text to generate the slug from
	 * @param separator
	 *            separator for whitespace replacement
	 * @return the slug of the given text that contains alphanumeric symbols and separator only
	 * @since 1.0
	 * @see <a href="http://www.codecodex.com/wiki/Generate_a_url_slug">http://www.codecodex.com/wiki/Generate_a_url_slug</a>
	 */
	public static String slug(String input, String separator) {
		String slug = adaptSlug(input, separator);
		return slug.toLowerCase(Locale.ENGLISH);
	}
	
	/**
	 * Creates a slug (latin text with no whitespace or other symbols) for a longer text (i.e. to
	 * use in URLs). Uses "-" as a whitespace separator.
	 * 
	 * @param input
	 *            text to generate the slug from
	 * @return the slug of the given text that contains alphanumeric symbols and "-" only
	 * @since 1.0
	 */
	public static String slug(String input) {
		return slug(input, "-");
	}
	
	/**
	 * Creates a slug but does not change capitalization.
	 * 
	 * @param input
	 * @param separator
	 * @return
	 */
	private static String adaptSlug(String input, String separator) {
		String nowhitespace = WHITESPACE.matcher(input).replaceAll(separator);
		String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
		return NONLATIN.matcher(normalized).replaceAll("");
	}
	
	
	/**
	 * Reads all headings in the given HTML content as a hierarchy. Subsequent smaller headings are
	 * nested within bigger ones, e.g. {@code <h2>} is nested under preceding {@code <h1>}.
	 * <p>
	 * Only headings with IDs are included in the hierarchy. The result elements contain ID and
	 * heading text for each heading. The hierarchy is useful to generate a Table of Contents for a
	 * page.
	 * </p>
	 * 
	 * @param content
	 *            HTML content to extract heading hierarchy from
	 * @return a list of top-level heading items (with id and text). The remaining headings are
	 *         nested within these top-level items. Empty list if no headings are in the content.
	 * @since 1.0
	 */
	public static List<? extends IdElement> headingTree(String content) {

		Document doc = Jsoup.parseBodyFragment(content);
		Element body = doc.body();

		List<String> headIds = concat(HEADINGS, "[id]", true);

		// select all headings that have an ID
		List<Element> headings = body.select(StringUtil.join(headIds, ", "));

		List<HeadingItem> headingItems = new ArrayList<HeadingItem>();
		for (Element heading : headings) {
			headingItems.add(new HeadingItem(heading.id(), heading.text(), headingIndex(heading)));
		}

		List<HeadingItem> topHeadings = new ArrayList<HeadingItem>();
		Stack<HeadingItem> parentHeadings = new Stack<HeadingItem>();

		for (HeadingItem heading : headingItems) {

			while (!parentHeadings.isEmpty()
					&& parentHeadings.peek().headingIndex >= heading.headingIndex) {
				parentHeadings.pop();
			}

			if (parentHeadings.isEmpty()) {
				// top level heading - no parents
				topHeadings.add(heading);
			} else {
				// add to the children of topmost stack parent
				parentHeadings.peek().children.add(heading);
			}

			// push the heading onto stack
			parentHeadings.push(heading);
		}

		return topHeadings;
	}

	/**
	 * Retrieves numeric index of a heading.
	 * 
	 * @param element
	 * @return
	 */
	private static int headingIndex(Element element) {
		String tagName = element.tagName();
		if (tagName.startsWith("h")) {
			try {
				return Integer.parseInt(tagName.substring(1));
			} catch (Exception ex) {
				throw new IllegalArgumentException("Must be a header tag: " + tagName, ex);
			}
		} else {
			throw new IllegalArgumentException("Must be a header tag: " + tagName);
		}
	}

	private static class HeadingItem implements IdElement {
		private final String id;
		private final String text;
		private final int headingIndex;

		private final List<HeadingItem> children = new ArrayList<HeadingItem>();

		public HeadingItem(String id, String text, int headingIndex) {
			this.id = id;
			this.text = text;
			this.headingIndex = headingIndex;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public List<HeadingItem> getItems() {
			return Collections.unmodifiableList(children);
		}
	}

	/**
	 * Representation of a HTML element with ID and a text content. Other such elements can be
	 * nested within.
	 * 
	 * @author Andrius Velykis
	 * @since 1.0
	 */
	public interface IdElement {

		/**
		 * Retrieves the ID of the HTML element (attribute {@code id})
		 * 
		 * @return element {@code id} value
		 */
		public String getId();

		/**
		 * Retrieves the text contents of the HTML element (rendered for display)
		 * 
		 * @return text contents of the element
		 */
		public String getText();

		/**
		 * Retrieves the children of the HTML element (nested within the element)
		 * 
		 * @return nested items within the element
		 */
		public List<? extends IdElement> getItems();
	}
	
	
	/**
	 * A generic method to use jsoup parser on an arbitrary HTML body fragment. Allows writing
	 * HTML manipulations in the template without adding Java code to the class.
	 * 
	 * @param content
	 *            HTML content to parse
	 * @return the wrapper element for the parsed content (i.e. the body element as if the content
	 *         was body contents).
	 * @since 1.0
	 */
	public static Element parseBodyFragment(String content) {

		Document doc = Jsoup.parseBodyFragment(content);
		return doc.body();
	}
	
}
