/******************************************************************
*
* CyberLink for Objective-C
*
* UPnPPresentationViewController.h
*
* Copyright (C) Satoshi Konno 2011
*
* This is licensed under BSD-style license, see file COPYING.
*
******************************************************************/


#import <UIKit/UIKit.h>
#import <iAd/ADBannerView.h>
#import <CyberLink/UPnP.h>

@interface UPnPPresentationViewController : UIViewController <UIWebViewDelegate, NSXMLParserDelegate, NSURLConnectionDelegate>

@property (strong, nonatomic) NSMutableData *webData;
@property (strong, nonatomic) NSMutableString *soapResults;
@property (strong, nonatomic) NSXMLParser *xmlParser;
@property (nonatomic) BOOL elementFound;
@property (strong, nonatomic) NSString *matchingElement;
@property (strong, nonatomic) NSURLConnection *conn;
@property(retain) UIWebView *webView;
@property(retain) ADBannerView *adView;
@property(retain) CGUpnpDevice *device;
@property(retain) NSString *presentationURL;

- (IBAction)doQuery:(id)sender;
@end
