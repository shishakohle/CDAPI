package at.technikumvienna.CDAPI.CDA.datatypes;

/* "In the unit of measurement unit is expressed following
 * the Unified Code for Units of Measure (UCUM)
 * specification. This specification is a collection of
 * atomic units, prefixes and a corresponding grammar for
 * valid combinations to build all-natural units of
 * measurement. For a complete description of possible
 * units, see the Unified Code for Units of Measure
 * specification. If a value is not countable or if
 * measurable, for example expressed in milligrams or
 * liters, then this unit is a valid UCUM unit like “mg”
 * or “L”. If a value is countable, for example the number
 * of tablets, then this attribute contains “1”. In HL7 it
 * is required that all PQ data type units are valid UCUM
 * units."
 * Source: https://wiki.hl7.de/index.php?title=HL7_CDA_Core_Principles#Physical_Quantities_.28PQ.29
 * (2020-07-14)
 * */

public class PhysicalQuantity implements CdaDatatype
/**
 * The PQ datatype of CDA.
 * For more, see e.g.:
 * https://wiki.hl7.de/index.php?title=HL7_CDA_Core_Principles#Physical_Quantities_.28PQ.29
 * (2020-07-14)
 */
{
	private final static String cdaDatatype = "PQ";
	
	private String value;
	private String unit; // represented as a String for now. Should be an enum, or better imported from a code system file (UCUM). proof of concept only!
	
	public PhysicalQuantity(String value, String unit)
	{
		this.value = value!=null ? value : "";
		this.unit  = unit !=null ? unit  : "";
	}
	
	@Override
	public String getType()
	{
		return cdaDatatype;
	}

	@Override
	public String toXmlElement()
	{
		return "<value xsi:type=\"PQ\" value=\"" + this.value + "\" unit=\"" + this.unit + "\"/>";
	}
}
