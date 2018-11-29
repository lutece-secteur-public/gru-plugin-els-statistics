package fr.paris.lutece.plugins.statistics.elasticsearch;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.statistics.model.UserStatistic;
import fr.paris.lutece.plugins.statistics.model.UserStatistic.MediaType;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ElasticSearchClient
{

	private static final String PROPERTIES_IP_ES = "statistics.elasticsearch.ip";
	private static final String IP_ES = AppPropertiesService.getProperty( PROPERTIES_IP_ES );
	private static final String PROPERTIES_PORT_ES = "statistics.elasticsearch.port";
	private static final int PORT_ES = Integer.parseInt( AppPropertiesService.getProperty( PROPERTIES_PORT_ES ) );

	private static String BASE_INDEX_NAME = "user-statistics-";
	private static String DOC_TYPE = "doc";

	private static Object LOCK_INIT = new Object();

	private static RestClient client;
	private static ObjectMapper mapper;

	private static void init() throws UnknownHostException
	{

		synchronized ( LOCK_INIT )
		{
			if ( client == null )
			{
				mapper = new ObjectMapper();
				client = RestClient.builder( new HttpHost( IP_ES, PORT_ES, "http" ) ).build();
			}
		}
	}

	public static void close() throws IOException
	{
		client.close();
	}

	public static boolean addUserStatistic( UserStatistic userStatistic ) throws IOException
	{
		init();
		boolean reponse = false;
		if ( userStatistic.getEmailSenderName() != null )
		{
			userStatistic.setMediaType( MediaType.EMAIL );
			reponse = sendIndex( userStatistic );
		}
		if ( userStatistic.getBackofficeMessage() != null )
		{
			userStatistic.setMediaType( MediaType.BACKOFFICE );
			reponse = sendIndex( userStatistic );
		}
		if ( userStatistic.getSmsSenderName() != null )
		{
			userStatistic.setMediaType( MediaType.SMS );
			reponse = sendIndex( userStatistic );
		}
		if ( userStatistic.getMyDashboardSenderName() != null )
		{
			userStatistic.setMediaType( MediaType.MYDASHBOARD );
			reponse = sendIndex( userStatistic );
		}
		if ( !reponse )
		{
			userStatistic.setMediaType( MediaType.OTHER );
			reponse = sendIndex( userStatistic );
		}
		close();
		return reponse;
	}

	public static boolean sendIndex( UserStatistic userStatistic ) throws IOException
	{
		Date connectionDate = userStatistic.getConnectionDate() != null ? userStatistic.getConnectionDate()
				: new Date();
		String indexName = getIndexName( connectionDate );
		String json = mapper.writeValueAsString( userStatistic );
		Response response = client.performRequest( "POST", "/" + indexName + "/" + DOC_TYPE,
				Collections.<String, String> emptyMap(), new NStringEntity( json, ContentType.APPLICATION_JSON ) );
		return ( response.getStatusLine().getStatusCode() == 201 );

	}

	public static String getIndexName( Date indexDate )
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime( indexDate );
		return BASE_INDEX_NAME + cal.get( Calendar.YEAR ) + "-" + ( cal.get( Calendar.MONTH ) + 1 );
	}

}
