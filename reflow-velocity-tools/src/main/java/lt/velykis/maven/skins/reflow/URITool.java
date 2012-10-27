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

import java.net.URI;

import org.apache.maven.doxia.site.decoration.inheritance.URIPathDescriptor;
import org.apache.velocity.tools.config.DefaultKey;

/**
 * An Apache Velocity tool that provides utility methods to work with URIs/URLs and links.
 * 
 * @author Andrius Velykis
 * @since 1.0
 */
@DefaultKey("uriTool")
public class URITool {
	
	/**
	 * Resolves the link as relative to the base URI.
	 * <p>
	 * Relativizes only absolute links, if the link has the same scheme, host and port as
	 * the base, it is made into a relative link as viewed from the base.
	 * </p>
	 * <p>
	 * This is the same method that's used to relativize project links in Maven site.
	 * </p>
	 * 
	 * @param baseUri
	 *            URI that will serve as the base to calculate the relative one
	 * @param link
	 *            The link to relativize (make it relative to the base URI if possible)
	 * @return the relative link, if calculated, or the original link if not.
	 * @since 1.0
	 */
	public static String relativizeLink(final String baseUri, final String link) {
		// taken from org.apache.maven.doxia.site.decoration.inheritance.DecorationModelInheritanceAssembler

		if (link == null || baseUri == null) {
			return link;
		}

		// this shouldn't be necessary, just to swallow malformed hrefs
		try {
			
			final URIPathDescriptor path = new URIPathDescriptor(baseUri, link);
			return path.relativizeLink().toString();
			
		} catch (IllegalArgumentException e) {
			return link;
		}
	}
	
	public static URI toURI(final String uri) {
		return URI.create(uri);
	}
}
