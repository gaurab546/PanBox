/*
 * 
 *               Panbox - encryption for cloud storage 
 *      Copyright (C) 2014-2015 by Fraunhofer SIT and Sirrix AG 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Additonally, third party code may be provided with notices and open source
 * licenses from communities and third parties that govern the use of those
 * portions, and any licenses granted hereunder do not alter any rights and
 * obligations you may have under such open source licenses, however, the
 * disclaimer of warranty and limitation of liability provisions of the GPLv3 
 * will apply to all the product.
 * 
 */
package org.panbox.core.crypto;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.panbox.PanboxConstants;

public class EncodingHelper {
	
	public static byte[] decodeString(String str, EncodingType type) {
		switch (type) {
		case BASE64:
			
			//convert the url-safe back to plain base64
			str = str.replace('-', '+').replace('_', '/');
			
			//Because we do not have a padding and Base64.decodeBase64 from apache codec only supports padded Base64 string, we readd the padding		
			while(str.length() %4 != 0)
			{
				str = str.concat("=");
			}
			
			byte[] decoded = null;

			try {
				decoded = Base64.decodeBase64(str.getBytes(PanboxConstants.STANDARD_CHARSET));
			} catch (UnsupportedEncodingException e) {
				// encoding is broken. Will leave decoded as null
			}
			return decoded;
		default:
			break;
		}

		return null;
	}

	public static String encodeByte(byte[] data, EncodingType type) {
		switch (type) {
		case BASE64:
			Base64 b = new Base64();
			byte[] b64RawBytes = b.encode(data);
			
			String b64Str = "";
			try {
				b64Str = new String(b64RawBytes, PanboxConstants.STANDARD_CHARSET);
			} catch (UnsupportedEncodingException e) {
				// if this really fails UTF-8 must be broken at all
			}
			String b64UrlSafeStr = b64Str.replace('+', '-').replace('/', '_');
			
			//remove padding, because padding is "=" and we don't want this in our URLs
			//apache codec > 1.4 Base64.encodeBase64URLSafeString also skips the padding
			if(b64UrlSafeStr.endsWith("=="))
			{
				return b64UrlSafeStr.substring(0, b64UrlSafeStr.length()-2);
			}
			else if(b64UrlSafeStr.endsWith("="))
			{
				return b64UrlSafeStr.substring(0, b64UrlSafeStr.length()-1);
			}
			return b64UrlSafeStr;
		default:
			break;
		}
		return null;
	}
	
}
