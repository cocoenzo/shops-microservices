<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<head>
	<style>
       #map {
        height: 400px;
        width: 100%;
       }
    </style>
</head>
<body>

	<h1>Microservices Demo - Shop-Client Server</h1>
	<h3>Overview</h3>
	<ul>
		<li>Demo defines a simple web-application for accessing Shops microservice.</li>
	</ul>

	<h3>The Demo</h3>
		<h4>Read a Shop by Name</h4>
	 <form  action="@{/read/}" method="get">
	 	<label>Shop Name:</label><input type="text" value="" name="name" id="name"/><br/>
	 	<input type="submit" value="Search Shop"/>
	 </form>
	 
	 	<h4>Save a Shop</h4>
	 <form action="@{/save}" name="saveShop" id="saveShop" method="get">
	 	<table>
			<tbody>
				<tr>
					<td><label>Name:</label></td>
					<td><input type="text" value="" name="name" id="name"/></td>
				</tr>
				<tr>
					<td><label>street:</label></td>
					<td><input type="text" value="" name="street" id="street"/></td>
				</tr>
				<tr>
					<td>number:</td>
					<td><input type="text" value="" name="number" id="number"/></td>
				</tr>
				<tr>
					<td><label>Postal Code:</label></td>
					<td><input type="text" value="" name="postalCode" id="postalCode"/></td>
				</tr>
				<tr>	
					<td><input type="submit" value="Save Shop"/></td>
				</tr>
			</tbody>
		</table>
	 </form>
	
	<h4>Find Shops by Lat and Lng</h4> 
	 <form action="@{/find/}" method="get">
	 	<label for="latitude">latitude:</label><input type="text" value="" name="latitude" id="latitude"/><br/>
	 	<label for="longitude">longitude:</label><input type="text" value="" name="longitude" id="longitude"/><br/>
	 	<input type="submit" value="Search Shops"/>
	 </form>
	 
	<h3>Spring Boot URLs</h3>
	<p>For those interested, Spring Boot provides RESTful URLs for
		interrogating your application (they return JSON format data):</p>

	<ul>
		<li><a href="@{/beans}">The beans</a></li>
		<li><a href="@{/env}">The environment</a></li>
		<li><a href="@{/dump}">Thread dump</a></li>
		<li><a href="@{/health}">Application health</a></li>
		<li><a href="@{/info}">Application information</a></li>
		<li><a href="@{/metrics}">Application metrics</a></li>
		<li><a href="@{/trace}">Request call trace</a></li>
	</ul>


</body>
</html>