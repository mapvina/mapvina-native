#import <Mapbox.h>
#import <XCTest/XCTest.h>

@interface MLNMapSnapshotter (LogoTesting)

+ (MLNImage *)logoImageWithStyle:(MLNAttributionInfoStyle)style;

@end

@interface NSBundle (MapVinaLogoTesting)

+ (NSBundle *)mgl_frameworkBundle;

@end

@interface MLNMapSnapshotterLogoTests : XCTestCase
@end

@implementation MLNMapSnapshotterLogoTests

- (void)testLogoAssetsMatchAttributionStyles {
    UIImage *longLogo = [MLNMapSnapshotter logoImageWithStyle:MLNAttributionInfoStyleLong];
    UIImage *mediumLogo = [MLNMapSnapshotter logoImageWithStyle:MLNAttributionInfoStyleMedium];
    UIImage *shortLogo = [MLNMapSnapshotter logoImageWithStyle:MLNAttributionInfoStyleShort];

    XCTAssertEqualWithAccuracy(longLogo.size.width, 108.0, 0.01);
    XCTAssertEqualWithAccuracy(longLogo.size.height, 31.0, 0.01);
    XCTAssertEqualWithAccuracy(mediumLogo.size.width, 32.0, 0.01);
    XCTAssertEqualWithAccuracy(mediumLogo.size.height, 32.0, 0.01);
    XCTAssertNil(shortLogo);
}

- (void)testLegacyLogoAssetNamesRemainCompatible {
    NSBundle *bundle = [NSBundle mgl_frameworkBundle];
    UIImage *legacyCompactLogo = [UIImage imageNamed:@"mapvina-logo-icon"
                                           inBundle:bundle
                      compatibleWithTraitCollection:nil];
    UIImage *legacyHorizontalLogo = [UIImage imageNamed:@"mapvina-logo-stroke-gray"
                                              inBundle:bundle
                         compatibleWithTraitCollection:nil];

    XCTAssertEqualWithAccuracy(legacyCompactLogo.size.width, 32.0, 0.01);
    XCTAssertEqualWithAccuracy(legacyCompactLogo.size.height, 32.0, 0.01);
    XCTAssertEqualWithAccuracy(legacyHorizontalLogo.size.width, 108.0, 0.01);
    XCTAssertEqualWithAccuracy(legacyHorizontalLogo.size.height, 31.0, 0.01);
}

@end
