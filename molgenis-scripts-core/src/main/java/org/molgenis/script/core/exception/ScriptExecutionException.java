package org.molgenis.script.core.exception;

import java.text.MessageFormat;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.molgenis.data.i18n.LanguageServiceHolder.getLanguageService;

/**
 * Wraps exceptions that can occur during the execution of a script.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class ScriptExecutionException extends ScriptException
{
	private static final String ERROR_CODE = "SC04";

	private final String causeMessage;

	public ScriptExecutionException(String causeMessage)
	{
		super(ERROR_CODE);
		this.causeMessage = requireNonNull(causeMessage);
	}

	public ScriptExecutionException(Throwable cause)
	{
		super(ERROR_CODE, cause);
		this.causeMessage = cause.getLocalizedMessage();
	}

	@Override
	public String getMessage()
	{
		return format("cause:%s", causeMessage);
	}

	@Override
	public String getLocalizedMessage()
	{
		return getLanguageService().map(languageService ->
		{
			String format = languageService.getString(ERROR_CODE);
			return MessageFormat.format(format, causeMessage);
		}).orElse(super.getLocalizedMessage());
	}
}
