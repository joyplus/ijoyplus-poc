//
//  NSString+UPnP.m
//  UPnP
//
//  Created by 08 on 12-11-5.
//  Copyright (c) 2012年 iplusjoy. All rights reserved.
//

#import "NSString+UPnP.h"

@implementation NSString (NSString_UPnP)

-(BOOL)convertToBool
{
    if ([self compare:@"yes" options:NSCaseInsensitiveSearch] == NSOrderedSame ||
        [self compare:@"true"  options:NSCaseInsensitiveSearch] == NSOrderedSame ||
        [self isEqualToString:@"1"]) 
    {
        return YES;
    }
    else if(
        [self compare:@"no" options:NSCaseInsensitiveSearch] == NSOrderedSame ||
        [self compare:@"false"  options:NSCaseInsensitiveSearch] == NSOrderedSame ||
        [self isEqualToString:@"0"]) 
    {
        return NO; 
    }
    
    NSException *invalidBooleanValue = [NSException exceptionWithName:@"Invalid Value"
                                                               reason:@"The string has a value that cannot be converted to a boolean"
                                                             userInfo:nil];
    [invalidBooleanValue raise];
    return NO;
}

+ (NSString *)convertFromBool:(BOOL)boolean
{
    if (boolean) 
    {
        return @"true";
    }
    return @"false";
}

- (NSDictionary *)parseHTTTPHeaderInformation
{
    NSArray *headerLines = [self componentsSeparatedByString:@"\r\n"];
    if (headerLines.count == 0) 
    {
        return nil;
    }
    
    // Parse of the common request header versioning and type information before parsing out the rest of the header
    NSArray *requestInformation = [[headerLines objectAtIndex:0] componentsSeparatedByString:@" "];
    if (requestInformation.count < 3) 
    {
        return nil;
    }
    NSMutableDictionary *httpHeader = [NSMutableDictionary dictionaryWithCapacity:headerLines.count+2];
    [httpHeader setObject:[requestInformation objectAtIndex:0] forKey:@"Response Method"]; 
    [httpHeader setObject:[requestInformation objectAtIndex:1] forKey:@"Response URI"];  
    [httpHeader setObject:[requestInformation objectAtIndex:2] forKey:@"Response Version"]; 
    
    // Now parse out the remaining header lines and store them in the dictionary
    for (int headerLine = 1; headerLine < headerLines.count; headerLine++) 
    {
        NSString *headerLineDefinition = [headerLines objectAtIndex:headerLine];
        NSArray *nameAndValue = [headerLineDefinition componentsSeparatedByString:@": "];
        if (nameAndValue.count != 2) 
        {
            continue;
        }
        [httpHeader setObject:[nameAndValue objectAtIndex:1] forKey:[nameAndValue objectAtIndex:0]];
    }
    
    return httpHeader;
}

- (NSString *)DIDLLiteResponseString
{
    return [NSString stringWithFormat:@"<DIDL-Lite xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:upnp='urn:schemas-upnp-org:metadata-1-0/upnp/' xmlns='urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/'>%@</DIDL-Lite>", self];
}

- (NSString *)formatValue:(NSString *)value
{
    return [value stringByReplacingOccurrencesOfString:@"," withString:@"\\,"];
}

- (void)appendValue:(NSString *)value
{
    // escape any existing commas
    value = [self formatValue:value];
    if(self.length == 0)
    {
        // no need to append comma
        self = value;
        return;
    }
    
    self = [self stringByAppendingFormat:@",%@",value];
    return;
}

- (void)removeValue:(NSString *)value
{
    NSArray *values = [self componentsSeparatedByString:@","];
    
    // Loop over values in the value string and stop if we find a value equal to the one we want to removeß
    BOOL (^predicate)(id obj, NSUInteger idx, BOOL *stop) = ^(id obj, NSUInteger idx, BOOL *stop) {
        
        if ([obj isEqualToString:value]) 
        {
            *stop = YES;
            return YES;
        }
        return NO;
    };
    
    int indexOfValue = [values indexOfObjectPassingTest:predicate];
    
    if (indexOfValue != NSNotFound) 
    {
        // If we found the value, remove it from the list of components
        NSMutableArray *mutableValues = [NSMutableArray arrayWithArray:values];
        [mutableValues removeObjectAtIndex:indexOfValue];
        self = [mutableValues componentsJoinedByString:@","];
    }
}

- (NSString *)getExtension
{
    NSArray *components = [self componentsSeparatedByString:@"."];
    if (components.count < 2) 
    {
        return nil;
    }
    
    return [components lastObject];
}

- (NSString *)stringAsSoapMessage
{
    NSMutableString *soapMessageWrapper = [NSMutableString string];
    [soapMessageWrapper appendString:@"<?xml version=\"1.0\"?>"];
    [soapMessageWrapper appendString:@"<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"];
    [soapMessageWrapper appendString:@"<SOAP-ENV:Body>"];
    [soapMessageWrapper appendString:@"<SOAP-ENV:Fault>"];
    [soapMessageWrapper appendString:@"<faultcode>SOAP-ENV:Client</faultcode>"];
    [soapMessageWrapper appendString:self];
    [soapMessageWrapper appendString:@"</SOAP-ENV:Fault>"];
    [soapMessageWrapper appendString:@"</SOAP-ENV:Body>"];
    [soapMessageWrapper appendString:@"</SOAP-ENV:Envelope>"];
    
    return soapMessageWrapper;
}

+ (NSString *)upnpErrorMessageWithCode:(int)code andDescription:(NSString *)description forSchema:(NSString *)schema
{
    NSMutableString *upnpErrorMessage = [NSMutableString string];
    [upnpErrorMessage appendString:@"<faultstring>UPnPError</faultstring>"];
    [upnpErrorMessage appendString:@"<detail>"];
    [upnpErrorMessage appendFormat:@"<UPnPError xmlns=\"%@\">", schema];
    [upnpErrorMessage appendFormat:@"<errorCode xmlns="">%i</errorCode>", code];
    [upnpErrorMessage appendFormat:@"<errorDescription xmlns="">%@</errorDescription>", description];
    [upnpErrorMessage appendString:@"</UPnPError>"];
    [upnpErrorMessage appendString:@"</detail>"];
    
    return upnpErrorMessage;
}

+ (NSString *)errorWithCode:(int)code andDescription:(NSString *)errorDescription
{
    return [NSString stringWithFormat:@"<UPnPError><errorCode>%i</errorCode><errorDescription>%@</errorDescription></UPnPError>", code, errorDescription];
}

@end
