// Karma configuration
// Generated on Wed Apr 03 2013 05:57:56 GMT-0400 (EDT)


// base path, that will be used to resolve files and exclude
basePath = '';


// list of files / patterns to load in the browser
files = [
  JASMINE,
  JASMINE_ADAPTER,
  'https://ajax.googleapis.com/ajax/libs/angularjs/1.0.5/angular.min.js',
    'web-app/js/jquery.min.js',
  'web-app/js/bootstrap.js',
  'web-app/js/test/lib/angular/*.js',
  'web-app/js/*.js',
  'web-app/js/test/**/*.js'
];


// list of files to exclude
exclude = [
  'web-app/js/test/lib/angular/angular-scenario.js',
  'web-app/js/test/e2e/**/*.js',
  'web-app/js/test/web-server.js',
  'web-app/js/bootstrap.min.js'
];


// test results reporter to use
// possible values: 'dots', 'progress', 'junit'
reporters = ['progress'];


// web server port
port = 9876;


// cli runner port
runnerPort = 9100;


// enable / disable colors in the output (reporters and logs)
colors = true;


// level of logging
// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
logLevel = LOG_INFO;


// enable / disable watching file and executing tests whenever any file changes
autoWatch = true;


// Start these browsers, currently available:
// - Chrome
// - ChromeCanary
// - Firefox
// - Opera
// - Safari (only Mac)
// - PhantomJS
// - IE (only Windows)
browsers = ['Chrome'];


// If browser does not capture in given timeout [ms], kill it
captureTimeout = 60000;


// Continuous Integration mode
// if true, it capture browsers, run tests and exit
singleRun = false;
