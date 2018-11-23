package fr.paris.lutece.plugins.statistics.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStatistic {

	// Statistic date
	@JsonProperty("connection-date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date connectionDate;

	// UserStatistic
	@JsonProperty("user-id")
	private String customerId;

	@JsonProperty("birthday")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date birthday;

	@JsonProperty("civility")
	private String civility;

	@JsonProperty("connection-id")
	private String connectionId;

	// Demand
	@JsonProperty("demand-id")
	private String demandId;

	@JsonProperty("demand-reference")
	private String demandReference;

	@JsonProperty("demand-type-id")
	private String demandTypeId;

	@JsonProperty("demand-subtype-id")
	private String demandSubtypeId;

	@JsonProperty("demand-status-id")
	private Long demandStatusId;

	@JsonProperty("demand-title")
	private String demandTitle;

	@JsonProperty("demand-creation-date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date demandCreationDate;

	@JsonProperty("demand-closure-date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date demandClosureDate;

	@JsonProperty("demand-max-step")
	private Long demandMaxStep;

	@JsonProperty("demand-current-step")
	private Long demandCurrentStep;

	// Email
	@JsonProperty("email-sender-name")
	private String emailSenderName;

	@JsonProperty("email-sender-email")
	private String emailSenderEmail;

	@JsonProperty("email-recipient")
	private String emailRecipient;

	@JsonProperty("email-subject")
	private String emailSubject;

	// SMS
	@JsonProperty("sms-sender-name")
	private String smsSenderName;

	// Backoffice
	@JsonProperty("backoffice-message")
	private String backofficeMessage;

	@JsonProperty("backoffice-status-text")
	private String backofficeStatusText;

	// MyDashboard
	@JsonProperty("mydashboard-status-id")
	private Long myDashboardStatusId;

	@JsonProperty("mydashboard-status-text")
	private String myDashboardStatusText;

	@JsonProperty("mydashboard-subject")
	private String myDashboardSubject;

	@JsonProperty("mydashboard-sender-name")
	private String myDashboardSenderName;

	@JsonProperty("mydashboard-data")
	private String myDashboardData;

	// Meta
	public static enum MediaType {
		EMAIL("email"), SMS("sms"), BACKOFFICE("backoffice"), MYDASHBOARD("mydashboard"), OTHER("other");

		private String val;

		MediaType(String val) {
			this.val = val;
		}

		public String val() {
			return this.val;

		}
	}

	@JsonProperty("media-type")
	private MediaType mediaType;

	public Date getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(Date connectionDate) {
		this.connectionDate = connectionDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCivility() {
		return civility;
	}

	public void setCivility(String civility) {
		this.civility = civility;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public String getDemandId() {
		return demandId;
	}

	public void setDemandId(String demandId) {
		this.demandId = demandId;
	}

	public String getDemandReference() {
		return demandReference;
	}

	public void setDemandReference(String demandReference) {
		this.demandReference = demandReference;
	}

	public String getDemandTypeId() {
		return demandTypeId;
	}

	public void setDemandTypeId(String demandTypeId) {
		this.demandTypeId = demandTypeId;
	}

	public String getDemandSubtypeId() {
		return demandSubtypeId;
	}

	public void setDemandSubtypeId(String demandSubtypeId) {
		this.demandSubtypeId = demandSubtypeId;
	}

	public Long getDemandStatusId() {
		return demandStatusId;
	}

	public void setDemandStatusId(Long demandStatusId) {
		this.demandStatusId = demandStatusId;
	}

	public String getDemandTitle() {
		return demandTitle;
	}

	public void setDemandTitle(String demandTitle) {
		this.demandTitle = demandTitle;
	}

	public Date getDemandCreationDate() {
		return demandCreationDate;
	}

	public void setDemandCreationDate(Date demandCreationDate) {
		this.demandCreationDate = demandCreationDate;
	}

	public Date getDemandClosureDate() {
		return demandClosureDate;
	}

	public void setDemandClosureDate(Date demandClosureDate) {
		this.demandClosureDate = demandClosureDate;
	}

	public Long getDemandMaxStep() {
		return demandMaxStep;
	}

	public void setDemandMaxStep(Long demandMaxStep) {
		this.demandMaxStep = demandMaxStep;
	}

	public Long getDemandCurrentStep() {
		return demandCurrentStep;
	}

	public void setDemandCurrentStep(Long demandCurrentStep) {
		this.demandCurrentStep = demandCurrentStep;
	}

	public String getEmailSenderName() {
		return emailSenderName;
	}

	public void setEmailSenderName(String emailSenderName) {
		this.emailSenderName = emailSenderName;
	}

	public String getEmailSenderEmail() {
		return emailSenderEmail;
	}

	public void setEmailSenderEmail(String emailSenderEmail) {
		this.emailSenderEmail = emailSenderEmail;
	}

	public String getEmailRecipient() {
		return emailRecipient;
	}

	public void setEmailRecipient(String emailRecipient) {
		this.emailRecipient = emailRecipient;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	/**
	 * @return the smsSenderName
	 */
	public String getSmsSenderName()
	{
		return smsSenderName;
	}

	/**
	 * @param smsSenderName the smsSenderName to set
	 */
	public void setSmsSenderName( String smsSenderName )
	{
		this.smsSenderName = smsSenderName;
	}

	public String getBackofficeMessage() {
		return backofficeMessage;
	}

	public void setBackofficeMessage(String backofficeMessage) {
		this.backofficeMessage = backofficeMessage;
	}

	public String getBackofficeStatusText() {
		return backofficeStatusText;
	}

	public void setBackofficeStatusText(String backofficeStatusText) {
		this.backofficeStatusText = backofficeStatusText;
	}

	public Long getMyDashboardStatusId() {
		return myDashboardStatusId;
	}

	public void setMyDashboardStatusId(Long myDashboardStatusId) {
		this.myDashboardStatusId = myDashboardStatusId;
	}

	public String getMyDashboardStatusText() {
		return myDashboardStatusText;
	}

	public void setMyDashboardStatusText(String myDashboardStatusText) {
		this.myDashboardStatusText = myDashboardStatusText;
	}

	public String getMyDashboardSubject() {
		return myDashboardSubject;
	}

	public void setMyDashboardSubject(String myDashboardSubject) {
		this.myDashboardSubject = myDashboardSubject;
	}

	public String getMyDashboardSenderName() {
		return myDashboardSenderName;
	}

	public void setMyDashboardSenderName(String myDashboardSenderName) {
		this.myDashboardSenderName = myDashboardSenderName;
	}

	public String getMyDashboardData() {
		return myDashboardData;
	}

	public void setMyDashboardData(String myDashboardData) {
		this.myDashboardData = myDashboardData;
	}

	public String getMediaType() {
		return mediaType.val();
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

}
