package org.xacml4j.spring;

/*
 * #%L
 * Xacml4J Spring 3.x Support Module
 * %%
 * Copyright (C) 2009 - 2014 Xacml4J.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class FunctionProvidersDefinitionParser extends AbstractBeanDefinitionParser
{

	@Override
	protected AbstractBeanDefinition parseInternal(Element element,
			ParserContext parserContext) {
		BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(FunctionProvidersFactoryBean.class);
		List<Element> childElements = DomUtils.getChildElementsByTagName(
				element, "FunctionProvider");
	      if (childElements != null && childElements.size() > 0) {
	         parseChildComponents(childElements, factory);
	      }
	    return factory.getBeanDefinition();
	}

	private static BeanDefinitionBuilder parseComponent(Element element)
	{
		BeanDefinitionBuilder component = BeanDefinitionBuilder.rootBeanDefinition(FunctionProvider.class);
		String clazz = element.getAttribute("class");
		if(StringUtils.hasText(clazz)){
	    	  component.addPropertyValue("providerClass", clazz);
		}
	    String ref = element.getAttribute("ref");
	    if(StringUtils.hasText(ref)){
	    	  component.addPropertyReference("providerInstance", ref);
	    }
	    return component;
	}

	private static void parseChildComponents(List<Element> childElements, BeanDefinitionBuilder factory) {
		ManagedList<BeanDefinition> children = new ManagedList<BeanDefinition>(childElements.size());
		for (Element childElement : childElements) {
			BeanDefinitionBuilder child = parseComponent(childElement);
			children.add(child.getBeanDefinition());
		}
		factory.addPropertyValue("providers", children);
	}
}
