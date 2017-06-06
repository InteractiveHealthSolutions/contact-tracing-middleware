<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="js/angular.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script>
var app = angular.module('myApp', ["ngRoute"]);
/* app.controller('myCtrl', function($scope) {
    $scope.firstName = "John";
    $scope.lastName = "Doe";
}); */

app.config(function ($routeProvider, $locationProvider) {
    $routeProvider
    .when('/redirect.html', {
        templateUrl: "http://localhost:8080/tbrmobileweb/redirect.html"
    })
})
         
app.controller('FormSubmitController',[ '$scope','$http','$location', function($scope, $http, $location) {

        $scope.list = [];
        $scope.name="a";
            $scope.headerText = 'AngularJS Post Form Spring MVC example: Submit below form';
            $scope.formData = []; //should set object on init.
            $scope.Questions = {namea:['question1','answer1'],nameb:['q2','a2']};
            var response = $http.get('getFormById.html')
            .then(
			       function(response){
			         // success callback
			         $scope.forms = response.data;
			         console.log(response);
/*  			          window.location.href = '${pageContext.request.contextPath}'+"/redirect.html";   	 */
			       }, 
			       function(response){
			         // failure callback
			       }
			    );
            $scope.getFormById = function(id){
            	console.log(id);
            	$http.get('getFormById.html?id='+id);
            }
            $scope.submit = function() {
            	var formData = {
                        "name1" : $scope.formData.a,
                        "name2" : $scope.formData.b,
                };
            	

                //Empty list data after process
                $scope.list = [];
            };
        }]);

</script>

</head>
<body>
<div ng-controller="FormSubmitController">

<form ng-submit="submit()">
	<table>
	<tr ng-repeat="f in forms">
	<td>{{f.form_question}}</td>
	</tr>
	<tr>
	<td><input type="submit"></td>
	</tr>
	</table>
</form>
</div>
</body>
</html>