<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/ui-bootstrap.min.css">
<script src="js/angular.min.js"></script>
<script src="js/ui-bootstrap.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
        
<script>

var app = angular.module('myApp', ['ui.bootstrap']);
/* app.controller('myCtrl', function($scope) {
    $scope.firstName = "John";
    $scope.lastName = "Doe";
}); */

app.controller('ModalInstanceCtrl', function ($scope, $http, $modalInstance, params, id, forms) {
	
	$scope.dataToSend=[];
	$scope.params = params;
	$scope.forms = forms;
	
	$scope.id = id;
	  //console.log(selectedProducts);
	  $scope.ok = function () {
		  
		  $scope.dataToSend[0] = {"formId":$scope.id};
		  $scope.dataToSend[1] = $scope.params;
	      $http.post("submitForm.html", $scope.dataToSend)
   	   .then(
   	       function(response){
   	         // success callback
   	         if(response.data != null && response.data > 0){
   	        	 for(f=0; f<$scope.forms.length; f++){
   	        		 if($scope.forms[f].id == $scope.id){
   	        			$scope.forms[f].submitted = true;
   	        		 }
   	        	 }
   	        	alert("Saved successfully");
   	        	$modalInstance.close($scope.forms);//); $scope.selected.item);
   	         }
   	         else{
   	        	 alert("Some error occurred");
   	        	 $scope.show = 1;
   	         }
   	       }, 
   	       function(response){
   	         // failure callback
   	       }
   	    );
	  };

	  $scope.cancel = function () {
	    $modalInstance.dismiss('cancel');
	  };
	});
	
app.controller('FormSubmitController',[ '$scope','$modal','$log','$http', function($scope,$modal, $log, $http) {

        $scope.list = [];
        $scope.formParams;
        $scope.show=0;
            $scope.formData = []; //should set object on init.
            $scope.Questions = {namea:['question1','answer1'],nameb:['q2','a2']};
            var response = $http.get('getForms.html')
            .then(
			       function(response){
			         // success callback
			         console.log(response.data);
			         $scope.forms = response.data;
			       }, 
			       function(response){
			         // failure callback
			       }
			    );
            $scope.getFormById = function(id){
            	$http.get('getFormById.html?id='+id)
            	.then(function(response) {
            		$scope.formId = id;
            		$scope.formData[0] = response.data[0];
            		$scope.formData[1] = response.data[1];
            		$scope.formData[2]={};
        			$scope.formParams = response.data[2];
        			$scope.changeProducts();
    			}	
            	,function error(response){
    	
    			});
            }
            
            $scope.cancel = function(){
            	$scope.show = 0;
            	$scope.formParams = null;
            }
          
            $scope.submitData = function(){
            	console.log($scope.formData);
            	$http.post("submitForm.html", $scope.formData)
            	   .then(
            	       function(response){
            	         // success callback
            	         if(response.data != null && response.data > 0){
            	        	 alert("Saved successfully");
            	        	 $scope.show = 0;
            	         }
            	         else{
            	        	 alert("Some error occurred");
            	        	 $scope.show = 1;
            	         }
            	       }, 
            	       function(response){
            	         // failure callback
            	       }
            	    );
            };
            
            $scope.changeProducts = function() {
                //$scope.items = ['item1', 'item2', 'item3'];
                var modalInstance = $modal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    
                    //size: size,
                    resolve: {
                        params: function() {
                        	console.log("++++++++++++++");
                            console.log($scope.formParams);
                            return $scope.formParams;
                        },
                		id : function(){
                			return $scope.formId;
                		},
                		forms: function(){
                			return $scope.forms;
                		}
                    }
                });
                
                modalInstance.result.then(function (forms) {
                    $scope.forms = forms;
                    
                }, function () {
                    $log.info('Modal dismissed at: ' + new Date());
                });
            };
        }]);


</script>

<style>
table,td,th{
border:1px solid gray;
border-collapse: collapse;
padding:5px;
}
</style>
</head>
<body ng-app="myApp">

<div ng-show="show<=0" style="margin: 50px;" ng-controller="FormSubmitController">
	<table class="center">
		<tr>
			<th colspan="4">Forms</th>
		</tr>
		
		<tr ng-if="forms.length == 0">
			<td colspan="4">No Forms to Display!</td>
		</tr>
		
		<tr ng-if="forms.length > 0">
			<th>User SID</th>
			<th>User Name</th>
			<th>Form Type</th>
			<th>Submitted</th>
		</tr>
		
		<tr ng-repeat="f in forms" ng-if="!f.submitted">
			<td>{{f.userId}}</td>
			<td>{{f.userName}}</td>
			<td ng-if="f.submitted">{{f.form_type}}</td>
			<td ng-if="!f.submitted"><a ng-click="getFormById(f.id)" href="#">{{f.form_type}}</a></td>
			<td style="text-align: center;" ng-if="f.submitted"><a style="height: 25px; width: 25px;" class="linkiconS iconSuccess"></a></td>
			<td ng-if="!f.submitted"><a style="height: 25px; width: 25px;" class="linkiconS iconError"></a></td>
		</tr>
	</table>

<script type="text/ng-template" id="myModalContent.html">
<div class="modal-header">
	<h3 class="modal-title">Form Q&A</h3>
</div>
 
<div style="max-height:500px; overflow-y:scroll; " class="modal-body">
<form>
	<table class="center">
		<tr>
			<th>Question</th>
			<th>Answer</th>
		</tr>

		<tr ng-repeat="(key,value) in params" ng-if="value.question != 'PASSWORD'">
			 <td>{{value.question}}</td>
			 <td style="width:auto;">{{value.answer}}</td>
		</tr>
	</table>
</form>
</div>
<div class="modal-footer">
	<button class="btn btn-primary" ng-click="ok()">Submit</button>
	<button class="btn btn-warning" ng-click="cancel()">Cancel</button>
</div>
</script>
</div>

</body>
</html>