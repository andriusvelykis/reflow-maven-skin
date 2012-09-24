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

import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.generic.SafeConfig;
import org.apache.velocity.tools.generic.ValueParser;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * An Apache Velocity tool that simplifies retrieval of custom configuration values for a
 * Maven Site.
 * <p>
 * The tool is configured to access Maven site configuration of a skin inside {@code <custom>}
 * element of site descriptor. It supports global properties (defined at skin level) and per-page
 * properties (defined in {@code <page><mypage>} element). The per-page properties override the
 * global ones.
 * </p>
 * <p>
 * A sample configuration would be like that:
 * </p>
 * 
 * <pre>
 * {@code
 * <custom>
 *   <reflowSkin>
 *     <prop1>value1</prop1>
 *     <prop2>
 *       <prop21>value2</prop21>
 *     </prop2>
 *     <pages>
 *       <mypage project="myproject">
 *         <prop1>override value1</prop1>
 *       </mypage>
 *     </pages>
 *   </reflowSkin>
 * </custom>
 * }
 * </pre>
 * <p>
 * To get the value of {@code prop1}, one would simply use {@code $config.prop1}. This would return
 * "override value1". Then {@code $config.prop2} would return "value2" - the global value.
 * </p>
 * <p>
 * The tool allows querying the value easily, falling back from page to global configuration to
 * {@code null}, if none is available. It also provides convenience accessors for common values.
 * </p>
 * <p>
 * Note
 * </p>
 * 
 * @author Andrius Velykis
 * @since 1.0
 */
@DefaultKey("config")
public class SkinConfigTool extends SafeConfig {

	public static final String DEFAULT_KEY = "config";

	/** By default use Reflow skin configuration tag */
	public static final String SKIN_KEY = "reflowSkin";

	private String key = DEFAULT_KEY;
	private String skinKey = SKIN_KEY;

	/* Create dummy nodes to avoid null checks */
	private Xpp3Dom globalProperties = new Xpp3Dom("");
	private Xpp3Dom pageProperties = new Xpp3Dom("");
	private String namespace = "";

	/**
	 * {@inheritDoc}
	 * 
	 * @see SafeConfig#configure(ValueParser)
	 */
	@Override
	protected void configure(ValueParser values) {
		String altkey = values.getString("key");
		if (altkey != null) {
			setKey(altkey);
		}

		// allow changing skin key in the configuration
		String altSkinKey = values.getString("skinKey");
		if (altSkinKey != null) {
			this.skinKey = altSkinKey;
		}

		// retrieve the decoration model from Velocity context
		Object velocityContext = values.get("velocityContext");

		if (!(velocityContext instanceof ToolContext)) {
			return;
		}

		ToolContext ctxt = (ToolContext) velocityContext;
		Object decorationObj = ctxt.get("decoration");

		if (!(decorationObj instanceof DecorationModel)) {
			return;
		}

		DecorationModel decoration = (DecorationModel) decorationObj;
		Object customObj = decoration.getCustom();

		if (!(customObj instanceof Xpp3Dom)) {
			return;
		}

		// Now that we have the custom node, get the global properties
		// under the skin tag
		Xpp3Dom customNode = (Xpp3Dom) customObj;
		Xpp3Dom skinNode = customNode.getChild(skinKey);
		String namespaceKey = ":" + skinKey;
		
		if (skinNode == null) {
			// try searching with any namespace
			for (Xpp3Dom child : customNode.getChildren()) {
				if (child.getName().endsWith(namespaceKey)) {
					skinNode = child;
					break;
				}
			}
		}
		
		if (skinNode != null) {
			globalProperties = skinNode;
			
			if (skinNode.getName().endsWith(namespaceKey)) {
				// extract the namespace (including the colon)
				namespace = skinNode.getName().substring(0, skinNode.getName().length() - namespaceKey.length() + 1);
			}

			// for page properites, retrieve the file name and drop the `.html`
			// extension - this will be used, i.e. `index` instead of `index.html`
			Xpp3Dom pagesNode = getChild(skinNode, "pages");
			Object alignedFileObj = ctxt.get("alignedFileName");
			if (pagesNode != null && (alignedFileObj instanceof String)) {

				String alignedFile = (String) alignedFileObj;

				// drop the extension
				int lastDot = alignedFile.lastIndexOf(".");
				if (lastDot >= 0) {
					alignedFile = alignedFile.substring(0, lastDot);
				}

				String artifactId = null;
				Object projectObj = ctxt.get("project");
				if (projectObj instanceof MavenProject) {
					MavenProject project = (MavenProject) projectObj;
					artifactId = project.getArtifactId();
				}

				// Get the page for the file
				Xpp3Dom page = getChild(pagesNode, alignedFile);

				// Now check if the project artifact ID is set, and if so, if it matches the
				// current project. This allows preventing accidental reuse of parent page
				// configs in children modules
				if (page != null && artifactId != null) {
					String pageProject = page.getAttribute("project");
					if (!artifactId.equals(pageProject)) {
						// project ID is different - do not use the config
						page = null;
					}
				}

				if (page != null) {
					pageProperties = page;
				}
			}
		}
	}
	
