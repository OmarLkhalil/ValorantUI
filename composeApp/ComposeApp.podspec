Pod::Spec.new do |spec|
  spec.name                     = 'ComposeApp'
  spec.version                  = '1.0'
  spec.homepage                 = 'https://github.com/OmarLkhalil/ValorantUI'
  spec.source                   = { :path => '.' }
  spec.authors                  = 'Omar Mohamed Khalil'
  spec.license                  = 'MIT'
  spec.summary                  = 'Kotlin Multiplatform Shared Module'
  spec.vendored_frameworks      = 'build/cocoapods/framework/ComposeApp.framework'
  spec.libraries                = 'c++'
  spec.ios.deployment_target    = '14.1'

  spec.pod_target_xcconfig = {
    'KOTLIN_PROJECT_PATH' => ':composeApp',
    'PRODUCT_MODULE_NAME' => 'ComposeApp',
  }

  spec.script_phases = [
    {
      :name => 'Build ComposeApp',
      :execution_position => :before_compile,
      :shell_path => '/bin/sh',
      :script => <<-SCRIPT
        set -ev
        REPO_ROOT="$PODS_TARGET_SRCROOT"
        "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" :composeApp:embedAndSignAppleFrameworkForXcode
      SCRIPT
    }
  ]
end

