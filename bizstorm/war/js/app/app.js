var offerDashboard = angular.module("offerDashboard", [
    "offerDashboard.services",
    "offerDashboard.controllers",
    "offerDashboard.filters",
    "offerDashboard.directives",
    "ui.bootstrap"
]);

offerDashboard.config(function ($routeProvider){
	$routeProvider.when('/login',{		
		controller : 'LoginController',
		templateUrl : 'partials/login.html'
	});

	$routeProvider.when('/home',{
		controller : 'HomeController'
		templateUrl : 'partials/home.html'	
	});

	$routeProvider.otherwise({redirectTo: '/login'});
});

app.service('loginService', function($rootScope, $location) {
	    this.auth = function (){
	    	authenticate($rootScope, $location);
	    };
	});

app.controller('LoginController', function ($scope,logginService){
	$scope.auth = logginService.auth();
});

app.controller('HomeController',function ($scope,$location){
	if(!$scope.signed){
		$location.path('/login');
	}
});
