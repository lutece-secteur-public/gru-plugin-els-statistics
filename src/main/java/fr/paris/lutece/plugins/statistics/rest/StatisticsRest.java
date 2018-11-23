/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.statistics.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.paris.lutece.plugins.grubusiness.business.notification.Notification;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.statistics.constants.StatisticsConstants;
import fr.paris.lutece.plugins.statistics.elasticsearch.ElasticSearchClient;
import fr.paris.lutece.plugins.statistics.model.UserStatistic;
import fr.paris.lutece.plugins.statistics.service.UserStatisticEncryptionService;
import fr.paris.lutece.plugins.statistics.tools.ObjectConverter;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

@Path( RestConstants.BASE_PATH + StatisticsConstants.PLUGIN_NAME )
public class StatisticsRest
{

	private static final String PROPERTIES_APPLICATION_CODE = "grusupply.application.code";
	private static final String APPLICATION_CODE = AppPropertiesService.getProperty( PROPERTIES_APPLICATION_CODE );

	private static final String BEAN_IDENTITYSTORE_SERVICE = "statistics-rest.identitystore.service";

	// Other constants
	private static final String STATUS_RECEIVED = "{ \"acknowledge\" : { \"status\": \"received\" } }";

	/**
	 * Get the Json of the demand and customer
	 * 
	 * @throws UserNotSignedException
	 */
	@POST
	@Path( "demand" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getDemandJson( String strJson, @Context HttpServletRequest request ) throws UserNotSignedException
	{
		try
		{
			// Format from JSON
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure( DeserializationFeature.UNWRAP_ROOT_VALUE, false );
			mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

			Notification notification = mapper.readValue( strJson, Notification.class );

			IdentityService IdentityService = SpringContextService.getBean( BEAN_IDENTITYSTORE_SERVICE );
			IdentityDto identityDto = IdentityService.getIdentity(
					notification.getDemand().getCustomer().getConnectionId(),
					notification.getDemand().getCustomer().getId(), APPLICATION_CODE );

			UserStatistic userStatistic = ObjectConverter.instance().notificationtoStatistic( notification,
					identityDto );

			UserStatistic userStatisticEncrypted = UserStatisticEncryptionService.instance().encrypt( userStatistic );

			ElasticSearchClient.addUserStatistic( userStatisticEncrypted );
		}
		catch ( JsonParseException ex )
		{
			return error( ex + " :" + ex.getMessage(), ex );
		}
		catch ( JsonMappingException ex )
		{
			return error( ex + " :" + ex.getMessage(), ex );
		}
		catch ( IOException ex )
		{
			return error( ex + " :" + ex.getMessage(), ex );
		}
		catch ( NullPointerException ex )
		{
			return error( ex + " :" + ex.getMessage(), ex );
		}
		catch ( Exception ex )
		{
			return error( ex + " :" + ex.getMessage(), ex );
		}

		return Response.status( Response.Status.CREATED ).entity( STATUS_RECEIVED ).build();
	}

	/**
	 * Build an error response
	 * 
	 * @param strMessage
	 *            The error message
	 * @param ex
	 *            An exception
	 * @return The response
	 */
	private Response error( String strMessage, Throwable ex )
	{
		if ( ex != null )
		{
			AppLogService.error( strMessage, ex );
		}
		else
		{
			AppLogService.error( strMessage );
		}

		String strError = "{ \"status\": \"Error : " + strMessage + "\" }";

		return Response.status( Response.Status.INTERNAL_SERVER_ERROR ).entity( strError ).build();
	}

}