	/**
	 * Retrieves the child node. Tests both default name and with namespace.
	 * 
	 * @param parentNode
	 * @param name
	 * @return
	 */
	private Xpp3Dom getChild(Xpp3Dom parentNode, String name) {
		Xpp3Dom child = parentNode.getChild(name);
		if (child != null) {
			return child;
		}
		
		return parentNode.getChild(namespace + name);
	}

	/**
	 * Sets the key under which this tool has been configured.
	 * 
	 * @since 1.0
	 */
	protected void setKey(String key) {
		if (key == null) {
			throw new NullPointerException("SkinConfigTool key cannot be null");
		}
		this.key = key;
	}

	/**
	 * Should return the key under which this tool has been configured. The default is `config`.
	 * 
	 * @since 1.0
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Default accessor for config properties. Instead of using {@code $config.get("myproperty")},
	 * one can utilise Velocity fallback onto the default getter and use {@code $config.myproperty}.
	 * 
	 * @param property
	 *            the property of interest
	 * @return configuration node if found in the following sequence:
	 *         <ol>
	 *         <li>In page configuration</li>
	 *         <li>In global configuration</li>
	 *         <li>{@code null} otherwise</li>
	 *         </ol>
	 * @since 1.0
	 */
	public Xpp3Dom get(String property) {

		// first try page properties
		Xpp3Dom propNode = getChild(pageProperties, property);
		if (propNode == null) {
			// try global
			propNode = getChild(globalProperties, property);
		}

		return propNode;
	}

	/**
	 * Retrieves the text value of the given {@code property}, e.g. as in
	 * {@code <myprop>value</myprop>}.
	 * 
	 * @param property
	 *            the property of interest
	 * @return the configuration value if found in page or globally, {@code null} otherwise.
	 * @see #get(String)
	 * @since 1.0
	 */
	public String value(String property) {

		Xpp3Dom propNode = get(property);

		if (propNode == null) {
			// not found
			return null;
		}

		return propNode.getValue();
	}

	/**
	 * A convenience method to check if the value of the {@code property} is {@code "true"}.
	 * 
	 * @param property
	 *            the property of interest
	 * @return {@code true} if the configuration value is set either in page or globally, and is
	 *         equal to {@code "true"}.
	 * @see #get(String)
	 * @since 1.0
	 */
	public boolean is(String property) {
		return "true".equals(value(property));
	}

	/**
	 * A convenience method to check if the value of the {@code property} is {@code "false"}. Useful
	 * for properties that are enabled by default - checks if the property is set to {@code "false"}
	 * explicitly.
	 * 
	 * @param property
	 *            the property of interest
	 * @return {@code true} if the configuration value is set either in page or globally, and is
	 *         equal to {@code "false"}. Note that this will return {@code false} if property is not
	 *         set at all.
	 * @see #get(String)
	 * @since 1.0
	 */
	public boolean not(String property) {
		return "false".equals(value(property));
	}

	/**
	 * A convenience method to check if the {@code property} is set to a specific value.
	 * 
	 * @param property
	 *            the property of interest
	 * @param value
	 *            the property value to check
	 * @return {@code true} if the configuration value is set either in page or globally, and is
	 *         equal to {@code value}.
	 * @see #get(String)
	 * @since 1.0
	 */
	public boolean isValue(String property, String value) {
		return value != null && value.equals(value(property));
	}

}
