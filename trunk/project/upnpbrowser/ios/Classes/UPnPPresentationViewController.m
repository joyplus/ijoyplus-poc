/******************************************************************
*
* CyberLink for Objective-C
*
* UPnPPresentationViewController.m
*
* Copyright (C) Satoshi Konno 2011
*
* This is licensed under BSD-style license, see file COPYING.
*
******************************************************************/

#import "UPnPPresentationViewController.h"


@interface UPnPPresentationViewController ()

@end

@implementation UPnPPresentationViewController

@synthesize webData;
@synthesize soapResults;
@synthesize xmlParser;
@synthesize elementFound;
@synthesize matchingElement;
@synthesize conn;
@synthesize webView;
@synthesize adView;
@synthesize device;
@synthesize presentationURL;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
        // Custom initialization
    }
    return self;
}

- (void)loadView 
{
	CGRect scrSize = [[UIScreen mainScreen] applicationFrame];
	scrSize.origin.y = 0;
	
	self.view = [[UIView alloc] initWithFrame:scrSize];
	webView = [[UIWebView alloc] initWithFrame:[self.view frame]];
	[webView setDelegate:self];
	[self.view addSubview: webView];
	
	[self.view setAutoresizesSubviews:YES];
	[self.view setAutoresizingMask:(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight)];
	[webView setAutoresizesSubviews:YES];
	[webView setAutoresizingMask:(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight)];
	
	[self.view sizeToFit];
	[webView sizeToFit];
}

- (void)viewDidLoad 
{
	[super viewDidLoad];

	NSString *urlBase = [device urlBase];
	NSString *ssdpLocationURL = [device locationURL];

	self.presentationURL = [device presentationURL];
	
	NSLog(@"urlBase = %@", urlBase);
	NSLog(@"presentatilURL = %@", presentationURL);
	NSLog(@"ssdpLocationURL = %@", ssdpLocationURL);
	
	if (![presentationURL hasPrefix:@"http://"]) {
		if (urlBase != nil && 0 < [urlBase length]) {
			self.presentationURL = [NSString stringWithFormat:@"%@%@",
							  [device urlBase],
							  [device presentationURL]];
		}
		else if (ssdpLocationURL != nil && 0 < [ssdpLocationURL length]) {
			self.presentationURL = [NSString stringWithFormat:@"%@%@",
							  [device locationURL],
							  [device presentationURL]];
		}
	}
	NSLog(@"presentatilURL = %@", presentationURL);
	
    NSData * data = [[NSData alloc] init];
    
    NSURL  *url  = [[NSURL alloc] initWithString: @"http://api.joyplus.tv/joyplus-service/video/t.mp4"];
	NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
	[request setHTTPMethod:@"POST"];
	[request setHTTPBody:data];
	[request setValue:[NSString stringWithFormat:@"%d", [data length]] forHTTPHeaderField:@"Content-Length"];
	 
    NSURLConnection  *con = [[NSURLConnection alloc] initWithRequest:request delegate:self];
        if(con)
        {
            NSLog(@"print Responsed");
        }
     
    NSLog(@"print %@  ", request);
    
	[request release];
  
    
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
	
	[webView loadRequest:[NSURLRequest requestWithURL:
						  [NSURL URLWithString:presentationURL]]];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView
{
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];	
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
	[[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];	

	NSString *errMsg = [error localizedFailureReason];
	if (errMsg == nil || [errMsg length] <= 0) {
		errMsg = [NSString stringWithFormat:@"Presentation URL is invalid !! (%@)",
				  [self presentationURL]];
	}
	/*
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"iNetFrame" message:errMsg
												   delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil];
	[alert show];	
	[alert release];
     */
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void)dealloc {
    [super dealloc];
}


@end
