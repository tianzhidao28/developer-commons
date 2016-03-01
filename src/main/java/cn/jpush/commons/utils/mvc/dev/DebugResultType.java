package cn.jpush.commons.utils.mvc.dev;


import cn.jpush.commons.utils.web.MediaTypes;

/**
 * 
 */
public enum DebugResultType {
	JSON(MediaTypes.JSON_UTF_8),
	XML(MediaTypes.TEXT_XML_UTF_8),
	TEXT(MediaTypes.TEXT_PLAIN_UTF_8)	
	;

	private String mediaType;
	DebugResultType(String mediaType) {
		this.mediaType = mediaType;
	}
}
