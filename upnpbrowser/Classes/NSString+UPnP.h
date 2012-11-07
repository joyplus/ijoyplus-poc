//
//  NSString+UPnP.h
//  UPnP
//
//  Created by 08 on 12-11-5.
//  Copyright (c) 2012年 iplusjoy. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (NSString_UPnP)

- (BOOL)convertToBool;

// Converts a value back to the UPnP supported boolean string of true/false.
+ (NSString *)convertFromBool:(BOOL)boolean;

// Given an HTTP packet, parse the header information into a dictionary.  Some common fields to take notice of are:
//          "Response Method"  = The type of response (e.x. M-SEARCH)
//          "Response URI"     = The response identifier (e.x. *)
//          "Response Version" = The response version (e.x. HTTP 1/1)
- (NSDictionary *)parseHTTTPHeaderInformation;

// Wraps string content in DIDL-Lite tag for response string to UPnP services
- (NSString *)DIDLLiteResponseString;

// Returns the filename extension (or nil if not applicable)
- (NSString *)getExtension;

// Wraps the current string as an XML SOAP message
- (NSString *)stringAsSoapMessage;

// A wrapper for an upnp error response message
+ (NSString *)upnpErrorMessageWithCode:(int)code andDescription:(NSString *)description forSchema:(NSString *)schema;

// Returns an UPnP XML error response message
+ (NSString *)errorWithCode:(int)code andDescription:(NSString *)errorDescription;

@end
