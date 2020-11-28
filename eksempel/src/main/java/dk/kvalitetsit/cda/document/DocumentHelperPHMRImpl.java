package dk.kvalitetsit.cda.document;

import java.util.Date;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;

import dk.kvalitetsit.cda.dto.CdaMetadata;
import dk.s4.hl7.cda.codes.Loinc;
import dk.s4.hl7.cda.convert.PHMRXmlCodec;
import dk.s4.hl7.cda.model.phmr.PHMRDocument;

public class DocumentHelperPHMRImpl implements DocumentHelper {
	
	private DocumentFactoryPHMRImpl documentFactoryPhmrImpl;
	private static PHMRXmlCodec codec = new PHMRXmlCodec();
	private static Code PHMR_CODE = new Code(Loinc.PHMR_CODE, new LocalizedString(Loinc.PMHR_DISPLAYNAME), Loinc.OID);
	
	public DocumentHelperPHMRImpl() {
		documentFactoryPhmrImpl = new DocumentFactoryPHMRImpl();
	}
	
	public String createDocumentAsXML(String externalIdForNewDocument, Date from, Date to) {
		
		if (documentFactoryPhmrImpl == null) {
			documentFactoryPhmrImpl = new DocumentFactoryPHMRImpl();
		}
		
		PHMRDocument phmrDocument = documentFactoryPhmrImpl.defineAsCDA(externalIdForNewDocument, from, to);
		String xmlDocument = codec.encode(phmrDocument);
		return xmlDocument;
	}
	
	public Date getDocumentEffectiveTimeFromXML(String document) {
		PHMRDocument phmrDocument = codec.decode(document);	
		return phmrDocument.getEffectiveTime();
	}
	
	public Code getDocumentTypeCode() {
		return PHMR_CODE;
	}	
	
	public CdaMetadata createCdaMetadata() {
		CdaMetadata cdaMetaData = new CdaMetadata();
		cdaMetaData.setAvailabilityStatus(AvailabilityStatus.APPROVED);
		cdaMetaData.setObjectType(DocumentEntryType.STABLE);
		cdaMetaData.setClassCode(new dk.kvalitetsit.cda.dto.Code("Klinisk rapport","001", "1.2.208.184.100.9"));
		cdaMetaData.setFormatCode(new dk.kvalitetsit.cda.dto.Code("DK PHMR schema", "urn:ad:dk:medcom:phmr:full", "1.2.208.184.100.10"));
		cdaMetaData.setHealthcareFacilityTypeCode(new dk.kvalitetsit.cda.dto.Code("hjemmesygepleje","550621000005101","2.16.840.1.113883.6.96"));
		cdaMetaData.setPracticeSettingCode(new dk.kvalitetsit.cda.dto.Code("almen medicin", "408443003", "2.16.840.1.113883.6.96"));
		cdaMetaData.setSubmissionTime(new Date());
		return cdaMetaData;
	}
	
}
