package fr.paris.lutece.plugins.statistics.service;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import fr.paris.lutece.plugins.statistics.model.UserStatistic;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class UserStatisticEncryptionService implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4361682010033230542L;
	private static final String BEAN_ENCRYPT_SERVICE = "statistics-rest.encryptionService";
	private static UserStatisticEncryptionService _singleton;
	private static boolean bIsInitialized = false;

	private KeySpec keySpec;
	private SecretKey key;
	private IvParameterSpec iv;

	/**
	 * Returns the unique instance
	 * 
	 * @return The unique instance
	 */
	public static UserStatisticEncryptionService instance()
	{
		if ( !bIsInitialized )
		{
			try
			{
				_singleton = SpringContextService.getBean( BEAN_ENCRYPT_SERVICE );
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

	public UserStatisticEncryptionService() throws Exception
	{
			final MessageDigest md = MessageDigest.getInstance( "md5" );
			final byte[] digestOfPassword = md
					.digest( Base64.decodeBase64( String.valueOf( serialVersionUID ).getBytes() ) );
			final byte[] keyBytes = Arrays.copyOf( digestOfPassword, 24 );
			for ( int j = 0, k = 16; j < 8; )
			{
				keyBytes[k++] = keyBytes[j++];
			}

			keySpec = new DESedeKeySpec( keyBytes );

			key = SecretKeyFactory.getInstance( "DESede" ).generateSecret( keySpec );

			iv = new IvParameterSpec( new byte[8] );
	}

	public UserStatistic encrypt( UserStatistic user ) throws Exception
	{
		if ( user.getConnectionId() != null )
		{
			user.setConnectionId( encrypt( user.getConnectionId() ) );
		}

		if ( user.getCustomerId() != null )
		{
			user.setCustomerId( encrypt( user.getCustomerId() ) );
		}
		return user;
	}

	public UserStatistic decrypt( UserStatistic user ) throws Exception
	{
		if ( user.getConnectionId() != null )
		{
			user.setConnectionId( decrypt( user.getConnectionId() ) );
		}

		if ( user.getCustomerId() != null )
		{
			user.setCustomerId( decrypt( user.getCustomerId() ) );
		}
		return user;
	}

	private String encrypt( String strToEncrypt ) throws Exception
	{
			Cipher ecipher = Cipher.getInstance( "DESede/CBC/PKCS5Padding", "SunJCE" );
			ecipher.init( Cipher.ENCRYPT_MODE, key, iv );

			if ( strToEncrypt == null )
				return null;

			// Encode the string into bytes using utf-8
			byte[] utf8 = strToEncrypt.getBytes( "UTF8" );

			// Encrypt
			byte[] enc = ecipher.doFinal( utf8 );

			// Encode bytes to base64 to get a string
			return new String( Base64.encodeBase64( enc ), "UTF-8" );
	}

	private String decrypt( String strToDecrypt ) throws Exception
	{
			Cipher dcipher = Cipher.getInstance( "DESede/CBC/PKCS5Padding", "SunJCE" );
			dcipher.init( Cipher.DECRYPT_MODE, key, iv );

			if ( strToDecrypt == null )
				return null;

			// Decode base64 to get bytes
			byte[] dec = Base64.decodeBase64( strToDecrypt.getBytes() );

			// Decrypt
			byte[] utf8 = dcipher.doFinal( dec );

			// Decode using utf-8
			return new String( utf8, "UTF8" );
	}
}
