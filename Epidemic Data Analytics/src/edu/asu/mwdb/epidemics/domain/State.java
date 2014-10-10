package edu.asu.mwdb.epidemics.domain;
public enum State {
	
	US_AK("US-AK"), 
	US_AL("US-AL"), 
	US_AR("US-AR"), 
	US_AZ("US-AZ"), 
	US_CA("US-CA"), 
	US_CO("US-CO"), 
	US_CT("US-CT"), 
	US_DC("US-DC"), 
	US_DE("US-DE"),
	US_FL("US-FL"), 
	US_GA("US-GA"), 
	US_HI("US-HI"), 
	US_IA("US-IA"), 
	US_ID("US-ID"), 
	US_IL("US-IL"), 
	US_IN("US-IN"), 
	US_KS("US-KS"), 
	US_KY("US-KY"), 
	US_LA("US-LA"), 
	US_MA("US-MA"), 
	US_MD("US-MD"), 
	US_ME("US-ME"), 
	US_MI("US-MI"), 
	US_MN("US-MN"), 
	US_MO("US-MO"), 
	US_MS("US-MS"), 
	US_MT("US-MT"), 
	US_NC("US-NC"), 
	US_ND("US-ND"), 
	US_NE("US-NE"), 
	US_NH("US-NH"), 
	US_NJ("US-NJ"), 
	US_NM("US-NM"), 
	US_NV("US-NV"), 
	US_NY("US-NY"), 
	US_OH("US-OH"), 
	US_OK("US-OK"), 
	US_OR("US-OR"),
	US_PA("US-PA"), 
	US_RI("US-RI"), 
	US_SC("US-SC"), 
	US_SD("US-SD"), 
	US_TN("US-TN"), 
	US_TX("US-TX"), 
	US_UT("US-UT"), 
	US_VA("US-VA"), 
	US_VT("US-VT"), 
	US_WA("US-WA"), 
	US_WI("US-WI"), 
	US_WV("US-WV"), 
	US_WY("US-WY");
	
	private String stateName;
	
	private State(String stateName) {
		this.stateName = stateName;
	}
	
	@Override
	public String toString() {
		return stateName;
	}
}
