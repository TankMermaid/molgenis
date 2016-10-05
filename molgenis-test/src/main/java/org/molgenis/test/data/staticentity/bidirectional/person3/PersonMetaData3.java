package org.molgenis.test.data.staticentity.bidirectional.person3;

import org.molgenis.data.meta.SystemEntityMetaData;
import org.molgenis.data.meta.model.Attribute;
import org.springframework.stereotype.Component;

import static org.molgenis.MolgenisFieldTypes.AttributeType.ONE_TO_MANY;
import static org.molgenis.MolgenisFieldTypes.AttributeType.XREF;
import static org.molgenis.data.meta.model.EntityMetaData.AttributeRole.ROLE_ID;
import static org.molgenis.data.meta.model.EntityMetaData.AttributeRole.ROLE_LABEL;
import static org.molgenis.data.meta.model.Package.PACKAGE_SEPARATOR;
import static org.molgenis.data.system.model.RootSystemPackage.PACKAGE_SYSTEM;

@Component
public class PersonMetaData3 extends SystemEntityMetaData
{
	private static final String SIMPLE_NAME = "Person3";
	public static final String NAME = PACKAGE_SYSTEM + PACKAGE_SEPARATOR + SIMPLE_NAME;

	public static final String ID = "id";
	public static final String LABEL = "label";
	public static final String ATTR_PARENT = "parent";
	public static final String ATTR_CHILDREN = "children";

	PersonMetaData3()
	{
		super(SIMPLE_NAME, PACKAGE_SYSTEM);
	}

	@Override
	public void init()
	{
		setLabel("Person");

		addAttribute(ID, ROLE_ID).setLabel("Identifier");
		addAttribute(LABEL, ROLE_LABEL).setNillable(true).setLabel("Label");
		Attribute parentAttr = addAttribute(ATTR_PARENT).setDataType(XREF).setRefEntity(this).setNillable(true);
		addAttribute(ATTR_CHILDREN).setDataType(ONE_TO_MANY).setRefEntity(this).setMappedBy(parentAttr)
				.setNillable(false);
	}
}
