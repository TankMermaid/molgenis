package org.molgenis.data.populate;

import static com.google.common.collect.Streams.stream;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.molgenis.data.util.MolgenisDateFormat.FAILED_TO_PARSE_ATTRIBUTE_AS_DATETIME_MESSAGE;
import static org.molgenis.data.util.MolgenisDateFormat.FAILED_TO_PARSE_ATTRIBUTE_AS_DATE_MESSAGE;
import static org.molgenis.data.util.MolgenisDateFormat.parseInstant;
import static org.molgenis.data.util.MolgenisDateFormat.parseLocalDate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityReferenceCreator;
import org.molgenis.data.meta.AttributeType;
import org.molgenis.data.meta.IllegalAttributeTypeException;
import org.molgenis.data.meta.model.Attribute;
import org.molgenis.data.meta.model.EntityType;
import org.molgenis.data.util.AttributeUtils;
import org.molgenis.util.ListEscapeUtils;
import org.molgenis.util.UnexpectedEnumException;
import org.springframework.stereotype.Component;

/** Populate entity values for attributes with default values */
@Component
public class DefaultValuePopulator {
  private final EntityReferenceCreator entityReferenceCreator;

  public DefaultValuePopulator(EntityReferenceCreator entityReferenceCreator) {
    this.entityReferenceCreator = requireNonNull(entityReferenceCreator);
  }

  /**
   * Populates an entity with default values
   *
   * @param entity populated entity
   */
  public void populate(Entity entity) {
    stream(entity.getEntityType().getAllAttributes())
        .filter(Attribute::hasDefaultValue)
        .forEach(attr -> populateDefaultValues(entity, attr));
  }

  private void populateDefaultValues(Entity entity, Attribute attr) {
    Object defaultValueAsString = getDefaultValue(attr);
    entity.set(attr.getName(), defaultValueAsString);
  }

  private Object getDefaultValue(Attribute attr) {
    return AttributeUtils.getDefaultTypedValue(attr, entityReferenceCreator);
  }

  private Object convertToTypedValue(Attribute attr, String valueAsString) {
    AttributeType attrType = attr.getDataType();
    switch (attrType) {
      case BOOL:
        return convertBool(attr, valueAsString);
      case CATEGORICAL:
      case FILE:
      case XREF:
        return convertRef(attr, valueAsString);
      case CATEGORICAL_MREF:
      case MREF:
      case ONE_TO_MANY:
        return convertMref(attr, valueAsString);
      case DATE:
        return convertDate(attr, valueAsString);
      case DATE_TIME:
        return convertDateTime(attr, valueAsString);
      case DECIMAL:
        return convertDecimal(valueAsString);
      case EMAIL:
      case ENUM:
      case HTML:
      case HYPERLINK:
      case SCRIPT:
      case STRING:
      case TEXT:
        return valueAsString;
      case INT:
        return convertInt(valueAsString);
      case LONG:
        return convertLong(valueAsString);
      case COMPOUND:
        throw new IllegalAttributeTypeException(attrType);
      default:
        throw new UnexpectedEnumException(attrType);
    }
  }

  private static Long convertLong(String valueAsString) {
    return Long.valueOf(valueAsString);
  }

  private static Integer convertInt(String valueAsString) {
    return Integer.valueOf(valueAsString);
  }

  private static Double convertDecimal(String valueAsString) {
    return Double.valueOf(valueAsString);
  }

  private static LocalDate convertDate(Attribute attr, String valueAsString) {
    try {
      return parseLocalDate(valueAsString);
    } catch (DateTimeParseException e) {
      throw new RuntimeException(
          format(FAILED_TO_PARSE_ATTRIBUTE_AS_DATE_MESSAGE, attr.getName(), valueAsString));
    }
  }

  private static Instant convertDateTime(Attribute attr, String valueAsString) {
    try {
      return parseInstant(valueAsString);
    } catch (DateTimeParseException e) {
      throw new RuntimeException(
          format(FAILED_TO_PARSE_ATTRIBUTE_AS_DATETIME_MESSAGE, attr.getName(), valueAsString));
    }
  }

  private static Boolean convertBool(Attribute attr, String valueAsString) {
    if (valueAsString.equalsIgnoreCase(TRUE.toString())) {
      return true;
    } else if (valueAsString.equalsIgnoreCase(FALSE.toString())) {
      return false;
    } else {
      throw new RuntimeException(
          format(
              "Attribute [%s] value [%s] cannot be converter to type [%s]",
              attr.getName(), valueAsString, Boolean.class.getSimpleName()));
    }
  }

  private Entity convertRef(Attribute attr, String idValueAsString) {
    EntityType refEntityType = attr.getRefEntity();
    Object idValue = convertToTypedValue(refEntityType.getIdAttribute(), idValueAsString);
    return entityReferenceCreator.getReference(refEntityType, idValue);
  }

  private List<Entity> convertMref(Attribute attr, String idValuesAsString) {
    List<String> valuesAsString = ListEscapeUtils.toList(idValuesAsString);
    return valuesAsString
        .stream()
        .map(refValueAsString -> convertRef(attr, refValueAsString))
        .collect(toList());
  }
}
