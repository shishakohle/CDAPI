package at.technikumvienna.CDAPI.CDA.datatypes;

public interface CdaDatatype
{
	public String getType(); // Strings for now, to be replaced by an enum of CdaDatatypes
	public String toXmlElement();
}
