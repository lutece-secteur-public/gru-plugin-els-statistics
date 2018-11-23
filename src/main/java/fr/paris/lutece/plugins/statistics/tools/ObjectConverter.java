package fr.paris.lutece.plugins.statistics.tools;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import fr.paris.lutece.plugins.grubusiness.business.customer.Customer;
import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.statistics.business.Statistic;
import fr.paris.lutece.plugins.statistics.model.UserStatistic;
import fr.paris.lutece.plugins.statistics.model.UserStatistic.MediaType;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class ObjectConverter extends ConfigurableMapper
{

	private static final String BEAN_CONVERTER_SERVICE = "statistics-rest.objectConverter";
	private static ObjectConverter _singleton;
	private static boolean bIsInitialized = false;
	private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

	public static ObjectConverter instance()
	{
		if ( !bIsInitialized )
		{
			try
			{
				_singleton = SpringContextService.getBean( BEAN_CONVERTER_SERVICE );
			}
			catch ( NoSuchBeanDefinitionException e )
			{
				AppLogService.info( "No encryption bean found" );
			}
			finally
			{
				bIsInitialized = true;
			}
		}

		return _singleton;
	}

	public UserStatistic notificationtoStatistic( Notification notif, IdentityDto identity )
	{
		mapperFactory.classMap( Notification.class, UserStatistic.class )
				.field( "demand.id", "demandId" )
				.field( "demand.reference", "demandReference" )
				.field( "demand.typeId", "demandTypeId" )
				.field( "demand.subtypeId", "demandSubtypeId" )
				.field( "demand.statusId", "demandStatusId" )
				.field( "demand.title", "demandTitle" )
				.field( "demand.creationDate", "demandCreationDate" )
				.field( "demand.closureDate", "demandClosureDate" )
				.field( "demand.maxSteps", "demandMaxStep" )
				.field( "demand.currentStep", "demandCurrentStep" )
				.field( "emailNotification.senderName", "emailSenderName" )
				.field( "emailNotification.senderEmail", "emailSenderEmail" )
				.field( "emailNotification.recipient", "emailRecipient" )
				.field( "emailNotification.subject", "emailSubject" )
				.field( "smsNotification.senderName", "smsSenderName" )
				.field( "backofficeNotification.message", "backofficeMessage" )
				.field( "backofficeNotification.statusText", "backofficeStatusText" )
				.field( "myDashboardNotification.statusId", "myDashboardStatusId" )
				.field( "myDashboardNotification.statusText", "myDashboardStatusText" )
				.field( "myDashboardNotification.subject", "myDashboardSubject" )
				.field( "myDashboardNotification.senderName", "myDashboardSenderName" )
				.field( "myDashboardNotification.data", "myDashboardData" )
				.register();
		
		mapperFactory.classMap( IdentityDto.class, UserStatistic.class )
				.field( "connectionId", "connectionId" )
				.field( "customerId", "customerId" )
				.customize(new CustomMapper<IdentityDto,UserStatistic>() {
		            @Override
		            public void mapAtoB(IdentityDto ident, UserStatistic us, MappingContext mappingContext) {
		              String birthdate =  ident.getAttributes().entrySet().stream().filter( att -> att.getKey().equals( "birthdate" ) ).findFirst().get().getValue().getValue();
		              us.setBirthday( DateHelper.parse( DateHelper.DDMMYYYY_SLASH, birthdate ) );
		              String gender =  ident.getAttributes().entrySet().stream().filter( att -> att.getKey().equals( "gender" ) ).findFirst().get().getValue().getValue();
		              us.setCivility( gender );
		              us.setConnectionDate( new Date() );
		              us.setMediaType( MediaType.EMAIL );
		            }
		        })
				.register();
		
		UserStatistic user = new UserStatistic();
		mapperFactory.getMapperFacade(Notification.class, UserStatistic.class).map( notif, user );
		mapperFactory.getMapperFacade(IdentityDto.class, UserStatistic.class).map( identity, user );
		
		return user;
	}
	
	public UserStatistic identityToStatistic( IdentityDto identity )
	{
		mapperFactory.classMap( IdentityDto.class, UserStatistic.class )
				.field( "connectionId", "connectionId" )
				.field( "customerId", "customerId" )
				.register();
		MapperFacade mapper = mapperFactory.getMapperFacade();
		return mapper.map( identity, UserStatistic.class );
	}
	
}
