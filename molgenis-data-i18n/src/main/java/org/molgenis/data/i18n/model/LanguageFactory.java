package org.molgenis.data.i18n.model;

import org.molgenis.data.AbstractSystemEntityFactory;
import org.molgenis.data.populate.EntityPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LanguageFactory extends AbstractSystemEntityFactory<Language, LanguageMetaData, String>
{
	@Autowired
	LanguageFactory(LanguageMetaData languageMetaData, EntityPopulator entityPopulator)
	{
		super(Language.class, languageMetaData, entityPopulator);
	}

	/**
	 * Creates a language with the given code and name
	 *
	 * @param code language code, e.g. "en"
	 * @param name language name, e.g. "English"
	 * @param active language active, e.g "true"
	 * @return new language
	 */
	public Language create(String code, String name, boolean active)
	{
		Language language = super.create(code);
		language.setName(name);
		language.setActive(active);
		return language;
	}
}
