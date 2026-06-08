Pod::Spec.new do |s|
    version = "#{ENV['VERSION']}"

    s.name = 'MapVina'
    s.version = version
    s.license = { :type => 'BSD', :file => "LICENSE.md" }
    s.homepage = 'https://mapvina.com/'
    s.authors = { 'MapVina' => '' }
    s.summary = 'Open source vector map solution for iOS with full styling capabilities.'
    s.platform = :ios
    s.source = {
        :http => "https://github.com/mapvina/mapvina-native/releases/download/ios-v#{version.to_s}/MapVina.dynamic.xcframework.zip",
        :type => "zip"
    }
    s.social_media_url  = 'https://mastodon.social/@mapvina'
    s.ios.deployment_target = '12.0'
    s.ios.vendored_frameworks = "MapVina.xcframework"
end
