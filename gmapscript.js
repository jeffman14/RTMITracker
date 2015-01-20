    //<![CDATA[
    var markers = [];
    var loaded = false;
    var map;
    var infoWindow;
    var eta;

//localStorage.removeItem("lastname");
//alert (localStorage.getItem("positionLatLng"));
    function load() {
      document.getElementById("panel").style.visibility = "hidden";
      document.getElementById("buttonClose").style.visibility = "hidden";
      map = new google.maps.Map(document.getElementById("map"), {
        center: new google.maps.LatLng(7.608070,125.127586),
        zoom: 8,
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        zoomControl: true,        
        zoomControlOptions: {
        style: google.maps.ZoomControlStyle.LARGE,
        mapTypeControlOptions: {
                mapTypeIds          : [
                                            google.maps.MapTypeId.ROADMAP, 
                                            google.maps.MapTypeId.HYBRID,
                                            google.maps.MapTypeId.SATELLITE , 
                                            google.maps.MapTypeId.TERRAIN
                                           
                                      ],
        style               : google.maps.MapTypeControlStyle.HORIZONTAL_BAR
            },
        position: google.maps.ControlPosition.RIGHT_TOP
      }
      });

    
    
      infoWindow = new google.maps.InfoWindow;
      $.get( "/phpxml_gen.php" , function ( response ) {
        response.map( function ( each ) {
          var point = new google.maps.LatLng(parseFloat( each.lat ),parseFloat( each.lng ));         

          var html = "Bus <b>" + each.bodyno + "</b> from <b>" + each.origin+ "</b> to <b>"+ each.desti + "</b> at <b>" + each.speed + "</b>Kph "+" with <b>" + each.vacancy+ "</b> vacant seats. ETA <b>" + each.eta+"</b>"+"Current Location:"+each.address;
          var marker = new google.maps.Marker({
            map: map,
            position: point,
            animation: google.maps.Animation.DROP          
          });

          markers.push( marker );

          bindInfoWindow(marker, map, infoWindow, html);          
          loaded = true;
        } );                  
      } );
    }

    setInterval( function ( ) {
      if ( loaded ) {
        loaded = false;
        $.get( "/phpxml_gen.php" , function ( response ) {          
          for ( var index = 0 , len = markers.length ; index < len ; index ++ ) {
            var each = response[index];
            var html = "Bus <b>" + each.bodyno + "</b> from <b>" + each.origin+ "</b> to <b>"+ each.desti + "</b> at <b>" + each.speed + "</b>Kph "+" with <b>" + each.vacancy+ "</b> vacant seats. ETA <b>" + eta+"</b>"+"Current Location:"+each.address;
            infoWindow.setContent( html );          
            var latlng = new google.maps.LatLng(response[index].lat, response[index].lng);
            markers[index].setPosition( latlng );
          }
          console.log( "finished" );
          loaded = true;
        } );
      }
    } , 5000 );

    function btnCloseDiv(){
      //document.getElementById("buttonClose").style.visibility = "hidden";
      //document.getElementById("panel").style.visibility = "hidden";
      //directionsDisplay.setDirections(null);
      //directionsDisplay.setMap(null);
    }

    function bindInfoWindow(marker, map, infoWindow, html) {

      google.maps.event.addListener(marker, 'click', function( event ) {
//document.getElementById("panel").style.visibility = "visible";
//document.getElementById("buttonClose").style.visibility = "visible";
//localStorage.setItem("positionLatLng", marker.getPosition( ) );
//location.reload();

      infoWindow.setContent(html);
      infoWindow.open(map, marker);     
      setDirection( marker );
      });
    }
    var start , end;

    function setDirection ( marker ) {
      start = marker.getPosition( );
      //start=localStorage.getItem("positionLatLng");
      end = 'Liceo de Cagayan university';
      directionService();
      
    }
  
    function directionService( ) {
       var directionsService = new google.maps.DirectionsService();
       var directionsDisplay = new google.maps.DirectionsRenderer(); 
       directionsDisplay.setMap( map );
       directionsDisplay.setOptions( { suppressMarkers: true } );
       directionsDisplay.setPanel( document.getElementById('panel') );

       var request = {
       origin:start, 
       destination:end,
       travelMode: google.maps.TravelMode.DRIVING,
       optimizeWaypoints: true
     };

       directionsService.route(request, function(response, status) {
         if (status == google.maps.DirectionsStatus.OK) {
           directionsDisplay.setDirections(response);
           var route = response.routes[0];
            
            var summaryPanel = document.getElementById('panel');
            summaryPanel.innerHTML = '';
            // For each route, display summary information.
            for (var i = 0; i < route.legs.length; i++) {
            var routeSegment = i + 1;
            eta=route.legs[i].duration.text;
            }
         } else {
          console.log( "Error: " + status );
         }
       });
    }
    //]]>
