//
//  CMLog.h
//  QuickScale
//
//  Created by Jelle De Laender on 29/03/12.
//  Rewrote on 11/08/2012
//  Copyright (c) 2012 CodingMammoth. All rights reserved.
//

#import <Foundation/Foundation.h>

#define LOG_VERBOSE_LEVEL   0
#define LOG_INFO_LEVEL      1
#define LOG_WARNING_LEVEL   2
#define LOG_ERROR_LEVEL     3

#define REPORTLEVEL LOG_WARNING_LEVEL

// Define Error
#define CMLogError( s, ... )  NSLog( @"<%p %@:(%d)> [ERROR] %@", self, [[NSString stringWithUTF8String:__FILE__] lastPathComponent], __LINE__, [NSString stringWithFormat:(s), ##__VA_ARGS__] )

// Define Warning
#if REPORTLEVEL <= LOG_WARNING_LEVEL
#define CMLogWarning( s, ... )  NSLog( @"<%p %@:(%d)> [WARN] %@", self, [[NSString stringWithUTF8String:__FILE__] lastPathComponent], __LINE__, [NSString stringWithFormat:(s), ##__VA_ARGS__] )
#else
#define CMLogWarning( s, ... )
#endif

// Define Info
#if REPORTLEVEL <= LOG_INFO_LEVEL
#define CMLogInfo( s, ... )  NSLog( @"<%p %@:(%d)> [INFO] %@", self, [[NSString stringWithUTF8String:__FILE__] lastPathComponent], __LINE__, [NSString stringWithFormat:(s), ##__VA_ARGS__] )
#else
#define CMLogInfo( s, ... )
#endif

// Define Info
#if REPORTLEVEL <= LOG_VERBOSE_LEVEL
#define CMLogVerbose( s, ... )  NSLog( @"<%p %@:(%d)> [VERBOSE] %@", self, [[NSString stringWithUTF8String:__FILE__] lastPathComponent], __LINE__, [NSString stringWithFormat:(s), ##__VA_ARGS__] )
#else
#define CMLogVerbose( s, ... ) [NSString stringWithFormat:(s), ##__VA_ARGS__];
#endif


#define CMLogSize( s ) NSLog( @"<%f, %f>", s.width, s.height )
#define CMSizeAsString( s ) [NSString stringWithFormat:@"<%f, %f>", s.width, s.height]

